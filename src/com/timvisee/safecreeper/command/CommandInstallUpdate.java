package com.timvisee.safecreeper.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.util.UpdateChecker;

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
				UpdateChecker uc = SafeCreeper.instance.getUpdateChecker();
				uc.refreshUpdatesData();
				
				if(!uc.isNewVersionAvailable()) {
					sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] No new version available!");
				} else {
					
					String newVer = uc.getNewestVersion();
					
					// Make sure the new version is compatible with the current bukkit version
					if(!uc.isNewVersionCompatibleWithCurrentBukkit()) {
						sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] New Safe Creeper version available: v" + String.valueOf(newVer));
						sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] The new version is not compatible with your Bukkit version!");
						sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Please update your Bukkkit to " +  uc.getRequiredBukkitVersion() + " or higher!");
					} else {
						if(uc.isUpdateDownloaded())
							sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] New version already downloaded (v" + String.valueOf(newVer) + "). Server reload required!");
						else {
							sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Downloading new version (v" + String.valueOf(newVer) + ")");
							uc.downloadUpdate();
							sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Update downloaded, server reload required!");
						}
					}
					return true;
				}
				
				return true;
			}
		}
		
		return false;
	}
}
