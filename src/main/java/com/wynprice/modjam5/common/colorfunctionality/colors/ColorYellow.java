package com.wynprice.modjam5.common.colorfunctionality.colors;

import java.util.UUID;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class ColorYellow extends ColorFunction {

	public ColorYellow() {
		super(43f, 68f, RangeType.HUE);
	}
	
	private static final UUID HEALTH_UUID = UUID.fromString("dc1683d6-1d1e-4b20-bc57-b594b899ffad");
    private static final AttributeModifier EXTRA_HEALTH_MODIFIER = (new AttributeModifier(HEALTH_UUID, "World Paint Health Modifier", 0.5D, 1)).setSaved(false);

	
	@Override
	public void onMobTick(EntityLivingBase entity) {
		IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		if(!attribute.hasModifier(EXTRA_HEALTH_MODIFIER)) {
			attribute.applyModifier(EXTRA_HEALTH_MODIFIER);
		}
	}
	
	@Override
	public boolean recieveAwayCalls() {
		return true;
	}
	
	@Override
	public void onAwayMobTick(EntityLivingBase entity) {
		IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		if(attribute.hasModifier(EXTRA_HEALTH_MODIFIER)) {
			attribute.removeModifier(EXTRA_HEALTH_MODIFIER);
		}
	}

}
