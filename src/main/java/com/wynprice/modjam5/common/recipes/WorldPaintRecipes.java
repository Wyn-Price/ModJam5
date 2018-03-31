package com.wynprice.modjam5.common.recipes;

import java.util.List;

import com.google.common.collect.Lists;
import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.registries.WorldPaintItems;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldPaintRecipes {
	
	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().registerAll(new RecipeBoots());
	}
	
	public static class RecipeBoots extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe{
		
		public RecipeBoots() {
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
	        
	        int color = ((ItemArmor)itemstack.getItem()).getColor(itemstack);
	        
	        NBTTagCompound nbttagcompound = bootStack.getTagCompound();

            if (nbttagcompound == null)
            {
                nbttagcompound = new NBTTagCompound();
                bootStack.setTagCompound(nbttagcompound);
            }

            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

            if (!nbttagcompound.hasKey("display", 10))
            {
                nbttagcompound.setTag("display", nbttagcompound1);
            }

            nbttagcompound1.setInteger("color", color);
	        
			return bootStack;
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
}
