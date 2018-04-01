package com.wynprice.modjam5.common.network;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.network.packets.MessagePacketColorGuiClosed;
import com.wynprice.modjam5.common.network.packets.MessagePacketRequestCapability;
import com.wynprice.modjam5.common.network.packets.MessagePacketSyncChunk;
import com.wynprice.modjam5.common.network.packets.MessagePacketSyncEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class WorldPaintNetwork {
	
	private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(WorldPaint.MODID);

	public static void preInit()
	{
		registerMessage(MessagePacketSyncChunk.class, Side.CLIENT);
		registerMessage(MessagePacketRequestCapability.class, Side.SERVER);
		registerMessage(MessagePacketColorGuiClosed.class, Side.SERVER);
		registerMessage(MessagePacketSyncEntity.class, Side.CLIENT);

	}
	
	private static int idCount = -1;
    public static void registerMessage(Class claz, Side recievingSide)
    {
    	INSTANCE.registerMessage(claz, claz, idCount++, recievingSide);
    }
    
	public static void sendToServer(IMessage message)
	{
		INSTANCE.sendToServer(message);
	}
	
	public static void sendToPlayer(EntityPlayer player, IMessage message)
	{
		if(!player.world.isRemote)
			INSTANCE.sendTo(message, (EntityPlayerMP) player);
	}
	
	public static void sendToPlayersInWorld(World world, IMessage message)
	{
		if(world == null)
			sendToAll(message);
		else if(!world.isRemote)
			INSTANCE.sendToDimension(message, world.provider.getDimension());
	}
	
	public static void sendToAll(IMessage message)
	{
		INSTANCE.sendToAll(message);
	}	
}
