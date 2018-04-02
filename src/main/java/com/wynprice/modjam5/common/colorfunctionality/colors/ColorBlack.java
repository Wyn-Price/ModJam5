package com.wynprice.modjam5.common.colorfunctionality.colors;

import java.awt.Color;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class ColorBlack extends ColorFunction {

	public ColorBlack() {
		super(0f, 0f, RangeType.HUE);
	}
	
	@Override
	public void onMobTick(EntityLivingBase entity) {
		if(!entity.world.isRemote) {
			entity.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 15, 0, false, false));
		}
	}
	
	@Override
	public boolean shouldApply(float[] hsb) {
		Color color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
		return Math.max(color.getRed(), Math.max(color.getGreen(), color.getBlue())) < 55;
	}

}
