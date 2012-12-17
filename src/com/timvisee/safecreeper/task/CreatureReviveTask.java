package com.timvisee.safecreeper.task;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.timvisee.safecreeper.SafeCreeper;

public class CreatureReviveTask implements Runnable {
	
	private Creature c;
	private Location l;
	private LivingEntity target;
	
	public CreatureReviveTask(Creature c, Location l) {
		this.c = c;
		this.l = l;
	}
	
	@Override
	public void run() {
		World w = this.l.getWorld();
		
		// Get the current control name
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(this.c);
		
		// Spawn the entity
		Entity e = w.spawnEntity(this.l, this.c.getType());
		
		// Set the looking direction of the entity
		e.getLocation().setPitch(this.c.getLocation().getPitch());
		c.getLocation().setYaw(this.c.getLocation().getYaw());
		
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
	
	public Entity getCreature() {
		return this.c;
	}
	
	public void setCreature(Creature c) {
		this.c = c;
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
}
