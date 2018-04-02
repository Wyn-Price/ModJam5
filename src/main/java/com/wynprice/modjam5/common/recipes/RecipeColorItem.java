package com.wynprice.modjam5.common.recipes;

import java.util.function.Supplier;

import com.wynprice.modjam5.common.registries.WorldPaintItems;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeColorItem extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	private final Supplier<Item> itemsupplier;
	
	public RecipeColorItem(String name, Supplier<Item> itemsupplier) {
		this.itemsupplier = itemsupplier;
		setRegistryName(name);
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
				} else if(itemStack.getItem() == itemsupplier.get()) {
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
				} else if(itemStack.getItem() == itemsupplier.get()) {
					paint = itemStack;
				} else {
					return ItemStack.EMPTY;
				}
			}
		}
		ItemStack stack = ColorUtils.setColor(paint.copy(), ColorUtils.getColor(picker));
		stack.setCount(1);
		return stack;
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
