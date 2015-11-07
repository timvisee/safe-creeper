package com.timvisee.safecreeper.entity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.timvisee.safecreeper.SafeCreeper;

public class SCLivingEntityReviveManager {
	
	List<SCLivingEntityRevive> livingEntityRevive = new ArrayList<SCLivingEntityRevive>();
	
	public SCLivingEntityReviveManager() {
		
	}
	
	public void addLivingEntiy(SCLivingEntityRevive leRevive) {
		this.livingEntityRevive.add(leRevive);
	}
	
	public List<SCLivingEntityRevive> getLivingEntities() {
		return this.livingEntityRevive;
	}
	
	public boolean isReviver(LivingEntity le) {
		for(SCLivingEntityRevive entry : this.livingEntityRevive) {
			if(entry.getReviver().equals(le))
				return true;
		}
		return false;
	}
	
	public SCLivingEntityRevive getItem(LivingEntity reviver) {
		for(SCLivingEntityRevive entry : this.livingEntityRevive) {
			if(entry.getReviver().equals(reviver))
				return entry;
		}
		return null;
	}
	
	public void onReviverReachedTarget(LivingEntity reviver) {
		if(!isReviver(reviver))
			return;
		
		SCLivingEntityRevive revive = getItem(reviver);
		
		revive(reviver);

		// Get the current control name
		String controlName = SafeCreeper.instance.getConfigHandler().getControlName(revive.getLivingEntity(), "OtherMobControl");
		
		boolean broadcastRevive = SafeCreeper.instance.getConfigHandler().getOptionBoolean(revive.getLivingEntity().getWorld(), controlName, "Reviving.Reviver.BroadcastRevive", true, true, revive.getLivingEntity().getLocation());
		double broadcastRadius = SafeCreeper.instance.getConfigHandler().getOptionDouble(revive.getLivingEntity().getWorld(), controlName, "Reviving.Reviver.BroadcastRadius", 25, true, revive.getLivingEntity().getLocation());
		
		if(broadcastRevive) {
			List<Entity> nearbyEntities = reviver.getNearbyEntities(broadcastRadius, broadcastRadius, broadcastRadius);
			for(Entity e : nearbyEntities) {
				if(e instanceof Player) {
					Player p = (Player) e;
					
					p.sendMessage(ChatColor.GOLD + "A killed " + ChatColor.YELLOW + revive.getLivingEntity().getType().getName().toLowerCase() + ChatColor.GOLD + " got revived by a " + ChatColor.YELLOW + reviver.getType().getName().toLowerCase() + ChatColor.GOLD + "!");
				}
			}
		}
	}
	
	public void onReviverLostTarget(LivingEntity reviver) {
		if(!isReviver(reviver))
			return;
		
		SCLivingEntityRevive revive = getItem(reviver);
		if(revive == null)
			return;

		this.livingEntityRevive.remove(revive);
	}
	
	public void revive(LivingEntity reviver) {
		SCLivingEntityRevive revive = getItem(reviver);
		if(revive == null)
			return;
		
		revive.revive();
		this.livingEntityRevive.remove(revive);
	}
}
