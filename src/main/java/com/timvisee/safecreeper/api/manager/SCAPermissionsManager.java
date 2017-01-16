package com.timvisee.safecreeper.api.manager;

import com.timvisee.safecreeper.permission.PermissionsManager;
import com.timvisee.safecreeper.permission.PermissionsManager.PermissionsSystemType;
import org.bukkit.entity.Player;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.List;
import java.util.logging.Logger;

public class SCAPermissionsManager {

    /**
     * Permissions manager instance
     */
    private PermissionsManager permsMan;

    /**
     * Constructor
     *
     * @param permsMan SCPermissionsManager instance
     */
    public SCAPermissionsManager(PermissionsManager permsMan) {
        this.permsMan = permsMan;
    }

    /**
     * Get the SCPermissionsManager instance
     *
     * @return SCPermissionsManager instance
     */
    public PermissionsManager getSCPermissionsManager() {
        return this.permsMan;
    }

    /**
     * Set the SCPermissionsManager instance
     *
     * @param pm SCPermissionsManager instance
     */
    public void setSCPermissionsManager(PermissionsManager pm) {
        this.permsMan = pm;
    }

    /**
     * Return the permissions system where the permissions manager is currently hooked into
     *
     * @return permissions system type
     */
    public PermissionsSystemType getUsedPermissionsSystemType() {
        return this.permsMan.getUsedPermissionsSystemType();
    }

    /**
     * Check if the permissions manager is currently hooked into any of the supported permissions systems
     *
     * @return false if there isn't any permissions system used
     */
    public boolean isEnabled() {
        return this.permsMan.isEnabled();
    }

    /**
     * Setup and hook into the permissions systems
     *
     * @return the detected permissions system
     */
    public PermissionsSystemType setup() {
        return this.permsMan.setup();
    }

    /**
     * Break the hook with the current hooked permissions system
     */
    public void unhook() {
        this.permsMan.unhook();
    }

    /**
     * Method called when a plugin is being enabled
     *
     * @param e Event instance
     */
    public void onPluginEnable(PluginEnableEvent e) {
        this.permsMan.onPluginEnable(e);
    }

    /**
     * Method called when a plugin is being disabled
     *
     * @param e Event instance
     */
    public void onPluginDisable(PluginDisableEvent e) {
        this.permsMan.onPluginDisable(e);
    }

    /**
     * Get the logger instance.
     *
     * @return Logger instance.
     */
    public Logger getLogger() {
        return this.permsMan.getLogger();
    }

    /**
     * Set the logger instance.
     *
     * @param log Logger instance.
     */
    public void setLogger(Logger log) {
        this.permsMan.setLogger(log);
    }

    /**
     * Check if the player has permission. If no permissions system is used, the player has to be OP
     *
     * @param p         player
     * @param permsNode permissions node
     * @return true if the player is permitted
     */
    public boolean hasPermission(Player p, String permsNode) {
        return this.permsMan.hasPermission(p, permsNode);
    }

    /**
     * Check if a player has permission
     *
     * @param p         player
     * @param permsNode permission node
     * @param def       default if no permissions system is used
     * @return true if the player is permitted
     */
    public boolean hasPermission(Player p, String permsNode, boolean def) {
        return this.permsMan.hasPermission(p, permsNode, def);
    }

    /**
     * Get the permission groups the player is currently in
     *
     * @param p The player to get the permissions groups from
     * @return Permissions groups the player is in
     */
    public List<String> getGroups(Player p) {
        return this.permsMan.getGroups(p);
    }
}
