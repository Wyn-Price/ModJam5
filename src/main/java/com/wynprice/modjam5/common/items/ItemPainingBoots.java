package com.wynprice.modjam5.common.items;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ItemPainingBoots extends ItemArmor {

	public static final ArmorMaterial MATERIAL = EnumHelper.addArmorMaterial("PAINTINGBOOTS", WorldPaint.MODID + ":paintingboots", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.BLOCK_CLOTH_BREAK, 2.0F);
	
	public ItemPainingBoots() {
		super(MATERIAL, 0, EntityEquipmentSlot.FEET);
		setRegistryName("paintingboots");
		setUnlocalizedName("paintingboots");
	}
	
	@Override
	public boolean hasOverlay(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getColor(ItemStack stack) {
		return ColorUtils.getColor(stack);
	}
	
	@EventBusSubscriber(modid=WorldPaint.MODID)
	public static class TickHandler {
		@SubscribeEvent
		public static void onEntityTick(PlayerTickEvent event) {
			if(!event.player.world.isRemote) {
				if(event.player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemPainingBoots) {
					BlockPos pos = event.player.getPosition().down();
					ItemStack stack = event.player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
					if(event.player.world.getBlockState(pos).getMaterial() != Material.AIR) {
						WorldColorsHandler.putInfo(event.player.world, pos, new WorldColorsHandler.DataInfomation(((ItemPainingBoots)stack.getItem()).getColor(stack), true, pos, new int[0]), true);
					}
				}
			}
		}
	}

}
