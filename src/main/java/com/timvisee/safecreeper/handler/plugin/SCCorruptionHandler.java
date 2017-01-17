package com.timvisee.safecreeper.handler.plugin;

import com.mcdr.corruption.Corruption;
import com.mcdr.corruption.CorruptionAPI;
import com.mcdr.corruption.entity.Boss;
import com.timvisee.safecreeper.SCLogger;
import com.timvisee.safecreeper.SafeCreeper;
import org.bukkit.entity.Entity;

public class SCCorruptionHandler extends SCPluginHandler {

    private static String PLUGIN_NAME = "Corruption";

    private boolean corEnabled = false;

    /**
     * Constructor
     *
     * @param log SCLogger
     */
    public SCCorruptionHandler(SCLogger log) {
        super(PLUGIN_NAME, log);
    }

    /**
     * Try to hook into Corruption
     */
    public void hook() {
        this.corEnabled = false;

        // Corruption has to be installed/enabled
        if(!SafeCreeper.instance.getServer().getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
            SafeCreeper.instance.getLogger().info("Disabling Corruption usage, plugin not found.");
            return;
        }

        try {
            // Get the Corruption plugin
            Corruption cor = (Corruption) SafeCreeper.instance.getServer().getPluginManager().getPlugin(PLUGIN_NAME);

            // The Corruption plugin may not be null
            if(cor == null) {
                SafeCreeper.instance.getLogger().info("Disabling Corruption usage, Corruption not found.");
                return;
            }

            // Hooked into Corruption, show status message
            SafeCreeper.instance.getLogger().info("Hooked into Corruption!");
            this.corEnabled = true;

        } catch(NoClassDefFoundError ex) {
            SafeCreeper.instance.getLogger().info("Unable to hook into Corruption, plugin not found.");
            return;
        } catch(Exception ex) {
            SafeCreeper.instance.getLogger().info("Unable to hook into Corruption, plugin not found.");
            return;
        }
    }

    /**
     * Is Safe Creeper hooked into Corruption
     */
    public boolean isHooked() {
        return this.corEnabled;
    }

    /**
     * Unhook Corruption
     */
    public void unhook() {
        this.corEnabled = false;
        this.log.info("Unhooked Corruption!");
    }

    /**
     * Check if an entity is a Corrution boss
     *
     * @param e the entity to check
     * @return true if the entity is a Lab boss
     */
    public boolean isBoss(Entity e) {
        if(!isHooked())
            return false;

        return (CorruptionAPI.isBoss(e));
    }

    /**
     * Check if the Corrution hook is enabled
     *
     * @return true if enabled
     */
    public boolean isCorEnabled() {
        return this.corEnabled;
    }

    /**
     * Get a boss instance
     *
     * @param e Entity to get the boss instance from
     * @return Boss instance or null
     */
    public Boss getBoss(Entity e) {
        // Make sure Safe Creeper is hooked into Corruption
        if(!isHooked())
            return null;

        // Make sure the entity is a boss
        if(!isBoss(e))
            return null;

        return CorruptionAPI.getBoss(e);
    }
}
