package com.timvisee.safecreeper.api.manager;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import com.timvisee.safecreeper.SCLogger;
import com.timvisee.safecreeper.manager.SCPermissionsManager;
import com.timvisee.safecreeper.manager.SCPermissionsManager.PermissionsSystemType;

public class SCAPermissionsManager {
	
	/** @var pm SCPermissionsManager instance */
	private SCPermissionsManager pm;
	
	/**
	 * Constructor
	 * @param pm SCPermissionsManager instance
	 */
	public SCAPermissionsManager(SCPermissionsManager pm) {
		this.pm = pm;
	}
	
	/**
	 * Get the SCPermissionsManager instance
	 * @return SCPermissionsManager instance
	 */
	public SCPermissionsManager getSCPermissionsManager() {
		return this.pm;
	}
	
	/**
	 * Set the SCPermissionsManager instance
	 * @param pm SCPermissionsManager instance
	 */
	public void setSCPermissionsManager(SCPermissionsManager pm) {
		this.pm = pm;
	}
	
	/**
	 * Return the permissions system where the permissions manager is currently hooked into
	 * @return permissions system type
	 */
	public PermissionsSystemType getUsedPermissionsSystemType() {
		return this.pm.getUsedPermissionsSystemType();
	}

	/**
	 * Check if the permissions manager is currently hooked into any of the supported permissions systems
	 * @return false if there isn't any permissions system used
	 */
	public boolean isEnabled() {
		return this.pm.isEnabled();
	}
	
	/**
	 * Setup and hook into the permissions systems
	 * @return the detected permissions system
	 */
	public PermissionsSystemType setup() {
		return this.pm.setup();
	}
	
	/**
	 * Break the hook with the current hooked permissions system
	 */
	public void unhook() {
		this.pm.unhook();
	}
	
	/**
	 * Method called when a plugin is being enabled
	 * @param e Event instance
	 */
	public void onPluginEnable(PluginEnableEvent e) {
		this.pm.onPluginEnable(e);
	}
	
	/**
	 * Method called when a plugin is being disabled
	 * @param e Event instance
	 */
	public void onPluginDisable(PluginDisableEvent e) {
		this.pm.onPluginDisable(e);
	}
	
	/**
	 * Get the logger instance
	 * @return SCLogger instance
	 */
	public SCLogger getSCLogger() {
		return this.pm.getSCLogger();
	}
	
	/**
	 * Set the logger instance
	 * @param log SCLogger instance
	 */
	public void setSCLogger(SCLogger log) {
		this.pm.setSCLogger(log);
	}
	
	/**
	 * Check if the player has permission. If no permissions system is used, the player has to be OP
	 * @param p player
	 * @param permsNode permissions node
	 * @return true if the player is permitted
	 */
	public boolean hasPermission(Player p, String permsNode) {
		return this.pm.hasPermission(p, permsNode);
	}
	
	/**
	 * Check if a player has permission
	 * @param player player
	 * @param permissionNode permission node
	 * @param def default if no permissions system is used
	 * @return true if the player is permitted
	 */
	public boolean hasPermission(Player p, String permsNode, boolean def) {
		return this.pm.hasPermission(p, permsNode, def);
	}
	
	/**
	 * Get the permission groups the player is currently in
	 * @param p The player to get the permissions groups from
	 * @return Permissions groups the player is in
	 */
	public List<String> getGroups(Player p) {
		return this.pm.getGroups(p);
	}
}
