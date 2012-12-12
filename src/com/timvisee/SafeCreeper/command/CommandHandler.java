package com.timvisee.safecreeper.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.util.SCFileUpdater;

public class CommandHandler {
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if(commandLabel.equalsIgnoreCase("safecreeper") || commandLabel.equalsIgnoreCase("sc")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("config") || args[0].equalsIgnoreCase("c")) {
				// Check wrong command values
				if(args.length < 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				if(args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("s")) {
					
					// Check permissions
					if(sender instanceof Player) {
						if(!SafeCreeper.instance.getPermissionsManager().hasPermission((Player) sender, "safecreeper.command.config.set")) {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
							return true;
						}
					}
					
					if(args[2].equalsIgnoreCase("global") || args[2].equalsIgnoreCase("g")) {
						if(args.length != 5) {
							sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
							return true;
						}
						if(SafeCreeper.instance.getConfigManager().getGlobalConfig().isBoolean(args[3].toString())) {
							SafeCreeper.instance.getConfigManager().getGlobalConfig().set(args[3].toString(), Boolean.parseBoolean(args[4].toString()));
							SafeCreeper.instance.getConfigManager().saveGlobalConfig();
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + String.valueOf(Boolean.parseBoolean(args[4].toString())) + ChatColor.GOLD + " (boolean)");
							return true;
						} else if(SafeCreeper.instance.getConfigManager().getGlobalConfig().isInt(args[3].toString())) {
							SafeCreeper.instance.getConfigManager().getGlobalConfig().set(args[3].toString(), Integer.parseInt(args[4].toString()));
							SafeCreeper.instance.getConfigManager().saveGlobalConfig();
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + String.valueOf(Integer.parseInt(args[4].toString())) + ChatColor.GOLD + " (integer)");
							return true;
						} else if(SafeCreeper.instance.getConfigManager().getGlobalConfig().isString(args[3].toString())) {
							SafeCreeper.instance.getConfigManager().getGlobalConfig().set(args[3].toString(), args[4].toString());
							SafeCreeper.instance.getConfigManager().saveGlobalConfig();
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " (string)");
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + args[3].toString());
							sender.sendMessage(ChatColor.DARK_RED + "Invalid path!");
							return true;
						}
						
					} else if(args[2].equalsIgnoreCase("world") || args[2].equalsIgnoreCase("w")) {
						if(args.length != 6) {
							sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
							return true;
						}
						String wname = args[3].toString();
						if(SafeCreeper.instance.getConfigManager().getGlobalConfig().isBoolean(args[4].toString())) {
							if(!SafeCreeper.instance.getConfigManager().worldConfigExist(args[3].toString())) {
								FileConfiguration c;
							    c = new YamlConfiguration();
							    SafeCreeper.instance.getConfigManager().addWorldConfig(args[3].toString(), c);
							    sender.sendMessage(ChatColor.GOLD + "The config file for the world " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " did not exists.");
							    sender.sendMessage(ChatColor.GOLD + "A new file has been created.");
							}
							SafeCreeper.instance.getConfigManager().getWorldConfig(wname).set(args[4].toString(), Boolean.parseBoolean(args[5].toString()));
							SafeCreeper.instance.getConfigManager().saveWorldConfig(wname);
							sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + String.valueOf(Boolean.parseBoolean(args[5].toString())) + ChatColor.GOLD + " (boolean)");
							return true;
							
						} else if(SafeCreeper.instance.getConfigManager().getGlobalConfig().isInt(args[3].toString())) {
							if(!SafeCreeper.instance.getConfigManager().worldConfigExist(args[3].toString())) {
								FileConfiguration c;
							    c = new YamlConfiguration();
							    SafeCreeper.instance.getConfigManager().addWorldConfig(args[3].toString(), c);
							    sender.sendMessage(ChatColor.GOLD + "The config file for the world " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " did not exists.");
							    sender.sendMessage(ChatColor.GOLD + "A new file has been created.");
							}
							SafeCreeper.instance.getConfigManager().getWorldConfig(wname).set(args[4].toString(), Integer.parseInt(args[5].toString()));
							SafeCreeper.instance.getConfigManager().saveWorldConfig(wname);
							sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + String.valueOf(Integer.parseInt(args[5].toString())) + ChatColor.GOLD + " (integer)");
							return true;
						} else if(SafeCreeper.instance.getConfigManager().getGlobalConfig().isString(args[3].toString())) {
							if(!SafeCreeper.instance.getConfigManager().worldConfigExist(args[3].toString())) {
								FileConfiguration c;
							    c = new YamlConfiguration();
							    SafeCreeper.instance.getConfigManager().addWorldConfig(args[3].toString(), c);
							    sender.sendMessage(ChatColor.GOLD + "The config file for the world " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " did not exists.");
							    sender.sendMessage(ChatColor.GOLD + "A new file has been created.");
							}
							SafeCreeper.instance.getConfigManager().getWorldConfig(wname).set(args[4].toString(), args[5].toString());
							SafeCreeper.instance.getConfigManager().saveWorldConfig(wname);
							sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + args[5].toString() + ChatColor.GOLD + " (string)");
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + args[4].toString());
							sender.sendMessage(ChatColor.DARK_RED + "Invalid path!");
							return true;
						}
					}
					
				} else if(args[1].equalsIgnoreCase("get") || args[1].equalsIgnoreCase("g")) {
					
					// Check permissions
					if(sender instanceof Player) {
						if(!SafeCreeper.instance.getPermissionsManager().hasPermission((Player) sender, "safecreeper.command.config.get")) {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
							return true;
						}
					}
					
					if(args[2].equalsIgnoreCase("global") || args[2].equalsIgnoreCase("g")) {
						if(args.length != 4) {
							sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
							return true;
						}
						if(SafeCreeper.instance.getConfigManager().getGlobalConfig().isBoolean(args[3].toString())) {
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + String.valueOf(SafeCreeper.instance.getConfigManager().getGlobalConfig().getBoolean(args[3].toString())) + ChatColor.GOLD + " (boolean)");
							return true;
						} else if(SafeCreeper.instance.getConfigManager().getGlobalConfig().isInt(args[3].toString())) {
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + String.valueOf(SafeCreeper.instance.getConfigManager().getGlobalConfig().getInt(args[3].toString())) + ChatColor.GOLD + " (integer)");
							return true;
						} else if(SafeCreeper.instance.getConfigManager().getGlobalConfig().isString(args[3].toString())) {
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " (string)");
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + args[3].toString());
							sender.sendMessage(ChatColor.DARK_RED + "Invalid path!");
							return true;
						}
						
					} else if(args[2].equalsIgnoreCase("world") || args[2].equalsIgnoreCase("w")) {
						if(args.length != 5) {
							sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
							return true;
						}
						if(SafeCreeper.instance.getConfigManager().worldConfigExist(args[3].toString())) {
							String wname = args[3].toString();
							
							if(SafeCreeper.instance.getConfigManager().getWorldConfig(wname).isBoolean(args[4].toString())) {
								sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + String.valueOf(SafeCreeper.instance.getConfigManager().getWorldConfig(wname).getBoolean(args[4].toString())) + ChatColor.GOLD + " (boolean)");
								return true;
							} else if(SafeCreeper.instance.getConfigManager().getWorldConfig(wname).isInt(args[4].toString())) {
								sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + String.valueOf(SafeCreeper.instance.getConfigManager().getWorldConfig(wname).getInt(args[4].toString())) + ChatColor.GOLD + " (integer)");
								return true;
							} else if(SafeCreeper.instance.getConfigManager().getWorldConfig(wname).isString(args[4].toString())) {
								sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " (string)");
								return true;
							} else {
								sender.sendMessage(ChatColor.DARK_RED + args[4].toString());
								sender.sendMessage(ChatColor.DARK_RED + "Invalid path! This path doesn't exist.");
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "No world config file found for the world " + ChatColor.GOLD + args[3].toString());
							return true;
						}
					}
				}
				sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("v")
					 || args[0].equalsIgnoreCase("about") || args[0].equalsIgnoreCase("a")) {
				// Check wrong command values
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				PluginDescriptionFile pdfFile = SafeCreeper.instance.getDescription();
				sender.sendMessage(ChatColor.YELLOW + "This server is running Safe Creeper v" + pdfFile.getVersion());
				sender.sendMessage(ChatColor.YELLOW + "Safe Creeper is made by Tim Visee - timvisee.com");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reload")) {
				// Check wrong command values
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				// Check permission
				if(sender instanceof Player) {
					if(!SafeCreeper.instance.getPermissionsManager().hasPermission((Player) sender, "safecreeper.command.reload")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				// Setup permissions
				SafeCreeper.instance.setupPermissionsManager();
				
				// Load all the config files again
		    	long t = System.currentTimeMillis();
				sender.sendMessage("");
				sender.sendMessage(ChatColor.YELLOW + "Reloading SafeCreeper...");
				
				// Reload all the configs
				SafeCreeper.instance.getConfigManager().reloadAllConfigs();
				
				// Update all config files if they're out-dated
				((SCFileUpdater) new SCFileUpdater()).updateFiles();
				
				// Reload all the entity data
				if(SafeCreeper.instance.getLivingEntityManager() != null) {
					SafeCreeper.instance.getLivingEntityManager().save();
					SafeCreeper.instance.getLivingEntityManager().load();
				}
				
		    	long duration = System.currentTimeMillis() - t;
				sender.sendMessage(ChatColor.GREEN + "SafeCreeper has been reloaded, took " + String.valueOf(duration) + " ms!");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reloadpermissions") || args[0].equalsIgnoreCase("reloadperms")) {
				// Check wrong command values
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				// Check permission
				if(sender instanceof Player) {
					if(!SafeCreeper.instance.getPermissionsManager().hasPermission((Player) sender, "safecreeper.command.reloadperms")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				// Setup permissions
				SafeCreeper.instance.setupPermissionsManager();
				
				// Show a succes message
				sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] " + ChatColor.GREEN + "Permissions reloaded!");
				return true;
			}
			
			
			
			if(args[0].equalsIgnoreCase("butcher") || args[0].equalsIgnoreCase("b")) {
				// Run the butcher command trough the butcher command class
				return CommandButcher.onCommand(sender, cmd, commandLabel, args);
				
			} else if(args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("checkupdates")) {
				// Run the check command trough the check command class
				return CommandCheck.onCommand(sender, cmd, commandLabel, args);
				
			} else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
				// Run the help command trough the help command class
				return CommandHelp.onCommand(sender, cmd, commandLabel, args);
			}
			
			sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
			sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
			return true;
		}
		
		return false;
	}
}
