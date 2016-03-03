package com.timvisee.safecreeper.block.state;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;

import com.timvisee.safecreeper.block.SCBlock;
import com.timvisee.safecreeper.block.SCBlockLocation;

public class SCBlockState {

    private SCBlockLocation loc;

    private int type;
    private byte data;

    /**
     * Constructor
     *
     * @param b Block
     */
    public SCBlockState(Block b) {
        this.loc = new SCBlockLocation(b);

        this.type = b.getTypeId();
        this.data = b.getData();
    }

    /**
     * Constructor
     *
     * @param b SCBlock
     */
    public SCBlockState(SCBlock b) {
        this.loc = new SCBlockLocation(b);

        this.type = b.getTypeId();
        this.data = b.getData();
    }

    /**
     * Constructor
     *
     * @param b Block state
     */
    public SCBlockState(BlockState b) {
        this.loc = new SCBlockLocation(b.getBlock());

        this.type = b.getTypeId();
        this.data = b.getData().getData();
    }

    /**
     * Constructor
     *
     * @param loc  Block location
     * @param type Block type ID
     * @param data Block data value
     */
    public SCBlockState(SCBlockLocation loc, int type, byte data) {
        this.loc = loc;

        this.type = type;
        this.data = data;
    }

    /**
     * Constructor
     *
     * @param loc      Block location
     * @param material Block material
     * @param data     Block data value
     */
    public SCBlockState(SCBlockLocation loc, Material material, byte data) {
        this(loc, material.getId(), data);
    }

    /**
     * Constructor
     *
     * @param loc      Block location
     * @param material Block material
     */
    public SCBlockState(SCBlockLocation loc, Material material) {
        this(loc, material, (byte) 0);
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
     * Get the location of the block
     *
     * @return Location of the block
     */
    public Location getLocation() {
        return this.loc.getLocation();
    }

    /**
     * Get the x coord of the block
     *
     * @return x coord
     */
    public int getX() {
        return this.loc.getX();
    }

    /**
     * Get the y coord of the block
     *
     * @return y coord
     */
    public int getY() {
        return this.loc.getY();
    }

    /**
     * Get the z coord of the block
     *
     * @return z coord
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
     * Get the type ID of the block state
     *
     * @return Block state type ID
     */
    public int getTypeId() {
        return this.type;
    }

    /**
     * Set the type ID of the block state
     *
     * @param typeId Block type ID
     */
    public void setTypeId(int typeId) {
        this.type = typeId;
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
     * Check if the block state equals to air
     *
     * @return
     */
    public boolean isAir() {
        return (type == 0);
    }

    /**
     * Is the block a liquid
     *
     * @return True if the block is a liquid
     */
    public boolean isLiquid() {
        return (type == 8 || type == 9 ||
                type == 10 || type == 11);
    }

    /**
     * Get the block state type
     */
    public SCBlockStateType getStateType() {
        return SCBlockStateType.NORMAL;
    }

    /**
     * Apply the current state to the block
     *
     * @return True if succeed
     */
    public boolean apply() {
        Block b = getBlock();

        // Make sure the block is not null
        if (b == null)
            return false;

        // Apply the block state to the block
        b.setTypeId(this.type);
        b.setData(this.data);

        // Return true
        return true;
    }

    /**
     * Store the block state
     *
     * @param cfg Configuration section to store the block state in
     */
    public void save(ConfigurationSection cfg) {
        // Make sure the config section is not null
        if (cfg == null)
            return;

        // Store the type
        cfg.set("stateType", getStateType().getName());

        // Store the block location
        ConfigurationSection locSect = cfg.createSection("loc");
        this.loc.save(locSect);

        // Store the block state
        cfg.set("typeId", this.type);
        cfg.set("data", (int) this.data);
    }

    /**
     * Load the data in a configuration section
     *
     * @param cfg Configuration section to store the data in
     */
    public static SCBlockState load(ConfigurationSection cfg) {
        // Make sure the param is not null
        if (cfg == null)
            return null;

        // Get the state type of the block
        String stateType = cfg.getString("stateType", "NORMAL");

        // Load the state
        if (stateType.equals(SCBlockStateType.NORMAL.getName())) {
            // Get the block location
            ConfigurationSection locSect = cfg.getConfigurationSection("loc");
            SCBlockLocation loc = SCBlockLocation.load(locSect);

            // Get the block type ID and data
            int typeId = cfg.getInt("typeId", 0);
            byte data = (byte) cfg.getInt("data", 0);

            return new SCBlockState(loc, typeId, data);

        } else if (stateType.equals(SCBlockStateType.BEACON.getName())) {
            return SCBeaconState.load(cfg);

        } else if (stateType.equals(SCBlockStateType.COMMAND_BLOCK.getName())) {
            // Get the command block state and return the instance
            return SCCommandBlockState.load(cfg);

        } else if (stateType.equals(SCBlockStateType.CONTAINER_BLOCK.getName())) {
            // Get the container block state and return the instance
            return SCContainerBlockState.load(cfg);

        } else if (stateType.equals(SCBlockStateType.JUKEBOX.getName())) {
            // Get the jukebox state and return the instance
            return SCJukeboxState.load(cfg);

        } else if (stateType.equals(SCBlockStateType.SIGN.getName())) {
            // Get the sign state and return the instance
            return SCSignState.load(cfg);

        } else if (stateType.equals(SCBlockStateType.SKULL.getName())) {
            // Get the skull state and return the instance
            return SCSkullState.load(cfg);

        } else if (stateType.equals(SCBlockStateType.SPAWNER.getName())) {
            // Get the skull state and return the instance
            return SCSpawnerState.load(cfg);
        }

        // Unable to construct any state, try to construct the normal state
        // Get the block location
        ConfigurationSection locSect = cfg.getConfigurationSection("loc");
        SCBlockLocation loc = SCBlockLocation.load(locSect);

        // Get the block type ID and data
        int typeId = cfg.getInt("typeId", 0);
        byte data = (byte) cfg.getInt("data", 0);

        // Return the new block state
        return new SCBlockState(loc, typeId, data);
    }
}
