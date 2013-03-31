package com.timvisee.safecreeper.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.timvisee.safecreeper.SafeCreeper;

public class SCFileUpdater {
	
	/**
	 * Update all Safe Creeper files if there's any need for that
	 */
	public void updateFiles() {
		// Update main config file
		updateConfig();
		
		// Update the global config file
		updateGlobalConfig();
		
		// Update the world configs
		updateAllWorldsConfig();
	}
	
	/**
	 * Check if a config file is up-to-date
	 * @param f the config file
	 * @return true if it's up-to-date
	 */
	public boolean isConfigUpToDate(File f) {
		// Check if the file exists
		if(!f.exists())
			return false;
		
		// Load the shops file
		YamlConfiguration c = new YamlConfiguration();
		try {
			c.load(f);
		} catch (FileNotFoundException e) {
			System.out.println("[SafeCreeper] Error while loading config file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("[SafeCreeper] Error while loading config file!");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			System.out.println("[SafeCreeper] Error while loading config file!");
			e.printStackTrace();
		}
		
		return isConfigUpToDate(c);
	}
	
	/**
	 * Check if the config is up-to-date
	 * @param c the config to check
	 * @return true if it's up-to-date
	 */
	public boolean isConfigUpToDate(Configuration c) {
		String pluginVer = SafeCreeper.instance.getDescription().getVersion();
		String configVer = c.getString("version", "0.1");
		
		// If the config files equals to the config version, return true
		return (!isOlderVersion(pluginVer, configVer));
	}
	
	@SuppressWarnings("unused")
	public void updateConfig() {
		// Get the global config file
		FileConfiguration c = SafeCreeper.instance.getConfigManager().getGlobalConfig();
		
		// Check if the file is up-to-date, if so, cancel the update progress
		if(isConfigUpToDate(c))
			return;
		
		System.out.println("[SafeCreeper] Updating the Safe Creeper config file...");
		
		long t = System.currentTimeMillis();
		
		// Get the current plugin and config versions
		String pluginVer = SafeCreeper.instance.getDescription().getVersion();
		String configVer = c.getString("version", "0.1");
		
		// Load the default global config file 
		YamlConfiguration defc = new YamlConfiguration();
		try {
			defc.load(SafeCreeper.instance.getResource("res/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		// Make sure the default config file is loaded
		if(defc == null)
			return;
		
		// Backup the current file version, so it won't be lost if something goes wrong
		backupConfig();
		
		// Create a new config file
		FileConfiguration newc = new YamlConfiguration();
		
		// Loop through all the config file items to update the file
		Set<String> keys = defc.getConfigurationSection("").getKeys(true);
		for(String k : keys) {
			
			if(k.equalsIgnoreCase("version"))
				continue;
			
			if(!defc.isSet(k))
				newc.createSection(k);
				
			else if(defc.isBoolean(k))
				newc.set(k, c.getBoolean(k, defc.getBoolean(k)));
				
			else if(defc.isDouble(k))
				newc.set(k, c.getDouble(k, defc.getDouble(k)));
			
			else if(defc.isInt(k))
				newc.set(k, c.getInt(k, defc.getInt(k)));
				
			else if(defc.isItemStack(k))
				newc.set(k, c.getItemStack(k, defc.getItemStack(k)));
				
			else if(defc.isList(k))
				newc.set(k, c.getList(k, defc.getList(k)));
				
			else if(defc.isLong(k))
				newc.set(k, c.getLong(k, defc.getLong(k)));
				
			else if(defc.isOfflinePlayer(k))
				newc.set(k, c.getOfflinePlayer(k, defc.getOfflinePlayer(k)));
				
			else if(defc.isString(k))
				newc.set(k, c.getString(k, defc.getString(k)));
				
			else if(defc.isVector(k))
				newc.set(k, c.getVector(k, defc.getVector(k)));
				
			else if(defc.isConfigurationSection(k))
				newc.createSection(k);
		}
		
		// Update the version number in the config file
		newc.set("version", pluginVer);
		
		// Add some description to the config file
		newc.options().header("Safe Creeper Config - Automaticly updated from v" + configVer + " to v" + pluginVer + " by Safe Creeper. Old file backuped in 'plugins/SafeCreeper/old_files' folder.");
		
		// Save the config file
		File configFile = new File(SafeCreeper.instance.getDataFolder(), "config.yml");
		try {
			newc.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		System.out.println("[SafeCreeper] Safe Creeper config file updated, took " + String.valueOf(duration) + " ms!");
	}
	
	/**
	 * Update a config
	 * @param c the config to update
	 */
	@SuppressWarnings("unused")
	public void updateGlobalConfig() {
		// Get the global config file
		FileConfiguration c = SafeCreeper.instance.getConfigManager().getGlobalConfig();
		
		// Check if the file is up-to-date, if so, cancel the update progress
		if(isConfigUpToDate(c))
			return;
		
		System.out.println("[SafeCreeper] Updating the global config file...");
		
		long t = System.currentTimeMillis();
		
		// Get the current plugin and config versions
		String pluginVer = SafeCreeper.instance.getDescription().getVersion();
		String configVer = c.getString("version", "0.1");
		
		// Load the default global config file 
		YamlConfiguration defc = new YamlConfiguration();
		try {
			defc.load(SafeCreeper.instance.getResource("res/global.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		// Make sure the default config file is loaded
		if(defc == null)
			return;
		
		// Backup the current file version, so it won't be lost if something goes wrong
		backupGlobalConfig();
		
		// Create a new config file
		FileConfiguration newc = new YamlConfiguration();
		
		// Loop through all the config file items to update the file
		Set<String> keys = defc.getConfigurationSection("").getKeys(true);
		for(String k : keys) {
			
			String kOld = k;
			
			if(isOlderVersion("1.3", configVer)) {
				
				// Update the old 'EnableBetweenLevels' system to the new 'Locations' system
				if(k.endsWith(".Locations")) {
					newc.createSection(k);
					
					String controlName = k.replace(".Locations", "");
					
					if(c.isSet(controlName + ".EnableBetweenLevels.Enabled") && c.isSet(controlName + ".EnableBetweenLevels.Levels")) {
						if(c.getBoolean(controlName + ".EnableBetweenLevels.Enabled", false)) {
							String levelsString = c.getString(controlName + ".EnableBetweenLevels.Levels", "0-256");
							newc.set(controlName + ".Locations.Levels." + levelsString + ".Enabled", true);
							continue;
						}
					}
				}
				
				// Update the old FoodLock'er in the PlayerControl
				if(k.equals("PlayerControl.FoodMeter.CanIncrease")) {
					newc.set("PlayerControl.FoodMeter.CanIncrease", !c.getBoolean("PlayerControl.LockFoodmeter", !defc.getBoolean(k)));
					continue;
					
				} else if(k.equals("PlayerControl.FoodMeter.CanDecrease")) {
					newc.set("PlayerControl.FoodMeter.CanDecrease", !c.getBoolean("PlayerControl.LockFoodmeter", !defc.getBoolean(k)));
					continue;
				}
			}
			
			// Splitted skeleton control into skeleton and wither skeleton control
			if(isOlderVersion("1.3.6.5", configVer)) {
				
				// Update the old 'EnableBetweenLevels' system to the new 'Locations' system
				if(k.startsWith("WitherSkeletonControl")) {
					kOld = k.replaceFirst("WitherSkeletonControl", "SkeletonControl");
				}
			}
			
			// Don't remove the effects list from older files while converting to a new one
			if(k.endsWith(".Effects.Triggers")) {
				newc.createSection(k);
			}
			
			// Don't remove the locations list from older files while converting to a new one
			if(k.endsWith(".Locations")) {
				newc.createSection(k);
			}
			
			// Update the old FoodLock'er in the PlayerControl
			if(k.equals("PlayerControl.FoodMeter.CanIncrease")) {
				newc.set("PlayerControl.FoodMeter.CanIncrease", c.getBoolean("PlayerControl.LockFoodmeter", defc.getBoolean(k)));
				continue;
				
			} else if(k.equals("PlayerControl.FoodMeter.CanDecrease")) {
				newc.set("PlayerControl.FoodMeter.CanDecrease", c.getBoolean("PlayerControl.LockFoodmeter", defc.getBoolean(k)));
				continue;
			}
			
			// Update the old 'KeepXPOnDeath' and 'DropXPOnDeath' from the PlayerControl
			if(k.equals("PlayerControl.CustomDrops.XP.Enabled")) {
				if(c.getBoolean("PlayerControl.KeepXPOnDeath", defc.getBoolean(k)) ||
						c.getBoolean("PlayerControl.DropXPOnDeath", defc.getBoolean(k))) {
					newc.set("PlayerControl.CustomDrops.XP.Enabled", true);
					continue;
				}
				
			} else if(k.equals("PlayerControl.CustomDrops.XP.KeepLevel")) {
				newc.set("PlayerControl.CustomDrops.XP.KeepLevel", c.getBoolean("PlayerControl.KeepXPOnDeath", defc.getBoolean(k)));
				continue;
				
			} else if(k.equals("PlayerControl.CustomDrops.XP.KeepXPLevel")) {
				newc.set("PlayerControl.CustomDrops.XP.KeepXPLevel", c.getBoolean("PlayerControl.KeepXPOnDeath", defc.getBoolean(k)));
				continue;
				
			} else if(k.equals("PlayerControl.CustomDrops.XP.DropXP")) {
				newc.set("PlayerControl.CustomDrops.XP.DropXP", c.getBoolean("PlayerControl.DropXPOnDeath", defc.getBoolean(k)));
				continue;
			}
			
			// Update the old 'DropItemOnDeath' from the EndermanControl
			if(k.equals("EndermanControl.CustomDrops.DropItemOnDeath")) {
				newc.set("EndermanControl.CustomDrops.DropItemOnDeath", c.getBoolean("EndermanControl.DropItemOnDeath", defc.getBoolean(k)));
				continue;
			}
			
			if(k.equalsIgnoreCase("version"))
				continue;
			
			if(!defc.isSet(k))
				newc.createSection(k);
				
			else if(defc.isBoolean(k))
				newc.set(k, c.getBoolean(kOld, defc.getBoolean(k)));
				
			else if(defc.isDouble(k))
				newc.set(k, c.getDouble(kOld, defc.getDouble(k)));
				
			else if(defc.isInt(k))
				newc.set(k, c.getInt(kOld, defc.getInt(k)));
				
			else if(defc.isItemStack(k))
				newc.set(k, c.getItemStack(kOld, defc.getItemStack(k)));
				
			else if(defc.isList(k))
				newc.set(k, c.getList(kOld, defc.getList(k)));
				
			else if(defc.isLong(k))
				newc.set(k, c.getLong(kOld, defc.getLong(k)));
				
			else if(defc.isOfflinePlayer(k))
				newc.set(k, c.getOfflinePlayer(kOld, defc.getOfflinePlayer(k)));
				
			else if(defc.isString(k))
				newc.set(k, c.getString(kOld, defc.getString(k)));
				
			else if(defc.isVector(k))
				newc.set(k, c.getVector(kOld, defc.getVector(k)));
				
			else if(defc.isConfigurationSection(k))
				newc.createSection(k);
		}
		
		// Copy the effects list from the old files into the new one (They may not get lost!)
		Set<String> controlNames = c.getConfigurationSection("").getKeys(false);
		for(String controlName : controlNames) {
			
			if(!c.contains(controlName + ".Enabled"))
				continue;
			
			if(!c.contains(controlName + ".Effects.Triggers"))
				continue;
			
			Set<String> effectTriggers = c.getConfigurationSection(controlName + ".Effects.Triggers").getKeys(true);
			for(String et : effectTriggers) {

				et = controlName + ".Effects.Triggers." + et; 
				
				if(c.isBoolean(et))
					newc.set(et, c.getBoolean(et, defc.getBoolean(et)));
					
				else if(c.isDouble(et))
					newc.set(et, c.getDouble(et, defc.getDouble(et)));
					
				else if(c.isInt(et))
					newc.set(et, c.getInt(et, defc.getInt(et)));
					
				else if(c.isItemStack(et))
					newc.set(et, c.getItemStack(et, defc.getItemStack(et)));
					
				else if(c.isList(et))
					newc.set(et, c.getList(et, defc.getList(et)));
					
				else if(c.isLong(et))
					newc.set(et, c.getLong(et, defc.getLong(et)));
					
				else if(c.isOfflinePlayer(et))
					newc.set(et, c.getOfflinePlayer(et, defc.getOfflinePlayer(et)));
					
				else if(c.isString(et))
					newc.set(et, c.getString(et, defc.getString(et)));
					
				else if(c.isVector(et))
					newc.set(et, c.getVector(et, defc.getVector(et)));
					
				else if(c.isConfigurationSection(et))
					newc.createSection(et);
			}
		}
		
		// Copy the locations list from the old files into the new one (They may not get lost!)
		controlNames = c.getConfigurationSection("").getKeys(false);
		for(String controlName : controlNames) {
			if(!c.contains(controlName + ".Enabled"))
				continue;
			
			if(!c.contains(controlName + ".Locations"))
				continue;
			
			Set<String> locationContents = c.getConfigurationSection(controlName + ".Locations").getKeys(true);
			for(String lc : locationContents) {

				lc = controlName + ".Locations." + lc; 
				
				if(c.isBoolean(lc))
					newc.set(lc, c.getBoolean(lc, defc.getBoolean(lc)));
					
				else if(c.isDouble(lc))
					newc.set(lc, c.getDouble(lc, defc.getDouble(lc)));
					
				else if(c.isInt(lc))
					newc.set(lc, c.getInt(lc, defc.getInt(lc)));
					
				else if(c.isItemStack(lc))
					newc.set(lc, c.getItemStack(lc, defc.getItemStack(lc)));
					
				else if(c.isList(lc))
					newc.set(lc, c.getList(lc, defc.getList(lc)));
					
				else if(c.isLong(lc))
					newc.set(lc, c.getLong(lc, defc.getLong(lc)));
					
				else if(c.isOfflinePlayer(lc))
					newc.set(lc, c.getOfflinePlayer(lc, defc.getOfflinePlayer(lc)));
					
				else if(c.isString(lc))
					newc.set(lc, c.getString(lc, defc.getString(lc)));
					
				else if(c.isVector(lc))
					newc.set(lc, c.getVector(lc, defc.getVector(lc)));
					
				else if(c.isConfigurationSection(lc))
					newc.createSection(lc);
			}
		}
		
		// Update the version number in the config file
		newc.set("version", pluginVer);
		
		// Add some description to the config file
		newc.options().header("Global Config - Automaticly updated from v" + configVer + " to v" + pluginVer + " by Safe Creeper. Old file backuped in 'plugins/SafeCreeper/old_files' folder.");
		
		// Save the config file
		SafeCreeper.instance.getConfigManager().setGlobalConfig(newc);
		SafeCreeper.instance.getConfigManager().saveGlobalConfig();

		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		System.out.println("[SafeCreeper] Global config file updated, took " + String.valueOf(duration) + " ms!");
	}
	
	public void updateAllWorldsConfig() {
		List<String> configWorlds = SafeCreeper.instance.getConfigManager().listConfigWorlds();
		for(String w : configWorlds)
			updateWorldConfig(w);
	}
	
	/**
	 * Update a config
	 * @param c the config to update
	 */
	public void updateWorldConfig(String w) {
		// Get the global config file
		FileConfiguration c = SafeCreeper.instance.getConfigManager().getWorldConfig(w);
		FileConfiguration globalc = SafeCreeper.instance.getConfigManager().getGlobalConfig();
		
		// Check if the file is up-to-date, if so, cancel the update progress
		if(isConfigUpToDate(c))
			return;
		
		System.out.println("[SafeCreeper] Updating the world config '" + w + "' file...");
		
		long t = System.currentTimeMillis();
		
		// Get the current plugin and config versions
		String pluginVer = SafeCreeper.instance.getDescription().getVersion();
		String configVer = c.getString("version", "0.1");
		
		// Backup the current file version, so it won't be lost if something goes wrong
		backupWorldConfig(w);
		
		// Create a new config file
		FileConfiguration newc = new YamlConfiguration();
		
		// Loop through all the config file items to update the file
		Set<String> keys = c.getConfigurationSection("").getKeys(true);
		for(String k : keys) {
			
			if(isOlderVersion("1.3", configVer)) {
				
				// Update the old 'EnableBetweenLevels' system to the new 'Locations' system
				if(k.endsWith(".EnableBetweenLevels")) {
					
					String controlName = k.replace(".EnableBetweenLevels", "");
					
					newc.createSection(controlName + ".Locations");
					
					if(c.getBoolean(controlName + ".EnableBetweenLevels.Enabled", false)) {
						String levelsString = c.getString(controlName + ".EnableBetweenLevels.Levels", "0-256");
						newc.set(controlName + ".Locations.Levels." + levelsString + ".Enabled", true);
					}
					continue;
					
				} else if(k.contains(".EnableBetweenLevels")) {
					continue;
				}
				
				// Update the old FoodLock'er in the PlayerControl
				if(k.equals("PlayerControl.LockFoodmeter")) {
					newc.set("PlayerControl.FoodMeter.CanIncrease", !c.getBoolean("PlayerControl.LockFoodmeter", false));
					newc.set("PlayerControl.FoodMeter.CanDecrease", !c.getBoolean("PlayerControl.LockFoodmeter", false));
					continue;
				}
			}
			
			// Update the old 'KeepXPOnDeath' and 'DropXPOnDeath' from the PlayerControl
			if(k.equals("PlayerControl.KeepXPOnDeath")) {
				newc.set("PlayerControl.CustomDrops.Enabled", true);
				newc.set("PlayerControl.CustomDrops.XP.Enabled", true);
				newc.set("PlayerControl.CustomDrops.XP.KeepXP", c.getBoolean("PlayerControl.KeepXPOnDeath", globalc.getBoolean("PlayerControl.CustomDrops.XP.KeepXP")));
				newc.set("PlayerControl.CustomDrops.XP.KeepLevel", c.getBoolean("PlayerControl.KeepXPOnDeath", globalc.getBoolean("PlayerControl.CustomDrops.XP.KeepLevel")));
				continue;
			} else if(k.equals("PlayerControl.DropXPOnDeath")) {
				newc.set("PlayerControl.CustomDrops.Enabled", true);
				newc.set("PlayerControl.CustomDrops.XP.Enabled", true);
				newc.set("PlayerControl.CustomDrops.XP.DropXP", c.getBoolean("PlayerControl.DropXPOnDeath", globalc.getBoolean("PlayerControl.CustomDrops.XP.DropXP")));
				continue;
			}
			
			// Update the old 'DropItemOnDeath' from the EndermanControl
			if(k.equals("EndermanControl.DropItemOnDeath")) {
				newc.set("PlayerControl.CustomDrops.Enabled", true);
				newc.set("EndermanControl.CustomDrops.DropItemOnDeath", c.getBoolean("EndermanControl.DropItemOnDeath", globalc.getBoolean("EndermanControl.CustomDrops.DropItemOnDeath")));
				continue;
			}
			
			if(k.equalsIgnoreCase("version"))
				continue;
			
			if(globalc.isBoolean(k))
				newc.set(k, c.getBoolean(k, globalc.getBoolean(k)));
				
			else if(globalc.isDouble(k))
				newc.set(k, c.getDouble(k, globalc.getDouble(k)));
				
			else if(globalc.isInt(k))
				newc.set(k, c.getInt(k, globalc.getInt(k)));
				
			else if(globalc.isItemStack(k))
				newc.set(k, c.getItemStack(k, globalc.getItemStack(k)));
				
			else if(globalc.isList(k))
				newc.set(k, c.getList(k, globalc.getList(k)));
				
			else if(globalc.isLong(k))
				newc.set(k, c.getLong(k, globalc.getLong(k)));
				
			else if(globalc.isOfflinePlayer(k))
				newc.set(k, c.getOfflinePlayer(k, globalc.getOfflinePlayer(k)));
				
			else if(globalc.isString(k))
				newc.set(k, c.getString(k, globalc.getString(k)));
				
			else if(globalc.isVector(k))
				newc.set(k, c.getVector(k, globalc.getVector(k)));
				
			else {
				if(!c.isSet(k))
					newc.createSection(k);
					
				else if(c.isBoolean(k))
					newc.set(k, c.getBoolean(k, globalc.getBoolean(k)));
					
				else if(c.isDouble(k))
					newc.set(k, c.getDouble(k, globalc.getDouble(k)));
					
				else if(c.isInt(k))
					newc.set(k, c.getInt(k, globalc.getInt(k)));
					
				else if(c.isItemStack(k))
					newc.set(k, c.getItemStack(k, globalc.getItemStack(k)));
					
				else if(c.isList(k))
					newc.set(k, c.getList(k, globalc.getList(k)));
					
				else if(c.isLong(k))
					newc.set(k, c.getLong(k, globalc.getLong(k)));
					
				else if(c.isOfflinePlayer(k))
					newc.set(k, c.getOfflinePlayer(k, globalc.getOfflinePlayer(k)));
					
				else if(c.isString(k))
					newc.set(k, c.getString(k, globalc.getString(k)));
					
				else if(c.isVector(k))
					newc.set(k, c.getVector(k, globalc.getVector(k)));
					
				else if(c.isConfigurationSection(k))
					newc.createSection(k);
			}
		}
		
		// Update the version number in the config file
		newc.set("version", pluginVer);
		
		// Add some description to the config file
		newc.options().header("World Config - Automaticly updated from v" + configVer + " to v" + pluginVer + " by Safe Creeper. Old file backuped in 'plugins/SafeCreeper/old_files' folder.");
		
		// Save the config file
		SafeCreeper.instance.getConfigManager().setWorldConfig(w, newc);
		SafeCreeper.instance.getConfigManager().saveWorldConfig(w);

		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		System.out.println("[SafeCreeper] World config '" + w + "' file updated, took " + String.valueOf(duration) + " ms!");
	}
	
	public void backupConfig() {
		System.out.println("[SafeCreeper] Creating backup of Safe Creeper config file...");
		
		long t = System.currentTimeMillis();
		
		// Get the global config file
		FileConfiguration c = SafeCreeper.instance.getConfig();
		
		// Get the current config version
		String configVer = c.getString("version", "0.1");
		
		// Get the path to copy the file to
		File configFile = new File(SafeCreeper.instance.getDataFolder(), "config.yml");
		File backupFile = new File(SafeCreeper.instance.getDataFolder(), "old_files" + File.separator + "v" + configVer + File.separator + "config.yml");
		
		// Make sure the parent dirs exists
		((File) new File(backupFile.getParent())).mkdirs();
		
		// Copy the config file to it's backup location
		copy(configFile, backupFile);
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		System.out.println("[SafeCreeper] Safe Creeper config backup created, took " + String.valueOf(duration) + " ms!");
	}
	
	public void backupGlobalConfig() {
		System.out.println("[SafeCreeper] Creating backup of global config file...");
		
		long t = System.currentTimeMillis();
		
		// Get the global config file
		FileConfiguration c = SafeCreeper.instance.getConfigManager().getGlobalConfig();
		
		// Get the current config version
		String configVer = c.getString("version", "0.1");
		
		// Get the path to copy the file to
		File globalConfigFile = SafeCreeper.instance.getConfigManager().getGlobalConfigFile();
		File backupFile = new File(SafeCreeper.instance.getDataFolder(), "old_files" + File.separator + "v" + configVer + File.separator + "global.yml");
		
		// Make sure the parent dirs exists
		((File) new File(backupFile.getParent())).mkdirs();
		
		// Copy the config file to it's backup location
		copy(globalConfigFile, backupFile);
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		System.out.println("[SafeCreeper] Global config backup created, took " + String.valueOf(duration) + " ms!");
	}
	
	public void backupWorldConfig(String w) {
		System.out.println("[SafeCreeper] Creating backup of world config '" + w + "' file...");
		
		long t = System.currentTimeMillis();
		
		// Get the world config file
		FileConfiguration c = SafeCreeper.instance.getConfigManager().getWorldConfig(w);
		
		// Get the current config version
		String configVer = c.getString("version", "0.1");
		
		// Get the path to copy the file to
		File worldConfigFile = SafeCreeper.instance.getConfigManager().getWorldConfigFile(w);
		File backupFile = new File(SafeCreeper.instance.getDataFolder(), "old_files" + File.separator + "v" + configVer + File.separator + "worlds" + File.separator + w + ".yml");
		
		// Make sure the parent dirs exists
		((File) new File(backupFile.getParent())).mkdirs();
		
		// Copy the config file to it's backup location
		copy(worldConfigFile, backupFile);
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		System.out.println("[SafeCreeper] World config '" + w + "' backup created, took " + String.valueOf(duration) + " ms!");
	}
	
	/**
	 * Check if a version number is older than the current plugin version number
	 * @param pluginVer the current plugin version
	 * @param checkVer the version to check
	 * @return true if the version number is older
	 */
	private boolean isOlderVersion(String pluginVer, String checkVer) {
        String s1 = normalisedVersion(pluginVer);
        String s2 = normalisedVersion(checkVer);
        int cmp = s1.compareTo(s2);
        return (cmp > 0);
    }
	
	/**
	 * Check if a version number is newer than the current plugin version number
	 * @param pluginVer the current plugin version
	 * @param checkVer the version to check
	 * @return true if the version number is newer
	 */
	@SuppressWarnings("unused")
	private boolean isNewerVersion(String pluginVer, String checkVer) {
        String s1 = normalisedVersion(pluginVer);
        String s2 = normalisedVersion(checkVer);
        int cmp = s1.compareTo(s2);
        return (cmp < 0);
    }
	
	/**
	 * Check if a version number is the same as the current plugin version number
	 * @param pluginVer the current plugin version
	 * @param checkVer the version to check
	 * @return true if the version number is the same
	 */
	@SuppressWarnings("unused")
	private boolean isSameVersion(String pluginVer, String checkVer) {
        String s1 = normalisedVersion(pluginVer);
        String s2 = normalisedVersion(checkVer);
        int cmp = s1.compareTo(s2);
        return (cmp == 0);
    }

	/**
	 * Normalize a version number (String)
	 * @param ver version number
	 * @return normalized version number
	 */
	private String normalisedVersion(String ver) {
        return normalisedVersion(ver, ".", 4);
    }

	/**
	 * Normalize a version number (String)
	 * @param ver version number
	 * @param sep seperation character
	 * @param maxWidth max width
	 * @return normalized version number
	 */
	private String normalisedVersion(String ver, String sep, int maxWidth) {
        String[] split = Pattern.compile(sep, Pattern.LITERAL).split(ver);
        StringBuilder sb = new StringBuilder();
        
        for (String s : split)
            sb.append(String.format("%" + maxWidth + 's', s));
        
        return sb.toString();
    }

	private void copy(File f1, File f2) {
		InputStream in = null;
		try {
			in = new FileInputStream(f1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		copy(in, f2);
	}

	private void copy(InputStream in, File file) {
		// Make sure the input isn't null
		if(in == null)
			return;
		
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
