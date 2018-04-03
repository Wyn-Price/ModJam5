package com.wynprice.modjam5.common.colorfunctionality.colors;

import java.util.UUID;

import com.wynprice.modjam5.common.WorldPaintConfig;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

public class ColorYellow extends ColorFunction {

	public ColorYellow() {
		super("yellow", WorldPaintConfig.COLOR_VALUES.yellowMin, WorldPaintConfig.COLOR_VALUES.yellowMax, RangeType.HUE);
	}
	
	private static final UUID HEALTH_UUID = UUID.fromString("dc1683d6-1d1e-4b20-bc57-b594b899ffad");
    private static final AttributeModifier EXTRA_HEALTH_MODIFIER = (new AttributeModifier(HEALTH_UUID, "World Paint Health Modifier", WorldPaintConfig.COLOR_FUNCTIONS.yellowModifierAmount, WorldPaintConfig.COLOR_FUNCTIONS.yellowModifierOperation)).setSaved(false);

	
	@Override
	public void onMobTick(EntityLivingBase entity) {
		IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		if(!attribute.hasModifier(EXTRA_HEALTH_MODIFIER)) {
			attribute.applyModifier(EXTRA_HEALTH_MODIFIER);
		}
		if(entity.getEntityWorld().getTotalWorldTime() % (WorldPaintConfig.COLOR_FUNCTIONS.yellowFoodSeconds * 20) == 0 && entity instanceof EntityPlayer) {
			((EntityPlayer)entity).getFoodStats().addStats(WorldPaintConfig.COLOR_FUNCTIONS.yellowFoodLevel, WorldPaintConfig.COLOR_FUNCTIONS.yellowFoodSaturation);
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
