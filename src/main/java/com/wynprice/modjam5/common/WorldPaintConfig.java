package com.wynprice.modjam5.common;

import java.util.ArrayList;

import com.wynprice.modjam5.WorldPaint;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid=WorldPaint.MODID, category = "")
@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldPaintConfig {
	
	public static final General GENERAL = new General();
	public static final ColorValues COLOR_VALUES = new ColorValues();
	public static final ColorFunctions COLOR_FUNCTIONS = new ColorFunctions();

	
	public static class General {
		@Config.Comment("The ID of the EntityThrownPaint")
		@Config.Name("entity_thrown_id")
		public int entityThrownID = 65;
		
		@Config.Comment("The level of blending that will be used between colors. A higher number will take longer")
		@Config.Name("color_blend")
		@Config.RangeInt(min=0,max=100)
		public int colorBlend = 1;
		
		@Config.Comment("The chance that, per random tick on a painted block, the block will spread")
		@Config.Name("spread_change")
		public float spreadChance;
		
		@Config.Comment("The list of blocks that can have paint spreaded to them. These will also be the blocks that determin if a color effect is used or not")
		@Config.Name("spreadable_blocks")
		public String[] allowedBlocks = {
				"minecraft:grass",
				"minecraft:dirt",
				"minecraft:farmland",
				
				"minecraft:leaves",
				"minecraft:leaves2",
				
				"minecraft:log",
				"minecraft:log2",
				
				"minecraft:water",
				"minecraft:flowing_water",
				
				"minecraft:vine",
				"minecraft:waterlily",
				
				"minecraft:tallgrass",
				"minecraft:double_plant",
				"minecraft:reeds",
				"minecraft:wheat"
		};
		
		@Config.Comment("The minimum and maximum size for the paint explosion caused by the EntityPaintThrown. This is the distance from the hit position, to the edge. Meaning the box that is created is double this length")
		@Config.Name("min_paint_explosion_size")
		public int minPaintExplosion = 4;		
		@Config.Name("max_paint_explosion_size")
		public int maxPaintExplosion = 7;
		
		@Config.Ignore
		ArrayList<Block> cache = null;
		
		public ArrayList<Block> getAllowedBlocks() {
			if(cache == null) {
				cache = new ArrayList<>();
				for(String string : allowedBlocks) {
					Block block = Block.getBlockFromName(string);
					if(block == null || block == Blocks.AIR) {
						continue;
					}
					cache.add(block);
				}
			}
			return cache;
		}
	}
	
	public static class ColorFunctions {
		@Config.Comment("When the areas painted black, the probability that, on a random block tick, a hostile mob will spawn")
		@Config.Name("black_chance_spawn_hostile")
		public float blackSpawnHostile = 0.05f;
		
		@Config.Comment("Should any entity in a blue painted area be given a jump boost effect")
		@Config.Name("blue_jump_boost")
		public boolean blueJumpBoost = true;
		
		@Config.Comment("Should any entity in a blue painted area have fall damage negated")
		@Config.Name("blue_stop_fall_damage")
		public boolean blueFallDamage = true;
		
		@Config.Comment("Should any entity in a blue painted area bounce on the blocks (like slime blocks)")
		@Config.Name("blue_bouncy_blocks")
		public boolean blueBouncyBlocks = true;
		
		@Config.Comment("When the areas painted gray, the probability that, per tick, the furnaces are updated\nAnd the amount of ticks the furnaces are updated by")
		@Config.Name("gray_chance_furnace_smelt")
		public float grayFurnaceSmelt = 0.05f;
		@Config.Name("gray_ticks_furnace_smelt")
		public int grayFurnaceAmount = 5;
		
		@Config.Comment("When the areas painted green, the probability that, on a random block tick where the block ticked is a leaf block, that leaf block will plant saplings nearby")
		@Config.Name("green_chance_plant_sapling")
		public float greenPlantSaplings = 0.05f;
		
		@Config.Comment("When the areas painted green, the probability that, on a random block tick, the bonemeal effect will take place")
		@Config.Name("green_chance_bonemeal_area")
		public float greenBonemealArea = 0.5f;
	
		@Config.Comment("When the areas painted green, the probability that, on a random block tick, dirt will be turned into grass")
		@Config.Name("green_change_dirt_to_grass")
		public boolean greenChangeDirt = true;
		
		@Config.Comment("Should every block in an area painted orange be slippy")
		@Config.Name("orange_slippy_blocks")
		public boolean orangeSlippyBlocks = true;
	
		@Config.Comment("When the areas painted pink, the probability that, on a random block tick, a peacfull mob will spawn")
		@Config.Name("pink_chance_spawn_peacfull")
		public float pinkSpawnPeacfull = 0.005f;
	
		@Config.Comment("Should any entity in a purple painted area be given strength")
		@Config.Name("purple_strength")
		public boolean purpleStrength = true;
		
		@Config.Comment("Should any undead entity in a red painted area, be extinguished when theyre on fire")
		@Config.Name("red_undead_extinguish")
		public boolean redUndeadExtinguished = true;
		
		@Config.Comment("Should any non undead entities, in a red painted area, be set on fire")
		@Config.Name("red_not_undead_burn")
		public boolean redNormalBurn = true;
		
		@Config.Comment("Should any entity in a white painted area be turned invisible")
		@Config.Name("white_invisible")
		public boolean whiteInvisible = true;
		
		@Config.Comment("The Health Modifier Options for when in a yellow painted area.")
		@Config.RequiresMcRestart
		@Config.Name("yellow_modifier_operation")
		public int yellowModifierOperation = 1;
		@Config.RequiresMcRestart
		@Config.Name("yellow_modifier_amount")
		public double yellowModifierAmount = 0.5D;
		
		@Config.Comment("How many seconds it takes while in a yellow painted area for hunger to be refilled, and how much hunger and saturation is refilled")
		@Config.Name("yellow_food_seconds")
		public float yellowFoodSeconds = 20;
		@Config.Name("yellow_food_level")
		public int yellowFoodLevel = 1;
		@Config.Name("yellow_food_saturation")
		public float yellowFoodSaturation = 1F;
		
	}
	
	public static class ColorValues {
		@Config.Comment("The minimum and maximum hue for the effect of the blue color to be active. Between 0 - 360")
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("blue_min")
		public double blueMin = 203d;

		@Config.RangeDouble(min=0,max=360)
		@Config.Name("blue_max")
		public double blueMax = 250d;

		

	    @Config.Comment("The minimum and maximum hue for the effect of the green color to be active. Between 0 - 360")
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("green_min")
		public double greenMin = 70d;
	    
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("green_max")
		public double greenMax = 153d;

		

	    @Config.Comment("The minimum and maximum hue for the effect of the purple color to be active. Between 0 - 360")
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("purple_min")
		public double purpleMin = 240d;

		@Config.RangeDouble(min=0,max=360)
		@Config.Name("purple_max")
		public double purpleMax = 290d;

		

	    @Config.Comment("The minimum and maximum hue for the effect of the red color to be active. Between 0 - 360\nFor red, the default would be 0 - 12 and 350 - 360. It loops around")
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("red_min")
		public double redMin = 350d;
	    
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("red_max")
		public double redMax = 12d;

		
		
	    @Config.Comment("The minimum and maximum hue for the effect of the yellow color to be active. Between 0 - 360")
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("yellow_min")
		public double yellowMin = 43f;

		@Config.RangeDouble(min=0,max=360)
		@Config.Name("yellow_max")
		public double yellowMax = 68f;

		

	    @Config.Comment("The minimum and maximum hue for the effect of the orange color to be active. Between 0 - 360")
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("orange_min")
		public double orangeMin = 13d;

		@Config.RangeDouble(min=0,max=360)
		@Config.Name("orange_max")
		public double orangeMax = 50d;


		
	    @Config.Comment("The minimum and maximum hue for the effect of the pink color to be active. Between 0 - 360")
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("pink_min")
		public double pinkMin = 295d;
	    
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("pink_max")
		public double pinkMax = 325d;
		
		
		
		
		@Config.Comment("The minimum and maximum rgb values, and the range for the rbg values, that are needed for the effect of the gray color to be active. Between 0 - 360\n")
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("gray_min")
		public double grayMin = 55d;
		    
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("gray_max")
		public double grayMax = 200d;
		
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("gray_range")
		public double grayRange = 15d;
		
		
		
		
		@Config.Comment("The Point of which if all rbg values are lower than this number, the color black will be chosen")
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("black_dropoff")
		public double blackDropOff = 55; 
		
		@Config.Comment("The Point of which if all rbg values are higher than this number, the color white will be chosen")
		@Config.RangeDouble(min=0,max=360)
		@Config.Name("white_dropoff")
		public double whiteDropOff = 200; 
	}
	
	@SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(WorldPaint.MODID.equals(event.getConfigID())) {
            ConfigManager.sync(WorldPaint.MODID, Config.Type.INSTANCE);
            GENERAL.cache = null;
        }
    }
}
