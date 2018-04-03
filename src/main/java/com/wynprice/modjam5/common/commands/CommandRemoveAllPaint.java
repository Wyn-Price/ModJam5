package com.wynprice.modjam5.common.commands;

import org.omg.CORBA.BooleanHolder;

import com.wynprice.modjam5.common.network.WorldPaintNetwork;
import com.wynprice.modjam5.common.network.packets.MessagePacketSyncChunk;
import com.wynprice.modjam5.common.utils.capability.CapabilityHandler;
import com.wynprice.modjam5.common.utils.capability.CapabilityHandler.IDataInfomationProvider;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandRemoveAllPaint extends CommandBase {

	@Override
	public String getName() {
		return "removeallpaint";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.removepaint.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		long millis = System.currentTimeMillis();
		BooleanHolder holder = new BooleanHolder();
		holder.value = true;
		server.getWorld(sender.getEntityWorld().provider.getDimension()).getChunkProvider().id2ChunkMap.values().forEach((chunk) -> {
			IDataInfomationProvider provider = chunk.getCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP);
			if(provider != null) {
				provider.clear();
				WorldPaintNetwork.sendToPlayersInWorld(server.getEntityWorld(), new MessagePacketSyncChunk(chunk).setRefreshChunk(holder.value));
				holder.value = false;
			}
		});
		
		sender.sendMessage(new TextComponentTranslation("commands.removepaint.done", System.currentTimeMillis() - millis));
	}
}
