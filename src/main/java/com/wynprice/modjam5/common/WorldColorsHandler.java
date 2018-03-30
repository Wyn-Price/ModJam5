package com.wynprice.modjam5.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.wynprice.modjam5.WorldPaint;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		System.out.println(SAVELOCAION);
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
	
	public static void putColor(int dim, BlockPos pos, DataInfomation info) {
		pos = new BlockPos(pos); //Make sure not mutatable
		HashMap<BlockPos, DataInfomation> innerMap = colorMap.containsKey(dim) ? new HashMap<>() : colorMap.get(dim);
		innerMap.put(pos, info);
		colorMap.put(dim, innerMap);
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
				nbt_data.setInteger("color", energized_map.get(i).get(pos).getColor());
				nbt_data.setBoolean("doesSpread", energized_map.get(i).get(pos).isSpreadable());
				
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
				putColor(dimension, new BlockPos(posX, posY, posZ), new DataInfomation(nbt.getInteger("color"), nbt.getBoolean("doesSpread")));
			}
		}
	}
	
	public static class DataInfomation {
		private final int color;
		private final boolean isSpreadable;
		
		public DataInfomation(int color, boolean isSpreadable) {
			this.color = color;
			this.isSpreadable = isSpreadable;
		}
		
		public int getColor() {
			return color;
		}
		
		public boolean isSpreadable() {
			return isSpreadable;
		}
	}
}
