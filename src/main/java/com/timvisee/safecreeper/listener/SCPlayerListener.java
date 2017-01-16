package com.timvisee.safecreeper.listener;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.handler.SCUpdatesHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.Random;

public class SCPlayerListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Location l = p.getLocation();
        World w = l.getWorld();
        Random rand = new Random();

        // Handle the 'CustomDrops' feature
        // Make sure the custom drops feature is enabled
        if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "PlayerControl", "CustomDrops.Enabled", false, true, l)) {

            // Should Safe Creeper overwrite the default drops
            if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "PlayerControl", "CustomDrops.OverwriteDefaultDrops", false, true, l))
                event.getDrops().clear();

            // Check if XP is enabled from the Custom Drops feature
            if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "PlayerControl", "CustomDrops.XP.Enabled", false, true, l)) {

                // Should XP be dropped
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "PlayerControl", "CustomDrops.XP.DropXP", true, true, l))
                    event.setDroppedExp(0);
                else {

                    // Apply the drop chance
                    double dropChance = SafeCreeper.instance.getConfigHandler().getOptionDouble(w, "PlayerControl", "CustomDrops.XP.DropChance", 100, true, l);
                    if(((int) dropChance * 10) <= rand.nextInt(1000))
                        event.setDroppedExp(0);

                    // Apply the drop multiplier
                    double xpMultiplier = SafeCreeper.instance.getConfigHandler().getOptionDouble(w, "PlayerControl", "CustomDrops.XP.Multiplier", 1, true, l);
                    if(xpMultiplier != 1 && xpMultiplier >= 0)
                        event.setDroppedExp((int) (event.getDroppedExp() * xpMultiplier));
                }

                // Should XP be kept
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "PlayerControl", "CustomDrops.XP.KeepXP", false, true, l))
                    event.setNewTotalExp(0);
                else
                    event.setNewTotalExp(p.getTotalExperience());

                // Should XP levels be kept
                event.setKeepLevel(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "PlayerControl", "CustomDrops.XP.KeepLevel", false, true, l));
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        Location l = event.getRespawnLocation();
        World w = l.getWorld();
        Random rand = new Random();

        // Check if mobs are able to spawn
        String controlName = SafeCreeper.instance.getConfigHandler().getControlName(p);
        if(!SafeCreeper.instance.getConfigHandler().isValidControl(controlName))
            controlName = "OtherMobControl";

        boolean customHealthEnabled = SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomHealth.Enabled", false, true, l);
        if(customHealthEnabled) {
            double customHealthMin = SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomHealth.MinHealth", p.getMaxHealth(), true, l) - 1;
            double customHealthMax = SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomHealth.MaxHealth", p.getMaxHealth(), true, l);
            double customHealth =
                    rand.nextInt((int) (Math.max(customHealthMax - customHealthMin, 1))) + customHealthMin;

            // Set the max health and the health of the player
            p.setMaxHealth(customHealthMax);
            p.setHealth(customHealth);
        } else {
            // Reset the max health of the player
            p.setMaxHealth(20);
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Block b = event.getBlockClicked();
        Location l = b.getLocation();
        World w = b.getWorld();

        switch(event.getBucket()) {
            case WATER_BUCKET:
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "WaterControl", "CanPlaceWater", true, true, l))
                    event.setCancelled(true);
                break;

            case LAVA_BUCKET:
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "LavaControl", "CanPlaceLava", true, true, l))
                    event.setCancelled(true);
                break;
            default:
        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player p = event.getPlayer();
        Location l = event.getBed().getLocation();
        World w = p.getWorld();

        // Check if the player is allowed to sleep, if not, cancel the event
        if(!hasBypassPermission(event.getPlayer(), "BedControl", "PlayerCanSleep", false))
            if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "BedControl", "PlayerCanSleep", true, true, l))
                event.setCancelled(true);

        // Play the control effects
        if(!event.isCancelled())
            SafeCreeper.instance.getConfigHandler().playControlEffects("PlayerControl", "Sleeping", l);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Block b = event.getClickedBlock();

        // Make sure the block instance is not null
        if(b == null)
            return;

        Location l = b.getLocation();
        World w = l.getWorld();
        Environment we = w.getEnvironment();
        Action a = event.getAction();

        // Is the player interacting with a bed
        if(b.getType().equals(Material.BED_BLOCK)) {
            // Is the player trying to enter the bed
            if(a.equals(Action.RIGHT_CLICK_BLOCK)) {
                // Is the bed going to explode in this world environment
                if(we.equals(Environment.NETHER) || we.equals(Environment.THE_END)) {
                    // Check if the bed's may explode
                    if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "BedControl", "CanExplode", true, true, l)) {
                        // Cancel the bed interaction which causes the bed to explode
                        event.setCancelled(true);

                        // Show a message to the player
                        switch(we) {
                            case NETHER:
                                p.sendMessage("You can't sleep in the Nether!");
                                break;
                            case THE_END:
                                p.sendMessage("You can't sleep in The End!");
                                break;
                            default:
                                p.sendMessage("You can't sleep right here!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity e = event.getRightClicked();
        Location l = e.getLocation();
        World w = l.getWorld();

        // Control villager trading
        if(e instanceof Villager) {
            if(!hasBypassPermission(event.getPlayer(), "VillagerControl", "CanTrade", false))
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "VillagerControl", "CanTrade", true, true, l))
                    event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        Location l = p.getLocation();

        // Make sure the player has permission to see update notifications
        if(SafeCreeper.instance.getPermissionsManager().hasPermission(p, "safecreeper.notification.update", p.isOp()) &&
                SafeCreeper.instance.getConfig().getBoolean("updateChecker.enabled", true) &&
                SafeCreeper.instance.getConfig().getBoolean("updateChecker.notifyForUpdatesInGame", true)) {

            SCUpdatesHandler uc = SafeCreeper.instance.getUpdatesHandler();

            // Check if any update exists
            if(uc.isUpdateAvailable(true)) {
                final String newVer = uc.getNewestVersion(true);

                if(uc.isUpdateDownloaded()) {
                    p.sendMessage(ChatColor.YELLOW + "[SafeCreeper] New Safe Creeper update downloaded! (v" + newVer + ")");
                    p.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Server reload required!");
                } else {
                    p.sendMessage(ChatColor.YELLOW + "[SafeCreeper] New Safe Creeper update available! (v" + newVer + ")");
                    p.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Use " + ChatColor.GOLD + "/sc installupdate" + ChatColor.YELLOW + " to install the update!");
                }
            } else if(uc.isUpdateAvailable(false)) {
                final String newVer = uc.getNewestVersion(false);

                p.sendMessage(ChatColor.YELLOW + "[SafeCreeper] New incompatible Safe Creeper update available! (v" + newVer + ")");
                p.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Please update CraftBukkit to the latest available version!");
            }
        }

        // Play effects
        SafeCreeper.instance.getConfigHandler().playControlEffects("PlayerControl", "Join", l);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player p = event.getPlayer();
        Location l = p.getLocation();
        World w = l.getWorld();

        if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "PlayerControl", "CanPickupItems", true, true, l))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player p = event.getPlayer();
        Location l = p.getLocation();
        World w = l.getWorld();
        boolean sneaking = event.isSneaking();

        // Is the player allowed to sneak
        if(!sneaking)
            if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "PlayerControl", "CanSneak", true, true, l))
                event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        Player p = event.getPlayer();
        Location l = p.getLocation();
        World w = l.getWorld();
        boolean sprinting = event.isSprinting();

        // Is the player allowed to sneak
        if(!sprinting)
            if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "PlayerControl", "CanSprint", true, true, l))
                event.setCancelled(true);
    }

    public boolean hasBypassPermission(Player player, String controlName, String bypassName, boolean def) {
        if(!SafeCreeper.instance.getPermissionsManager().isEnabled())
            return def;
        return SafeCreeper.instance.getPermissionsManager().hasPermission(player, "safecreeper.bypass." + controlName + "." + bypassName, def);
    }
}
