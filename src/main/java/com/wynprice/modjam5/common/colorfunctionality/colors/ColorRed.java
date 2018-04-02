package com.wynprice.modjam5.common.colorfunctionality.colors;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunctions;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunction.RangeType;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ColorRed extends ColorFunction {

	public ColorRed() {
		super(12f, 350f, RangeType.HUE);
	}

	@Override
	public void onMobTick(EntityLivingBase entity) {
		if(entity.isEntityUndead()) {
			entity.extinguish();
		} else {
			if(!(entity instanceof EntityPlayer) || !(((EntityPlayer)entity).isCreative() || ((EntityPlayer)entity).isSpectator() )) {
				entity.setFire(10);
			}
		}
	}
	
	@Override
	public boolean shouldApply(float[] hsb) {
		return !ColorFunctions.WHITE.shouldApply(hsb) && !ColorFunctions.BLACK.shouldApply(hsb) && (hsb[0] <= minRange / 360f || hsb[0] >= maxRange / 360f);
	}
	
}
