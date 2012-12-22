package com.timvisee.safecreeper.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.entity.SlimeSplitEvent;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.entity.SCLivingEntity;
import com.timvisee.safecreeper.entity.SCLivingEntityRevive;
import com.timvisee.safecreeper.task.CreatureReviveTask;
import com.timvisee.safecreeper.util.SCEntityEquipment;

public class SCEntityListener implements Listener {
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity e = event.getEntity();
		Location l = e.getLocation();
		EntityType et = event.getEntityType();
		SpawnReason sr = event.getSpawnReason();
		World w = event.getLocation().getWorld();
		Random rand = new Random();
		
		// Get the current control name
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e, "OtherMobControl");
		
		switch (sr) {
		case BREEDING:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnByBreeding", true, true, l))
				event.setCancelled(true);
			break;
			
		case BUILD_IRONGOLEM:
		case BUILD_SNOWMAN:
		case BUILD_WITHER:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnWhenBuild", true, true, l))
				event.setCancelled(true);
			break;
			
		case CHUNK_GEN:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnWithWorldGeneration", true, true, l))
				event.setCancelled(true);
			break;
			
		case CUSTOM:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnCustom", true, true, l))
				event.setCancelled(true);
			break;
			
		case EGG:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnFromEgg", true, true, l))
				event.setCancelled(true);
			break;
			
		case JOCKEY:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnAsJockey", true, true, l))
				event.setCancelled(true);
			break;
			
		case LIGHTNING:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnFromLightning", true, true, l))
				event.setCancelled(true);
			break;
			
		case DEFAULT:
		case NATURAL:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnNaturally", true, true, l))
				event.setCancelled(true);
			break;
			
		case SLIME_SPLIT:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnFromSlimeSplit", true, true, l))
				event.setCancelled(true);
			break;
			
		case SPAWNER:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnFromSpawner", true, true, l))
				event.setCancelled(true);
			break;
			
		case SPAWNER_EGG:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnFromSpawnerEgg", true, true, l))
				event.setCancelled(true);
			break;
			
		case VILLAGE_DEFENSE:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnForVillageDefence", true, true, l))
				event.setCancelled(true);
			break;
			
		case VILLAGE_INVASION:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnForVillageInvasion", true, true, l))
				event.setCancelled(true);
			break;
			
		default:
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Spawning.CanSpawnFromOther", true, true, l))
				event.setCancelled(true);
		}
		
		// Spawn creatures as baby and/or with custom age
		switch(et) {
		case CHICKEN:
			Chicken chicken = (Chicken) e;
			
			// Check if the custom age control is enabled
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Enabled", false, true, l)) {
				// Check if the age thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.Enabled", false, true, l)) {
					int minAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MinAge", 1, true, l);
					int maxAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MaxAge", 1, true, l);
					int age = rand.nextInt(Math.max(Math.max(minAge, maxAge) + 1, 0) - Math.max(minAge, 0));
					boolean lockAge = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.AgeLock", false, true, l);
					chicken.setAge(age);
					chicken.setAgeLock(lockAge);
				}
				// Check if the baby thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.Enabled", true, true, l)) {
					boolean spawnAsBaby = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.SpawnAsBaby", false, true, l);
					double spawnAsBabyChance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "CustomAge.Baby.SpawnAsBabyChance", 50, true, l);
					if(spawnAsBaby)
						if(((int) spawnAsBabyChance * 10) > rand.nextInt(1000))
							chicken.setBaby();
						else
							chicken.setAdult();
				}
			}
			break;
			
		case COW:
			Cow cow = (Cow) e;
			
			// Check if the custom age control is enabled
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Enabled", false, true, l)) {
				// Check if the age thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.Enabled", false, true, l)) {
					int minAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MinAge", 1, true, l);
					int maxAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MaxAge", 1, true, l);
					int age = rand.nextInt(Math.max(Math.max(minAge, maxAge) + 1, 0) - Math.max(minAge, 0));
					boolean lockAge = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.AgeLock", false, true, l);
					cow.setAge(age);
					cow.setAgeLock(lockAge);
				}
				// Check if the baby thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.Enabled", true, true, l)) {
					boolean spawnAsBaby = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.SpawnAsBaby", false, true, l);
					double spawnAsBabyChance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "CustomAge.Baby.SpawnAsBabyChance", 50, true, l);
					if(spawnAsBaby)
						if(((int) spawnAsBabyChance * 10) > rand.nextInt(1000))
							cow.setBaby();
						else
							cow.setAdult();
				}
			}
			break;
		
		case MUSHROOM_COW:
			MushroomCow mc = (MushroomCow) e;
			
			// Check if the custom age control is enabled
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Enabled", false, true, l)) {
				// Check if the age thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.Enabled", false, true, l)) {
					int minAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MinAge", 1, true, l);
					int maxAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MaxAge", 1, true, l);
					int age = rand.nextInt(Math.max(Math.max(minAge, maxAge) + 1, 0) - Math.max(minAge, 0));
					boolean lockAge = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.AgeLock", false, true, l);
					mc.setAge(age);
					mc.setAgeLock(lockAge);
				}
				// Check if the baby thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.Enabled", true, true, l)) {
					boolean spawnAsBaby = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.SpawnAsBaby", false, true, l);
					double spawnAsBabyChance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "CustomAge.Baby.SpawnAsBabyChance", 50, true, l);
					if(spawnAsBaby)
						if(((int) spawnAsBabyChance * 10) > rand.nextInt(1000))
							mc.setBaby();
						else
							mc.setAdult();
				}
			}
			break;
			
		case OCELOT:
			Ocelot o = (Ocelot) e;
			
			// Check if the custom age control is enabled
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Enabled", false, true, l)) {
				// Check if the age thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.Enabled", false, true, l)) {
					int minAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MinAge", 1, true, l);
					int maxAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MaxAge", 1, true, l);
					int age = rand.nextInt(Math.max(Math.max(minAge, maxAge) + 1, 0) - Math.max(minAge, 0));
					boolean lockAge = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.AgeLock", false, true, l);
					o.setAge(age);
					o.setAgeLock(lockAge);
				}
				// Check if the baby thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.Enabled", true, true, l)) {
					boolean spawnAsBaby = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.SpawnAsBaby", false, true, l);
					double spawnAsBabyChance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "CustomAge.Baby.SpawnAsBabyChance", 50, true, l);
					if(spawnAsBaby)
						if(((int) spawnAsBabyChance * 10) > rand.nextInt(1000))
							o.setBaby();
						else
							o.setAdult();
				}
			}
			break;
			
		case PIG:
			Pig p = (Pig) e;
			
			// Check if the custom age control is enabled
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Enabled", false, true, l)) {
				// Check if the age thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.Enabled", false, true, l)) {
					int minAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MinAge", 1, true, l);
					int maxAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MaxAge", 1, true, l);
					int age = rand.nextInt(Math.max(Math.max(minAge, maxAge) + 1, 0) - Math.max(minAge, 0));
					boolean lockAge = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.AgeLock", false, true, l);
					p.setAge(age);
					p.setAgeLock(lockAge);
				}
				// Check if the baby thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.Enabled", true, true, l)) {
					boolean spawnAsBaby = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.SpawnAsBaby", false, true, l);
					double spawnAsBabyChance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "CustomAge.Baby.SpawnAsBabyChance", 50, true, l);
					if(spawnAsBaby)
						if(((int) spawnAsBabyChance * 10) > rand.nextInt(1000))
							p.setBaby();
						else
							p.setAdult();
				}
			}
			break;
		
		case VILLAGER:
			Villager v = (Villager) e;
			
			// Check if the custom age control is enabled
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Enabled", false, true, l)) {
				// Check if the age thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.Enabled", false, true, l)) {
					int minAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MinAge", 1, true, l);
					int maxAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MaxAge", 1, true, l);
					int age = rand.nextInt(Math.max(Math.max(minAge, maxAge) + 1, 0) - Math.max(minAge, 0));
					boolean lockAge = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.AgeLock", false, true, l);
					v.setAge(age);
					v.setAgeLock(lockAge);
				}
				// Check if the baby thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.Enabled", true, true, l)) {
					boolean spawnAsBaby = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.SpawnAsBaby", false, true, l);
					double spawnAsBabyChance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "CustomAge.Baby.SpawnAsBabyChance", 50, true, l);
					if(spawnAsBaby)
						if(((int) spawnAsBabyChance * 10) > rand.nextInt(1000))
							v.setBaby();
						else
							v.setAdult();
				}
			}
			break;
		
		case WOLF:
			Wolf wolf = (Wolf) e;
			
			// Check if the custom age control is enabled
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Enabled", false, true, l)) {
				// Check if the age thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.Enabled", false, true, l)) {
					int minAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MinAge", 1, true, l);
					int maxAge = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomAge.Age.MaxAge", 1, true, l);
					int age = rand.nextInt(Math.max(Math.max(minAge, maxAge) + 1, 0) - Math.max(minAge, 0));
					boolean lockAge = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Age.AgeLock", false, true, l);
					wolf.setAge(age);
					wolf.setAgeLock(lockAge);
				}
				// Check if the baby thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.Enabled", true, true, l)) {
					boolean spawnAsBaby = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.SpawnAsBaby", false, true, l);
					double spawnAsBabyChance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "CustomAge.Baby.SpawnAsBabyChance", 50, true, l);
					if(spawnAsBaby)
						if(((int) spawnAsBabyChance * 10) > rand.nextInt(1000))
							wolf.setBaby();
						else
							wolf.setAdult();
				}
			}
			break;
			
		case ZOMBIE:
			Zombie z = (Zombie) e;
			
			// Check if the custom age control is enabled
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Enabled", false, true, l)) {
				// Check if the baby thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.Enabled", true, true, l)) {
					boolean spawnAsBaby = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.SpawnAsBaby", false, true, l);
					double spawnAsBabyChance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "CustomAge.Baby.SpawnAsBabyChance", 50, true, l);
					if(spawnAsBaby)
						z.setBaby(((int) spawnAsBabyChance * 10) > rand.nextInt(1000));
				}
			}
			break;
			
		case PIG_ZOMBIE:
			PigZombie pz = (PigZombie) e;
			
			// Check if the custom age control is enabled
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Enabled", false, true, l)) {
				// Check if the baby thing is enabled
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.Enabled", true, true, l)) {
					boolean spawnAsBaby = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomAge.Baby.SpawnAsBaby", false, true, l);
					double spawnAsBabyChance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "CustomAge.Baby.SpawnAsBabyChance", 50, true, l);
					if(spawnAsBaby)
						pz.setBaby(((int) spawnAsBabyChance * 10) > rand.nextInt(1000));
				}
			}
			break;
			
		default:
			break;
		}
		
		// Set if the entity can pickup items
		if(e instanceof Creature) {
			Creature c = (Creature) e;
			
			if(SafeCreeper.instance.getConfigManager().isControlEnabled(w.getName(), controlName, false, l)) {
				boolean canPickupItems = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CanPickupItems", true, true, l);
				c.setCanPickupItems(canPickupItems);
			}
		}
		
		// Spawning potion effects
		if(e instanceof Creature) {
			Creature c = (Creature) e;
			
			boolean spawningPotionEffectsEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "SpawningPotionEffects.Enabled", false, true, l);
			if(spawningPotionEffectsEnabled) {
		    	
		    	List<String> effects = SafeCreeper.instance.getConfigManager().getOptionKeysList(l.getWorld(), controlName, "SpawningPotionEffects.Effects", new ArrayList<String>(), true, l);
	
		    	for(String effect : effects) {
		    		
					String effectName = SafeCreeper.instance.getConfigManager().getOptionString(w, controlName, "SpawningPotionEffects.Effects." + effect + ".Effect", "", true, l).toUpperCase().replace(" ", "_");
	    			int effectDuration = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "SpawningPotionEffects.Effects." + effect + ".Duration", 240, true, l);
	    			double effectChance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "SpawningPotionEffects.Effects." + effect + ".Chance", 100, true, l);
		    		int effectAmplifier = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "SpawningPotionEffects.Effects." + effect + ".Level", 1, true, l);
		    		
		    		// The effect type has to be set
		    		if(effectName.trim().equals("")) {
						System.out.println("[SafeCreeper] [ERROR] Unknown effect: " + effectName);
		    			continue;
		    		}
		    		
		    		// Make sure the rotation is not smaller than zero
		    		if(effectDuration < 0)
		    			effectDuration *= -1;
		    		
		    		// Make sure the chance is not negative
		    		if(effectChance < 0) {
						System.out.println("[SafeCreeper] [ERROR] Effect chance may not be negative!");
		    			continue;
		    		}
		    		
		    		// Make sure the amplifier is not smaller than 1
		    		if(effectAmplifier == 0) {
						System.out.println("[SafeCreeper] [ERROR] Effect amplifier may not be zero!");
		    			continue;
		    		}
		    		if(effectAmplifier < 0)
		    			effectAmplifier *= -1;
		    		
		    		// Calculate chance
		    		if(((int) effectChance * 10) < rand.nextInt(1000))
		    			continue;
		    		
		    		// Try to cast the effect name into a PotionEffectType
		    		PotionEffectType pet = null;
		    		try {
		    			pet = PotionEffectType.getByName(effectName);
		    			if(pet == null) {
		    				System.out.println("[SafeCreeper] [ERROR] Unknown Potion Effect: " + effectName);
			    			continue;
		    			}
		    		} catch(Exception ex) {
		    			System.out.println("[SafeCreeper] [ERROR] Unknown Potion Effect: " + effectName);
		    			continue;
		    		}
		    		
		    		// Add the effect to the creature
		    		PotionEffect pe = new PotionEffect(pet, effectDuration, effectAmplifier);
		    		c.addPotionEffect(pe);
		    	}
			}
		}
			
		
		// Handle custom health of creatures
		if(!event.isCancelled()) {
			LivingEntity le = (LivingEntity) e;
			
			// Make sure the entity isn't a LAB boss!
			if(!SafeCreeper.instance.getLabHandler().isBoss(le)) {
				boolean customHealthEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomHealth.Enabled", false, true, l);
				if(customHealthEnabled) {
					int customHealthMin = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomHealth.MinHealth", le.getMaxHealth(), true, l) - 1;
					int customHealthMax = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomHealth.MaxHealth", le.getMaxHealth(), true, l);
					int customHealth = 1;
					
					int diff = customHealthMax-customHealthMin;
					
					customHealth = rand.nextInt(Math.max(diff, 1)) + customHealthMin;
					
					// Create a SCLivingEntity from the living entity and set the health
					SCLivingEntity scle = new SCLivingEntity(le);
					scle.setHealth(customHealth);
					SafeCreeper.instance.getLivingEntityManager().addLivingEntity(scle);
				}
			}
		}
		
		// Handle the CustomEquipment feature
		if(e instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) e;
			
			// Apply the equipment from the config files
			SCEntityEquipment eq = new SCEntityEquipment(le);
			eq.applyEquipmentFromConfig();
		}
		
		// If the entity is powered, should it be a powered creeper?
		if(et.equals(EntityType.CREEPER))
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "CreeperControl", "AlwaysPowered", false, true, l))
				((Creeper) e).setPowered(true);
		
		// Handle the spawning effects, make sure the spawning isn't canceled yet!
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "Spawned", l);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity e = event.getEntity();
		Location l = e.getLocation();
		World w = e.getWorld();
		DamageCause dc = event.getCause();
		
		// If the entity is a living entity, it may not have negative or zero health
		if(e instanceof LivingEntity) {
			if(((LivingEntity) e).getHealth() <= 0)
				return;
		}
		
		switch(dc) {
		case DROWNING:
			if(e instanceof LivingEntity) {
				String controlName = SafeCreeper.instance.getConfigManager().getControlName(e);
				if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
					controlName = "OtherMobControl";
				
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CanDrown", true, true, l))
					event.setCancelled(true);
				
				// Handle the spawning effects, make sure the spawning isn't canceled yet!
				if(!event.isCancelled())
					SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "Drowning", l);
			}
			break;
		
		case ENTITY_ATTACK:
		case ENTITY_EXPLOSION:
			EntityDamageByEntityEvent eventEntity = (EntityDamageByEntityEvent) event;
			Entity damager = eventEntity.getDamager();
			
			if(e instanceof LivingEntity) {
				String controlName = SafeCreeper.instance.getConfigManager().getControlName(damager);
				if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
					controlName = "OtherMobControl";
				
				if(e instanceof HumanEntity) {
					if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "DamagePlayers", true, true, l))
						event.setCancelled(true);

					// Handle the spawning effects, make sure the spawning isn't canceled yet!
					if(!event.isCancelled())
						SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "DamagedPlayer", l);
					
				} else if(e instanceof LivingEntity) {
					if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "DamageMobs", true, true, l))
						event.setCancelled(true);
					
					// Handle the spawning effects, make sure the spawning isn't canceled yet!
					if(!event.isCancelled())
						SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "DamagedMob", l);
				}
			}
			break;
			
		default:
		}
		
		/*dc.equals(DamageCause.)*/
		
		if(dc != null) {
			switch(dc) {
			case  DROWNING:
				if(e instanceof HumanEntity) {
					// Could players drown
					if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WaterControl", "PlayerDrowning", true, true, l)) { event.setCancelled(true); }
					
				} else if(e instanceof LivingEntity) {
					// Could mobs drown
					if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WaterControl", "MobDowning", true, true, l)) { event.setCancelled(true); }
				}
				break;
				
			case FIRE:
			case FIRE_TICK:
				if(e instanceof HumanEntity) {
					// Could fire hurt players
					if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "FireControl", "DamagePlayers", true, true, l)) { event.setCancelled(true); }
					
				} else if(e instanceof LivingEntity) {
					// Could fire hurt mobs
					if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "FireControl", "DamageMobs", true, true, l)) { event.setCancelled(true); }
				}
				break;
				
			case LAVA:
				if(e instanceof HumanEntity) {
					// Could lava hurt players
					if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "LavaControl", "DamagePlayers", true, true, l)) { event.setCancelled(true); }
					
				} else if(e instanceof LivingEntity) {
					// Could lava hurt mobs
					if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "LavaControl", "DamageMobs", true, true, l)) { event.setCancelled(true); }
				}
				break;
				
			case LIGHTNING:
				if(e instanceof HumanEntity) {
					// Could lightning hurt players
					if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "LightningControl", "DamagePlayers", true, true, l)) { event.setCancelled(true); }
					
				} else if(e instanceof LivingEntity) {
					// Could lightning hurt mobs
					if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "LightningControl", "DamageMobs", true, true, l)) { event.setCancelled(true); }
				}
				break;
				
			default:
			}
		}
		
		// Get the current control name
		SafeCreeper.instance.getLivingEntityManager().onEntityDamage(event);
		
		// Get the control name
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e, "OtherMobControl");
		
		// Handle the spawning effects, make sure the spawning isn't canceled yet!
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "Damaged", l);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		// Prevent event errors!
		if(event.getEntity() == null)
			return;
		
		Entity e = event.getEntity();
		Location l = e.getLocation();
		World w = l.getWorld();
		
		// Get the current control name
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e, "OtherMobControl");
		
		// Play the effects for the control
		SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "Died", l);
		
		// Put the block bellow in a try/catch block to prevent errors in the console caused by a null poniter from the event.getCause() function!
		try {
			// Check if .getCause() is set, if not skip this part
			if(e.getLastDamageCause().getCause() != null) {
				
				// Get .getCause()
				DamageCause lastDamageCause = e.getLastDamageCause().getCause();
				
				if(!lastDamageCause.equals(DamageCause.ENTITY_EXPLOSION)) {
					if(e instanceof Creeper) {
						if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "CreeperControl", "ExplodeOnDeath", false, true, l)) {
							boolean costumExplosionStrengthEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "CreeperControl", "CostumExplosionStrength.Enabled", false, true, l);
							boolean destroyWorld = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "CreeperControl", "DestroyWorld", true, true, l);
							int costumExplosionStrength = SafeCreeper.instance.getConfigManager().getOptionInt(w, "CreeperControl", "CostumExplosionStrength.ExplosionStrength", 3, true, l);
							
							if(!destroyWorld)
								w.createExplosion(l, 0);
							else
								if(!costumExplosionStrengthEnabled)
									w.createExplosion(l, 3);
								else
									w.createExplosion(l, costumExplosionStrength);
						}
					}
				}
			}
			
		} catch(NullPointerException ex) {
			// The .getCause() function was probably null, ignore the error!
		}
		
		if(e instanceof Enderman) {
			Enderman enderman = (Enderman) e;
			
			// Drop the item from the enderman if it's enabled and if it isn't air
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "EndermanControl", "DropItemOnDeath", false, true, l)) {
				if(!enderman.getCarriedMaterial().getItemType().equals(Material.AIR)) {
					// First drop the item
					ItemStack drop = new ItemStack(enderman.getCarriedMaterial().getItemTypeId(), 1, (short) 0);
					drop.setData(enderman.getCarriedMaterial());
					event.getDrops().add(drop);
					
					// Remove the item from the enderman (to make it more realistic)
					enderman.setCarriedMaterial(new MaterialData(Material.AIR));
				}
			}
		}
		
		// Creature Reviving
		if(e instanceof Creature) {
			Creature c = (Creature) e;
			
			boolean revivingEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Reviving.Enabled", false, true, l);
			
			if(revivingEnabled) {
								
				// Create a new random generator, to handle the chance options
				Random rand = new Random();
				
				double chance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "Reviving.Chance", 25, true, l);
				double radius = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "Reviving.Radius", 0, true, l);
				double minDelay = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "Reviving.MinDelay", 0, true, l);
				double maxDelay = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "Reviving.MaxDelay", 1.5, true, l);
				boolean rememberTarget = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Reviving.RememberTarget", true, true, l);
				boolean reviverRevivingEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "Reviving.Reviver.Enabled", false, true, l);
				
				if(((int) chance * 10) > rand.nextInt(1000)) {
					
					// Generate the spawn location
					Location spawnLoc = l.clone();
					spawnLoc.add(rand.nextDouble() * (radius * 2) - radius, 0, rand.nextDouble() * (radius * 2) - radius);
					
					if(!reviverRevivingEnabled) {
						// Reviving with tasks
						
						// Create the task
						CreatureReviveTask task = new CreatureReviveTask(c, spawnLoc);
						if(rememberTarget)
							task.setTarget(c.getTarget());
						
						// Creating the task delay
						minDelay = Math.max(minDelay, 0);
						maxDelay = Math.max(maxDelay, 0);
						double delayDiff = Math.max(minDelay, maxDelay) - Math.min(minDelay, maxDelay);
						double delay = Math.min(minDelay, maxDelay) + (rand.nextDouble() * delayDiff);
						
						// Schedule the created task
						SafeCreeper.instance.getServer().getScheduler().scheduleSyncDelayedTask(SafeCreeper.instance, task, ((int) delay * 20));
						
					} else {
						
						if(SafeCreeper.instance.getTVNLibHandler().isEnabled()) {
							// Reviving with revivers
							double reviverRevivingMaxDistance = SafeCreeper.instance.getConfigManager().getOptionDouble(w, controlName, "Reviving.Reviver.MaxDistance", 16, true, l);
							
							List<Entity> ee = c.getNearbyEntities(reviverRevivingMaxDistance, reviverRevivingMaxDistance, reviverRevivingMaxDistance);
							Creature nearest = null;
							double distance = -1;
							for(Entity entry : ee) {
								if(entry == null)
									continue;
								
								if(entry instanceof Creature) {
									Creature entryc = (Creature) entry;
									double dist = entry.getLocation().distance(e.getLocation());
									if(distance == -1 || distance > dist) {
										if(!SafeCreeper.instance.getLivingEntityReviveManager().isReviver(entryc)) {
											nearest = entryc;
										}
									}
								}					
							}
							
							if(nearest != null) {
								// An entity is found around the died creature, make a reviver from him.
								Creature reviver = nearest;
								SCLivingEntityRevive revive = new SCLivingEntityRevive(c, spawnLoc, reviver);
								SafeCreeper.instance.getLivingEntityReviveManager().addLivingEntiy(revive);
							}
							
						} else {
							
							// TVNLib is not installed or enabled, show a message in the console
							SafeCreeper.instance.getSCLogger().error("TVNLib not installed or enabled, can't use reviver feature!");
						}
					}
				}
			}
		}
		
		// Handle the death event for the living entity manager
		SafeCreeper.instance.getLivingEntityManager().onEntityDeath(event);
	}
	
	@EventHandler
	public void onExplosionPrime(ExplosionPrimeEvent event) {
		Entity e = event.getEntity();
		Location l = e.getLocation();
		World w = l.getWorld();		
		
		if(e instanceof TNTPrimed) {
			
			// PrimedTNTLimit
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "TNTControl", "PrimedTNTLimit.Enabled", false, true, l)) {
				// The PrimedTNTLimit feature is enabled
				int radius = SafeCreeper.instance.getConfigManager().getOptionInt(w, "TNTControl", "PrimedTNTLimit.Radius", 8, true, l);
				int maxPrimedTNTInRadius = SafeCreeper.instance.getConfigManager().getOptionInt(w, "TNTControl", "PrimedTNTLimit.MaxPrimedInRadius", 48, true, l);
				int maxPrimedInWorld = SafeCreeper.instance.getConfigManager().getOptionInt(w, "TNTControl", "PrimedTNTLimit.MaxPrimedInWorld", -1, true, l);
				
				int primedInRadius = 0;
				int primedInWorld = 0;
				
				for(Entity entry : w.getEntities()) {
					if(entry instanceof TNTPrimed) {
						primedInWorld++;
						if(l.distance(entry.getLocation()) <= radius)
							primedInRadius++;
					}
				}
				
				// Check max TNT in radius
				if(maxPrimedTNTInRadius >= 0)
					if(primedInRadius > maxPrimedTNTInRadius)
						event.setCancelled(true);
				
				// Check max TNT in radius
				if(maxPrimedInWorld >= 0)
					if(primedInWorld > maxPrimedInWorld)
						event.setCancelled(true);
			}
			
			// Check if the TNT could be primed
			if(event.getFire()) {
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "TNTControl", "TNTPriming.CanBePrimedByFire", true, true, l))
					event.setCancelled(true);
			} else {
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "TNTControl", "TNTPriming.CanBePrimedByOther", true, true, l))
					event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		Entity e = event.getEntity();
		World w = event.getLocation().getWorld();
		Location l = event.getLocation();
		
		// Get the current control name
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e);
		
		// Make sure the current control is enabled, if not, Safe Creeper should not take over the explosion
		if(!SafeCreeper.instance.getConfigManager().isControlEnabled(w.getName(), controlName, false, l))
			return;
		
		// Set the default explosion size of the current explosion
		int defExplosionSize = 3;
		if(e instanceof SmallFireball)
			defExplosionSize = 0;
			
		else if(e instanceof WitherSkull || e instanceof Fireball)
			defExplosionSize = 1;
			
		else if(e instanceof EnderDragon)
			defExplosionSize = 2;
			
		else if(e instanceof Creeper)
			defExplosionSize = 3;
			
		else if(e instanceof TNTPrimed)
			defExplosionSize = 4;
			
		else if(e instanceof Wither)
			defExplosionSize = 6;
		
		
		// Could the entity destroy the world?
		boolean b = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "DestroyWorld", true, true, l);
		
		for(Block entry : event.blockList())
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "DestroyWorld", true, true, entry.getLocation()))
				b = false;
		
		boolean costumExplosionStrengthEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CostumExplosionStrength.Enabled", false, true, l);
		defExplosionSize = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CostumExplosionStrength.ExplosionStrength", defExplosionSize, true, l);
		if(!costumExplosionStrengthEnabled) {
			if(!b) {
				event.setCancelled(true);
				SafeCreeper.instance.getStaticsManager().addCreeperExplosionNerfed();
			}
		} else {
			event.setCancelled(true);
			SafeCreeper.instance.getStaticsManager().addCreeperExplosionNerfed();
		}
		if(!b) {
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "EnableExplosionSound", true, true, l)) {
				createExplosionSound(w, l);
			}
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "EnableExplosionSmoke", true, true, l)) {
				w.playEffect(l, Effect.SMOKE, 3);
			}
			
		} else {
			if(costumExplosionStrengthEnabled) {
				if(defExplosionSize > 0) {
					SafeCreeper.instance.disableOtherExplosions = true;
					w.createExplosion(l, defExplosionSize);
				}
			}
		}
		
		// Custom Explosions - Falling Blocks
		boolean customExplosionsEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomExplosions.Enabled", false, true, l);
		if(customExplosionsEnabled) {
			boolean flyingBlocksEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CustomExplosions.FlyingBlocks.Enabled", false, true, l);
			if(flyingBlocksEnabled) {
				Random rand = new Random();
				for(Block entry : event.blockList()) {
					Location entryLoc = entry.getLocation();
					int flyingBlockChance = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "CustomExplosions.FlyingBlocks.Chance", 80, true, entryLoc);
					if(flyingBlockChance > rand.nextInt(100)) {
						Vector entryVec = entryLoc.toVector().subtract(l.toVector()).normalize();
						entryVec.add(new Vector(0, 0.6, 0));
						
						FallingBlock fb = entry.getWorld().spawnFallingBlock(entryLoc.add(0, 0.6, 0), entry.getTypeId(), entry.getData());
						fb.setVelocity(entryVec);
						fb.setDropItem(false);
					}
				}
			}
		}
		
		// Play control effects
		SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "Explode", l);
	}
	
	@EventHandler
	public void onEntityTeleport(EntityTeleportEvent event) {
		Entity e = event.getEntity();
		World w = e.getWorld();
		Location l = e.getLocation();
		
		if(e instanceof LivingEntity) {
			String controlName = SafeCreeper.instance.getConfigManager().getControlName(e);
			if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
				controlName = "OtherMobControl";
			
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CanTeleport", true, true, l))
				event.setCancelled(true);
			
			// Play control effects
			if(!event.isCancelled())
				SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "Teleported", l);
		}
	}
	
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		Entity e = event.getEntity();
		Block from = event.getBlock();
		Material to = event.getTo();
		World w = e.getWorld();
		Location l = from.getLocation();
		
		if(e instanceof Enderman) {
			
			if(from.getType().equals(Material.AIR) && !to.equals(Material.AIR)) {
				
				boolean canPlace = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "EndermanControl", "CanPlaceBlock", true, true, l);
				if(!canPlace) {
					boolean clearHands = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "EndermanControl", "ClearHandsOnPlace", false, true, l);
					if(clearHands) {
						Enderman enderman = (Enderman) e;
						enderman.eject();
					}
					
					event.setCancelled(true);
				}
				
				// Play control effects
				SafeCreeper.instance.getConfigManager().playControlEffects("EndermanControl", "PickedUpBlock", l);
			}
			
			if(!from.getType().equals(Material.AIR) && to.equals(Material.AIR)) {
				
				boolean canPickup = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "EndermanControl", "CanPickupBlock", true, true, l);
				if(!canPickup) {
					boolean cloneBlock = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "EndermanControl", "CanCloneBlock", false, true, l);
					if(cloneBlock) {
						Enderman enderman = (Enderman) e;
						enderman.setCarriedMaterial(new MaterialData(to));
					}
					
					event.setCancelled(true);
				}
				
				// Play control effects
				if(!event.isCancelled())
					SafeCreeper.instance.getConfigManager().playControlEffects("EndermanControl", "PlacedDownBlock", l);
			}
		}
		
		if(e instanceof Sheep) {
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "SheepControl", "CanEatGrass", true, true, l)) {
				event.setCancelled(true);
			}
			
			SafeCreeper.instance.getConfigManager().playControlEffects("SheepControl", "EatGrass", l);
		}
	}
	
	@EventHandler
	public void onPigZap(PigZapEvent event) {
		Entity e = event.getEntity();
		World w = e.getWorld();
		Location l = e.getLocation();
		
		if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PigControl", "TurnIntoPigZombieOnLightning", true, true, l))
			event.setCancelled(true);
		
		// Play control effects
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects("PigControl", "PigZap", l);
	}
	
	@EventHandler
	public void onSheepRegrowWool(SheepRegrowWoolEvent event) {
		Entity e = event.getEntity();
		World w = e.getWorld();
		Location l = e.getLocation();
		
		if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "SheepControl", "RegrowWool", true, true, l))
			event.setCancelled(true);
		
		// Play control effects
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects("SheepControl", "Regrown", l);
	}
	
	@EventHandler
	public void onSheepDyeWool(SheepDyeWoolEvent event) {
		Entity e = event.getEntity();
		World w = e.getWorld();
		Location l = e.getLocation();
		
		if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "SheepControl", "CanBeDyed", true, true, l))
			event.setCancelled(true);
		
		// Play control effects
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects("SheepControl", "Dyed", l);
	}

	@EventHandler
	public void onSlimeSplit(SlimeSplitEvent event) {
		Entity e = event.getEntity();
		World w = e.getWorld();
		Location l = e.getLocation();
		
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e, "OtherMobControl");
		
		if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CanSplit", true, true, l))
			event.setCancelled(true);
		
		if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "SplitIntoCostumAmount.Enabled", false, true, l)) {
			int min = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "SplitIntoCostumAmount.SplitIntoMin", 2, true, l);
			int max = SafeCreeper.instance.getConfigManager().getOptionInt(w, controlName, "SplitIntoCostumAmount.SplitIntoMax", 4, true, l);
			Random rand = new Random();
			int newAmount = rand.nextInt(max-min) + min;
			event.setCount(newAmount);
		}
		
		// Play control effects
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "Splitted", l);
	}
	
	@EventHandler
	public void onEntityTame(EntityTameEvent event) {
		Entity e = event.getEntity();
		World w = e.getWorld();
		Location l = e.getLocation();
		
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e, "OtherMobControl");
		
		if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CanBeTamed", true, true, l))
			event.setCancelled(true);
		
		// Play control effects
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "Tamed", l);
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Projectile p = event.getEntity();
		Location l = p.getLocation();
		LivingEntity s = p.getShooter();
		World w = p.getWorld();
		
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(s, "OtherMobControl");
		
		if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CanLaunchProjectile", true, true, l))
			event.setCancelled(true);
		
		// Play control effects
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "LaunchedProjectile", l);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		World w = p.getWorld();
		Location l = p.getLocation();
		
		if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "KeepXPOnDeath", false, true, l))
			event.setKeepLevel(true);
		
		if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "DropXPOnDeath", false, true, l))
			event.setDroppedExp(0);
		
		// Play control effects
		SafeCreeper.instance.getConfigManager().playControlEffects("PlayerControl", "Died", l);
	}
	
	@EventHandler
	public void onEntityBreakDoor(EntityBreakDoorEvent event) {
		Entity e = event.getEntity();
		Block b = event.getBlock();
		World w = b.getWorld();
		Location l = b.getLocation();
		
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e);
		
		if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CanBreakDoor", true, true, l))
			event.setCancelled(true);
		
		// Play control effects
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "BrokeDoor", l);
	}
	
	@EventHandler
	public void onEntityCreatePortal(EntityCreatePortalEvent event) {
		Entity e = event.getEntity();
		Location l = e.getLocation();
		PortalType t = event.getPortalType();
		World w = e.getWorld();
		
		if(e instanceof Player) {
			switch(t) {
			case CUSTOM:
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CanCreateOtherPortal", true, true, l))
					event.setCancelled(true);
				break;
			case ENDER:
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CanCreateEndPortal", true, true, l))
					event.setCancelled(true);
				break;
			case NETHER:
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "CanCreateNetherPortal", true, true, l))
					event.setCancelled(true);
				break;
			default:
			}
		}
		
		// Play control effects
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects("PlayerControl", "CreatedPortal", l);
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		Entity e = event.getEntity();
		Location l = e.getLocation();
		World w = e.getWorld();
		
		int oldLevel = -1;
		int newLevel = event.getFoodLevel();
		
		// If the entity is a player, cast the entity to a player object and get it's old foodlevel
		if(e instanceof Player) {
			Player p = (Player) e;
			oldLevel = p.getFoodLevel();
		}
		
		// Is the food level decreased?
		if(oldLevel > newLevel && oldLevel != -1) {
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "FoodMeter.CanDecrease", true, true, l))
				event.setCancelled(true);

			// Play control effects
			if(!event.isCancelled())
				SafeCreeper.instance.getConfigManager().playControlEffects("PlayerControl", "FoodLevelIncreased", l);
		}
		
		// Is the food level increased?
		if(oldLevel < newLevel && oldLevel != -1) {
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "FoodMeter.CanIncrease", true, true, l))
				event.setCancelled(true);

			// Play control effects
			if(!event.isCancelled())
				SafeCreeper.instance.getConfigManager().playControlEffects("PlayerControl", "FoodLevelDecreased", l);
		}
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		Entity e = event.getEntity();
		Location l = e.getLocation();
		Entity t = event.getTarget();
		World w = event.getEntity().getWorld();
		
		if(e instanceof Creature) {
			String controlName = SafeCreeper.instance.getConfigManager().getControlName(e);
			
			if(t instanceof HumanEntity) {
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CanTargetPlayer", true, true, l))
					event.setCancelled(true);
				
				// Play control effects
				if(!event.isCancelled())
					SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "TargetedPlayer", l);
			} else if(t instanceof LivingEntity) {
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, controlName, "CanTargetMob", true, true, l))
					event.setCancelled(true);
				
				// Play control effects
				if(!event.isCancelled())
					SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "TargetedMob", l);
			}
		}
	}

	@EventHandler
	public void onCreeperPower(CreeperPowerEvent event) {
		World w = event.getEntity().getWorld();
		Entity e = event.getEntity();
		Location l = e.getLocation();
		
		if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "CreeperControl", "PoweredByLightning", true, true, l))
			event.setCancelled(true);
		
		// Play control effects
		if(!event.isCancelled())
			SafeCreeper.instance.getConfigManager().playControlEffects("CreeperControl", "Charged", l);
	}
	
	public void createExplosionSound(World world, Location location) {
		SafeCreeper.instance.disableOtherExplosions = true;
		world.createExplosion(location, 0, false);
		SafeCreeper.instance.disableOtherExplosions = false;
	}
}
