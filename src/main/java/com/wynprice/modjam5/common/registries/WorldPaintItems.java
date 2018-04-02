package com.wynprice.modjam5.common.registries;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.items.ItemColorPicker;
import com.wynprice.modjam5.common.items.ItemPainingBoots;
import com.wynprice.modjam5.common.items.ItemPaintBrush;
import com.wynprice.modjam5.common.items.ItemPaintScrubber;
import com.wynprice.modjam5.common.items.ItemThrowablePaint;

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
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldPaintItems {
	
	public static final Item COLORPICKER = new ItemColorPicker();
	public static final Item PAINTING_BOOTS = new ItemPainingBoots();
	public static final Item THROWABLE_PAINT = new ItemThrowablePaint();
	public static final Item PAINT_SCRUBBER = new ItemPaintScrubber();
	public static final Item PAINTBRUSH = new ItemPaintBrush();
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		registerItems(event.getRegistry(), PAINTING_BOOTS, THROWABLE_PAINT, COLORPICKER, PAINT_SCRUBBER, PAINTBRUSH);
	}
	
	private final static ArrayList<Item> itemList = new ArrayList<>();
	
	private static void registerItems(IForgeRegistry<Item> registry, Item... items) {
		itemList.addAll(Lists.newArrayList(items));
		for(Item item : items) {
			item.setCreativeTab(WorldPaint.TAB);
		}
		registry.registerAll(items);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegister(ModelRegistryEvent event) {
		registerRenderForItems(itemList.toArray(new Item[0]));
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenderForItems(Item... items) {
		for(Item item : items) {
			registerRenderForItem(item);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenderForItem(Item item) {
		registerRenderForItem(item, (stack) -> new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenderForItem(Item item, ItemMeshDefinition definition) {
		ModelLoader.setCustomMeshDefinition(item, definition);
	}
}
