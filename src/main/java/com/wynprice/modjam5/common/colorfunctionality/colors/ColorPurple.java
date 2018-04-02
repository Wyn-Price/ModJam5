package com.wynprice.modjam5.common.colorfunctionality.colors;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class ColorPurple extends ColorFunction {

	public ColorPurple() {
		super(240f, 307f, RangeType.HUE);
	}
	
	@Override
	public void onMobTick(EntityLivingBase entity) {
		if(!entity.world.isRemote) {
			entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 15, 3, false, false));
		}
	}

}
