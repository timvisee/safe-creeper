package com.timvisee.safecreeper.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.timvisee.safecreeper.SafeCreeper;

public class CommandPostStatistics {
	
	public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if(commandLabel.equalsIgnoreCase("safecreeper") || commandLabel.equalsIgnoreCase("sc")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
		
			if(args[0].equalsIgnoreCase("poststatistics") || args[0].equalsIgnoreCase("sendstatistics") ||
					args[0].equalsIgnoreCase("poststatus") || args[0].equalsIgnoreCase("sendstatus")) {
				// Check wrong command values
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				// Check permission
				if(sender instanceof Player) {
					if(!SafeCreeper.instance.getPermissionsManager().hasPermission((Player) sender, "safecreeper.command.poststatistics")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Posting Safe Creeper statistics...");
				
				long t = System.currentTimeMillis();
				
				boolean result = SafeCreeper.instance.getStatisticsManager().postStatistics();
				
				long delay = System.currentTimeMillis() - t;
				
				if(result)
					sender.sendMessage(ChatColor.GREEN + "[SafeCreeper] Safe Creeper statistics send, took " + delay + " ms!");
				else
					sender.sendMessage(ChatColor.GREEN + "[SafeCreeper] Failed to post statistics to Safe Creeper statistics servers after " + delay + " ms!");
				
				return true;
			}
		}
		
		return false;
	}
}
