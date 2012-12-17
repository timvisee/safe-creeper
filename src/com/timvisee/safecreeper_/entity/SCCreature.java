package com.timvisee.safecreeper.entity;

import org.bukkit.entity.Creature;
public class SCCreature extends SCLivingEntity {
	
	public SCCreature(Creature c) {
		super(c);
	}
	
	public Creature getCreature() {
		return (Creature) getLivingEntity();
	}
}
