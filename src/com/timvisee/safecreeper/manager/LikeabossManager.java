package com.timvisee.safecreeper.manager;

import org.bukkit.entity.Entity;

import com.timvisee.safecreeper.SafeCreeper;

import com.mcdr.likeaboss.LabAPI;
import com.mcdr.likeaboss.Likeaboss;

public class LikeabossManager {
	
	private boolean labEnabled = false;
	
	/**
	 * Constructor
	 */
	public LikeabossManager() {
		setup();
	}
	
	/**
	 * Setup the Likeaboss manager, hook into Likeaboss
	 */
	public void setup() {
    	this.labEnabled = false;
    	
    	// Likeaboss has to be installed/enabled
		if(!SafeCreeper.instance.getServer().getPluginManager().isPluginEnabled("Likeaboss")) {
			SafeCreeper.instance.getSCLogger().info("Disabling Likeaboss usage, plugin not found.");
			return;
		}
		
		try {
			// Get the Likeaboss plugin
			Likeaboss lab = (Likeaboss) SafeCreeper.instance.getServer().getPluginManager().getPlugin("Likeaboss");
	        
			// The Likeaboss plugin may not be null
	        if(lab == null) {
	        	SafeCreeper.instance.getSCLogger().info("Disabling Likeaboss usage, Likeaboss not found.");
	            return;
	        }
	        
	        // Hooked into Likeaboss, show status message
	        SafeCreeper.instance.getSCLogger().info("Hooked into Likeaboss!");
	        this.labEnabled = true;
	        
		} catch(NoClassDefFoundError ex) {
			SafeCreeper.instance.getSCLogger().info("Unable to hook into Likeaboss, plugin not found.");
			return;
		} catch(Exception ex) {
			SafeCreeper.instance.getSCLogger().info("Unable to hook into Likeaboss, plugin not found.");
			return;
		}
	}
	
	/**
	 * Check if an entity is a Likeaboss boss
	 * @param e the entity to check
	 * @return true if the entity is a Lab boss
	 */
	public boolean isBoss(Entity e) {
		if(this.labEnabled)
			return (LabAPI.isBoss(e));
		return false;
	}
	
	/**
	 * Check if the Likeaboss hook is enabled
	 * @return true if enabled
	 */
	public boolean isLabEnabled() {
		return this.labEnabled;
	}
}
