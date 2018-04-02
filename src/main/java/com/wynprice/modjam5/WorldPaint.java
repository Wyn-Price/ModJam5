package com.wynprice.modjam5;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;

import org.apache.logging.log4j.Logger;

import com.wynprice.modjam5.common.CommonProxy;
import com.wynprice.modjam5.common.WorldCreativeTab;
import com.wynprice.modjam5.common.commands.CommandRemoveAllPaint;
import com.wynprice.modjam5.common.registries.WorldPaintItems;
import com.wynprice.modjam5.common.utils.ColorUtils;

@Mod(modid = WorldPaint.MODID, name = WorldPaint.NAME, version = WorldPaint.VERSION)
public class WorldPaint
{
    public static final String MODID = "worldpaint";
    public static final String NAME = "World Paint";
    public static final String VERSION = "1.0";

    private static Logger logger;
    
    public static final CreativeTabs TAB = new WorldCreativeTab();
    
    @SidedProxy(clientSide="com.wynprice.modjam5.client.ClientProxy",serverSide="com.wynprice.modjam5.server.ServerProxy")
    private static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    	proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit(event);
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
    	event.registerServerCommand(new CommandRemoveAllPaint());
    }
    
    public static Logger getLogger() {
		return logger;
	}
    
    public static CommonProxy getProxy() {
		return proxy;
	}
}
