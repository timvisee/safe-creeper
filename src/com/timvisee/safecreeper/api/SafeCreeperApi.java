package com.timvisee.safecreeper.api;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.timvisee.safecreeper.SafeCreeper;

public class SafeCreeperApi {
	
	public static String SC_PLUGIN_NAME = "SafeCreeper";
	
	private SafeCreeper sc;
	private Plugin p;
	
	/**
	 * Constructor
	 */
	public SafeCreeperApi(Plugin p) {
		// Store the plugin instance
		this.p = p;
		
		// Try to hook into SafeCreeper
		hook();
	}
	
	/**
	* Hook Safe Creeper
	* @return True if succeed
	*/
	public boolean hook() {
		Logger log = Logger.getLogger("Minecraft");
		
		try {
    		// Unhook SafeCreeper first if already hooked
			if(isHooked())
				unhook();
			
			// Try to get the SafeCreeper plugin instance
			Plugin p = Bukkit.getServer().getPluginManager().getPlugin(SC_PLUGIN_NAME);
			if(p == null && !(p instanceof SafeCreeper)) {
				if(this.p != null)
					log.info("[" + this.p.getName() + "] Can't hook into Safe Creeper, plugin not found!");
					
				return false;
			}

			// Show a status message
			if(this.p != null)
				log.info("[" + this.p.getName() + "] Hooked into Safe Creeper!");
    		
			// Set the SafeCreeper plugin instance
			this.sc = (SafeCreeper) p;
			
			// Make sure the Safe Creeper API is enabled
			if(!this.sc.getApiManager().isEnabled()) {
				log.info("[" + this.p.getName() + "] Can't hook into Safe Creeper, API not enabled!");
				this.sc = null;
				return false;
			}
			
			// Register the current API session in Safe Creeper
			this.sc.getApiManager().registerApiSession(this);
			
			// Hook succeed, return true
			return true;
	        
    	} catch(NoClassDefFoundError ex) {
    		// Unable to hook into MobArena, show warning/error message.
			if(this.p != null)
				log.info("[" + this.p.getName() + "] Error while hooking into Safe Creeper!");
    		return false;
    	} catch(Exception ex) {
    		// Unable to hook into MobArena, show warning/error message.
			if(this.p != null)
				log.info("[" + this.p.getName() + "] Error while hooking into Safe Creeper!");
    		return false;
    	}
    }
	
	/**
	 * Check if the plugin is hooked into SafeCreeper
	 * @return True if hooked
	 */
	public boolean isHooked() {
		return (this.sc != null);
	}
	
	/**
	 * Unhook Safe Creeper
	 */
	public void unhook() {
		Logger log = Logger.getLogger("Minecraft");
		
		// Unhook Safe Creeper
		this.sc = null;
		
		// Show a status message
		if(this.p != null)
			log.info("[" + this.p.getName() + "] Unhooked Safe Creeper!");
	}
	
	/**
	 * Get the Safe Creeper plugin instance
	 * @return Safe Creeper plugin instance, null if not hooked into Safe Creeper
	 */
	public SafeCreeper getSC() {
		return getSafeCreeper();
	}
	
	/**
	 * Get the Safe Creeper plugin instance
	 * @return Safe Creeper plugin instance, null if not hooked into Safe Creeper
	 */
	public SafeCreeper getSafeCreeper() {
		return this.sc;
	}
	
	/**
	 * Set the Safe Creeper plugin instance
	 * @param p Safe Creeper plugin instance
	 */
	public void getSC(SafeCreeper p) {
		setSafeCreeper(p);
	}
	
	/**
	 * Set the Safe Creeper plugin instance
	 * @param p Safe Creeper plugin instance
	 */
	public void setSafeCreeper(SafeCreeper p) {
		this.sc = p;
	}
	
	/**
	 * Get the plugin instance that hooked into Safe Creeper and uses this API layer
	 * @return Plugin instance
	 */
	public Plugin getPlugin() {
		return this.p;
	}
	
	/**
	 * Get the running Safe Creeper version
	 * @return Safe Creeper version number, empty string if not hooked into Safe Creeper
	 */
	public String getVersion() {
		return this.sc.getVersion();
	}
}
