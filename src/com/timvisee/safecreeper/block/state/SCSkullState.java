package com.timvisee.safecreeper.block.state;

import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.configuration.ConfigurationSection;

import com.timvisee.safecreeper.block.SCBlockLocation;

public class SCSkullState extends SCBlockState {

	private SkullType skullType;
	private String owner;
	private BlockFace rot;
	
	/**
	 * Constructor
	 * @param s Skull block
	 */
	public SCSkullState(Skull s) {
		// Construct the parent class
		super(s.getBlock());
		
		// Store the skulls owner name and it's rotation
		this.skullType = s.getSkullType();
		this.owner = s.getOwner();
		this.rot = s.getRotation();
	}
	
	/**
	 * Constructor
	 * @param b Skull block
	 */
	public SCSkullState(Block b) {
		this((Skull) b.getState());
	}
	
	/**
	 * Constructor
	 * @param loc Block location
	 * @param typeId Block type Id
	 * @param data Block data
	 * @param owner Skull owner
	 * @param rot Skull rotation
	 */
	public SCSkullState(SCBlockLocation loc, int typeId, byte data, SkullType skullType, String owner, BlockFace rot) {
		// Construct the parent class
		super(loc, typeId, data);
		
		// Store the skull owner and the skull rotation
		this.skullType = skullType;
		this.owner = owner;
		this.rot = rot;
	}
	
	/**
	 * Get the skull block
	 * @return Skull block
	 */
	public Skull getSkullBlock() {
		return (Skull) getBlock().getState();
	}
	
	/**
	 * Get the skull type
	 * @return Skull type
	 */
	public SkullType getSkullType() {
		return this.skullType;
	}
	
	/**
	 * Set the skull type of the skull
	 * @param skullType Skull type
	 */
	public void setSkullType(SkullType skullType) {
		this.skullType = skullType;
	}
	
	/**
	 * Get the skull owner
	 * @return Skull owner
	 */
	public String getOwner() {
		return this.owner;
	}
	
	/**
	 * Set the skull owner
	 * @param owner Skull owner name
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/**
	 * Get the skull rotation
	 * @return Skull rotation
	 */
	public BlockFace getRotation() {
		return this.rot;
	}
	
	/**
	 * Set the skull rotation
	 * @param rot Skull rotation
	 */
	public void setRotation(BlockFace rot) {
		this.rot = rot;
	}
	
	/**
	 * Get the block state type
	 */
	public BlockStateType getStateType() {
		return BlockStateType.SKULL;
	}
	
	/**
	 * Apply the block state to the block
	 * @return True if succeed
	 */
	public boolean apply() {
		if(!super.apply())
			return false;
		
		// Get the skull
		Skull s = getSkullBlock();
		
		// Make sure the skull instance is not null
		if(s == null)
			return false;
		
		// Set the skull owner and rotation
		s.setOwner(this.owner);
		s.setRotation(this.rot);
		
		// Update the skull
		s.update();
		
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
		
		// Store the spawner spawned type
		configSection.set("skullType", this.skullType.name());
		configSection.set("skullOwner", this.owner);
		configSection.set("skullRot", this.rot.name());
	}

	/**
	 * Load the data in a configuration section
	 * @param configSection Configuration section to store the data in
	 */
	public static SCSkullState load(ConfigurationSection configSection) {
		// Make sure the param is not null
		if(configSection == null)
			return null;
		
		// Get the block location
		ConfigurationSection locSection = configSection.getConfigurationSection("loc");
		SCBlockLocation loc = SCBlockLocation.load(locSection);
		
		// Get the block type ID and data
		int typeId = configSection.getInt("typeId", 0);
		byte data = (byte) configSection.getInt("data", 0);
		
		// Get the owner and the rotation of the skull
		SkullType skullType = SkullType.valueOf(configSection.getString("skullType", "PLAYER"));
		String skullOwner = configSection.getString("skullOwner", "");
		BlockFace skullRot = BlockFace.valueOf(configSection.getString("skullRot", "SELF"));
		
		// Construct the sign state and return the instance
		return new SCSkullState(loc, typeId, data, skullType, skullOwner, skullRot);
	}
}
