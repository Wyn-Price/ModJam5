package com.wynprice.modjam5.common.colorfunctionality.colors;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class ColorBlue extends ColorFunction {

	public ColorBlue() {
		super(203f, 250f, RangeType.HUE);
	}

	@Override
	public void onMobTick(EntityLivingBase entity) {
		if(!entity.world.isRemote) {
			entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 15, 4, false, false));
		}
	}
	
	@Override
	public boolean onEntityDamaged(EntityLivingBase entity, DamageSource source, float amount) {
		if(source == DamageSource.FALL) {
			return true;
		}
		return super.onEntityDamaged(entity, source, amount);
	}
	
}
