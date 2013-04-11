package com.timvisee.safecreeper.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.manager.SCConfigManager;

public class SCWorldListener implements Listener {
	
	@EventHandler
	public void onWorldLoad(WorldLoadEvent e) {
		World w = e.getWorld();
		Location l = w.getSpawnLocation();
		SCConfigManager cm = SafeCreeper.instance.getConfigManager();
		Random rand = new Random();
		
		// Check if the world control is enabled
		if(cm.isControlEnabled(w.getName(), "WorldControl", false, l)) {
			// Should the world keep it's spawn in it's memory
			final boolean keepSpawnInMemory = cm.getOptionBoolean(w, "WorldControl", "KeepSpawnInMemory", w.getKeepSpawnInMemory(), true, l);
			w.setKeepSpawnInMemory(keepSpawnInMemory);
			
			// Has the world a custom difficulty
			if(cm.getOptionBoolean(w, "WorldControl", "CustomDifficulty.Enabled", false, true, l)) {
				// Get the difficulty
				int diff = cm.getOptionInt(w, "WorldControl", "CustomDifficulty.WorldDifficulty", w.getDifficulty().getValue(), true, l);
				
				// Change the difficulty of the world
				w.setDifficulty(Difficulty.getByValue(diff));
			}
			
			// Has the world a custom difficulty
			if(cm.getOptionBoolean(w, "WorldControl", "CustomSpawning.Enabled", false, true, l)) {
				// Get if animals and monsters are allowed to spawn
				boolean allowAnimals = cm.getOptionBoolean(w, "WorldControl", "CustomSpawning.AllowAnimals", true, true, l);
				boolean allowMonsters = cm.getOptionBoolean(w, "WorldControl", "CustomSpawning.AllowMonsters", true, true, l);
				
				// Set if animals and monsters are allowed to spawn
				w.setSpawnFlags(allowAnimals, allowMonsters);
				
				// Set the custom spawn limits
				w.setAnimalSpawnLimit(cm.getOptionInt(w, "WorldControl", "CustomSpawning.AnimalSpawnLimit", w.getAnimalSpawnLimit(), true, l));
				w.setMonsterSpawnLimit(cm.getOptionInt(w, "WorldControl", "CustomSpawning.MonsterSpawnLimit", w.getMonsterSpawnLimit(), true, l));
				w.setAmbientSpawnLimit(cm.getOptionInt(w, "WorldControl", "CustomSpawning.AmbientSpawnLimit", w.getAmbientSpawnLimit(), true, l));
				w.setWaterAnimalSpawnLimit(cm.getOptionInt(w, "WorldControl", "CustomSpawning.WaterAnimalSpawnLimit", w.getWaterAnimalSpawnLimit(), true, l));
				
				// Set the ticks per animal and monster spawns
				w.setTicksPerAnimalSpawns(cm.getOptionInt(w, "WorldControl", "CustomSpawning.TicksPerAnimalSpawns", (int) w.getTicksPerAnimalSpawns(), true, l));
				w.setTicksPerMonsterSpawns(cm.getOptionInt(w, "WorldControl", "CustomSpawning.TicksPerMonsterSpawns", (int) w.getTicksPerMonsterSpawns(), true, l));
			}
			
			// Set if the world should auto save
			w.setAutoSave(cm.getOptionBoolean(w, "WorldControl", "AutoSave", w.isAutoSave(), true, l));
			
			// Set if PVP is enabled in this world
			w.setPVP(cm.getOptionBoolean(w, "WorldControl", "PVP", w.getPVP(), true, l));
			
			// Check if custom game rules are enabled
			if(cm.getOptionBoolean(w, "WorldControl", "CustomGameRules.Enabled", false, true, l)) {
				// Get a list of custom game rules
				List<String> keys = cm.getOptionKeysList(w, "WorldControl", "CustomGameRules.Rules", new ArrayList<String>(), true, l);
				
				// Apply all the game rules
				for(String rule : keys) {
					if(!w.isGameRule(rule)) {
						System.out.println("Unknown game rule: '" + rule + "'");
						continue;
					}
					
					// Get the value to set the game rule to
					String value = cm.getOptionString(w, "WorldControl", "CustomGameRules.Rules." + rule, w.getGameRuleValue(rule), true, l);
					
					// Make sure the value is valid
					if(!value.equals("true") && !value.equals("false")) {
						System.out.println("Unknown game rule value for '" + rule + "': '" + value + "'");
						continue;
					}
					
					// Set the game rule
					w.setGameRuleValue(rule, value);
				}
			}
		}
		
		// Check if it should always rain or thunder
		boolean alwaysRain = cm.getOptionBoolean(w, "WeatherControl", "AlwaysRain", false, true, l);
		boolean alwaysThunderStorm = cm.getOptionBoolean(w, "WeatherControl", "AlwaysThunderStorm", false, true, l);
		
		// If it should always rain, make it rain in the world
		if(alwaysRain && !w.hasStorm()) {
			// Make it storm in the world
			w.setStorm(true);
			
			// Get the default weather duration
			int duration = w.getWeatherDuration();
			
			// Apply the custom weather duration
			if(cm.getOptionBoolean(w, "WeatherControl", "CustomDurations.Enabled", false, true, l)) {
				if(cm.getOptionBoolean(w, "WeatherControl", "CustomDurations.Weather.Enabled", false, true, l)) {
					int minDuration = cm.getOptionInt(w, "WeatherControl", "CustomDurations.Weather.MinDuration", 720, true, l);
					int maxDuration = cm.getOptionInt(w, "WeatherControl", "CustomDurations.Weather.MaxDuration", 1080, true, l);
					
					duration = Math.max(minDuration + rand.nextInt(maxDuration - minDuration), 1);
				}
			}
			
			// Set the weather duration
			w.setWeatherDuration(duration);
		}
		
		// If it should always thunder, make it thunder in the world
		if(alwaysThunderStorm && !w.isThundering()) {
			// Make it thundering in the world
			w.setThundering(true);
			
			// Get the default thunder duration
			int duration = w.getThunderDuration();
			
			// Apply the custom thunder duration
			if(cm.getOptionBoolean(w, "WeatherControl", "CustomDurations.Enabled", false, true, l)) {
				if(cm.getOptionBoolean(w, "WeatherControl", "CustomDurations.ThunderStorm.Enabled", false, true, l)) {
					int minDuration = cm.getOptionInt(w, "WeatherControl", "CustomDurations.ThunderStorm.MinDuration", 240, true, l);
					int maxDuration = cm.getOptionInt(w, "WeatherControl", "CustomDurations.ThunderStorm.MaxDuration", 360, true, l);
					
					duration = Math.max(minDuration + rand.nextInt(maxDuration - minDuration), 1);
				}
			}
			
			// Set the thunder duration
			w.setThunderDuration(duration);
		}
		
		// Is rain, thunder and clear weather allowed in this world
		boolean clearAllowed = true;
		boolean rainAllowed = true;
		boolean thunderAllowed = true;
		if(cm.getOptionBoolean(w, "WeatherControl", "CustomWeatherTypes.Enabled", false, true, l)) {
			clearAllowed = cm.getOptionBoolean(w, "WeatherControl", "CustomWeatherTypes.Clear.Allowed", true, true, l);
			rainAllowed = cm.getOptionBoolean(w, "WeatherControl", "CustomWeatherTypes.Rain.Allowed", true, true, l);
			rainAllowed = cm.getOptionBoolean(w, "WeatherControl", "CustomWeatherTypes.ThunderStorm.Allowed", true, true, l);
		}
		
		// Is clear allowed
		if(!w.hasStorm() && !w.isThundering() && !clearAllowed)
			w.setStorm(true);
		
		// Is rain allowed
		if(w.hasStorm() && !rainAllowed)
			w.setStorm(false);
		
		// Is thunder allowed
		if(w.isThundering() && !thunderAllowed)
			w.setThundering(false);
	}
}
