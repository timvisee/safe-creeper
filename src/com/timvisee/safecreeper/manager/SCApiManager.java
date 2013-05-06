package com.timvisee.safecreeper.manager;

import java.util.ArrayList;
import java.util.List;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.api.SafeCreeperApi;

public class SCApiManager {

	boolean apiEnabled = false;
	
	List<SafeCreeperApi> apiSessions = new ArrayList<SafeCreeperApi>();
	
	/**
	 * Constructor
	 */
	public SCApiManager() { }
	
	/**
	 * Constructor
	 * @param enableApi True to enable the Safe Creeper API
	 */
	public SCApiManager(boolean enableApi) {
		setEnabled(apiEnabled);
	}
	
	/**
	 * Register an API session
	 * @param api
	 */
	public void registerApiSession(SafeCreeperApi api) {
		if(isApiSession(api))
			return;
		
		// Add the API session
		this.apiSessions.add(api);
		
		// Show a 'hooked' message
		if(api.getPlugin() != null)
			SafeCreeper.instance.getSCLogger().info(api.getPlugin().getName() + " hooked into Safe Creeper!");
	}
	
	/**
	 * Check if the param instance is a registered API session
	 * @param api Safe Creeper API (layer) instance
	 * @return
	 */
	public boolean isApiSession(SafeCreeperApi api) {
		return this.apiSessions.contains(api);
	}
	
	/**
	 * Get the amount of active API sessions
	 * @return Amount of active API sessions
	 */
	public int getApiSessionsCount() {
		return this.apiSessions.size();
	}
	
	/**
	 * Unregister the an API session, automaticly forces the plugin of the API session to unhook Safe Creeper
	 * @param api Safe Creeper API (layer) instance
	 */
	public void unregisterApiSession(SafeCreeperApi api) {
		unregisterApiSession(api, true);
	}
	
	/**
	 * Unregister the an API session
	 * @param api Safe Creeper API (layer) instance
	 * @param forceUnhook True to force the plugin to unhook Safe Creeper
	 */
	public void unregisterApiSession(SafeCreeperApi api, boolean forceUnhook) {
		// Should the plugin unhook Safe Creeper
		if(forceUnhook)
			api.unhook();
		
		// Make sure this api session was registered
		if(!isApiSession(api))
			return;
		
		// Remove/unregister the api instance
		this.apiSessions.remove(api);
	}
	
	/**
	 * Unregister all active API sessions, automaticly forces the plugins of the sessions to unhook
	 */
	public void unregisterAllApiSessions() {
		for(int i = 0; i < this.apiSessions.size(); i++) {
			// Get the current entry
			SafeCreeperApi api = this.apiSessions.get(i);
			
			// Make sure the entry is not null
			if(api == null)
				continue;
			
			// Unregister the current entry
			unregisterApiSession(api);
			
			i--;
		}
	}
	
	/**
	 * Set if the Safe Creeper API is enabled or not
	 * @param enabled True to enable the Safe Creeper API
	 */
	public void setEnabled(boolean enabled) {
		// Make sure the value is different than before
		if(this.apiEnabled != enabled) {
			// Enable or disable the API
			this.apiEnabled = enabled;
			
			// Show a status message
			if(enabled)
				SafeCreeper.instance.getSCLogger().info("Safe Creeper API enabled!");
			else
				SafeCreeper.instance.getSCLogger().info("Safe Creeper API disabled!");
			
			// Unregister all api sessions if the API was disabled
			if(!enabled)
				unregisterAllApiSessions();
		}
	}
	
	/**
	 * Check if the Safe Creeper API is enabled
	 * @return True if the API is enabled
	 */
	public boolean isEnabled() {
		return this.apiEnabled;
	}
}
