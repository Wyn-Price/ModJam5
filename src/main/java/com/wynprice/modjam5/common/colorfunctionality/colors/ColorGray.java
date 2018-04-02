package com.wynprice.modjam5.common.colorfunctionality.colors;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.DataInfomation;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunctions;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class ColorGray extends ColorFunction {

	public ColorGray() {
		super(0, 0, ColorFunction.RangeType.HUE);
	}
	
	@Override
	public void onWorldTick(World world) {
		List<TileEntity> entityMap = world.loadedTileEntityList;
		int size = world.tickableTileEntities.size();
		for(int i = 0; i < size; i++) {
			if(world.tickableTileEntities.size() <= i) {
				continue;
			}
			TileEntity entry = world.tickableTileEntities.get(i);
			DataInfomation info = WorldColorsHandler.getInfo(world, entry.getPos().down());
			if(!info.isDefault()) {
				ColorFunction function = ColorUtils.findClosestPaletteColorTo(info.getColor());
				if(function == this && entry instanceof TileEntityFurnace && new Random().nextFloat() < 0.5f) { //TODO config
					for(int i2 = 0; i2 < 5; i2++) {//TODO config
						((TileEntityFurnace) entry).update();
					}
				}
			}
		}
	}
	
	private boolean canSmelt(TileEntityFurnace te) {
		if (((ItemStack)te.getStackInSlot(0)).isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(te.getStackInSlot(0));

            if (itemstack.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack itemstack1 = te.getStackInSlot(2);

                if (itemstack1.isEmpty())
                {
                    return true;
                }
                else if (!itemstack1.isItemEqual(itemstack))
                {
                    return false;
                }
                else if (itemstack1.getCount() + itemstack.getCount() <= te.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize())  // Forge fix: make furnace respect stack sizes in furnace recipes
                {
                    return true;
                }
                else
                {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        }
	}
	
	@Override
	public boolean shouldApply(float[] hsb) {
		Color color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
		return Math.max(color.getRed(), Math.max(color.getGreen(), color.getBlue())) > 55 && Math.min(color.getRed(), Math.min(color.getGreen(), color.getBlue())) < 200 && 
				((Math.abs(color.getRed() - color.getGreen())) + (Math.abs(color.getGreen() - color.getBlue()))) / 2f < 15;
	}

}
