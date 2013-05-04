package com.timvisee.safecreeper.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.util.SCFileUpdater;
import com.timvisee.safecreeper.util.SCUpdateChecker;

public class SafeCreeperApi {
	
	private static SafeCreeper plugin;
	
	/**
	* Hook into Safe Creeper
	* @return SC instance
	*/
	public static SafeCreeper hookSafeCreeper() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("SafeCreeper");
		if (plugin == null && !(plugin instanceof SafeCreeper))
			return null;
		return (SafeCreeper) plugin;
    }
	
	/**
	 * Get the control name from an entity
	 * @param e the entity
	 * @return the control name
	 */
	public String getControlName(Entity e) {
		return SafeCreeper.instance.getConfigManager().getControlName(e);
	}
	
	/**
	 * Get the control name from an entity
	 * @param e the entity
	 * @param def the default control name
	 * @return the control name
	 */
	public String getControlName(Entity e, String def) {
		return SafeCreeper.instance.getConfigManager().getControlName(e, def);
	}
	
	/**
	 * Reload all the config files
	 */
	public void reloadAllConfigs() {
		SafeCreeper.instance.getConfigManager().reloadAllConfigs();
	}
	
	/**
	 * Reload the global config file
	 */
	public void reloadGlobalConfig() {
		SafeCreeper.instance.getConfigManager().reloadGlobalConfig();
	}
	
	/**
	 * Reload all the world config files
	 */
	public void reloadWorldConfigs() {
		SafeCreeper.instance.getConfigManager().reloadWorldConfigs();
	}
	
	/**
	 * Update all outdated config files, this is an automated process, the files are automatically backuped once they're going to be updated
	 */
	public void updateConfigFiles() {
		((SCFileUpdater) new SCFileUpdater()).updateFiles();
	}
	
	/**
	 * Reload the permissions core, this will re-hook in all the permissions systems
	 */
	public void reloadPermissions() {
		// Reload the permissions core
		SafeCreeper.instance.setupPermissionsManager();
	}

	public enum SafeCreeperManagerType {
		CONFIG_MANAGER,
		LIKEABOSS_MANAGER,
		PERMISSIONS_MANAGER,
		STATICS_MANAGER;
	}
	
	/**
	 * Re-Setup a manager in Safe Creeper
	 * @param managerType the manager type
	 * @return true if succeed
	 */
	public boolean setupManager(SafeCreeperManagerType managerType) {
		try {
			switch (managerType) {
			case CONFIG_MANAGER:
				SafeCreeper.instance.setupConfigManager();
				break;
				
			case LIKEABOSS_MANAGER:
				SafeCreeper.instance.setupCorruptionManager();
				break;
				
			case PERMISSIONS_MANAGER:
				SafeCreeper.instance.setupPermissionsManager();
				break;
				
			case STATICS_MANAGER:
				SafeCreeper.instance.setupConfigManager();
				break;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public enum SafeCreeperHandlerType {
		TVNLibHandler;
	}
	
	/**
	 * Re-Setup a handler in Safe Creeper
	 * @param handlerType the handler type
	 * @return true if succeed
	 */
	public boolean setupHandler(SafeCreeperHandlerType handlerType) {
		try {
			switch(handlerType) {
			case TVNLibHandler:
				SafeCreeper.instance.setupTVNLibManager();
				break;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check for updates
	 * @return empty string when no update was found, returns version number in string when new version is found
	 */
	public String checkUpdates() {
		// Check for updates
		SCUpdateChecker uc = SafeCreeper.instance.getUpdateChecker();
		uc.refreshUpdatesData();
		
		if(uc.isNewVersionAvailable())
			return uc.getNewestVersion();
		return "";
	}
	
	/**
	 * Get the current Safe Creeper version running
	 * @return the current Safe Creeper version
	 */
	public String getVersion() {
		return SafeCreeperApi.plugin.getVersion();
	}
	
	/**
	* Set the SC plugin
	* @param plugin the SC plugin
	*/
	public static void setPlugin(SafeCreeper plugin) {
		SafeCreeperApi.plugin = plugin;
	}
	
	/**
	* Get the SC plugin
	* @return the SC plugin
	*/
	public static SafeCreeper getPlugin() {
		return plugin;
	}
}
