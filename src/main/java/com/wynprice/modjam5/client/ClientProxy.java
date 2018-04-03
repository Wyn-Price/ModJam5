
package com.wynprice.modjam5.client;

import com.wynprice.modjam5.client.entityrenders.RenderPaintThrown;
import com.wynprice.modjam5.common.CommonProxy;
import com.wynprice.modjam5.common.entities.EntityPaintThrown;
import com.wynprice.modjam5.common.registries.WorldPaintItems;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		RenderingRegistry.registerEntityRenderingHandler(EntityPaintThrown.class, RenderPaintThrown::new);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		Minecraft mc = Minecraft.getMinecraft();
		ItemColors ic = mc.getItemColors();
		ic.registerItemColorHandler((stack, tint) -> tint == 0 ? ColorUtils.getColor(stack) : -1, 
				WorldPaintItems.THROWABLE_PAINT, WorldPaintItems.COLORPICKER, WorldPaintItems.PAINTBRUSH, WorldPaintItems.PAINTING_BOOTS);
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}
}	
