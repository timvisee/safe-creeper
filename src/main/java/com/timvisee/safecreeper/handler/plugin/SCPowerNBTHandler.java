package com.timvisee.safecreeper.handler.plugin;

import com.timvisee.safecreeper.SCLogger;
import me.dpohvar.powernbt.api.NBTManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SCPowerNBTHandler extends SCPluginHandler {

    /**
     * The name of the plugin that is hooked.
     */
    private static final String PLUGIN_NAME = "PowerNBT";

    /**
     * A flag to track whether the PowerNBT plugin is hooked or not.
     */
    private boolean hooked = false;

    /**
     * Constructor.
     *
     * @param log        SCLogger
     */
    public SCPowerNBTHandler(SCLogger log) {
        super(PLUGIN_NAME, log);
    }

    @Override
    public void hook() {
        // Force-unhook the plugin before trying to hook into it
        this.hooked = false;

        // Factions has to be installed/enabled
        if(!Bukkit.getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
            this.log.info("Disabling " + PLUGIN_NAME + " usage, plugin not found.");
            return;
        }

        try {
            // Get the PowerNBT plugin
            Plugin plugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);

            // The factions plugin may not be null
            if (plugin == null) {
                this.log.info("Unable to hook into Factions, plugin not found!");
                return;
            }

            this.hooked = true;

            // Hooked into Factions, show status message
            this.log.info("Hooked into " + PLUGIN_NAME + "!");

        } catch(NoClassDefFoundError | Exception ex) {
            // Unable to hook into Factions, show warning/error message.
            this.log.info("Error while hooking into " + PLUGIN_NAME + "!");
        }
    }

    @Override
    public boolean isHooked() {
        return this.hooked & getNBTManager() != null;
    }

    @Override
    public void unhook() {
        this.hooked = false;

        // Show an unhook message
        this.log.info("Unhooked " + PLUGIN_NAME + "!");
    }

    /**
     * Get the NBT Manager from the PowerNBT API.
     *
     * @return The NBT Manager instance, or null if the plugin isn't properly hooked.
     */
    public NBTManager getNBTManager() {
        return NBTManager.getInstance();
    }
}
