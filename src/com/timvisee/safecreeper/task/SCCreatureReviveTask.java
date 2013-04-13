package com.timvisee.safecreeper.task;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.garbagemule.MobArena.framework.Arena;
import com.timvisee.safecreeper.SafeCreeper;

public class SCCreatureReviveTask  extends SCTask {
	
	private Creature c;
	private Location l;
	private LivingEntity target;
	private Arena mobArena = null;
	
	/**
	 * Constructor
	 * @param c Creature
	 * @param l Loation
	 */
	public SCCreatureReviveTask(Creature c, Location l) {
		this.c = c;
		this.l = l;
	}
	
	/**
	 * Task
	 */
	@Override
	public void run() {
		World w = this.l.getWorld();
		
		// Get the current control name
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(this.c);
		
		// Spawn the entity
		Creature c = (Creature) w.spawnEntity(this.l, this.c.getType());
		
		// Set the looking direction of the entity
		c.getLocation().setPitch(this.c.getLocation().getPitch());
		c.getLocation().setYaw(this.c.getLocation().getYaw());
		
		// Re-Target the previous target
		if(this.target != null)
			c.setTarget(this.target);
		
		// If the Living Entity was inside any Mob Arena add it to the arena again
		if(isInMobArena())
			SafeCreeper.instance.getMobArenaManager().addMonsterToArena(this.mobArena, c);
		
		// Play the control effects
		SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "Revived", l);
	}
	
	/**
	 * Get the creature
	 * @return Creature
	 */
	public Entity getCreature() {
		return this.c;
	}
	
	/**
	 * Set the creature
	 * @param c Creature
	 */
	public void setCreature(Creature c) {
		this.c = c;
	}
	
	/**
	 * Get the creature location
	 * @return Location
	 */
	public Location getLocation() {
		return this.l;
	}
	
	/**
	 * Set the creature location
	 * @param l location
	 */
	public void setLocation(Location l) {
		this.l = l;
	}
	
	/**
	 * Get the target
	 * @return Target
	 */
	public LivingEntity getTarget() {
		return this.target;
	}
	
	/**
	 * Has the creature any target
	 * @return true if has target
	 */
	public boolean hasTarget() {
		return (this.target != null);
	}
	
	/**
	 * Set the target
	 * @param target Target
	 */
	public void setTarget(LivingEntity target) {
		this.target = target;
	}
	
	/**
	 * Check if the living entity is inside any mob arena
	 * @return True if inside any arena
	 */
	public boolean isInMobArena() {
		return (this.mobArena != null);
	}
	
	/**
	 * Get the mob arena the living entity is in
	 * @return Mob arena or null
	 */
	public Arena getMobArena() {
		return this.mobArena;
	}
	
	/**
	 * Set the mob arena the living entity is in
	 * @param mobArena Mob arena
	 */
	public void setMobArena(Arena mobArena) {
		this.mobArena = mobArena;
	}
	
	/**
	 * Get the task name
	 * @return Task name
	 */
	public String getTaskName() {
		return "Safe Creeper creature revive task";
	}
}
