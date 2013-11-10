package com.timvisee.safecreeper.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.handler.SCUpdatesHandler;

public class CommandInstallUpdate {
	
public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if(commandLabel.equalsIgnoreCase("safecreeper") || commandLabel.equalsIgnoreCase("sc")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
		
			if(args[0].equalsIgnoreCase("installupdate") || args[0].equalsIgnoreCase("installupdates")) {
				// Check wrong command values
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				// Check permission
				if(sender instanceof Player) {
					if(!SafeCreeper.instance.getPermissionsManager().hasPermission((Player) sender, "safecreeper.command.installupdate")) {
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
						sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] New version already downloaded (v" + String.valueOf(newVer) + "). Server reload required!");
					else {
						sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Downloading new version (v" + String.valueOf(newVer) + ")");
						uc.downloadUpdate();
						sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Update downloaded, server reload required!");
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
