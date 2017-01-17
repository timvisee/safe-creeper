package com.timvisee.safecreeper.block.state;

import com.timvisee.safecreeper.block.SCBlock;
import com.timvisee.safecreeper.block.SCBlockLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;

public class SCBlockState {

    private SCBlockLocation loc;

    private Material type;
    private byte data;

    /**
     * Constructor
     *
     * @param b Block
     */
    public SCBlockState(Block b) {
        this.loc = new SCBlockLocation(b);

        this.type = b.getType();
        this.data = b.getData();
    }

    /**
     * Constructor.
     *
     * @param block SCBlock
     */
    public SCBlockState(SCBlock block) {
        this.loc = new SCBlockLocation(block);

        this.type = block.getType();
        this.data = block.getData();
    }

    /**
     * Constructor
     *
     * @param block Block state
     */
    public SCBlockState(BlockState block) {
        this.loc = new SCBlockLocation(block.getBlock());

        this.type = block.getType();
        this.data = block.getData().getData();
    }

    /**
     * Constructor
     *
     * @param loc  Block location
     * @param type Block material
     * @param data Block data value
     */
    public SCBlockState(SCBlockLocation loc, Material type, byte data) {
        this.loc = loc;

        this.type = type;
        this.data = data;
    }

    /**
     * Constructor
     *
     * @param loc      Block location
     * @param type Block material
     */
    public SCBlockState(SCBlockLocation loc, Material type) {
        this(loc, type, (byte) 0);
    }

    /**
     * Load the data in a configuration section
     *
     * @param config Configuration section to load the block data from.
     */
    public static SCBlockState load(ConfigurationSection config) {
        // Make sure the param is not null
        if(config == null)
            return null;

        // Get the state type of the block
        final String stateType = config.getString("stateType", "NORMAL");

        // Load the state
        if(stateType.equals(SCBlockStateType.BEACON.getName())) {
            // Get the beacon state and return the instance
            return SCBeaconState.load(config);

        } else if(stateType.equals(SCBlockStateType.COMMAND_BLOCK.getName())) {
            // Get the command block state and return the instance
            return SCCommandBlockState.load(config);

        } else if(stateType.equals(SCBlockStateType.CONTAINER_BLOCK.getName())) {
            // Get the container block state and return the instance
            return SCContainerBlockState.load(config);

        } else if(stateType.equals(SCBlockStateType.JUKEBOX.getName())) {
            // Get the jukebox state and return the instance
            return SCJukeboxState.load(config);

        } else if(stateType.equals(SCBlockStateType.SIGN.getName())) {
            // Get the sign state and return the instance
            return SCSignState.load(config);

        } else if(stateType.equals(SCBlockStateType.SKULL.getName())) {
            // Get the skull state and return the instance
            return SCSkullState.load(config);

        } else if(stateType.equals(SCBlockStateType.SPAWNER.getName())) {
            // Get the skull state and return the instance
            return SCSpawnerState.load(config);
        }

        // Get the block location
        final ConfigurationSection locSect = config.getConfigurationSection("loc");
        final SCBlockLocation loc = SCBlockLocation.load(locSect);

        // Create a variable for the block material
        Material type;

        // Load the material if the proper key is available
        if(config.isString("type"))
            type = Material.getMaterial(config.getString("type"));

        else if(config.isInt("typeId"))
            //noinspection deprecation
            type = Material.getMaterial(config.getInt("typeId"));

        else {
            // Show an error message, and return null
            System.out.println("Failed to load stored block state, type is missing.");
            return null;
        }

        // Get the block data
        final byte data = (byte) config.getInt("data", 0);

        // Create a block instance and return it
        return new SCBlockState(loc, type, data);
    }

    /**
     * Get the BlockLocation of the block
     *
     * @return BlockLocation
     */
    public SCBlockLocation getBlockLocation() {
        return this.loc;
    }

    /**
     * Get the block
     *
     * @return Block
     */
    public Block getBlock() {
        return this.loc.getBlock();
    }

    /**
     * Get the location of the block.
     *
     * @return Location of the block.
     */
    public Location getLocation() {
        return this.loc.getLocation();
    }

    /**
     * Get the x coordinate of the block.
     *
     * @return x coordinate.
     */
    public int getX() {
        return this.loc.getX();
    }

    /**
     * Get the y coordinate of the block.
     *
     * @return y coordinate.
     */
    public int getY() {
        return this.loc.getY();
    }

    /**
     * Get the z coordinate of the block.
     *
     * @return z coordinate.
     */
    public int getZ() {
        return this.loc.getZ();
    }

    /**
     * Get the world the block is in
     *
     * @return World the block is in
     */
    public World getWorld() {
        return this.loc.getWorld();
    }

    /**
     * Get the type material of the block state.
     *
     * @return Block state type material.
     */
    public Material getType() {
        return this.type;
    }

    /**
     * Set the type material of the block state.
     *
     * @param type Block type material.
     */
    public void setType(Material type) {
        this.type = type;
    }

    /**
     * Get the data of the block state
     *
     * @return Block state data
     */
    public byte getData() {
        return this.data;
    }

    /**
     * Set the data of the block state
     *
     * @param data Block data
     */
    public void setData(byte data) {
        this.data = data;
    }

    /**
     * Check if the block state equals to air.
     *
     * @return True if this block is air, false if not.
     */
    public boolean isAir() {
        return this.type.equals(Material.AIR);
    }

    /**
     * Is the block a liquid. Water or lava.
     *
     *
     * @return True if the block is a liquid.
     */
    public boolean isLiquid() {
        return (this.type.equals(Material.WATER) || this.type.equals(Material.STATIONARY_WATER) ||
                this.type.equals(Material.LAVA) || this.type.equals(Material.STATIONARY_LAVA));
    }

    /**
     * Get the block state type.
     */
    public SCBlockStateType getStateType() {
        return SCBlockStateType.NORMAL;
    }

    /**
     * Apply the current state to the block.
     *
     * @return True if succeed.
     */
    public boolean apply() {
        // Get the block
        final Block block = getBlock();

        // Make sure the block is not null
        if(block == null)
            return false;

        // Apply the block state to the block
        block.setType(this.type);
        block.setData(this.data);

        // Return true
        return true;
    }

    /**
     * Store the block state.
     *
     * @param config Configuration section to store the block state in.
     */
    public void save(ConfigurationSection config) {
        // Make sure the config section is not null
        if(config == null)
            return;

        // Store the type
        config.set("stateType", getStateType().getName());

        // Store the block location
        ConfigurationSection locSect = config.createSection("loc");
        this.loc.save(locSect);

        // Store the block state
        config.set("type", this.type);
        config.set("data", (int) this.data);
    }
}
