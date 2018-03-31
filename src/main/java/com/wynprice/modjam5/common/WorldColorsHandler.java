package com.wynprice.modjam5.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.sun.jna.platform.unix.X11.XClientMessageEvent.Data;
import com.wynprice.modjam5.WorldPaint;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldColorsHandler {
	
	private static HashMap<Integer, HashMap<BlockPos, DataInfomation>> colorMap = new HashMap<>();	
	
	public static final File SAVELOCAION = new File(FMLCommonHandler.instance().getSavesDirectory(), FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName() + "/" + WorldPaint.MODID + "_data.dat");
	
	@SubscribeEvent
	public static void save(WorldEvent.Save event) {
		if(event.getWorld().isRemote) return;		
		try {
			CompressedStreamTools.writeCompressed(saveToNBT(), new FileOutputStream(SAVELOCAION));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SubscribeEvent
	public static void load(WorldEvent.Load event) {
		if(event.getWorld().isRemote) return;
		try {
			NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(SAVELOCAION));
			if(nbt != null)
				readFromNBT(nbt);
		} catch (FileNotFoundException e) {
			save(new WorldEvent.Save(event.getWorld())); //Create a anew file
		}
		catch (IOException e)  {
			e.printStackTrace();
			return;
		}
	}
	
	private static int tickCounter;
	
	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event) {
		if(event.world == null || event.world.provider == null || event.type == Type.SERVER || tickCounter++ % 20 != 0 && colorMap.containsKey(event.world.provider.getDimension())) return;
		Random rand = new Random();
		HashMap<BlockPos, DataInfomation> innerMap = colorMap.get(event.world.provider.getDimension());
		ArrayList<Tuple<Tuple<Integer, BlockPos>, DataInfomation>> appendList = new ArrayList<>();
		if(innerMap != null) {
			for(BlockPos pos : innerMap.keySet()) {
				DataInfomation info = innerMap.get(pos);
				if(info.isSpreadable() && Math.sqrt(info.getOrigin().distanceSq(pos)) < 25 && rand.nextFloat() < 0.5f) {//TODO change to config
					EnumFacing facing = EnumFacing.getFront(rand.nextInt());
					appendList.add(new Tuple<Tuple<Integer, BlockPos>, WorldColorsHandler.DataInfomation>(new Tuple(event.world.provider.getDimension(),  pos.offset(facing)), new DataInfomation(info.getColor(), info.isSpreadable(), info.getOrigin())));
				}
			}
		}
		
		for(Tuple<Tuple<Integer, BlockPos>, DataInfomation> tuple : appendList) {
			putInfo(tuple.getFirst().getFirst(), tuple.getFirst().getSecond(), tuple.getSecond());
		}
	}
	
	public static void putInfo(int dim, BlockPos pos, DataInfomation info) {
		pos = new BlockPos(pos); //Make sure not mutatable
		HashMap<BlockPos, DataInfomation> innerMap = colorMap.get(dim);
		if(innerMap == null) {
			innerMap = new HashMap<>();
		}
		innerMap.put(pos, info);
		colorMap.put(dim, innerMap);
	}
	
	@SideOnly(Side.CLIENT)
	public static DataInfomation getInfo(BlockPos pos) {
		World world = Minecraft.getMinecraft().world;
		if(world != null) {
			return getInfo(world.provider.getDimension(), pos);
		}
		return DataInfomation.DEFAULT;
	}
	
	public static DataInfomation getInfo(int dim, BlockPos pos) {
		return colorMap.containsKey(dim) && colorMap.get(dim).containsKey(pos) ? colorMap.get(dim).get(pos) : DataInfomation.DEFAULT;
	}
	
	public static NBTTagCompound saveToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound nbt_info = new NBTTagCompound();
		NBTTagCompound nbt_worlds = new NBTTagCompound();
		HashMap<Integer, HashMap<BlockPos, DataInfomation>> energized_map = colorMap;
		int[] dimensions = new int[energized_map.size()];
		for(int i = 0; i < dimensions.length; i++)
			dimensions[i] = (int) energized_map.keySet().toArray()[i];
		nbt_info.setIntArray("dimensions", dimensions);
		ArrayList<Integer> list = new ArrayList<>(energized_map.keySet());
		for(int i : list)
		{			
			if(energized_map.get(i) == null)
				continue;
			
			NBTTagCompound nbt_world = new NBTTagCompound();
			int[] blockPositions = new int[energized_map.get(i).size() * 3];
			int index = 0;
			ArrayList<BlockPos> list1 = new ArrayList<>(energized_map.get(i).keySet());
			for(BlockPos pos : list1)
			{
				NBTTagCompound nbt_data = new NBTTagCompound();
				blockPositions[index++] = pos.getX();
				blockPositions[index++] = pos.getY();
				blockPositions[index++] = pos.getZ();
				
				DataInfomation info = energized_map.get(i).get(pos);
				
				nbt_data.setInteger("color", info.getColor());
				nbt_data.setBoolean("doesSpread", info.isSpreadable());
				
				nbt_data.setInteger("originPosX", info.getOrigin().getX());
				nbt_data.setInteger("originPosY", info.getOrigin().getY());
				nbt_data.setInteger("originPosZ", info.getOrigin().getZ());

				nbt_world.setTag(String.valueOf(pos.getX()) + " " + String.valueOf(pos.getY()) + " " + String.valueOf(pos.getZ()), nbt_data);
			}
			nbt_world.setIntArray("blockpos", blockPositions);
			nbt_worlds.setTag("dimension_" + String.valueOf(i), nbt_world);
		}
		
		nbt.setTag("worlds", nbt_worlds);
		nbt.setTag("info", nbt_info);
		
		return nbt;
	}
	
	public static void readFromNBT(NBTTagCompound compound)
	{
		colorMap.clear();
		NBTTagCompound world_info = compound.getCompoundTag("info");
		for(int dimension : world_info.getIntArray("dimensions"))
		{
			NBTTagCompound nbt_world = compound.getCompoundTag("worlds").getCompoundTag("dimension_" + String.valueOf(dimension));
			int[] blockpos = nbt_world.getIntArray("blockpos");
			for(int i = 0; i < blockpos.length; i+=3)
			{
				int posX = blockpos[i];
				int posY = blockpos[i + 1];
				int posZ = blockpos[i + 2];
				NBTTagCompound nbt = nbt_world.getCompoundTag(posX + " " + posY + " " + posZ);
				putInfo(dimension, new BlockPos(posX, posY, posZ), new DataInfomation(nbt.getInteger("color"), nbt.getBoolean("doesSpread"), new BlockPos(nbt.getInteger("originPosX"), nbt.getInteger("originPosY"), nbt.getInteger("originPosZ"))));
			}
		}
	}
	
	public static class DataInfomation {
		
		public static final DataInfomation DEFAULT = new DataInfomation(-1, false, BlockPos.ORIGIN);
		
		private final int color;
		private final boolean isSpreadable;
		private final BlockPos origin;
		
		public DataInfomation(int color, boolean isSpreadable, BlockPos origin) {
			this.color = color;
			this.isSpreadable = isSpreadable;
			this.origin = origin;
		}
		
		public int getColor() {
			return color;
		}
		
		public boolean isSpreadable() {
			return isSpreadable;
		}
		
		public BlockPos getOrigin() {
			return origin;
		}
	}
}
