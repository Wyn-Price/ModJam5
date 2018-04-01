package com.wynprice.modjam5.client.handlers;

import java.util.HashMap;

import com.wynprice.modjam5.WorldPaint;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid=WorldPaint.MODID, value=Side.CLIENT)
public class EntityUpdateHandler {
	
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		if(event.player.world.isRemote) {
			for(Entity entity : event.player.world.getLoadedEntityList()) {
				if(ENTITY_MAP.containsKey(entity.getEntityId())) {
					entity.readFromNBT(ENTITY_MAP.get(entity.getEntityId()));
					ENTITY_MAP.remove(entity.getEntityId());
				}
			}
		}
			
	}
	
	private static final HashMap<Integer, NBTTagCompound> ENTITY_MAP = new HashMap<>();
	
	public static void addToMap(int id, NBTTagCompound tag)
	{
		ENTITY_MAP.put(id, tag);
	}
}
