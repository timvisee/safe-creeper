package com.timvisee.safecreeper.command;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.task.SCTask;
import com.timvisee.safecreeper.util.SCChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class CommandTasks {

    public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if(commandLabel.equalsIgnoreCase("safecreeper") || commandLabel.equalsIgnoreCase("sc")) {
            if(args.length == 0) {
                sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
                sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
                return true;
            }

            if(args[0].equalsIgnoreCase("task") || args[0].equalsIgnoreCase("tasks")) {
                // Check wrong command values
                if(args.length != 1) {
                    sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
                    sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
                    return true;
                }

                // Check permission
                if(sender instanceof Player) {
                    if(!SafeCreeper.instance.getPermissionsManager().hasPermission((Player) sender, "safecreeper.command.tasks")) {
                        sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
                        return true;
                    }
                }

                // Print the header
                sender.sendMessage(ChatColor.GREEN + SCChatUtils.fillLine("[ RUNNING SAFE CREEPER TASKS ]", "="));

                // List running Safe Creeper tasks
                List<BukkitTask> bt = Bukkit.getScheduler().getPendingTasks();
                for(BukkitTask t : bt) {
                    // Make sure the task is not null
                    if(t == null)
                        continue;

                    // Make sure the task owner is not null
                    if(t.getOwner() == null)
                        continue;

                    // Make sure Safe Creeper is the owner of the task
                    if(!t.getOwner().equals(SafeCreeper.instance))
                        continue;

                    // Get the task name
                    String taskName = ChatColor.ITALIC + "UNKNOWN";
                    if(t instanceof SCTask)
                        taskName = ((SCTask) t).getTaskName();

                    // Send the task info
                    sender.sendMessage(ChatColor.GOLD + "- Task ID: " + ChatColor.WHITE + String.valueOf(t.getTaskId()) +
                            ChatColor.GOLD + "  Name: " + ChatColor.WHITE + taskName);
                }

                return true;
            }
        }

        return false;
    }
}
