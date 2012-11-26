package com.timvisee.safecreeper.listener;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.entity.SCLivingEntity;

public class SafeCreeperPlayerListener implements Listener {
	
	
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player  p = event.getPlayer();
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
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CanSleep", true, true, l))
				event.setCancelled(true);
		
		// Play the control effects
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects("PlayerControl", "Sleeping", l);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		Location l = p.getLocation();
		
		if(p.isOp())
			if(SafeCreeper.instance.isUpdateAvailable)
				p.sendMessage(ChatColor.YELLOW + "[SafeCreeper] " + ChatColor.GREEN + "New version found! (v" + SafeCreeper.instance.newestVersion + ")");
		
		// Play effects
		SafeCreeper.instance.getConfigManager().playControlEffects("PlayerControl", "Join", l);
	}
	
	public boolean hasBypassPermission(Player player, String controlName, String bypassName, boolean def) {
		if(!SafeCreeper.instance.getPermissionsManager().isEnabled())
			return def;
		return SafeCreeper.instance.getPermissionsManager().hasPermission(player, "safecreeper.bypass." + controlName + "." + bypassName, def);
	}
}
