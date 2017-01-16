package com.timvisee.safecreeper.event;

import com.timvisee.safecreeper.SafeCreeper;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public abstract class SCEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    /**
     * Get the server instance
     *
     * @return Server instance
     */
    public Server getServer() {
        return SafeCreeper.instance.getServer();
    }

    /**
     * Get the plugin instance
     *
     * @return Plugin instance
     */
    public Plugin getPlugin() {
        return getSafeCreeper();
    }

    /**
     * Get the Safe Creeper instance
     *
     * @return Safe Creeper instance
     */
    public SafeCreeper getSafeCreeper() {
        return SafeCreeper.instance;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
