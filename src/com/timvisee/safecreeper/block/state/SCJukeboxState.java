package com.timvisee.safecreeper.block.state;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.configuration.ConfigurationSection;

import com.timvisee.safecreeper.block.SCBlockLocation;

public class SCJukeboxState extends SCBlockState {

	private Material disk;
	
	/**
	 * Constructor
	 * @param j Jukebox
	 */
	public SCJukeboxState(Jukebox j) {
		// Construct the parent class
		super(j.getBlock());
		
		// Store the playing disk
		this.disk = j.getPlaying();
	}
	
	/**
	 * Constructor
	 * @param b Jukebox block
	 */
	public SCJukeboxState(Block b) {
		this((Jukebox) b.getState());
	}
	
	/**
	 * Constuctor
	 * @param loc Block location
	 * @param disk Disk
	 */
	public SCJukeboxState(SCBlockLocation loc, Material disk) {
		// Construct the parent class
		super(loc, Material.JUKEBOX.getId(), (byte) 0);
		
		// Store the disk
		this.disk = disk;
	}
	
	/**
	 * Get the jukebox
	 * @return Jukebox
	 */
	public Jukebox getJukebox() {
		return (Jukebox) getBlock().getState();
	}
	
	/**
	 * Get the disk from the jukebox
	 * @return Jukebox disk
	 */
	public Material getDisk() {
		return this.disk;
	}
	
	/**
	 * Set the disk of the jukebox
	 * @param disk Jukebox disk
	 */
	public void setDisk(Material disk) {
		this.disk = disk;
	}
	
	/**
	 * Get the block state type
	 */
	public BlockStateType getStateType() {
		return BlockStateType.JUKEBOX;
	}
	
	/**
	 * Apply the block state to the block
	 * @return True if succeed
	 */
	public boolean apply() {
		if(!super.apply())
			return false;
		
		// Get the jukebox
		Jukebox j = getJukebox();
		
		// Make sure the jukebox is not null
		if(j == null)
			return false;
		
		// Put the disk back in the jukebox
		j.setPlaying(this.disk);
		
		// Update the jukebox
		j.update();
		
		// Return true
		return true;
	}
	
	/**
	 * Save the data in a configuration section
	 * @param configSection Configuration section to store the data in
	 */
	public void save(ConfigurationSection configSection) {
		// Make sure the param is not null
		if(configSection == null)
			return;
		
		// Save the main data from the parent class
		super.save(configSection);
		
		// Store the jukebox item
		configSection.set("jukeboxItem", this.disk.getId());
	}

	/**
	 * Load the data in a configuration section
	 * @param configSection Configuration section to store the data in
	 */
	public static SCJukeboxState load(ConfigurationSection configSection) {
		// Make sure the param is not null
		if(configSection == null)
			return null;
		
		// Get the block location
		ConfigurationSection locSection = configSection.getConfigurationSection("loc");
		SCBlockLocation loc = SCBlockLocation.load(locSection);
		
		// Get the block name and the selected command
		int jukeboxItemId = configSection.getInt("jukeboxItem", 0);
		Material jukeboxItem = Material.getMaterial(jukeboxItemId);
		
		// Construct the jukebox state and return the instance
		return new SCJukeboxState(loc, jukeboxItem);
	}
}
