package com.timvisee.safecreeper.manager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.timvisee.safecreeper.SCLogger;

public class SCWorldGuardManager extends SCPluginManager {
	
	private static String PLUGIN_NAME = "WorldGuard";
	
	private WorldGuardPlugin wg;
	
	/**
	 * Contributor
	 * @param log SCLogger
	 */
	public SCWorldGuardManager(SCLogger log) {
		super(PLUGIN_NAME, log);
	}
	
	/**
	 * Try to hook WorldGuard
	 */
	public void hook() {
		// WorldGuard has to be installed and enabled
    	if(!Bukkit.getServer().getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
    		this.log.info("Disabling WorldGuard usage, plugin not found.");
    		return;
    	}
    	
        try {
    		Plugin wg = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
			 
	        // WorldGuard may not b'//e loaded
	        if (wg == null || !(wg instanceof WorldGuardPlugin)) {
	        	this.wg = null;
	        	this.log.info("Unable to hook into WorldGuard, plugin not found!");
	        	return;
	        }
	        
	        this.wg = (WorldGuardPlugin) wg;
	        this.log.info("Hooked into WorldGuard!");
	        return;
	        
    	} catch(NoClassDefFoundError ex) {
    		// Unable to hook into PVPArena, show warning/error message.
    		this.log.info("Error while hooking into WorldGuard!");
    		return;
    	} catch(Exception ex) {
    		// Unable to hook into PVPArena, show warning/error message.
    		this.log.info("Error while hooking into WorldGuard!");
    		return;
    	} 
	}
	
	/**
	 * Check if Safe Creeper is hooked into WorldGuard
	 */
	public boolean isHooked() {
		return (this.wg != null);
	}
	
	/**
	 * Unhook WorldGuard
	 */
	public void unhook() {
        this.wg = null;
        this.log.info("Unhooked WorldGuard!");
	}
	
	/**
	 * Get World Guard 
	 * @return World Guard
	 */
	public WorldGuardPlugin getWorldGuard() {
		return this.wg;
	}
	
	/**
	 * Set the World Guard instance
	 * @param wg World Guard instance
	 */
	public void setWorldGuard(WorldGuardPlugin wg) {
		this.wg = wg;
	}
}
