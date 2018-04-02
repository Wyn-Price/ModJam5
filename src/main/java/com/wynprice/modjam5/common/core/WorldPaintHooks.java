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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
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
	
	@SideOnly(Side.CLIENT)
	public static int getProperBiomeColor(int color, IBlockAccess blockAccess, BlockPos pos, IWorldPaintColorResolver resolver) { //Needed colorResolver to be string as the class is inaccessable
		int type = Integer.valueOf(String.valueOf(resolver.toString().toCharArray()[43])); //1 = grass, 2 = foliage, 3 = water
		WorldColorsHandler.DataInfomation in = WorldColorsHandler.getInfo(Minecraft.getMinecraft().world, pos);
		if(in != null && !in.isDefault() && !in.isSpreadable()) {
			return in.getColor();
		}
		int i = 0;
		int j = 0;
		int k = 0;
		int blend = 1; //TODO config
		int defaultcol = 0x2D3FF4;
		int times = 0;
		for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-blend, -blend, -blend), pos.add(blend, blend, blend)))
		{
			times++;
			int l = resolver.getColorAtPos(blockAccess.getBiome(blockpos$mutableblockpos), blockpos$mutableblockpos);
			WorldColorsHandler.DataInfomation info = WorldColorsHandler.getInfo(Minecraft.getMinecraft().world, blockpos$mutableblockpos);
			if(info == null) info = WorldColorsHandler.DataInfomation.DEFAULT;
			if(!info.isDefault()) {
				l = info.getColor();
			} else if(type == 3) {
				if(l != 0xFFFFFF) {
					float r1 = ((l >> 16) & 0xFF) / 255f;
					float g1 = ((l >> 8) & 0xFF) / 255f;
					float b1 = (l & 0xFF) / 255f;
					
					float r2 = ((defaultcol >> 16) & 0xFF) / 255f;
					float g2 = ((defaultcol >> 8) & 0xFF) / 255f;
					float b2 = (defaultcol & 0xFF) / 255f;
					
					
					l = new Color(r1 * r2, g1 * g2, b1 * b2).getRGB();
				} else {
					l = defaultcol;
				}
				
			}
			i += (l & 16711680) >> 16;
			j += (l & 65280) >> 8;
			k += l & 255;
		}

		return (i / times & 255) << 16 | (j / times & 255) << 8 | k / times & 255;
	}
	
	public static float getBlockSlipperiness(Block block, IBlockState state, World world, BlockPos pos, Entity entity) {
		DataInfomation info = WorldColorsHandler.getInfo(world, pos);
		float _deafult = block.getSlipperiness(state, world, pos, entity);
		if(!info.isDefault() && allowedBlocks.contains(block)) {
			if(ColorUtils.findClosestPaletteColorTo(info.getColor()) == ColorFunctions.ORANGE) {
				return entity instanceof EntityItem ? 1f : 1.05f;
			}
		}
		
		return _deafult;
	}
}
