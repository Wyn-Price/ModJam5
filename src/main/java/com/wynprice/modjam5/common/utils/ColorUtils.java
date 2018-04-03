package com.wynprice.modjam5.common.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunctions;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Class used for centralized color calculations
 * @author Wyn Price
 *
 */
public class ColorUtils {
	/**
	 * Gets the color from the itemstack. Uses the vanilla method
	 * @param stack The input stack
	 * @return The color in the stack, or 0xFFFFFF (16777215) if it could not be found
	 */
	public static int getColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null)
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

            if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3))
            {
                return nbttagcompound1.getInteger("color") & 0xFFFFFF;
            }
        }

        return 16777215;
	}

	/**
	 * Sets the color to the item stack, using the vanilla method
	 * @param itemstack the itemstack
	 * @param color the color
	 * @return {@code itemstack}
	 */
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
	
	public static ColorFunction calculateClosestColor(int color) {
		ArrayList<ColorFunction> acceptedFunction = new ArrayList<>();

		for(ColorFunction function : ColorFunctions.ALL_FUNCTIONS) {
			if(function.shouldApply(Color.RGBtoHSB((color>>16)&0xFF, (color>>8)&0xFF, (color>>0)&0xFF, null))) {
				acceptedFunction.add(function);
			}
		}
		
		if(acceptedFunction.contains(ColorFunctions.GRAY)) {
			return ColorFunctions.GRAY;
		} else if(acceptedFunction.contains(ColorFunctions.WHITE)) {
			return ColorFunctions.WHITE;
		} else if(acceptedFunction.contains(ColorFunctions.BLACK)) {
			return ColorFunctions.BLACK;
		}
		
		if(acceptedFunction.isEmpty()) {
			acceptedFunction.add(ColorFunctions.NONE);
		}
		return acceptedFunction.get(new Random().nextInt(acceptedFunction.size()));
    }
}
