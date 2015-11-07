package com.timvisee.safecreeper.handler.plugin;

import net.slipcor.pvparena.PVPArena;
import net.slipcor.pvparena.api.PVPArenaAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import com.timvisee.safecreeper.SCLogger;

public class SCPVPArenaHandler extends SCPluginHandler {
	
	private static String PLUGIN_NAME = "pvparena";
	
	private boolean pvparenaEnabled = false;
	
	/**
	 * Constructor
	 * @param log SCLogger
	 */
	public SCPVPArenaHandler(SCLogger log) {
		super(PLUGIN_NAME, log);
	}
	
	/**
	 * Try to hook PVPArena
	 */
	public void hook() {
		pvparenaEnabled = false;
		
		// PVP Arena has to be installed/enabled
    	if(!Bukkit.getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
    		this.log.info("Disabling PVPArena usage, plugin not found.");
    		return;
    	}
    	
    	try {
    		// Try to get the PVPArena plugin
    		Plugin paPlugin = (PVPArena) Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
	        
    		// The plugin variable may not be null
	        if (paPlugin == null) {
	        	this.log.info("Unable to hook into PVPArena, plugin not found!");
	            return;
	        }
	        
	        pvparenaEnabled = true;
	        
	        // Hooked into PVPArena, show status message
	        this.log.info("Hooked into PVPArena!");
	        return;
	        
    	} catch(NoClassDefFoundError ex) {
    		// Unable to hook into PVPArena, show warning/error message.
    		this.log.info("Error while hooking into PVPArena!");
    		return;
    	} catch(Exception ex) {
    		// Unable to hook into PVPArena, show warning/error message.
    		this.log.info("Error while hooking into PVPArena!");
    		return;
    	} 
	}
	
	/**
	 * Check if Safe Creeper is hooked into PVPArena
	 */
	public boolean isHooked() {
		return this.pvparenaEnabled;
	}
	
	/**
	 * Unhook PVPArena
	 */
	public void unhook() {
        this.pvparenaEnabled = false;
        this.log.info("Unhooked PVPArena!");
	}
	
	/**
	 * Check if there's any PVP Arena at a location
	 * @param loc Location to check on
	 * @return True if there's any PVP Arena on this location
	 */
	public boolean isPVPArenaAt(Location loc) {
    	String aName = PVPArenaAPI.getArenaNameByLocation(loc);
    	return (!aName.equals(""));
    }
	
	/**
	 * Get the PVP Arena on a location
	 * @param loc Location to get the PVP Arena from
	 * @return PVP Arena
	 */
    public String getPVPArenaAt(Location loc) {
    	return PVPArenaAPI.getArenaNameByLocation(loc);
    }
}
