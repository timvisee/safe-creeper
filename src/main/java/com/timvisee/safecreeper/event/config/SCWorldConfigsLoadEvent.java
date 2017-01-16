package com.timvisee.safecreeper.event.config;

import com.timvisee.safecreeper.event.SCCancelableEvent;

import java.io.File;

public class SCWorldConfigsLoadEvent extends SCCancelableEvent {

    private File worldConfigsDir;

    /**
     * Constructor
     *
     * @param worldConfigsDir World Configs directory
     */
    public SCWorldConfigsLoadEvent(File worldConfigsDir) {
        this.worldConfigsDir = worldConfigsDir;
    }

    /**
     * Get the world configs path
     *
     * @return World configs path
     */
    public File getWorldConfigsDirectory() {
        return this.worldConfigsDir;
    }

    /**
     * Set the world configs path
     *
     * @param worldConfigsDir World configs path
     */
    public void setWorldConifgsDirectory(File worldConfigsDir) {
        // Make sure the param is a directory
        if(!worldConfigsDir.isDirectory())
            return;

        // Store the directory
        this.worldConfigsDir = worldConfigsDir;
    }
}
