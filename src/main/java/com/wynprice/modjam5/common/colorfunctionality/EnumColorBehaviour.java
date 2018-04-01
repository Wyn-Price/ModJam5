package com.wynprice.modjam5.common.colorfunctionality;

import java.awt.Color;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;

import com.wynprice.modjam5.common.colorfunctionality.ColorBehaviourManager.ILivingMobTick;
import com.wynprice.modjam5.common.colorfunctionality.ColorBehaviourManager.IRandomBlockTick;
import com.wynprice.modjam5.common.utils.PaletteColor;

import net.minecraft.block.IGrowable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum EnumColorBehaviour implements ColorBehaviourManager.IAll {
	PURPLE(new Color(0x9b4ece), 0.5f, 
			(entity) -> entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 15, 3, false, false)),
			IRandomBlockTick.NULL),
	
	RED(Color.RED, 0.35f, 
			(entity) -> {
				if(entity.isEntityUndead()) {
					entity.extinguish();
				} else {
					if(!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isCreative()) {
						entity.setFire(10);
					}
				}
			},
			IRandomBlockTick.NULL),
	
	GREEN(Color.GREEN, 0.5f,
			(entity) -> entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 110, 4, false, false)),
			(world, pos) -> {
				for(EnumFacing face : EnumFacing.values()) {
					if(world.getBlockState(pos.offset(face)).getBlock() instanceof IGrowable) {
						ItemDye.applyBonemeal(ItemStack.EMPTY, world, pos.offset(face));
					}
				}
				if(world.getBlockState(pos).getBlock() instanceof IGrowable) {
					ItemDye.applyBonemeal(ItemStack.EMPTY, world, pos);
				}
			}),
	
	WHITE(Color.WHITE, 1f,
			(entity) -> {
				entity.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 110, 0, false, false));
			},
			IRandomBlockTick.NULL),
	
	YELLOW(Color.YELLOW, 0.5f, 
			ILivingMobTick.NULL,
			IRandomBlockTick.NULL);
	
	private final Color baseColor;
	private final float chance;
	
	private final ColorBehaviourManager.ILivingMobTick mobTickEvent;
	private final ColorBehaviourManager.IRandomBlockTick blockTickEvent;

	
	private EnumColorBehaviour(Color color, float chance, ColorBehaviourManager.ILivingMobTick mobTickEvent, ColorBehaviourManager.IRandomBlockTick blockTickEvent) {
		this.baseColor = color;
		this.chance = chance;
		this.mobTickEvent = mobTickEvent;
		this.blockTickEvent = blockTickEvent;
	}
	
	public PaletteColor getPaletteColor() {
		return new PaletteColor(this);
	}
	
	public Color getBaseColor() {
		return baseColor;
	}
	
	public float getChance() {
		return chance;
	}
	
	
	
	@Override
	public void onMobTick(EntityLivingBase entity) {
		mobTickEvent.onMobTick(entity);
	}
	
	@Override
	public void onBlockTick(World world, BlockPos pos) {
		blockTickEvent.onBlockTick(world, pos);
	}
}
