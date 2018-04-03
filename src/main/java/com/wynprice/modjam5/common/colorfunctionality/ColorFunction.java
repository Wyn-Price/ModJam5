package com.wynprice.modjam5.common.colorfunctionality;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColorFunction {
	protected final double minRange;
	protected final double maxRange;
	protected final RangeType rangeType;
	protected final String colorName;
	
	public ColorFunction(String colorName, double min, double max, RangeType type) {
		this.minRange = min;
		this.maxRange = max;
		this.rangeType = type;
		this.colorName = colorName;
	}
	
	public void onMobTick(EntityLivingBase entity) {
		
	}
	
	public void onBlockTick(World world, BlockPos pos) {
		
	}
	
	public void onWorldTick(World world) {
		
	}
	
	public boolean onEntityDamaged(EntityLivingBase entity, DamageSource source, float amount) {
		return false;
	}

	public boolean recieveAwayCalls() {
		return false;
	}

	/**
	 * Called when {@link #recieveAwayCalls()} is true. Called every tick, on an entity that <b>IS NOT</b> in this color zone. Used to remove attributes
	 * @param entity 
	 */
	public void onAwayMobTick(EntityLivingBase entity) {
		
	}
	
	public boolean shouldApply(float[] hsb) {
		float value = hsb[rangeType.ordinal()] * 360f;
		boolean flag = true;
		if(rangeType == RangeType.HUE) {
			flag = hsb[1] < 0.5f && hsb[2] > 0.5f; 
		}
		return value >= minRange && value <= maxRange;
	}
	
	public String getColorName() {
		return colorName;
	}
	
	public enum RangeType {
		HUE, SATURATION, BRIGHTNESS;
	}
	
}
