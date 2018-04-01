package com.wynprice.modjam5.common;

import java.awt.Color;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.registries.WorldPaintItems;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class WorldCreativeTab extends CreativeTabs {

	public WorldCreativeTab() {
		super(WorldPaint.MODID);
	}

	@Override
	public ItemStack getIconItemStack() {
		return ColorUtils.setColor(new ItemStack(WorldPaintItems.COLORPICKER), Color.HSBtoRGB((float) (System.currentTimeMillis() / 10000d % 1d), 1f, 1f)); 
	}
	
	@Override
	public ItemStack getTabIconItem() {
		return ItemStack.EMPTY;
	}

}
