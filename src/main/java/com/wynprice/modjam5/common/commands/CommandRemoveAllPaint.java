package com.wynprice.modjam5.common.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.omg.CORBA.BooleanHolder;

import com.google.common.collect.Lists;
import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.CapabilityHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.CapabilityHandler.IDataInfomationProvider;
import com.wynprice.modjam5.common.network.WorldPaintNetwork;
import com.wynprice.modjam5.common.network.packets.MessagePacketSyncChunk;
import com.wynprice.modjam5.common.utils.BlockPosHelper;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.chunk.Chunk;

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
