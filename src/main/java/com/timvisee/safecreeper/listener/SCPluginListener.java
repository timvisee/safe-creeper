package com.timvisee.safecreeper.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.PluginDisableEvent;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.handler.plugin.SCPluginHandler;

public class SCPluginListener implements Listener {
	
	private List<SCPluginHandler> pluginManagers = new ArrayList<SCPluginHandler>();
	
	@EventHandler
	public void onPluginEnable(PluginEnableEvent e) {
		// Call methods in registered plugin managers
		for(SCPluginHandler pm : this.pluginManagers)
			pm.onPluginEnable(e);
		
		// Run the onPluginEnable method in other plugin manager classes
		SafeCreeper.instance.getPermissionsManager().onPluginEnable(e);
	}
	
	@EventHandler
	public void onPluginDisable(PluginDisableEvent e) {
		// Call methods in registered plugin managers
		for(SCPluginHandler pm : this.pluginManagers)
			pm.onPluginDisable(e);
		
		// Run the onPluginDisable method in other plugin manager classes
		SafeCreeper.instance.getPVPArenaHandler().onPluginDisable(e);
	}
	
	/**
	 * Register a plugin manager
	 * @param pm Plugin manager to register
	 */
	public void registerPluginManager(SCPluginHandler pm) {
		if(!isPluginManagerRegistered(pm))
			this.pluginManagers.add(pm);
	}
	
	/**
	 * Check if a plugin manager is registered
	 * @param pm Plugin manager to check for
	 * @return True if registered
	 */
	public boolean isPluginManagerRegistered(SCPluginHandler pm) {
		return this.pluginManagers.contains(pm);
	}
	
	/**
	 * Unregister a plugin manager
	 * @param pm Plugin manager to unregsiter
	 */
	public void unregisterPluginManager(SCPluginHandler pm) {
		if(isPluginManagerRegistered(pm))
			this.pluginManagers.remove(pm);
	}
}
