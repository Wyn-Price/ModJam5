package com.wynprice.modjam5.common.colorfunctionality.colors;

import java.awt.Color;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class ColorWhite extends ColorFunction {

	public ColorWhite() {
		super(0f, 0f, RangeType.HUE);
	}
	
	@Override
	public void onMobTick(EntityLivingBase entity) {
		entity.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 110, 0, false, false));
	}
	
	@Override
	public boolean shouldApply(float[] hsb) {
		Color color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
		return Math.min(color.getRed(), Math.min(color.getGreen(), color.getBlue())) > 200;
	}

}
