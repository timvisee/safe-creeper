package com.timvisee.safecreeper.handler.plugin;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import com.timvisee.safecreeper.SCLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class SCFactionsHandler extends SCPluginHandler {

    private static String PLUGIN_NAME = "Factions";

    private boolean factionsEnabled = false;

    /**
     * Constructor
     *
     * @param log SCLogger
     */
    public SCFactionsHandler(SCLogger log) {
        super(PLUGIN_NAME, log);
    }

    /**
     * Try to hook into the Factions plugin
     */
    public void hook() {
        this.factionsEnabled = false;

        // Factions has to be installed/enabled
        if(!Bukkit.getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
            this.log.info("Disabling Factions usage, plugin not found.");
            return;
        }

        try {
            // Get the Factions plugin
            Plugin plugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);

            // The factions plugin may not ben ull
            if(plugin == null) {
                this.log.info("Unable to hook into Factions, plugin not found!");
                return;
            }

            this.factionsEnabled = true;

            // Hooked into Factions, show status message
            this.log.info("Hooked into Factions!");

        } catch(NoClassDefFoundError | Exception ex) {
            // Unable to hook into Factions, show warning/error message.
            this.log.info("Error while hooking into Factions!");
        }
    }

    /**
     * Check if Safe Creeper is hooked into Factions
     */
    public boolean isHooked() {
        return this.factionsEnabled;
    }

    /**
     * Unhook Factions
     */
    public void unhook() {
        this.factionsEnabled = false;
        this.log.info("Unhooked Factions!");
    }

    /**
     * Check if there's any faction ad a location
     *
     * @param loc Location to check
     * @return True if there's any faction at the location
     */
    public boolean isFactionAt(Location loc) {
        try {
            // Get the faction
            Faction f = BoardColl.get().getFactionAt(PS.valueOf(loc));

            // If returned null, there's no faction found on this area
            if(f == null)
                return false;

            // The faction area has to be 'normal'
            return (f.isNormal());
        } catch(NoClassDefFoundError ex) {
            return false;
        } catch(Exception ex) {
            return false;
        }
    }

    /**
     * Get a faction at a location
     *
     * @param loc Location to get the faction from
     * @return Faction
     */
    public String getFactionAt(Location loc) {
        try {
            // Get the faction
            Faction f = BoardColl.get().getFactionAt(PS.valueOf(loc));

            // If the faction area equals to null, there's not faction on this area
            if(f == null)
                return "";

            // The faction area has to be 'normal'
            if(!f.isNormal())
                return "";

            // Return the faction name
            return f.getComparisonName();

        } catch(Exception ex) {
            return "";
        }
    }
}
