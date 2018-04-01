package com.wynprice.modjam5.common.colorfunctionality;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColorFunction {
	protected final float minRange;
	protected final float maxRange;
	protected final RangeType rangeType;
	
	public ColorFunction(float min, float max, RangeType type) {
		this.minRange = min;
		this.maxRange = max;
		this.rangeType = type;
	}
	
	public void onMobTick(EntityLivingBase entity) {
		
	}
	
	public void onBlockTick(World world, BlockPos pos) {
		
	}
	
	public boolean shouldApply(float[] hsb) {
		float value = hsb[rangeType.ordinal()] * 360f;
		return value >= minRange && value <= maxRange;
	}
	
	public enum RangeType {
		HUE, SATURATION, BRIGHTNESS;
	}
	
}
