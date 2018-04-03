package com.wynprice.modjam5.common.colorfunctionality.colors;

import com.wynprice.modjam5.common.WorldPaintConfig;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class ColorBlue extends ColorFunction {

	public ColorBlue() {
		super("blue", WorldPaintConfig.COLOR_VALUES.blueMin, WorldPaintConfig.COLOR_VALUES.blueMax, RangeType.HUE);
	}

	@Override
	public void onMobTick(EntityLivingBase entity) {
		if(!entity.world.isRemote && WorldPaintConfig.COLOR_FUNCTIONS.blueJumpBoost) {
			entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 15, 4, false, false));
		}
	}
	
	@Override
	public boolean onEntityDamaged(EntityLivingBase entity, DamageSource source, float amount) {
		if(source == DamageSource.FALL && WorldPaintConfig.COLOR_FUNCTIONS.blueFallDamage) {
			return true;
		}
		return super.onEntityDamaged(entity, source, amount);
	}
	
}
