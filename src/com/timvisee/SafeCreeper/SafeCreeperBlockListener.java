package com.timvisee.SafeCreeper;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;

public class SafeCreeperBlockListener implements Listener {
	public static SafeCreeper plugin;

	public SafeCreeperBlockListener(SafeCreeper instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		World world = event.getBlock().getWorld();
		
		// Check if the block is water
		if(block.getTypeId() == 8 || block.getTypeId() == 9) {
			// Could a player place water
			if(!hasBypassPermission(event.getPlayer(), "WaterControl", "CanPlaceWater", false)) {
				if(!getConfigSettings(world, "WaterControl", "CanPlaceWater", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		}
		
		// Check if the block is lava
		if(block.getTypeId() == 10 || block.getTypeId() == 11) {
			// Could a player place lava
			if(!hasBypassPermission(event.getPlayer(), "LavaControl", "CanPlaceLava", false)) {
				if(!getConfigSettings(world, "LavaControl", "CanPlaceLava", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		}
		
		// Check if the block is TNT
		if(block.getTypeId() == 46) {
			// Could a player place a TNT block
			if(!hasBypassPermission(event.getPlayer(), "TNTControl", "CanPlaceTNT", false)) {
				if(!getConfigSettings(world, "TNTControl", "CanPlaceTNT", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		World world = event.getBlock().getWorld();

		// Check if the block is TNT
		if(block.getTypeId() == 46) {
			// Could a player break a TNT block
			if(!hasBypassPermission(event.getPlayer(), "TNTControl", "CanBreakTNT", false)) {
				if(!getConfigSettings(world, "TNTControl", "CanBreakTNT", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		}
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		Block block = event.getBlock();
		World world = block.getWorld();
		
		// Could fire burn/break blocks
		if(!getConfigSettings(world, "FireControl", "EnableBlockFire", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
	}

	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		Block block = event.getBlock();
		World world = block.getWorld();
		
		if(event.getCause() == IgniteCause.FLINT_AND_STEEL) {
			// Could a player use flint and steel to ignite blocks
			if(!hasBypassPermission(event.getPlayer(), "FlintAndSteelControl", "EnableFlintAndSteel", false)) {
				if(!getConfigSettings(world, "FlintAndSteelControl", "EnableFlintAndSteel", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		}
		
		if(event.getCause() == IgniteCause.LAVA) {
			// Could lava cause fire
			if(!getConfigSettings(world, "LavaControl", "LavaFire", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
		}
		
		if(event.getCause() == IgniteCause.SPREAD) {
			// Could fire spread out
			if(!getConfigSettings(world, "FireControl", "FireSpread", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
		}
		
		if(event.getCause() == IgniteCause.LIGHTNING) {
			// Could lightning ignite blocks
			if(!getConfigSettings(world, "LightningControl", "IgniteBlocks", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
		}
	}

	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event) {
		Block block = event.getBlock();
		//Block toBlock = event.getToBlock();
		World world = block.getWorld();
		
		if(block.getTypeId() == 8 || block.getTypeId() == 9) {
			// This is water, check if water is able to spread
			if(!getConfigSettings(world, "WaterControl", "WaterSpread", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
			
		} else if(block.getTypeId() == 10 || block.getTypeId() == 11) {
			// This is lava, check if lava is able to spread
			if(!getConfigSettings(world, "LavaControl", "LavaSpread", true, true, true, block.getLocation().getBlockY())) { event.setCancelled(true); }
		}
	}

	
	
	
	public boolean getConfigSettings(World world, String controlName, String keyName, boolean def, boolean checkEnabled, boolean checkLevel, int currentLevel) {
		if(checkEnabled) {
			if(!configGetBoolean(world.getName(), controlName, "Enabled", false)) {
				return def;
			}
		}
		
		if(!configGetBoolean(world.getName(), controlName, keyName, def)) {
			
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
		return def;
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
		if(!plugin.usePermissions()) {
			return def;
		}
		if(!plugin.isPermissionsSystemEnabled()) {
			return def;
		}
		if(plugin.hasPermission(player, "safecreeper.bypass." + controlName + "." + bypassName, def)) {
			plugin.debug("bypassed - " + controlName + ":" + bypassName);
			return true;
		} else {
			plugin.debug("NOT bypassed - " + controlName + ":" + bypassName);
			return false;
		}
	}
}