package com.timvisee.safecreeper.manager;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.timvisee.safecreeper.SCLogger;
import com.timvisee.safecreeper.SafeCreeper;

public abstract class SCPluginManager {
	
	protected SCLogger log;
	
	private String pluginName;
	
	/**
	 * Constructor
	 * @param String pluginName
	 * @param log SCLogger
	 */
	public SCPluginManager(String pluginName, SCLogger log) {
		this.pluginName = pluginName;
		this.log = log;
		
		// Register the plugin manager
		SafeCreeper.instance.getPluginListener().registerPluginManager(this);
	}
	
	/**
	 * Set up the plugin hook
	 */
	public void setUp() {
		// Hook into the plugin
		hook();
	}
	
	/**
	 * Try to hook into the plugin
	 */
	public abstract void hook();
	
	/**
	 * Check if Safe Creeper is hooked into the plugin
	 * @return True if hooked
	 */
	public abstract boolean isHooked();
	
	/**
	 * Unhook the plugin
	 */
	public abstract void unhook();
	
	/**
	 * Method called when a plugin is being enabled
	 * @param e Event instance
	 */
	public void onPluginEnable(PluginEnableEvent e) {
		Plugin p = e.getPlugin();
		String pn = p.getName();
		
		// Is the plugin enabled
		if(pn.equals(pluginName)) {
			this.log.info(pluginName + " plugin enabled!");
			hook();
		}
	}
	
	/**
	 * Method called when a plugin is being disabled
	 * @param e Event instance
	 */
	public void onPluginDisable(PluginDisableEvent e) {
		Plugin p = e.getPlugin();
		String pn = p.getName();
		
		// Is the plugin disabled
		if(pn.equals(pluginName)) {
			this.log.info(pluginName + " plugin disabled unexpectedly!");
			unhook();
		}
	}
	
	/**
	 * Get the logger instance
	 * @return SCLogger instance
	 */
	public SCLogger getSCLogger() {
		return this.log;
	}
	
	/**
	 * Set the logger instance
	 * @param log SCLogger instance
	 */
	public void setSCLogger(SCLogger log) {
		this.log = log;
	}
	
	/**
	 * Get the plugin name
	 * @return Plugin name
	 */
	public String getPluginName() {
		return this.pluginName;
	}
}
