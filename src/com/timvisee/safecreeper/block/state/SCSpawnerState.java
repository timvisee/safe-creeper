package com.timvisee.safecreeper.block.state;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import com.timvisee.safecreeper.block.SCBlockLocation;

public class SCSpawnerState extends SCBlockState {
	
	private EntityType spawnedType;
	private int delay;
	
	/**
	 * Constructor
	 * @param s Creature spawner
	 */
	public SCSpawnerState(Block b) {
		this((CreatureSpawner) b.getState());
	}
	
	/**
	 * Constructor
	 * @param s Creature spawner
	 */
	public SCSpawnerState(CreatureSpawner s) {
		super(s.getBlock());
		
		this.spawnedType = s.getSpawnedType();
		this.delay = s.getDelay();
	}
	
	/**
	 * Constructor
	 * @param loc Spawner block location
	 * @param spawnedType Spawned entity type
	 * @param delay Spawner delay
	 */
	public SCSpawnerState(SCBlockLocation loc, EntityType spawnedType, int delay) {
		// Construct the parent class
		super(loc, Material.MOB_SPAWNER.getId(), (byte) 0);
		
		// Store the spawned entity type and the spawner delay
		this.spawnedType = spawnedType;
		this.delay = delay;
	}
	
	/**
	 * Get the creature spawner instance
	 * @return Creature spawner instance
	 */
	public CreatureSpawner getSpawner() {
		return (CreatureSpawner) getBlock().getState();
	}
	
	/**
	 * Get  the spawned type of the creature spawner
	 * @return Spawned type
	 */
	public EntityType getSpawnedType() {
		return this.spawnedType;
	}
	
	/**
	 * Set the spawned type of the creature spawned
	 * @param spawnedType Spawned type
	 */
	public void setSpawnedType(EntityType spawnedType) {
		this.spawnedType = spawnedType;
	}
	
	/**
	 * Get the spawn delay of the creature spawner
	 * @return Creature spawner spawn delay
	 */
	public int getSpawnDelay() {
		return this.delay;
	}
	
	/**
	 * Set the spawn delay 
	 * @param delay Set the spawn delay of the creature spawner state
	 */
	public void setSpawnDelay(int delay) {
		this.delay = delay;
	}
	
	/**
	 * Get the block state type
	 */
	public SCBlockStateType getStateType() {
		return SCBlockStateType.SPAWNER;
	}
	
	/**
	 * Apply the block state to the block
	 * @return True if succeed
	 */
	public boolean apply() {
		if(!super.apply())
			return false;
		
		// Get the sign
		CreatureSpawner s = getSpawner();
		
		// Make sure the creature spawners instance is not null
		if(s == null)
			return false;
		
		// Set the spawner
		s.setSpawnedType(this.spawnedType);
		s.setDelay(this.delay);
		
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
		configSection.set("spawnerSpawnedType", this.spawnedType.getTypeId());
		configSection.set("spawnerDelay", this.delay);
	}

	/**
	 * Load the data in a configuration section
	 * @param configSection Configuration section to store the data in
	 */
	public static SCSpawnerState load(ConfigurationSection configSection) {
		// Make sure the param is not null
		if(configSection == null)
			return null;
		
		// Get the block location
		ConfigurationSection locSection = configSection.getConfigurationSection("loc");
		SCBlockLocation loc = SCBlockLocation.load(locSection);
		
		// Get the spawned entity type and the spawner delay
		EntityType spawnerSpawnedType = EntityType.fromId(configSection.getInt("spawnerSpawnedType", 90));
		int spawnerDelay = configSection.getInt("spawnerDelay"); // TODO: Default spawner delay
		
		// Construct the spawner state and return the instance
		return new SCSpawnerState(loc, spawnerSpawnedType, spawnerDelay);
	}
}
