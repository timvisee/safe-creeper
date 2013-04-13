package com.timvisee.safecreeper.manager;

import org.bukkit.entity.Entity;

import com.mcdr.corruption.Corruption;
import com.mcdr.corruption.CorruptionAPI;
import com.mcdr.corruption.entity.Boss;
import com.timvisee.safecreeper.SafeCreeper;

public class SCCorruptionManager {
	
	private boolean corEnabled = false;
	
	/**
	 * Constructor
	 */
	public SCCorruptionManager() {
		setup();
	}
	
	/**
	 * Setup the Corruption manager, hook into Corruption
	 */
	public void setup() {
    	this.corEnabled = false;
    	
    	// Corruption has to be installed/enabled
		if(!SafeCreeper.instance.getServer().getPluginManager().isPluginEnabled("Corruption")) {
			SafeCreeper.instance.getSCLogger().info("Disabling Corruption usage, plugin not found.");
			return;
		}
		
		try {
			// Get the Corruption plugin
			Corruption cor = (Corruption) SafeCreeper.instance.getServer().getPluginManager().getPlugin("Corruption");
	        
			// The Corruption  plugin may not be null
	        if(cor == null) {
	        	SafeCreeper.instance.getSCLogger().info("Disabling Corruption usage, Corruption not found.");
	            return;
	        }
	        
	        // Hooked into Corruption, show status message
	        SafeCreeper.instance.getSCLogger().info("Hooked into Corruption!");
	        this.corEnabled = true;
	        
		} catch(NoClassDefFoundError ex) {
			SafeCreeper.instance.getSCLogger().info("Unable to hook into Corruption, plugin not found.");
			return;
		} catch(Exception ex) {
			SafeCreeper.instance.getSCLogger().info("Unable to hook into Corruption, plugin not found.");
			return;
		}
	}
	
	/**
	 * Check if an entity is a Corrution boss
	 * @param e the entity to check
	 * @return true if the entity is a Lab boss
	 */
	public boolean isBoss(Entity e) {
		if(!isHooked())
			return false;
		
		return (CorruptionAPI.isBoss(e));
	}
	
	/**
	 * Check if the Corrution hook is enabled
	 * @return true if enabled
	 */
	public boolean isCorEnabled() {
		return this.corEnabled;
	}
	
	/**
	 * Check if Safe Creeper is hooked into Corruption
	 * @return Is hooked
	 */
	public boolean isHooked() {
		return isCorEnabled();
	}
	
	/**
	 * Get a boss instance
	 * @param e Entity to get the boss instance from
	 * @return Boss instance or null
	 */
	public Boss getBoss(Entity e) {
		// Make sure Safe Creeper is hooked into Corruption
		if(!isHooked())
			return null;
		
		// Make sure the entity is a boss
		if(!isBoss(e))
			return null;
		
		return CorruptionAPI.getBoss(e);
	}
}
