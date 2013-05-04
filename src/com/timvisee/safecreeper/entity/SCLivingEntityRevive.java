package com.timvisee.safecreeper.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.timvisee.safecreeper.SafeCreeper;

public class SCLivingEntityRevive {
	
	private LivingEntity le;
	private LivingEntity reviver;
	private Location l;
	private LivingEntity target;
	
	public SCLivingEntityRevive(LivingEntity le, Location l, LivingEntity reviver) {
		this.le = le;
		this.l = l;
		this.reviver = reviver;
		
		// Move the reviver
		moveReviver();
	}
	
	public LivingEntity getLivingEntity() {
		return this.le;
	}
	
	public LivingEntity getReviver() {
		return this.reviver;
	}
	
	public void setReviver(LivingEntity reviver) {
		this.reviver = reviver;
	}
	
	public Location getLocation() {
		return this.l;
	}
	
	public void setLocation(Location l) {
		this.l = l;
	}
	
	public LivingEntity getTarget() {
		return this.target;
	}
	
	public boolean hasTarget() {
		return (this.target != null);
	}
	
	public void setTarget(LivingEntity target) {
		this.target = target;
	}
	
	public void moveReviver() {
		// Move the reviver to it's destination
		SafeCreeper.instance.getTVNLibManager().livingEntityTargetTo(reviver, this.l.getX(), this.l.getY(), this.l.getZ());
	}
	
	public void revive() {
		World w = this.l.getWorld();
		
		// Get the current control name
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(this.le);
		
		// Spawn the entity
		Entity e = w.spawnEntity(this.l, this.le.getType());
		
		// Set the looking direction of the entity
		e.getLocation().setPitch(this.le.getLocation().getPitch());
		le.getLocation().setYaw(this.le.getLocation().getYaw());
		
		// Re-Target the previous target
		if(this.target != null) {
			if(e instanceof Creature) {
				Creature c = (Creature) e;
				c.setTarget(this.target);
			}
		}
		
		// Play the control effects
		SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "Revived", l);
	}
}
