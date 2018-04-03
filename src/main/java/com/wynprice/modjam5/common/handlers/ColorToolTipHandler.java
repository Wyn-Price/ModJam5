package com.wynprice.modjam5.common.handlers;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid=WorldPaint.MODID, value=Side.CLIENT)
public class ColorToolTipHandler {
	
	@SubscribeEvent
	public static void onToolTip(ItemTooltipEvent event) {
		if(event.getItemStack().getItem() instanceof IColorToolTip) {
			event.getToolTip().add(1, new TextComponentTranslation("worldpaint.estimatedcolor", new TextComponentTranslation("worldpaint.color." + ColorUtils.findClosestPaletteColorTo(ColorUtils.getColor(event.getItemStack())).getColorName()).getUnformattedText()).getUnformattedText());
		}
	}
	
	public static interface IColorToolTip {
	}
}
