package com.wynprice.modjam5.common.registries;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.items.ItemPainingBoots;
import com.wynprice.modjam5.common.items.ItemThrowablePaint;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldPaintItems {
	
	public static final Item COLORPICKER = null; //TODO 
	public static final Item PAINTING_BOOTS = new ItemPainingBoots();
	public static final Item THROWABLE_PAINT = new ItemThrowablePaint();
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(PAINTING_BOOTS, THROWABLE_PAINT);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegister(ModelRegistryEvent event) {
		registerRenderForItems(PAINTING_BOOTS, THROWABLE_PAINT);
	}

	@SideOnly(Side.CLIENT)
	private static void registerRenderForItems(Item... items) {
		for(Item item : items) {
			registerRenderForItem(item);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerRenderForItem(Item item) {
		registerRenderForItem(item, (stack) -> new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerRenderForItem(Item item, ItemMeshDefinition definition) {
		ModelLoader.setCustomMeshDefinition(item, definition);
	}
}
