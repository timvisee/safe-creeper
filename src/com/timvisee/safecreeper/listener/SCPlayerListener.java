package com.timvisee.safecreeper.listener;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.entity.SCLivingEntity;

public class SCPlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		Location l = p.getLocation();
		World w = l.getWorld();
		Random rand = new Random();
		
		// Handle the 'CustomDrops' feature
		// Make sure the custom drops feature is enabled
		if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CustomDrops.Enabled", false, true, l)) {
			
			// Should Safe Creeper overwrite the default drops
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CustomDrops.OverwriteDefaultDrops", false, true, l))
				event.getDrops().clear();
			
			// Check if XP is enabled from the Custom Drops feature
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CustomDrops.XP.Enabled", false, true, l)) {

				// Should XP be dropped
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CustomDrops.XP.DropXP", true, true, l))
					event.setDroppedExp(0);
				else {
					
					// Apply the drop chance
					double dropChance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, "PlayerControl", "CustomDrops.XP.DropChance", 100, true, l);
					if(((int) dropChance * 10) <= rand.nextInt(1000))
						event.setDroppedExp(0);
					
					// Apply the drop multiplier
					double xpMultiplier = SafeCreeper.instance.getConfigManager().getOptionDouble(w, "PlayerControl", "CustomDrops.XP.Multiplier", 1, true, l);
					if(xpMultiplier != 1 && xpMultiplier >= 0)
						event.setDroppedExp((int) (event.getDroppedExp() * xpMultiplier));
				}
				
				// Should XP be kept
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CustomDrops.XP.KeepXP", false, true, l))
					event.setNewTotalExp(0);
				else
					event.setNewTotalExp(p.getTotalExperience());
				
				// Should XP levels be kept
				event.setKeepLevel(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CustomDrops.XP.KeepLevel", false, true, l));
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		Location l = event.getRespawnLocation();
		World w = l.getWorld();
		
		// Check if mobs are able to spawn
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(p);
		if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
			controlName = "OtherMobControl";
		
		// Set the health of the mob if CustomHealth is enabled
		LivingEntity le = (LivingEntity) p;
		
		boolean customHealthEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomHealth.Enabled", false, true, l);
		if(customHealthEnabled) {
			int customHealthMin = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomHealth.MinHealth", le.getMaxHealth(), true, l) - 1;
			int customHealthMax = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomHealth.MaxHealth", le.getMaxHealth(), true, l);
			int customHealth = 1;
			
			int diff = customHealthMax-customHealthMin;
			
			Random rand = new Random();
			customHealth = rand.nextInt(Math.max(diff, 1)) + customHealthMin;
			
			// Create a SCLivingEntity from the living entity and set the health
			SCLivingEntity scle = new SCLivingEntity(le);
			scle.setHealth(customHealth);
			SafeCreeper.instance.getLivingEntityManager().addLivingEntity(scle);
		}
	}
	
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Block b = event.getBlockClicked();
		Location l = b.getLocation();
		World w = b.getWorld();
		
		switch (event.getBucket()) {
		case WATER_BUCKET:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WaterControl", "CanPlaceWater", true, true, l))
				event.setCancelled(true);
			break;
			
		case LAVA_BUCKET:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "LavaControl", "CanPlaceLava", true, true, l))
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
		
		if(!hasBypassPermission(event.getPlayer(), "PlayerControl", "CanSleep", false))
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CanSleep", true, true, l))
				event.setCancelled(true);
		
		// Play the control effects
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects("PlayerControl", "Sleeping", l);
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Entity e = event.getRightClicked();
		Location l = e.getLocation();
		World w = l.getWorld();
		
		// Control villager trading
		if(e instanceof Villager) {
			if(!hasBypassPermission(event.getPlayer(), "VillagerControl", "CanTrade", false))
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "VillagerControl", "CanTrade", true, true, l))
					event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		Location l = p.getLocation();
		
		if(p.isOp())
			if(SafeCreeper.instance.isUpdateAvailable) {
				p.sendMessage(ChatColor.YELLOW + "[SafeCreeper] " + ChatColor.GREEN + "New version available! (v" + SafeCreeper.instance.newestVersion + ")");
				p.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Download: " + ChatColor.BLUE + " " + ChatColor.UNDERLINE + "http://dev.bukkit.org/server-mods/safe-creeper/");
			}
		
		// Play effects
		SafeCreeper.instance.getConfigManager().playControlEffects("PlayerControl", "Join", l);
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player p = event.getPlayer();
		Location l = p.getLocation();
		World w = l.getWorld();
		
		if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CanPickupItems", true, true, l))
			event.setCancelled(true);
	}
	
	public boolean hasBypassPermission(Player player, String controlName, String bypassName, boolean def) {
		if(!SafeCreeper.instance.getPermissionsManager().isEnabled())
			return def;
		return SafeCreeper.instance.getPermissionsManager().hasPermission(player, "safecreeper.bypass." + controlName + "." + bypassName, def);
	}
}
