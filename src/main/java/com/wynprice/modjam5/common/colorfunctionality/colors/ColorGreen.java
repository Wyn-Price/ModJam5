package com.wynprice.modjam5.common.colorfunctionality.colors;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import net.minecraft.block.IGrowable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColorGreen extends ColorFunction {

	public ColorGreen() {
		super(76f, 153f, RangeType.HUE);
	}
	
	@Override
	public void onMobTick(EntityLivingBase entity) {
		entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 110, 4, false, false));
	}
	
	@Override
	public void onBlockTick(World world, BlockPos pos) {
		for(EnumFacing face : EnumFacing.values()) {
			if(world.getBlockState(pos.offset(face)).getBlock() instanceof IGrowable) {
				ItemDye.applyBonemeal(ItemStack.EMPTY, world, pos.offset(face));
			}
		}
		if(world.getBlockState(pos).getBlock() instanceof IGrowable) {
			ItemDye.applyBonemeal(ItemStack.EMPTY, world, pos);
		}
	}
}
