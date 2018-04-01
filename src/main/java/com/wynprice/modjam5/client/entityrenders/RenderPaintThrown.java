package com.wynprice.modjam5.client.entityrenders;

import com.wynprice.modjam5.common.entities.EntityPaintThrown;
import com.wynprice.modjam5.common.registries.WorldPaintItems;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RenderPaintThrown extends RenderSnowball<EntityPaintThrown>{

	public RenderPaintThrown(RenderManager renderManagerIn) {
		super(renderManagerIn, WorldPaintItems.THROWABLE_PAINT, Minecraft.getMinecraft().getRenderItem());
	}
	
	@Override
	public ItemStack getStackToRender(EntityPaintThrown entityIn) {
		return ColorUtils.setColor(new ItemStack(WorldPaintItems.THROWABLE_PAINT), entityIn.getColor());
	}
}
