package com.timvisee.safecreeper;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public class SafeCreeperPaintingListener implements Listener {
	public static SafeCreeper plugin;

	public SafeCreeperPaintingListener(SafeCreeper instance) {
		plugin = instance;
	}
	
	public void createExplosionSound(World world, Location location) {
		plugin.disableOtherExplosions = true;
		world.createExplosion(location, 0, false);
		plugin.disableOtherExplosions = false;
	}
	
	public boolean getConfigSettings(World world, String controlName, String keyName, boolean def, boolean checkEnabled, boolean checkLevel, int currentLevel) {
		if(checkEnabled) {
			if(!configGetBoolean(world.getName(), controlName, "Enabled", false)) {
				return def;
			}
		}
			
		// Check if the user want to use this only between two layers
		if(checkLevel) {
			if(configGetBoolean(world.getName(), controlName + ".EnableBetweenLevels", "Enabled", false)) {
				String levelsString = configGetString(world.getName(), controlName + ".EnableBetweenLevels", "Levels", "0-256");
				List<String> levelsStringSplitted = Arrays.asList(levelsString.split(","));
				for(String levelsStringItem : levelsStringSplitted) {
					List<String> values = Arrays.asList(levelsStringItem.split("-"));
					if(values.size() == 1) {
						int level = Integer.parseInt(values.get(0));
						if(currentLevel == level) {
							return configGetBoolean(world.getName(), controlName, keyName, def);
						}
					} else if(values.size() == 2) {
						int minLevel = Math.min(Integer.parseInt(values.get(0)), Integer.parseInt(values.get(1)));
						int maxLevel = Math.max(Integer.parseInt(values.get(0)), Integer.parseInt(values.get(1)));
						if(currentLevel >= minLevel && currentLevel <= maxLevel) {
							return configGetBoolean(world.getName(), controlName, keyName, def);
						}
					}
				}
				return def;
			} else {
				return configGetBoolean(world.getName(), controlName, keyName, def);
			}
		} else {
			return configGetBoolean(world.getName(), controlName, keyName, def);
		}
	}
	
	// Some YAML config functions
	public boolean configGetBoolean(String world, String node, String key, boolean def) {
		FileConfiguration globalConfig = plugin.getGlobalConfig();
		
		if(plugin.worldConfigExist(world)) {
			FileConfiguration worldConfig =  plugin.getWorldConfig(world);
			
			boolean globalSetting = globalConfig.getBoolean(node + "." + key, def);
			boolean worldSetting = worldConfig.getBoolean(node + "." + key, globalSetting);
			
			return worldSetting;
		} else {			
			boolean globalSetting = globalConfig.getBoolean(node + "." + key, def);
			return globalSetting;
		}
	}
	
	public int configGetInt(String world, String node, String key, int def) {
		FileConfiguration globalConfig = plugin.getGlobalConfig();
		
		if(plugin.worldConfigExist(world)) {
			FileConfiguration worldConfig =  plugin.getWorldConfig(world);
			
			int globalSetting = globalConfig.getInt(node + "." + key, def);
			int worldSetting = worldConfig.getInt(node + "." + key, globalSetting);
			
			return worldSetting;
		} else {
			int globalSetting = globalConfig.getInt(node + "." + key, def);
			return globalSetting;
		}
	}
	
	public String configGetString(String world, String node, String key, String def) {
		FileConfiguration globalConfig = plugin.getGlobalConfig();
		
		if(plugin.worldConfigExist(world)) {
			FileConfiguration worldConfig =  plugin.getWorldConfig(world);
			
			String globalSetting = globalConfig.getString(node + "." + key, def);
			String worldSetting = worldConfig.getString(node + "." + key, globalSetting);
			
			return worldSetting;
		} else {
			String globalSetting = globalConfig.getString(node + "." + key, def);
			return globalSetting;
		}
	}
}
