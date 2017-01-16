package com.timvisee.safecreeper.event.config;

import com.timvisee.safecreeper.event.SCCancelableEvent;

import java.io.File;

public class SCWorldConfigLoadEvent extends SCCancelableEvent {

    private String world;
    private File worldConfig;

    /**
     * Constructor
     *
     * @param world       World
     * @param worldConfig World config being loaded
     */
    public SCWorldConfigLoadEvent(String world, File worldConfig) {
        this.world = world;
        this.worldConfig = worldConfig;
    }

    /**
     * Get the world name this config file is for
     *
     * @return World name
     */
    public String getWorld() {
        return this.world;
    }

    /**
     * Get the file of the world config
     *
     * @return World config file
     */
    public File getWorldConfigFile() {
        return this.worldConfig;
    }

    /**
     * Set the file of the world config
     *
     * @param worldConfig World config file
     */
    public void setWorldConfigFile(File worldConfig) {
        this.worldConfig = worldConfig;
    }
}
