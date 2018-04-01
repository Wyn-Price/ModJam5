package com.wynprice.modjam5.common.recipes;

import com.wynprice.modjam5.common.registries.WorldPaintItems;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeColoredPaint extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public RecipeColoredPaint() {
		setRegistryName("colored_paint");
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack paint = ItemStack.EMPTY;
		ItemStack picker = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if(!itemStack.isEmpty()) {
				if(itemStack.getItem() == WorldPaintItems.COLORPICKER) {
					picker = itemStack;
				} else if(itemStack.getItem() == WorldPaintItems.THROWABLE_PAINT) {
					paint = itemStack;
				} else {
					return false;
				}
			}
		}
		return !paint.isEmpty() && !picker.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack paint = ItemStack.EMPTY;
		ItemStack picker = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if(!itemStack.isEmpty()) {
				if(itemStack.getItem() == WorldPaintItems.COLORPICKER) {
					picker = itemStack;
				} else if(itemStack.getItem() == WorldPaintItems.THROWABLE_PAINT) {
					paint = itemStack;
				} else {
					return ItemStack.EMPTY;
				}
			}
		}
		return ColorUtils.setColor(paint.copy(), ColorUtils.getColor(picker));
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

}
