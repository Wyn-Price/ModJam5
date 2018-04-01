package com.wynprice.modjam5.common.recipes;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.registries.WorldPaintItems;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipePaintBoots extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	
	public RecipePaintBoots() {
		setRegistryName(WorldPaint.MODID, "paintboots");
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack bootStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() instanceof ItemArmor)
                {
                    ItemArmor itemarmor = (ItemArmor)itemstack1.getItem();

                    if(itemstack1.getItem() == WorldPaintItems.PAINTING_BOOTS) {
                    	if(bootStack.isEmpty()) {
    	                    bootStack = itemstack1;
                    	} else {
                    		return false;
                    	}
                    } else {
                    	if(itemarmor.hasColor(itemstack1) && itemstack.isEmpty()) {
    	                    itemstack = itemstack1;
                    	} else {
                    		return false;
                    	}
                    }
                } else {
                	return false;
                }
            }
        }

        return !itemstack.isEmpty() && !bootStack.isEmpty();
    }

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack itemstack = ItemStack.EMPTY;
        ItemStack bootStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() instanceof ItemArmor)
                {
                    ItemArmor itemarmor = (ItemArmor)itemstack1.getItem();

                    if(itemstack1.getItem() == WorldPaintItems.PAINTING_BOOTS) {
                    	if(bootStack.isEmpty()) {
    	                    bootStack = itemstack1;
                    	} else {
                    		return ItemStack.EMPTY;
                    	}
                    } else {
                    	if(itemarmor.hasColor(itemstack1) && itemstack.isEmpty()) {
    	                    itemstack = itemstack1;
                    	} else {
                    		return ItemStack.EMPTY;
                    	}
                    }
                } else {
                	return ItemStack.EMPTY;
                }
            }
        }
        return ColorUtils.setColor(bootStack,  ((ItemArmor)itemstack.getItem()).getColor(itemstack));
	}

	@Override
	public boolean canFit(int width, int height) {
        return width * height >= 2;
    }
	
	@Override
	public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
}
