package com.timvisee.safecreeper.entity;

import com.timvisee.safecreeper.SafeCreeper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public class SCLivingEntity {

    // Entity orientation (to get the ability to link this class to a bukkit entity)
    String entityId = "";

    // Living Entities health
    int health = 0;

    public SCLivingEntity(LivingEntity le) {
        this.entityId = le.getUniqueId().toString();
    }

    public SCLivingEntity(String entityId) {
        this.entityId = entityId;
    }

    public LivingEntity getLivingEntity() {
        for(World w : SafeCreeper.instance.getServer().getWorlds())
            for(LivingEntity le : w.getLivingEntities())
                if(equals(le))
                    return le;
        return null;
    }

    public boolean isOldEntity() {
        return (getLivingEntity() == null);
    }

    public String getEntityId() {
        return this.entityId;
    }

    public boolean equals(SCLivingEntity scle) {
        return (this.entityId == scle.getEntityId());
    }

    public boolean equals(Entity e) {
        return (this.entityId.equals(e.getUniqueId().toString()));
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int h) {
        this.health = h;

        // Set the health of the Bukkit Entity within the health boundary's
        LivingEntity le = getLivingEntity();
        if(le != null)
            le.setHealth(Math.max(1, Math.min(le.getMaxHealth(), this.health)));
    }

    public void damage(int d) {
        setHealth(getHealth() - d);
    }

    public void tick() {

    }

    public Projectile shootProjectile(Entity e) {
        return shootProjectile(e.getLocation());
    }

    @SuppressWarnings("unused")
    public Projectile shootProjectile(Location target) {
        // Test for custom mob abilities

        LivingEntity le = getLivingEntity();
        World w = le.getWorld();

        if(le == null)
            return null;

        Location spawn = le.getLocation().clone();
        Location tl = target.clone();

        tl.setY(tl.getY() + 1);
        spawn.setY(spawn.getY() + 3);
        Vector v = tl.toVector().subtract(spawn.toVector()).normalize();

        int arrows = Math.round(10);

		/*for (int i = 0; i < arrows; i++) {
			SmallFireball sn = (SmallFireball) w.spawnEntity(spawn, EntityType.SMALL_FIREBALL);
			// 0f -> 12.0f for real acurracy
			sn.setVelocity(v);
			sn.setShooter((LivingEntity) le);
			return sn;
		}*/

        for(int i = 0; i < arrows; i++) {
            Arrow ar = w.spawnArrow(spawn, v, 2.0f, 12f);
            // 0f -> 12.0f for real acurracy
            ar.setVelocity(ar.getVelocity());
            ar.setShooter((LivingEntity) le);
            //return ar;
        }
		
		/*
		
		for (int i = 0; i < arrows; i++) {
			Arrow ar = w.spawnArrow(spawn, v, 2.0f, 12f);
			// 0f -> 12.0f for real acurracy
			ar.setVelocity(ar.getVelocity());
			ar.setShooter((LivingEntity) le);
			//return ar;
		}
		
		v.add(new Vector(0, 0.05, 0));
		
		for (int i = 0; i < arrows; i++) {
			Snowball sn = (Snowball) w.spawnEntity(spawn, EntityType.SNOWBALL);
			// 0f -> 12.0f for real acurracy
			sn.setVelocity(v);
			sn.setShooter((LivingEntity) le);
			return sn;
		}*/
		
		/*v.add(new Vector(0, 0.05, 0));
		
		for (int i = 0; i < arrows; i++) {
			TNTPrimed sn = (TNTPrimed) w.spawnEntity(spawn, EntityType.PRIMED_TNT);
			// 0f -> 12.0f for real acurracy
			sn.setVelocity(v);
			/*sn.setShooter((LivingEntity) le);* /
			//return sn;
		}*/

        return null;
    }
}
