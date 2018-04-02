package com.wynprice.modjam5.common.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;
import com.wynprice.modjam5.client.IWorldPaintColorResolver;
import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.DataInfomation;
import com.wynprice.modjam5.common.colorfunctionality.ColorBehaviourEventDispatcher;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunctions;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldPaintHooks {
	
	public static ArrayList<Block> allowedBlocks = Lists.newArrayList(new Block[] {
			Blocks.GRASS,
			Blocks.DIRT,
			Blocks.FARMLAND,
			
			Blocks.LEAVES,
			Blocks.LEAVES2,
			
			Blocks.LOG,
			Blocks.LOG2,
			
			Blocks.WATER,
			Blocks.FLOWING_WATER,
			
			Blocks.VINE,
			Blocks.WATERLILY,
			
			Blocks.TALLGRASS,
			Blocks.DOUBLE_PLANT,
			Blocks.REEDS
	});
	
	public static void onRandomTick(Block block, World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(!allowedBlocks.contains(block)) return;
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
		if(!info.isDefault() && allowedBlocks.contains(block)) {
			if(ColorUtils.findClosestPaletteColorTo(info.getColor()) == ColorFunctions.ORANGE) {
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
		if(ColorUtils.findClosestPaletteColorTo(info.getColor()) == ColorFunctions.BLUE && !entityIn.isSneaking()) {
			entityIn.motionY = -entityIn.motionY * 0.9f;
			return;
		}
		block.onLanded(worldIn, entityIn);
	}
}
