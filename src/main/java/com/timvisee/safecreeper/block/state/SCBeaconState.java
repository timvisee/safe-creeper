package com.timvisee.safecreeper.block.state;

import org.bukkit.block.Beacon;
import org.bukkit.block.Block;

public class SCBeaconState extends SCBlockState {
	
	public int primaryEffectId = 0;
	public int secondaryEffectId = 0;

	/**
	 * Constructor
	 * @param b Beacon block
	 */
	public SCBeaconState(Beacon b) {
		// Construct the parent class
		super(b.getBlock());
    }
	
	/**
	 * Constructor
	 * @param b Beacon block
	 */
	public SCBeaconState(Block b) {
		this((Beacon) b.getState());
	}
	
	/**
	 * Get the beacon block
	 * @return
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
	 * Apply the block state to the block
	 * @return True if succeed
	 */
	public boolean apply() {
		if(!super.apply())
			return false;
		
		// Get the beacon
		Beacon b = getBeacon();
		
		/* // Put the item back in the chest
		for(int i = 0; i < inv.getSize(); i++)
			inv.setItem(i, this.contents.get(i));*/
		
		// Update the beacon
		b.update();
		
		// Return true
		return true;
	}
}
