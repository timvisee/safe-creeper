package com.timvisee.safecreeper.manager;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class SCWorldGuardManager {
	
	private Logger log;
	private WorldGuardPlugin wg;
	
	/**
	 * Contributor
	 * @param log Logger
	 */
	public SCWorldGuardManager(Logger log) {
		this.log = log;
	}
	
	/**
	 * Set up the World Guard hook
	 */
	public void setup() {
		// WorldGuard has to be installed and enabled
    	if(!Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
    		this.log.info("Disabling WorldGUard usage, plugin not found.");
    		return;
    	}
    	
		Plugin wg = Bukkit.getPluginManager().getPlugin("WorldGuard");
		 
        // WorldGuard may not b'//e loaded
        if (wg == null || !(wg instanceof WorldGuardPlugin)) {
        	this.wg = null;
        	this.log.info("Unable to hook into WorldGuard, plugin not found!");
        	return;
        }
        
        this.wg = (WorldGuardPlugin) wg;
        this.log.info("Hooked into WorldGuard!");
	}
	
	/**
	 * Get the logger instance
	 * @return Logger instance
	 */
	public Logger getLogger() {
		return this.log;
	}
	
	/**
	 * Set the logger instance
	 * @param log Logger instance
	 */
	public void setLogger(Logger log) {
		this.log = log;
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
	
	/**
	 * Check if the manager is hooked into World Guard
	 * @return True if hooked into World Guard
	 */
	public boolean isHooked() {
		return (this.wg != null);
	}
}
