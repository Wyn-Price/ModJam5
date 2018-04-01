package com.wynprice.modjam5.common;

import java.util.ArrayList;
import java.util.HashMap;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.network.WorldPaintNetwork;
import com.wynprice.modjam5.common.network.packets.MessagePacketRequestCapability;
import com.wynprice.modjam5.common.network.packets.MessagePacketSyncChunk;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldColorsHandler {
	
	@SubscribeEvent
	public static void chunkRegistry(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(new ResourceLocation(WorldPaint.MODID, "colorProvider"), new CapabilityHandler.ColorCapabilityProvider());
	}
	
	private static final ArrayList<ChunkPos> requestedChunks = new ArrayList<>();
	
	public static DataInfomation getInfo(World worldIn, BlockPos pos) {
		if(worldIn == null) return DataInfomation.DEFAULT; //Not usually called
		Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
		CapabilityHandler.IDataInfomationProvider cap = chunk.hasCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) ? chunk.getCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) : null;
		if(cap != null) {
			if(!cap.hasSynced() && !requestedChunks.contains(chunk.getPos())) {
				requestedChunks.add(chunk.getPos());
				WorldPaintNetwork.sendToServer(new MessagePacketRequestCapability(chunk));
			}
			if(cap.hasSynced() && requestedChunks.contains(chunk.getPos())) {
				requestedChunks.remove(chunk.getPos());
			}
			DataInfomation info = cap.getMap().get(pos);
			return info == null ? DataInfomation.DEFAULT : info;
		}
		return DataInfomation.DEFAULT;
	}
	
	public static void putInfo(World worldIn, BlockPos pos, DataInfomation info) {
		Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
		CapabilityHandler.IDataInfomationProvider cap = chunk.hasCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) ? chunk.getCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) : null;
		if(cap != null) {
			cap.getMap().put(pos, info);
			WorldPaintNetwork.sendToAll(new MessagePacketSyncChunk(worldIn.getChunkFromChunkCoords(chunk.x, chunk.z)));

		}
	}
	
	public static class DataInfomation {
				
		public static final DataInfomation DEFAULT = new DataInfomation(-1, false, BlockPos.ORIGIN, new int[0]);
		
		private final int color;
		private final boolean isSpreadable;
		private final BlockPos origin;
		private int[] spreadTo = new int[0];
		
		public DataInfomation(int color, boolean isSpreadable, BlockPos origin, int[] spreadTo) {
			this.color = color;
			this.isSpreadable = isSpreadable;
			this.origin = origin;
			this.spreadTo = spreadTo;

		}
		
		public int getColor() {
			return color;
		}
		
		public boolean isSpreadable() {
			return isSpreadable;
		}

		public final boolean isDefault() {
			return this == DEFAULT;
		}
		
		public BlockPos getOrigin() {
			return origin;
		}
		
		public int getX() {
			return origin.getX();
		}
		
		public int getY() {
			return origin.getY();
		}
		
		public int getZ() {
			return origin.getZ();
		}
		
		public int[] getSpreadTo() {
			return spreadTo;
		}
		
		public void addFace(EnumFacing face) {
			int[] aint = new int[this.spreadTo.length + 1];
			for(int i = 0; i < this.spreadTo.length; i++) {
				aint[i] = this.spreadTo[i];
			}
			aint[this.spreadTo.length] = face.getIndex();
		}
	}
	
	/**
	 * If you're hear to tell me how bad the capability is, I know. It works so im not touching it
	 * @author Wyn Price
	 *
	 */
	public static class CapabilityHandler {
	
		@CapabilityInject(IDataInfomationProvider.class)
	    public static Capability<IDataInfomationProvider> DATA_CAPABILITY = null;
		
		public static class ColorCapabilityProvider implements ICapabilitySerializable<NBTBase> {
	
			private final DefaultImpl defaultImpl = new DefaultImpl();
			
			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
				if(capability == DATA_CAPABILITY) {
					return true;
				}
				return false;
			}
	
			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
				if(capability == DATA_CAPABILITY) {
					return (T) defaultImpl;
				}
				return null;
			}

			@Override
			public NBTBase serializeNBT() {
				return ColorStorage.INSTANCE.writeNBT(DATA_CAPABILITY, defaultImpl, EnumFacing.UP);
			}

			@Override
			public void deserializeNBT(NBTBase nbt) {
				ColorStorage.INSTANCE.readNBT(DATA_CAPABILITY, defaultImpl, EnumFacing.UP, nbt);

			}
			
		}
		
		public static interface IDataInfomationProvider {
			HashMap<BlockPos, DataInfomation> getMap();
			
			boolean hasSynced();
			
			/**Internal use only*/
			void sync();
		}
		
		public static class DefaultImpl implements IDataInfomationProvider {
			private final HashMap<BlockPos, DataInfomation> map = new HashMap<>();
			private boolean synced;
			
			@Override
			public HashMap<BlockPos, DataInfomation> getMap() {
				return map;
			}
			
			@Override
			public void sync() {
				this.synced = true;
			}
			
			@Override
			public boolean hasSynced() {
				return synced;
			}
		}
		
		public static enum ColorStorage implements IStorage<IDataInfomationProvider> {
			
			INSTANCE;
			
			@Override
			public NBTBase writeNBT(Capability<IDataInfomationProvider> capability, IDataInfomationProvider instance,
					EnumFacing side) {
				NBTTagCompound nbt = new NBTTagCompound();
				for(BlockPos pos : instance.getMap().keySet()) {
					NBTTagCompound nbt_data = new NBTTagCompound();
					DataInfomation info = instance.getMap().get(pos);
					
					nbt_data.setInteger("color", info.getColor());
					nbt_data.setBoolean("doesSpread", info.isSpreadable());
					
					nbt_data.setInteger("originPosX", info.getX());
					nbt_data.setInteger("originPosY", info.getY());
					nbt_data.setInteger("originPosZ", info.getZ());
					
					nbt_data.setIntArray("spreadTo", info.getSpreadTo());
					
					nbt.setTag(pos.getX() + " " + pos.getY() + " " + pos.getZ(), nbt_data);
				}
				return nbt;
			}

			@Override
			public void readNBT(Capability<IDataInfomationProvider> capability, IDataInfomationProvider instance,
					EnumFacing side, NBTBase nbt) {
	            NBTTagCompound tag = (NBTTagCompound) nbt;
				for(String key : tag.getKeySet()) {
					NBTTagCompound data = tag.getCompoundTag(key);
					instance.getMap().put(new BlockPos(Integer.valueOf(key.split(" ")[0]), Integer.valueOf(key.split(" ")[1]), Integer.valueOf(key.split(" ")[2])),
					new DataInfomation(
							data.getInteger("color"), 
							data.getBoolean("doesSpread"), 
							new BlockPos(data.getInteger("originPosX"), data.getInteger("originPosY"), data.getInteger("originPosZ")), 
							data.getIntArray("spreadTo")
					));
				}
				
				instance.sync();
			}
			
		}	
	}
}
