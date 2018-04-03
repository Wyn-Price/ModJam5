package com.wynprice.modjam5.common.core;

import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;
import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldPaintConfig;
import com.wynprice.modjam5.common.colorfunctionality.ColorBehaviourEventDispatcher;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunctions;
import com.wynprice.modjam5.common.utils.ColorUtils;
import com.wynprice.modjam5.common.utils.capability.DataInfomation;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldPaintHooks {
	
	public static void onRandomTick(Block block, World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(new Random().nextFloat() < WorldPaintConfig.GENERAL.spreadChance || !WorldPaintConfig.GENERAL.getAllowedBlocks().contains(block)) {
			return;
		}
		if(!worldIn.isRemote) {
			DataInfomation info = WorldColorsHandler.getInfo(worldIn, pos);	
			ColorBehaviourEventDispatcher.onRandomBlockTick(worldIn, pos, info);
			if(info.isSpreadable()) {
				if(info.getSpreadTo().length >= EnumFacing.values().length) {
					return;
				}
				ArrayList<EnumFacing> facing = new ArrayList<>();
				for(EnumFacing face : EnumFacing.values()) {
					if(!Lists.newArrayList(info.getSpreadTo()).contains(face.getIndex())) {
						facing.add(face);
					}
				}
				
				EnumFacing dir = facing.get(rand.nextInt(facing.size()));
				
				info.addFace(dir);
				
				if(dir.getAxis() == Axis.Y) {
					pos = pos.offset(dir);
					dir = EnumFacing.getHorizontal(rand.nextInt());
				}
				DataInfomation dirInfo = WorldColorsHandler.getInfo(worldIn, pos.offset(dir));
				if(dirInfo.isDefault()) {
					WorldColorsHandler.putInfo(worldIn, pos.offset(dir), info, true);
				}
			}
		}
	}
	
	public static float getBlockSlipperiness(Block block, IBlockState state, World world, BlockPos pos, Entity entity) {
		DataInfomation info = WorldColorsHandler.getInfo(world, pos);
		if(!info.isDefault() && WorldPaintConfig.GENERAL.getAllowedBlocks().contains(block)) {
			if(ColorUtils.calculateClosestColor(info.getColor()) == ColorFunctions.ORANGE && WorldPaintConfig.COLOR_FUNCTIONS.orangeSlippyBlocks) {
				return entity instanceof EntityItem ? 1f : 1.05f;
			}
		}
		return block.getSlipperiness(state, world, pos, entity);
	}
	
	public static void onLanded(Block block, World worldIn, Entity entityIn) {
		BlockPos position = entityIn.getPosition();
		DataInfomation info = WorldColorsHandler.getInfo(worldIn, position);
		for(int i = 0; i < 3; i++) {
			if(info.isDefault() || worldIn.getBlockState(position).getMaterial() == Material.AIR) {
				position = position.down();
				info = WorldColorsHandler.getInfo(worldIn, position);
			}
		}
		if(ColorUtils.calculateClosestColor(info.getColor()) == ColorFunctions.BLUE && !entityIn.isSneaking() && WorldPaintConfig.COLOR_FUNCTIONS.blueBouncyBlocks) {
			entityIn.motionY = -entityIn.motionY * 0.9f;
			return;
		}
		block.onLanded(worldIn, entityIn);
	}
}
