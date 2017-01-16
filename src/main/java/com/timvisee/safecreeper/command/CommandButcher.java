package com.timvisee.safecreeper.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandButcher {

    public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if(commandLabel.equalsIgnoreCase("safecreeper") || commandLabel.equalsIgnoreCase("sc")) {
            if(args.length == 0) {
                sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
                sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
                return true;
            }

            if(args[0].equalsIgnoreCase("butcher") || args[0].equalsIgnoreCase("b")) {

                sender.sendMessage(ChatColor.DARK_RED + "This command is a work in progress and will be available soon!");
                return true;

				/*int radius = -1;
				
				if(isFlagSet(args, "pli")) {
					String pli = getFlagArgument(args, "pli");
					
					/* // Check permissions
					if(!pm.hasPermission((Player) sender, "simpleshowcase.command.shop.linkpricelistitem")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to use pricelist items!");
						return true;
					}
					
					String value = pli;
					List<String> values = Arrays.asList(value.split(":"));
					
					if(values.size() == 1) {
						if(plugin.getPricelistManager().countItemsWithName(values.get(0)) == 0) {
							sender.sendMessage(ChatColor.DARK_RED + values.get(0));
							sender.sendMessage(ChatColor.DARK_RED + "There's no pricelist item with this name!");
							return true;
						} else if(plugin.getPricelistManager().countItemsWithName(values.get(0)) == 1) {
							applyPricelistItem = plugin.getPricelistManager().getItem(values.get(0));
						} else {
							int count = plugin.getPricelistManager().countItemsWithName(values.get(0));
							sender.sendMessage(ChatColor.DARK_RED + values.get(0));
							sender.sendMessage(ChatColor.DARK_RED + String.valueOf(count) + " matching items found, choose one of these:");
							for(SSPricelistItem entry : plugin.getPricelistManager().getItems(values.get(0)))
								sender.sendMessage(ChatColor.GOLD + " - " + ChatColor.YELLOW + entry.getPricelistName() + ":" + entry.getItemName());
							return true;
						}
					} else {
						String pricelistName = values.get(0);
						String itemName = values.get(1);
						
						if(!plugin.getPricelistManager().isPricelistWithName(pricelistName)) {
							sender.sendMessage(ChatColor.DARK_RED + pricelistName + ChatColor.GRAY + ":" + itemName);
							sender.sendMessage(ChatColor.DARK_RED + "There's no pricelist with this name!");
							return true;
						}
						
						if(!plugin.getPricelistManager().isPricelistItemWithName(pricelistName, itemName)) {
							sender.sendMessage(ChatColor.GRAY + pricelistName + ":" + ChatColor.DARK_RED + itemName);
							sender.sendMessage(ChatColor.DARK_RED + "There's no item with this name in this pricelist!");
							return true;
						}
						
						applyPricelistItem = plugin.getPricelistManager().getItem(pricelistName, itemName);
					}* /
				}
				
				sender.sendMessage("hi");
				
				
				return true;*/
            }
        }

        return false;
    }

    public static boolean isFlagSet(String args[], String flag) {
        for(int i = 0; i < args.length; i++) {
            if(args[i].equalsIgnoreCase("-" + flag) || args[i].equalsIgnoreCase("/" + flag)) {
                return true;
            }
        }
        return false;
    }

    public static String getFlagArgument(String args[], String flag, String def) {
        for(int i = 0; i < args.length; i++) {
            if(args[i].equalsIgnoreCase("-" + flag) || args[i].equalsIgnoreCase("/" + flag)) {
                if(i + 1 < args.length) {
                    return args[i + 1].toString();
                } else {
                    return def;
                }
            }
        }
        return def;
    }

    public static String getFlagArgument(String args[], String flag) {
        return getFlagArgument(args, flag, "");
    }
}
