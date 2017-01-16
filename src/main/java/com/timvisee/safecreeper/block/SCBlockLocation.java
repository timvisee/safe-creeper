package com.timvisee.safecreeper.block;

import com.timvisee.safecreeper.SafeCreeper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

public class SCBlockLocation {

    String worldName;
    int x, y, z;

    // Cache
    World w = null;
    Location l = null;
    Block b = null;

    /**
     * Constructor
     *
     * @param worldName Block world
     * @param x         Block x
     * @param y         Block y
     * @param z         Block z
     */
    public SCBlockLocation(String worldName, int x, int y, int z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }


    /**
     * Constructor
     *
     * @param w Block world
     * @param x Block x
     * @param y Block y
     * @param z Block z
     */
    public SCBlockLocation(World w, int x, int y, int z) {
        this.worldName = w.getName();
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Constructor
     *
     * @param b Block
     */
    public SCBlockLocation(Block b) {
        this.worldName = b.getWorld().getName();
        this.x = b.getX();
        this.y = b.getY();
        this.z = b.getZ();
        this.w = b.getWorld();
        this.l = b.getLocation();
        this.b = b;
    }

    /**
     * Constructor
     *
     * @param b Block
     */
    public SCBlockLocation(SCBlock b) {
        this(b.getBlock());
    }

    /**
     * Constructor
     *
     * @param loc Block location
     */
    public SCBlockLocation(Location loc) {
        this.worldName = loc.getWorld().getName();
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.w = loc.getWorld();
        this.l = loc;
        this.b = loc.getBlock();
    }

    /**
     * Load the data in a configuration section
     *
     * @param configSection Configuration section to store the data in
     */
    public static SCBlockLocation load(ConfigurationSection configSection) {
        // Make sure the param is not null
        if(configSection == null)
            return null;

        // Make sure the world name exists
        if(!configSection.isString("world"))
            return null;

        // Make sure the world name is not empty
        if(configSection.getString("world", "").equals(""))
            return null;

        String worldName = configSection.getString("world");
        int x = configSection.getInt("x");
        int y = configSection.getInt("y");
        int z = configSection.getInt("z");

        // Construct and return the block location
        return new SCBlockLocation(worldName, x, y, z);
    }

    /**
     * Get the world the block is in
     *
     * @return
     */
    public World getWorld() {
        // If the world instance is cached, return the cached instance
        if(this.w != null)
            return this.w;

        // If the location instance is cached, get the world from the location cache
        if(this.l != null) {
            // Update the cache
            this.w = this.l.getWorld();

            // Return the cached world instance
            return this.w;
        }

        // If the block instance is cached, get the world from the block cache
        if(this.b != null) {
            // Update the cahche
            this.w = this.b.getWorld();
            this.l = this.b.getLocation();

            // Return the cached world instance
            return this.w;
        }

        // Loop through all loaded worlds to get the world instance
        for(World w : SafeCreeper.instance.getServer().getWorlds()) {
            if(w.getName().equals(this.worldName)) {
                // Update the cache
                this.w = w;

                // Return the world instance
                return w;
            }
        }

        // Try to construct the world instance
        World w = SafeCreeper.instance.getServer().getWorld(this.worldName);

        // Cache the world instance
        this.w = w;

        // Return the world instance
        return w;
    }

    /**
     * Set the world the block is in
     *
     * @param w
     */
    public void setWorld(World w) {
        this.worldName = w.getName();

        // Clear the cache
        clearCache();

        // Update the cache
        this.w = w;
    }

    /**
     * Get the name of the world the block is in
     *
     * @return Name of the world the block is in
     */
    public String getWorldName() {
        return this.worldName;
    }

    /**
     * Set the name of the world the block is in
     *
     * @param worldName Name of the world the block is in
     */
    public void setWorldName(String worldName) {
        this.worldName = worldName;
        clearCache();
    }

    /**
     * Get the blocks x coord
     *
     * @return x coord
     */
    public int getX() {
        return this.x;
    }

    /**
     * Set the blocks x coord
     *
     * @param x x coord
     */
    public void setX(int x) {
        this.x = x;
        clearCache();
    }

    /**
     * Get the blocks y coord
     *
     * @return y coord
     */
    public int getY() {
        return this.y;
    }

    /**
     * Set the blocks y coord
     *
     * @param y y coord
     */
    public void setY(int y) {
        this.y = y;
        clearCache();
    }

    /**
     * Get the blocks z coord
     *
     * @return z coord
     */
    public int getZ() {
        return this.z;
    }

    /**
     * Set the blocks z coord
     *
     * @param z z coord
     */
    public void setZ(int z) {
        this.z = z;
        clearCache();
    }

    /**
     * Get the location the block is at
     *
     * @return Location the block is at
     */
    public Location getLocation() {
        // If the location instance was cached, return the cached instance
        if(this.l != null)
            return this.l;

        // If the block instance was cached, return the loaction instance from the cached block
        if(this.b != null) {
            // Update the cache
            this.l = this.b.getLocation();

            // Return the location instance
            return this.l;
        }

        // If the world instance was cached, construct the location instance from it
        if(this.w != null) {
            // Update the chace
            this.b = this.w.getBlockAt(this.x, this.y, this.z);
            this.l = this.b.getLocation();

            // Return the location instance
            return this.l;
        }

        // Try to construct the location instance
        this.l = new Location(getWorld(), this.x, this.y, this.z);

        // Return the location instance
        return this.l;
    }

    /**
     * Set the location the block is at
     *
     * @param loc
     */
    public void setLocation(Location loc) {
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();

        // Clear the cache
        clearCache();

        // Update the cache
        this.w = loc.getWorld();
        this.l = loc;
        this.b = loc.getBlock();
    }

    public Block getBlock() {
        // If the block instance was cached, return the cached instance
        if(this.b != null)
            return this.b;

        // If the world instance was cached, get the block instance from it
        if(this.w != null) {
            // Update the chace
            this.b = w.getBlockAt(this.x, this.y, this.z);

            // Return the block instance
            return this.b;
        }

        // If the location instance was cached, get the block instance from it
        if(this.l != null) {
            // Update the cache
            this.b = l.getBlock();
            this.w = b.getWorld();

            // Return the block instance
            return this.b;
        }

        // Try to constuct the block instance
        this.w = getWorld();

        if(this.w != null) {
            this.b = this.w.getBlockAt(this.x, this.y, this.z);
            this.l = this.b.getLocation();

            // Return the block instance
            return this.b;
        }

        // Return null
        return null;
    }

    /**
     * Clear cache
     */
    public void clearCache() {
        this.w = null;
        this.l = null;
        this.b = null;
    }

    /**
     * Store the location in a configuration section
     *
     * @param configSection Configuration section to store the location in
     */
    public void save(ConfigurationSection configSection) {
        // Make sure the configuration section is not null
        if(configSection == null)
            return;

        // Store the data
        configSection.set("world", this.worldName);
        configSection.set("x", this.x);
        configSection.set("y", this.y);
        configSection.set("z", this.z);
    }
}
