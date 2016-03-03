package com.timvisee.safecreeper.block.state;

import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;

public class SCBeaconState extends SCBlockState {

    /**
     * The NBT tag name for the primary effect ID.
     */
    public static final String NBT_PRIMARY_EFFECT_TAG = "Primary";

    /**
     * The NBT tag name for the secondary effect ID.
     */
    public static final String NBT_SECONDARY_EFFECT_TAG = "Secondary";

    /**
     * The ID of the primary beacon effect.
     */
	private int primaryEffectId = 0;

    /**
     * The ID of the secondary beacon effect.
     */
    private int secondaryEffectId = 0;

	/**
	 * Constructor
	 * @param b Beacon block
	 */
	public SCBeaconState(Beacon b) {
		// Construct the parent class
		super(b.getBlock());

        NBTManager api = NBTManager.getInstance();

        NBTCompound c = api.read(b.getBlock());

        // Store the beacon effects
        primaryEffectId = c.getInt(NBT_PRIMARY_EFFECT_TAG);
        secondaryEffectId = c.getInt(NBT_SECONDARY_EFFECT_TAG);

        Bukkit.broadcastMessage("Primary: " + primaryEffectId);
        Bukkit.broadcastMessage("Secondary: " + secondaryEffectId);
        Bukkit.broadcastMessage("");
    }
	
	/**
	 * Constructor
	 * @param b Beacon block
	 */
	public SCBeaconState(Block b) {
		this((Beacon) b.getState());
	}
	
	/**
	 * Get the beacon block instance.
	 *
	 * @return Beacon block.
	 */
	public Beacon getBeacon() {
		return (Beacon) getBlock().getState();
	}
	
	/**
	 * Get the block state type
	 */
	public SCBlockStateType getStateType() {
		return SCBlockStateType.BEACON;
	}

    /**
     * Get the ID of primary the beacon effect.
     *
     * @return Effect ID.
     */
    public int getPrimaryEffectId() {
        return primaryEffectId;
    }

    /**
     * Get the ID of the secondary beacon effect.
     *
     * @return Effect ID.
     */
    public int getSecondaryEffectId() {
        return secondaryEffectId;
    }

    /**
	 * Apply the block state to the block
	 *
	 * @return True if succeed.
	 */
	public boolean apply() {
		if(!super.apply())
			return false;
		
		// Get the beacon
		Beacon b = getBeacon();

        NBTManager api = NBTManager.getInstance();

        NBTCompound c = api.read(b.getBlock());
        c.put(NBT_PRIMARY_EFFECT_TAG, primaryEffectId);
        c.put(NBT_SECONDARY_EFFECT_TAG, secondaryEffectId);

        api.write(b.getBlock(), c);

		// Update the beacon
		b.update();
		
		// Return true
		return true;
	}
}
