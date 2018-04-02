package com.wynprice.modjam5.common.colorfunctionality.colors;

import java.util.Random;
import java.util.UUID;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

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
		if(entity.getEntityWorld().getTotalWorldTime() % 400 == 0 && entity instanceof EntityPlayer) {
			((EntityPlayer)entity).getFoodStats().addStats(1, 1f);
		}
	}
	
	@Override
	public void onBlockTick(World world, BlockPos pos) {
		
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
