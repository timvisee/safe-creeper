package com.timvisee.safecreeper.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.timvisee.safecreeper.util.ExplosionSource;
import com.timvisee.safecreeper.util.ExplosionSource.ExplosionSourceType;

public class SCExplosionCreateEvent extends SCCancelableEvent {

	/**
	 * @var loc Location of the explosin
	 * @var source ExplosionSource
	 * @var blocklist List of affected blocks by the explosion
	 * @var destroyworld True to make the explosion damage the world
	 * @var explosionstrength Explosion strength
	 * @var throwPlayers True to make the explosion throw players
	 * @var throwMobs True to make the explosion throw Mobs
	 * @var throwSpeedMultiplier Throw speed multiplier
	 * @var flyingBlocks True to enable flying blocks
	 * @var flyingBlocksChance Flying blocks chance
	 * @var damagePlayers True to make the explosion damage players
	 * @var damageMobs True to make the explosion damage mobs
	 */
	private Location loc;
	private ExplosionSource source;
	private List<Block> blocklist = new ArrayList<Block>();
	private boolean destroyWorld;
	private int explosionStrength = 1;
	private boolean throwPlayers = true;
	private boolean throwMobs = true;
	private double throwSpeedMultiplier = 1;
	private boolean flyingBlocks = false;
	private double flyingBlocksChance = 80;
	private boolean damagePlayers = true;
	private boolean damageMobs = true;
	private boolean explosionSmoke = true;
	private boolean explosionSound = true;
	private boolean explosionCreateFire = false;
	
	/**
	 * Constructor
	 */
	public SCExplosionCreateEvent(Location loc, ExplosionSource source, int explosionStrength, boolean destroyWorld, List<Block> blocklist) {
		// Store the params
		this.loc = loc;
		this.source = source;
		this.explosionStrength = explosionStrength;
		this.destroyWorld = destroyWorld;
		this.blocklist = blocklist;
	}
	
	/**
	 * Get the location of the explosion
	 * @return Location Location of the explosion
	 */
	public Location getLocation() {
		return this.loc;
	}
	
	/**
	 * Get the world of the explosion
	 * @return World World of the explosion
	 */
	public World getWorld() {
		return this.loc.getWorld();
	}
	
	/**
	 * Get the explosion source
	 * @return Explosion source
	 */
	public ExplosionSource getSource() {
		return this.source;
	}
	
	/**
	 * Get the explosion source type
	 * @return Explosion source type
	 */
	public ExplosionSourceType getSourceType() {
		return this.source.getSourceType();
	}
	
	/**
	 * Get the blocklist with affected blocks by the explosion
	 * @return 
	 */
	public List<Block> getBlockList() {
		return this.blocklist;
	}
	
	/**
	 * Check if the world can be damaged by the explosion
	 * @return boolean True if the world can be damaged
	 */
	public boolean getDestroyWorld() {
		return this.destroyWorld;
	}
	
	/**
	 * Set if the world can be damaged by the explosion
	 * @param destroyWorld boolean True to damage the world
	 */
	public void setDestroyWorld(boolean destroyWorld) {
		this.destroyWorld = destroyWorld;
	}
	
	/**
	 * Get the explosion strength
	 * @return int Explosion strength
	 */
	public int getExplosionStrength() {
		return this.explosionStrength;
	}
	
	/**
	 * Set the explosion strength
	 * @param explosionStrength int Explosion strength
	 */
	public void setExplosionStrength(int explosionStrength) {
		this.explosionStrength = explosionStrength;
	}
	
	/**
	 * Check if players will be thrown by the explosion
	 * @return boolean True if players will be thrown
	 */
	public boolean getThrowPlayers() {
		return this.throwPlayers;
	}
	
	/**
	 * Set if players are going to be thrown by the explosion
	 * @param throwPlayers boolean True to make the explosion throw players
	 */
	public void setThrowPlayers(boolean throwPlayers) {
		this.throwPlayers = throwPlayers;
	}
	
