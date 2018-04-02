package com.wynprice.modjam5.common.registries;

import java.util.ArrayList;

import com.wynprice.modjam5.WorldPaint;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldPaintBlocks {
		
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll();
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll();
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegister(ModelRegistryEvent event) {
		WorldPaintItems.registerRenderForItems(items.toArray(new Item[0]));
	}
	
	private static ArrayList<Item> items = new ArrayList<>();
	
	private static Item createItemBlock(Block block, int stacksize) {
		ItemBlock item = new ItemBlock(block);
		item.setMaxStackSize(stacksize);
		item.setRegistryName(block.getRegistryName());
		items.add(item);
		return item;
	}
	
}
