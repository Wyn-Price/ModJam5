package com.wynprice.modjam5.common;

import com.wynprice.modjam5.common.network.WorldPaintNetwork;
import com.wynprice.modjam5.common.utils.capability.CapabilityHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		CapabilityManager.INSTANCE.register(CapabilityHandler.IDataInfomationProvider.class, CapabilityHandler.ColorStorage.INSTANCE, CapabilityHandler.DefaultImpl::new);
		WorldPaintNetwork.preInit();
	}
	
	public void init(FMLInitializationEvent event) {
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}

	public EntityPlayer getPlayer() {
		return null;
	}

}
