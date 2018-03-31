
package com.wynprice.modjam5.client;

import com.wynprice.modjam5.client.entityrenders.RenderPaintThrown;
import com.wynprice.modjam5.common.CommonProxy;
import com.wynprice.modjam5.common.entities.EntityPaintThrown;
import com.wynprice.modjam5.common.registries.WorldPaintItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.registry.IRenderFactory;
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
		ic.registerItemColorHandler((stack, tint) -> {
			 NBTTagCompound nbttagcompound = stack.getTagCompound();

	            if (nbttagcompound != null)
	            {
	                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

	                if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3))
	                {
	                    return nbttagcompound1.getInteger("color");
	                }
	            }

	            return -1;
		}, WorldPaintItems.THROWABLE_PAINT);
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}
}	