	/**
	 * Get if mobs will be thrown by the explosion
	 * @return boolean True if mobs will be thrown
	 */
	public boolean getThrowMobs() {
		return this.throwMobs;
	}
	
	/**
	 * Set if mobs will be thrown by the explosion
	 * @param throwMobs boolean True to make the explosion throw mobs
	 */
	public void setThrowMobs(boolean throwMobs) {
		this.throwMobs = throwMobs;
	}
	
	/**
	 * Get the throw speed multiplier
	 * @return double Throw speed multiplier
	 */
	public double getThrowSpeedMultiplier() {
		return this.throwSpeedMultiplier;
	}
	
	/**
	 * Set the throw speed multiplier
	 * @param throwSpeedMultiplier double Throw speed multiplier
	 */
	public void setThrowSpeedMultiplier(double throwSpeedMultiplier) {
		this.throwSpeedMultiplier = throwSpeedMultiplier;
	}
	
	/**
	 * Check if flying blocks are enabled
	 * @return boolean True if flying blocks are enabled
	 */
	public boolean getFlyingBlocksEnabled() {
		return this.flyingBlocks;
	}
	
	/** 
	 * Set if flying blocks are enabled
	 * @param flyingBlocks boolean True to enable flying blocks
	 */
	public void setFlyingBlocksEnabled(boolean flyingBlocks) {
		this.flyingBlocks = flyingBlocks;
	}
	
	/**
	 * Get the flying blocks chance
	 * @return double Flying blocks chance
	 */
	public double getFlyingBlocksChance() {
		return this.flyingBlocksChance;
	}
	
	/**
	 * Set the flying blocks chance
	 * @param flyingBlocksChance double Flying blocks chance
	 */
	public void setFlyingBlocksChance(double flyingBlocksChance) {
		this.flyingBlocksChance = flyingBlocksChance;
	}
	
	/**
	 * Check if the explosion will damage players
	 * @return
	 */
	public boolean getDamagePlayers() {
		return this.damagePlayers;
	}
	
	/**
	 * Set if players will be damaged by the explosion
	 * @param damagePlayers True to make the explosion damage players
	 */
	public void setDamagePlayers(boolean damagePlayers) {
		this.damagePlayers = damagePlayers;
	}
	
	/**
	 * Check if the explosion will damage mobs
	 * @return True if the explosion can damage mobs
	 */
	public boolean getDamageMobs() {
		return this.damageMobs;
	}
	
	/**
	 * Set if mobs will be damaged by the explosion
	 * @param damageMobs True to make the explosion damage mobs
	 */
	public void setDamageMobs(boolean damageMobs) {
		this.damageMobs = damageMobs;
	}
	
	/**
	 * Check if the explosion smoke is enabled
	 * @return True if the explosion smoke is enabled
	 */
	public boolean getExplosionSmoke() {
		return this.explosionSmoke;
	}
	
	/**
	 * Set if the explosion smoke is enabled
	 * @param explosionSmoke True if the explosion smoke should be enabled
	 */
	public void setExplosionSmoke(boolean explosionSmoke ) {
		this.explosionSmoke = explosionSmoke;
	}
	
	/**
	 * Check if the explosion sound is enabled
	 * @return True if the explosion sound is enabled
	 */
	public boolean getExplosionSound() {
		return this.explosionSound;
	}
	
	/**
	 * Set if the explosion sound is enabled
	 * @param explosionSound True to enable the explosion sound
	 */
	public void setExplosionSound(boolean explosionSound) {
		this.explosionSound = explosionSound;
	}
	
	/**
	 * Check if the explosion can create fire
	 * @return True if the explosion can create fire
	 */
	public boolean getExplosionCreateFire() {
		return this.explosionCreateFire;
	}
	
	/**
	 * Set if the explosion can create fire
	 * @param explosionCreateFire True if the explosion can create fire
	 */
	public void setExplosionCreateFire(boolean explosionCreateFire) {
		this.explosionCreateFire = explosionCreateFire;
	}
}
