package com.timvisee.safecreeper.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.handler.SCUpdatesHandler;

public class CommandCheck {
	
	public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if(commandLabel.equalsIgnoreCase("safecreeper") || commandLabel.equalsIgnoreCase("sc")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
		
			if(args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("checkupdates")) {
				// Check wrong command values
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				// Check permission
				if(sender instanceof Player) {
					if(!SafeCreeper.instance.getPermissionsManager().hasPermission((Player) sender, "safecreeper.command.checkupdates")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				// Setup permissions
				sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Checking for updates...");
				
				// Get the update checker and refresh the updates data
				SCUpdatesHandler uc = SafeCreeper.instance.getUpdatesHandler();
				uc.refreshBukkitUpdatesFeedData();
				
				// Check if any update exists
				if(uc.isUpdateAvailable(true)) {
					final String newVer = uc.getNewestVersion(true);
					
					if(uc.isUpdateDownloaded())
						sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] New version downloaded (v" + String.valueOf(newVer) + "). Server reload required!");
					else {
						sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] New version found: " + String.valueOf(newVer));
						sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Use " + ChatColor.GOLD + "/sc installupdate" +
								ChatColor.YELLOW + " to automaticly install the new version!");
					}
				} else if(uc.isUpdateAvailable(false)) {
					final String newVer = uc.getNewestVersion(false);
					
					sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] New incompatible Safe Creeper version available: v" + String.valueOf(newVer));
					sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Please update CraftBukkit to the latest available version!");
					
				} else {
					sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] No Safe Creeper update available!");
				}
				
				return true;
			}
		}
		
		return false;
	}
}
