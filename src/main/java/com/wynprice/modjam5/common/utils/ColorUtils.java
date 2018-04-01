package com.wynprice.modjam5.common.utils;

import com.wynprice.modjam5.common.colorfunctionality.EnumColorBehaviour;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ColorUtils {
	public static int getColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null)
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

            if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3))
            {
                return nbttagcompound1.getInteger("color");
            }
        }

        return 16777215;
	}

	public static ItemStack setColor(ItemStack itemstack, int color) {
		NBTTagCompound nbttagcompound = itemstack.getTagCompound();

        if (nbttagcompound == null)
        {
            nbttagcompound = new NBTTagCompound();
            itemstack.setTagCompound(nbttagcompound);
        }

        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

        if (!nbttagcompound.hasKey("display", 10))
        {
            nbttagcompound.setTag("display", nbttagcompound1);
        }

        nbttagcompound1.setInteger("color", color);		
	
        return itemstack;
	}
	
	public static EnumColorBehaviour findClosestPaletteColorTo(int color) {
		EnumColorBehaviour closestColor = null;
        double closestDistance = Integer.MAX_VALUE;
        for (EnumColorBehaviour behaviour : EnumColorBehaviour.values()) {
        	PaletteColor paletteColor = behaviour.getPaletteColor();
            double distance = paletteColor.distanceTo(color);
//            System.out.println(behaviour + "" + distance);
            if (distance <= closestDistance) {
                closestDistance = distance;
                closestColor = paletteColor.getBehaviour();
            }
        }
        return closestColor;
    }
}
