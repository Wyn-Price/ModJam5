package com.wynprice.modjam5.common.registries;

import com.wynprice.modjam5.WorldPaint;

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
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll();
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegister(ModelRegistryEvent event) {
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerRenderForItem(Block block) {
		registerRenderForItem(block, (stack) -> new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerRenderForItem(Block block, ItemMeshDefinition definition) {
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(block), definition);
	}
}
