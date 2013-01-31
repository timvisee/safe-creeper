package com.timvisee.safecreeper.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHelp {
	
	public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if(commandLabel.equalsIgnoreCase("safecreeper") || commandLabel.equalsIgnoreCase("sc")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
		
			if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
			
				if(args.length == 2) {
					if(args[1].equals("config") || args[1].equals("c")) {
						// View the help
						sender.sendMessage(ChatColor.GREEN + "==========[ SAFE CREEPER HELP ]==========");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " config get global <path> " + ChatColor.WHITE + ": Get global config value");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " config get world <world> <path> " + ChatColor.WHITE + ": Get world config value");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " config set global <path> <value> " + ChatColor.WHITE + ": Set global config value");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " config set world <world> <path> <value> " + ChatColor.WHITE + ": Set world config value");
						sender.sendMessage(" ");
						sender.sendMessage(ChatColor.GOLD + "path " + ChatColor.WHITE + ": The value path. Example: CreeperControl.Enabled");
						sender.sendMessage(ChatColor.GOLD + "world " + ChatColor.WHITE + ": The world name");
						sender.sendMessage(ChatColor.GOLD + "value " + ChatColor.WHITE + ": The new value. A boolean, integer or string.");
						return true;
					}
					
				} else {
					// Check wrong command values
					if(args.length != 1) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
						return true;
					}
					
					// View the help
					sender.sendMessage(ChatColor.GREEN + "==========[ SAFE CREEPER HELP ]==========");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " <help/h/?> " + ChatColor.WHITE + ": View help");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " <help/h/?> config " + ChatColor.WHITE + ": Config command help");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " reload " + ChatColor.WHITE + ": Reload config files");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " reloadperms " + ChatColor.WHITE + ": Reload permissions system");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " <checkupdates/check> " + ChatColor.WHITE + ": Check for updates");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " installupdates " + ChatColor.WHITE + ": Install Safe Creeper updates");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " <version/ver/v> " + ChatColor.WHITE + ": Check plugin version");
					return true;
				}
				
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
		}
		
		return false;
	}
}
