package com.timvisee.safecreeper.command;

import com.timvisee.safecreeper.util.SCChatUtils;
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
                        sender.sendMessage(ChatColor.GREEN + SCChatUtils.fillLine("[ SAFE CREEPER HELP: CONFIG ]", "="));
                        sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " config get global <path> " + ChatColor.WHITE + ": Get global config value"));
                        sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " config get world <world> <path> " + ChatColor.WHITE + ": Get world config value"));
                        sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " config set global <path> <value> " + ChatColor.WHITE + ": Set global config value"));
                        sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " config set world <world> <path> <value> " + ChatColor.WHITE + ": Set world config value"));
                        sender.sendMessage(SCChatUtils.getBlankLineString());
                        sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "path " + ChatColor.WHITE + ": The value path. " + ChatColor.GRAY + "(Example: CreeperControl.Enabled)"));
                        sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "world " + ChatColor.WHITE + ": The world name"));
                        sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "value " + ChatColor.WHITE + ": The new value. A boolean, integer or string."));
                        sender.sendMessage(SCChatUtils.getBlankLineString());
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
                    sender.sendMessage(ChatColor.GREEN + SCChatUtils.fillLine("[ SAFE CREEPER HELP ]", "="));
                    sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " <help/h/?> " + ChatColor.WHITE + ": View help"));
                    sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " <help/h/?> config " + ChatColor.WHITE + ": Config command help"));
                    sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " reload " + ChatColor.WHITE + ": Reload config files"));
                    sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " reloadperms " + ChatColor.WHITE + ": Reload permissions system"));
                    sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " poststatistics " + ChatColor.WHITE + ": Post the Safe Creeper statistics"));
                    sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " <checkupdates/check> " + ChatColor.WHITE + ": Check for updates"));
                    sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " installupdates " + ChatColor.WHITE + ": Install Safe Creeper updates"));
                    sender.sendMessage(SCChatUtils.fitString(ChatColor.GOLD + "/" + commandLabel + " <version/ver/v> " + ChatColor.WHITE + ": Check plugin version"));
                    sender.sendMessage(SCChatUtils.getBlankLineString());
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
