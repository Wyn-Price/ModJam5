package com.wynprice.modjam5.common.colorfunctionality.colors;

import java.util.Random;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.util.control.Exception;

public class ColorGreen extends ColorFunction {

	public ColorGreen() {
		super(70f, 153f, RangeType.HUE);
	}
	
	@Override
	public void onBlockTick(World world, BlockPos pos) {
		if(true) return;//TODO fix lag issues
		EnumFacing face = EnumFacing.getFront(new Random().nextInt());
		if(world.getBlockState(pos.offset(face)).getBlock() instanceof IGrowable) {
			if(ItemDye.applyBonemeal(ItemStack.EMPTY, world, pos.offset(face))) {
				if(!world.isRemote) {
	                world.playEvent(2005, pos, 0);
				}
				return;
			}
		}
		
		EnumFacing face2 = EnumFacing.getFront(new Random().nextInt());
		if(world.getBlockState(pos.offset(face2)).getBlock() == Blocks.DIRT) {
			world.setBlockState(pos.offset(face2), Blocks.GRASS.getDefaultState());
		}
		
		if(world.getBlockState(pos).getBlock() instanceof BlockLeaves && new Random().nextFloat() < 0.01f) { //Terrible code. I dont care. //TODO config and sort out 
			for(IProperty<?> property : world.getBlockState(pos).getBlock().getBlockState().getProperties()) {
				if(property.getValueClass() == BlockPlanks.EnumType.class) {
					BlockPlanks.EnumType enu = (BlockPlanks.EnumType) world.getBlockState(pos).getValue(property);
					EnumFacing facing = EnumFacing.getHorizontal(new Random().nextInt());
					BlockPos position = world.getTopSolidOrLiquidBlock(pos.offset(facing, 5));
					Block block = world.getBlockState(position).getBlock();
					if(!(block == Blocks.GRASS || block == Blocks.GRASS_PATH || block == Blocks.DIRT)) {
						position = position.down();
					}
					block = world.getBlockState(position).getBlock();
					if(!(block == Blocks.GRASS || block == Blocks.GRASS_PATH || block == Blocks.DIRT)) {
						position = position.down();
					}
					block = world.getBlockState(position).getBlock();
					if(block == Blocks.GRASS || block == Blocks.GRASS_PATH || block == Blocks.DIRT) {
						world.setBlockState(position.up(), Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, enu));
					}
				}
			}
		}
	}
}
