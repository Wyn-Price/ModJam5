package com.wynprice.modjam5.common.registries;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.recipes.RecipeColorItem;

import net.minecraft.init.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldPaintRecipes {
	
	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().registerAll( //Why are you doing Suppliers ?
				new RecipeColorItem("paintboots", () -> WorldPaintItems.PAINTING_BOOTS),
				new RecipeColorItem("colored_paint", () -> WorldPaintItems.THROWABLE_PAINT),
				new RecipeColorItem("paintbrush", () -> WorldPaintItems.PAINTBRUSH),
				new RecipeColorItem("vanilla_boots", () -> Items.LEATHER_BOOTS),
				new RecipeColorItem("vanilla_chestpiece", () -> Items.LEATHER_CHESTPLATE),
				new RecipeColorItem("vanilla_helmet", () -> Items.LEATHER_HELMET),
				new RecipeColorItem("vanilla_leggings", () -> Items.LEATHER_LEGGINGS)


		);
	}	
}
