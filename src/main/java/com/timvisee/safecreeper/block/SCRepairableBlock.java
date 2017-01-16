package com.timvisee.safecreeper.block;

import com.timvisee.safecreeper.block.state.SCBlockState;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

public class SCRepairableBlock {

    private SCBlockState state;
    private long repairAt;

    /**
     * Constructor
     *
     * @param state    Block state
     * @param repairAt Repair at (timestamp)
     */
    public SCRepairableBlock(SCBlockState state, long repairAt) {
        this.state = state;
        this.repairAt = repairAt;
    }

    /**
     * Load the data in a configuration section
     *
     * @param configSection Configuration section to store the data in
     */
    public static SCRepairableBlock load(ConfigurationSection configSection) {
        // Make sure the param is not null
        if(configSection == null)
            return null;

        // Get the repair at time
        long repairAt = (long) (System.currentTimeMillis() + (configSection.getDouble("repairDelay", 0) * 1000));

        // Get the block section
        ConfigurationSection blockSection = configSection.getConfigurationSection("blockState");

        // Get the block state
        SCBlockState blockState = SCBlockState.load(blockSection);

        // Make sure the block state is not null
        if(blockState == null)
            return null;

        // Construct the reparable block instance and return it
        return new SCRepairableBlock(blockState, repairAt);
    }

    /**
     * Get the block state
     *
     * @return Block state
     */
    public SCBlockState getBlockState() {
        return this.state;
    }

    /**
     * Get the block
     *
     * @return Block
     */
    public Block getBlock() {
        return this.state.getBlock();
    }

    /**
     * Check if the chunk the block is in is loaded
     *
     * @return True if the chunk is loaded, false otherwise.
     */
    public boolean isChunkLoaded() {
        Block b = getBlock();

        // Make sure the block is not null
        if(b == null)
            return false;

        // Check if the chunk is loaded
        return b.getChunk().isLoaded();
    }

    /**
     * Get the repair at time
     *
     * @return
     */
    public long getRepairAt() {
        return repairAt;
    }

    /**
     * Set the repair at time
     *
     * @param repairAt Repair at time
     */
    public void setRepairAt(long repairAt) {
        this.repairAt = repairAt;
    }

    /**
     * Repair the block
     */
    public void repair() {
        this.state.apply();
    }

    /**
     * Save the data in a configuration section
     *
     * @param configSection Configuration section to store the data in
     */
    public void save(ConfigurationSection configSection) {
        // Make sure the param is not null
        if(configSection == null)
            return;

        // Store the repair time / delay
        configSection.set("repairDelay", ((double) repairAt - System.currentTimeMillis()) / 1000);

        // Store the block state
        ConfigurationSection blockSection = configSection.createSection("blockState");
        this.state.save(blockSection);
    }
}