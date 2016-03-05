package com.timvisee.safecreeper.handler.plugin;

import com.timvisee.safecreeper.SCLogger;
import me.dpohvar.powernbt.api.NBTManager;

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
        this.hooked = true;
    }

    @Override
    public boolean isHooked() {
        return this.hooked & getNBTManager() != null;
    }

    @Override
    public void unhook() {
        this.hooked = false;
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
