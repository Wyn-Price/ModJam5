package com.wynprice.modjam5.common.registries;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.recipes.RecipeColoredPaint;
import com.wynprice.modjam5.common.recipes.RecipePaintBoots;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldPaintRecipes {
	
	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().registerAll(new RecipePaintBoots(), new RecipeColoredPaint());
	}	
}
