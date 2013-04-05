package com.timvisee.safecreeper.block.state;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SCBeaconState extends SCBlockState {
	
	List<ItemStack> contents = new ArrayList<ItemStack>();
	private int beaconInvSize;
	
	/**
	 * Constructor
	 * @param b Beacon block
	 */
	public SCBeaconState(Beacon b) {
		// Construct the parent class
		super(b.getBlock());
		
		// Get the inventory
		Inventory inv = b.getInventory();
		
		// Store the beacon contents
		this.contents.clear();
		for(ItemStack entry : inv.getContents())
			if(entry != null)
				this.contents.add(entry.clone());
			else
				this.contents.add(null);
			
		// Store the beacon inventory size
		this.beaconInvSize = inv.getSize();
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
	 * Get the beacon contents
	 */
	public List<ItemStack> getContents() {
		return this.contents;
	}
	
	/**
	 * Set the beacon contents
	 * @param contents Beacon contents
	 */
	public void setContents(List<ItemStack> contents) {
		this.contents = contents;
	}
	
	/**
	 * Get the beacons inventory size
	 * @return Beacon inventory size
	 */
	public int getBeaconInventorySize() {
		return this.beaconInvSize;
	}
	
	/**
	 * Set the beacon contents
	 * @param contents Beacon contents
	 */
	public void setContents(ItemStack[] contents) {
		this.contents.clear();
		for(ItemStack entry : contents) {
			if(entry != null)
				this.contents.add(entry.clone());
			else
				this.contents.add(null);
		}
	}
	
	/**
	 * Get the block state type
	 */
	public BlockStateType getStateType() {
		return BlockStateType.BEACON;
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
		
		// Get the inventory of the beacon
		Inventory inv = b.getInventory();
		
		// Make sure the beacon and the inventory instance are not null
		if(b == null || inv == null)
			return false;
		
		// Put the item back in the chest
		for(int i = 0; i < inv.getSize(); i++)
			inv.setItem(i, this.contents.get(i));
		
		// Update the beacon
		b.update();
		
		// Return true
		return true;
	}
}
