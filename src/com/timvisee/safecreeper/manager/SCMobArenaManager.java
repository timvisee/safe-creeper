package com.timvisee.safecreeper.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.MobArenaHandler;
import com.garbagemule.MobArena.framework.Arena;
import com.timvisee.safecreeper.SCLogger;

public class SCMobArenaManager extends SCPluginManager {
	
	private static String PLUGIN_NAME = "MobArena";
	
	private MobArenaHandler ma;
	
	/**
	 * Constructor
	 * @param log SCLogger
	 */
	public SCMobArenaManager(SCLogger log) {
		super(PLUGIN_NAME, log);
	}
	
	/**
	 * Try to hook into Safe Creeper
	 */
	public void hook() {
		// MobArena has to be installed/enabled
    	if(!Bukkit.getServer().getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
    		this.log.info("Disabling MobArena usage, plugin not found.");
    		return;
    	}
    	
    	try {
    		// Try to get the MobArenap plugin
    		Plugin plugin = (MobArena) Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
	        
	        if (plugin == null) {
	        	this.log.info("Unable to hook into MobArena, plugin not found!");
	            return;
	        }
	        
	        // Hooked into MobArena, show a status message
	        this.ma = new MobArenaHandler();
	        this.log.info("Hooked into MobArena!");
	        return;
	        
    	} catch(NoClassDefFoundError ex) {
    		// Unable to hook into MobArena, show warning/error message.
    		this.log.info("Error while hooking into MobArena!");
    		return;
    	} catch(Exception ex) {
    		// Unable to hook into MobArena, show warning/error message.
    		this.log.info("Error while hooking into MobArena!");
    		return;
    	}
	}
	
	/**
	 * Check if Safe Creeper is hooked into MobArena
	 */
	public boolean isHooked() {
		return (this.ma != null);
	}
	
	/**
	 * Unhook MobArena
	 */
	public void unhook() {
        // Unhook MobArena
        this.ma = null;
        this.log.info("Unhooked MobArena!");
	}
	
	/**
	 * Get the mob arena handler
	 * @return Get the mob arena handler instance
	 */
	public MobArenaHandler getMobArenaHandler() {
		return this.ma;
	}
	
	/**
	 * Set the mob arena handler instance
	 * @param ma
	 */
	public void setMobArenaHandler(MobArenaHandler ma) {
		this.ma = ma;
	}
	
	/**
	 * Is a location inside any Mob Arena region
	 * @param loc The location to check
	 * @return True if the location is inside any arena
	 */
	public boolean isInArena(Location loc) {
		// Make sure the MobArena instance is not null
		if(this.ma == null)
			return false;
		
		// Return if the location is inside any Mob Arena region
		try {
			return this.ma.inRegion(loc);
		} catch(Exception ex) {
			return false;
		}
	}
	
	/**
	 * Get the arena at any locatoin
	 * @param loc Location to get the arena from
	 * @return Arena
	 */
	public Arena getArenaAt(Location loc) {
		// Make sure the Mob Arena is not null
		if(this.ma == null)
			return null;
		
		// Get the arena at a location
		try {
			return this.ma.getArenaAtLocation(loc);
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * Is a living entity inside any mob arena
	 * @param le The living entity to check
	 * @return True if inside any arena
	 */
	public boolean isMonsterInArena(LivingEntity le) {
		//Bukkit.broadcastMessage("checking...");
		
		// Make sure the Mob Arena is not null
		if(this.ma == null)
			return false;
		
		/*
		if(this.ma.isMonsterInArena(le))
			Bukkit.broadcastMessage("TRUE!");
		else
			Bukkit.broadcastMessage("FALSE!");
		*/
		
		// Return if this monster is in any arena
		try {
			return this.ma.isMonsterInArena(le);
		} catch(Exception ex) {
			return false;
		}
	}
	
	/**
	 * Get the Mob Arena a living entity is in
	 * @param le Living entity to get the arena from
	 * @return Mob Arena or null
	 */
	public Arena getArenaWithMonster(LivingEntity le) {
		// Make sure the Mob Arena is not null
		if(this.ma == null)
			return null;
				
		// Return if this monster is in any arena
		try {
			return this.ma.getArenaWithMonster(le);
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * Get the arena at any location
	 * @param loc Location to get the arena from
	 * @return boolean False if failed
	 */
	public boolean addMonsterToArena(Arena arena, LivingEntity le) {
		// Make sure the arena and the monster are not null
		if(arena == null || le == null)
			return false;
		
		// Add the monster to the arena
		try {
			arena.getMonsterManager().addMonster(le);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}
