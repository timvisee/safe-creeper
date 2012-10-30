package com.timvisee.safecreeper;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class SafeCreeperPlayerListener implements Listener {
	public static SafeCreeper plugin;

	public SafeCreeperPlayerListener(SafeCreeper instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Block block = event.getBlockClicked();
		World world = block.getWorld();
		
		if(event.getBucket() == Material.WATER_BUCKET) {
			// Could a player place water
			if(getConfigSettings(world, "WaterControl", "CanPlaceWater", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
			
		} else if(event.getBucket() == Material.LAVA_BUCKET) {
			// Could a player place water
			if(getConfigSettings(world, "LavaControl", "CanPlaceLava", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
		}
	}
	
	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		
		if(!hasBypassPermission(event.getPlayer(), "PlayerControl", "CanSleep", false)) {
			if(getConfigSettings(world, "PlayerControl", "CanSleep", true, true, true, player.getLocation().getBlockY())) { event.setCancelled(true); }
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(player.isOp()) {
			if(plugin.isUpdateAvailable) {
				player.sendMessage(ChatColor.YELLOW + "[Safe Creeper] " + ChatColor.GREEN + "New version found! (v" + plugin.newestVersion + ")");
			}
		}
	}
	
	public boolean getConfigSettings(World world, String controlName, String keyName, boolean def, boolean checkEnabled, boolean checkLevel, int currentLevel) {
		if(checkEnabled) {
			if(configGetBoolean(world.getName(), controlName, "Enabled", false) == false) {
				return false;
			}
		}
		
		if(configGetBoolean(world.getName(), controlName, keyName, def) == false) {
			
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
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
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
	
	public boolean hasBypassPermission(Player player, String controlName, String bypassName, boolean def) {
		if(!plugin.getPermissionsManager().isEnabled())
			return def;
		if(plugin.getPermissionsManager().hasPermission(player, "safecreeper.bypass." + controlName + "." + bypassName, def)) {
			return true;
		} else {
			return false;
		}
	}
}
