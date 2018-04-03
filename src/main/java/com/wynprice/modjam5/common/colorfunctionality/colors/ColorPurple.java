package com.wynprice.modjam5.common.colorfunctionality.colors;

import com.wynprice.modjam5.common.WorldPaintConfig;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class ColorPurple extends ColorFunction {

	public ColorPurple() {
		super("purple", WorldPaintConfig.COLOR_VALUES.purpleMin, WorldPaintConfig.COLOR_VALUES.purpleMax, RangeType.HUE);
	}
	
	@Override
	public void onMobTick(EntityLivingBase entity) {
		if(!entity.world.isRemote && WorldPaintConfig.COLOR_FUNCTIONS.purpleStrength) {
			entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 15, 3, false, false));
		}
	}

}
