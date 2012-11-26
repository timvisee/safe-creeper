package com.timvisee.safecreeper.listener;

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

import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.entity.SCLivingEntity;

public class SafeCreeperEntityListener implements Listener {
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity e = event.getEntity();
		Location l = e.getLocation();
		EntityType et = event.getEntityType();
		SpawnReason sr = event.getSpawnReason();
		World w = event.getLocation().getWorld();
		
		// Check if mobs are able to spawn
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e);
		if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
			controlName = "OtherMobControl";
		
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
		
		// Set the health of the mob if CustomHealth is enabled
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
					
					Random rand = new Random();
					customHealth = rand.nextInt(Math.max(diff, 1)) + customHealthMin;
					
					// Create a SCLivingEntity from the living entity and set the health
					SCLivingEntity scle = new SCLivingEntity(le);
					scle.setHealth(customHealth);
					SafeCreeper.instance.getLivingEntityManager().addLivingEntity(scle);
				}
			}
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
		
		// Handle the damage event for the living entity manager
		SafeCreeper.instance.getLivingEntityManager().onEntityDamage(event);
		
		// Get the control name
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e);
		if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
			controlName = "OtherMobControl";
		
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
		DamageCause lastDamageCause = e.getLastDamageCause().getCause();
		Location l = e.getLocation();
		World w = l.getWorld();
		
		// Handle the spawning effects, make sure the spawning isn't canceled yet!
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e);
		if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
			controlName = "OtherMobControl";
		
		// Play the effects for the control
		SafeCreeper.instance.getConfigManager().playControlEffects(controlName, "Died", l);
		
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
		
		if(e instanceof Enderman) {
			Enderman enderman = (Enderman) e;
			
			// Drop the item from the enderman if it's enabled and if it isn't air
			if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "EndermanControl", "DropItemOnDeath", false, true, l)) {
				if(!enderman.getCarriedMaterial().getItemType().equals(Material.AIR)) {
					// First drop the item
					ItemStack drop = new ItemStack(enderman.getCarriedMaterial().getItemTypeId(), 1, (short) 0, enderman.getCarriedMaterial().getData());
					event.getDrops().add(drop);
					
					// Remove the item from the enderman (to make it more realistic)
					enderman.setCarriedMaterial(new MaterialData(Material.AIR));
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
		World w = event.getLocation().getWorld();
		Location l = event.getLocation();
		
		// The entity is a creeper
		if(event.getEntity() instanceof Creeper) {
			// Could a creeper destroy the world
			//event.setCancelled(SafeCreeper.instance.getConfigManager().getOptionBoolean(world, "CreeperControl", "DestroyWorld", false, true, l));
			boolean b = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "CreeperControl", "DestroyWorld", true, true, l);
			for(Block entry : event.blockList())
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "CreeperControl", "DestroyWorld", true, true, entry.getLocation()))
					b = false;
			boolean costumExplosionStrengthEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "CreeperControl", "CostumExplosionStrength.Enabled", false, true, l);
			int costumExplosionStrength = SafeCreeper.instance.getConfigManager().getOptionInt(w, "CreeperControl", "CostumExplosionStrength.ExplosionStrength", 3, true, l);
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
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "CreeperControl", "EnableExplosionSound", true, true, l)) {
					createExplosionSound(w, l);
				}
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "CreeperControl", "EnableExplosionSmoke", true, true, l)) {
					w.playEffect(l, Effect.SMOKE, 3);
				}
			} else {
				if(costumExplosionStrengthEnabled) {
					SafeCreeper.instance.disableOtherExplosions = true;
					w.createExplosion(l, costumExplosionStrength);
				}
			}
			
			// Play control effects
			SafeCreeper.instance.getConfigManager().playControlEffects("CreeperControl", "Explode", l);
			
		} else if(event.getEntity() instanceof EnderDragon) {
			// Could an enderdragon destroy the world
			//event.setCancelled(SafeCreeper.instance.getConfigManager().getOptionBoolean(world, "EnderDragonControl", "DestroyWorld", true, true, l));
			boolean b = !SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "EnderDragonControl", "DestroyWorld", true, true, l);
			for(Block entry : event.blockList())
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "EnderDragonControl", "DestroyWorld", true, true, entry.getLocation()))
					b = false;
			if(b) {
				event.setCancelled(true);
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "EnderDragonControl", "EnableExplosionSound", true, true, l)) {
					createExplosionSound(w, l);
				}
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "EnderDragonControl", "EnableExplosionSmoke", true, true, l)) {
					w.playEffect(l, Effect.SMOKE, 3);
				}
			}
			
			// Play control effects
			SafeCreeper.instance.getConfigManager().playControlEffects("EnderDragonControl", "Explode", l);
			
		} else if(event.getEntity() instanceof Fireball) {
			// Could a fireball destroy the world
			boolean b = !SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "FireballControl", "DestroyWorld", true, true, l);
			for(Block entry : event.blockList())
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "FireballControl", "DestroyWorld", true, true, entry.getLocation()))
					b = false;
			if(b) {
				event.setCancelled(true);
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "FireballControl", "EnableExplosionSound", true, true, l)) {
					createExplosionSound(w, l);
				}
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "FireballControl", "EnableExplosionSmoke", true, true, l)) {
					w.playEffect(l, Effect.SMOKE, 3);
				}
			}
			
			// Play control effects
			SafeCreeper.instance.getConfigManager().playControlEffects("FireballControl", "Explode", l);
			
		} else if(event.getEntity() instanceof TNTPrimed) {
			// Could TNT destroy the world
			boolean b = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "TNTControl", "DestroyWorld", true, true, l);
			for(Block entry : event.blockList())
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "TNTControl", "DestroyWorld", true, true, entry.getLocation()))
					b = false;
			boolean costumExplosionStrengthEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "TNTControl", "CostumExplosionStrength.Enabled", false, true, l);
			int costumExplosionStrength = SafeCreeper.instance.getConfigManager().getOptionInt(w, "TNTControl", "CostumExplosionStrength.ExplosionStrength", 4, true, l);
			if(!costumExplosionStrengthEnabled) {
				if(!b) {
					event.setCancelled(true);
					SafeCreeper.instance.getStaticsManager().addTNTExplosionNerfed();
				}
			} else {
				event.setCancelled(true);
				SafeCreeper.instance.getStaticsManager().addTNTExplosionNerfed();
			}
			if(!b) {
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "TNTControl", "EnableExplosionSound", true, true, l)) {
					createExplosionSound(w, l);
				}
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "TNTControl", "EnableExplosionSmoke", true, true, l)) {
					w.playEffect(l, Effect.SMOKE, 3);
				}
			} else {
				if(costumExplosionStrengthEnabled) {
					SafeCreeper.instance.disableOtherExplosions = true;
					w.createExplosion(l, costumExplosionStrength);
				}
			}
			
			// Play control effects
			SafeCreeper.instance.getConfigManager().playControlEffects("TNTControl", "Explode", l);
			
		} else if(event.getEntity() instanceof Wither) {
			// Could a creeper destroy the world
			//event.setCancelled(SafeCreeper.instance.getConfigManager().getOptionBoolean(world, "CreeperControl", "DestroyWorld", false, true, l));
			boolean b = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WitherControl", "DestroyWorld", true, true, l);
			for(Block entry : event.blockList())
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WitherControl", "DestroyWorld", true, true, entry.getLocation()))
					b = false;
			boolean costumExplosionStrengthEnabled = SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WitherControl", "CostumExplosionStrength.Enabled", false, true, l);
			int costumExplosionStrength = SafeCreeper.instance.getConfigManager().getOptionInt(w, "WitherControl", "CostumExplosionStrength.ExplosionStrength", 3, true, l);
			if(!costumExplosionStrengthEnabled) {
				if(!b) {
					event.setCancelled(true);
					//SafeCreeper.instance.getStaticsManager().addCreeperExplosionNerfed();
				}
			} else {
				event.setCancelled(true);
				//SafeCreeper.instance.getStaticsManager().addCreeperExplosionNerfed();
			}
			if(!b) {
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WitherControl", "EnableExplosionSound", true, true, l)) {
					createExplosionSound(w, l);
				}
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WitherControl", "EnableExplosionSmoke", true, true, l)) {
					w.playEffect(l, Effect.SMOKE, 3);
				}
			} else {
				if(costumExplosionStrengthEnabled) {
					SafeCreeper.instance.disableOtherExplosions = true;
					w.createExplosion(l, costumExplosionStrength);
				}
			}
			
			// Play control effects
			SafeCreeper.instance.getConfigManager().playControlEffects("WitherControl", "Explode", l);
			
		} else if(event.getEntity() instanceof WitherSkull) {
			// Could a WitherSkull destroy the world
			boolean b = !SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WitherSkullControl", "DestroyWorld", true, true, l);
			for(Block entry : event.blockList())
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WitherSkullControl", "DestroyWorld", true, true, entry.getLocation()))
					b = false;
			if(b) {
				event.setCancelled(true);
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WitherSkullControl", "EnableExplosionSound", true, true, l)) {
					createExplosionSound(w, l);
				}
				if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WitherSkullControl", "EnableExplosionSmoke", true, true, l)) {
					w.playEffect(l, Effect.SMOKE, 3);
				}
			}
			
			// Play control effects
			SafeCreeper.instance.getConfigManager().playControlEffects("WitherSkullControl", "Explode", l);
			
		} else {
			if(SafeCreeper.instance.disableOtherExplosions == false) {
				// Could other explosions destroy the world
				boolean b = !SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "OtherExplosions", "DestroyWorld", true, true, l);
				for(Block entry : event.blockList())
					if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "OtherExplosions", "DestroyWorld", true, true, entry.getLocation()))
						b = false;
				if(b) {
					event.setCancelled(true);
					if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "OtherExplosions", "EnableExplosionSound", true, true, l)) {
						createExplosionSound(w, l);
					}
					if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "OtherExplosions", "EnableExplosionSmoke", true, true, l)) {
						w.playEffect(l, Effect.SMOKE, 3);
					}
				}
			}
			
			// Play control effects
			SafeCreeper.instance.getConfigManager().playControlEffects("OtherExplosions", "Explode", l);
		}
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
		
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e);
		if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
			controlName = "OtherMobControl";
		
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
		
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(e);
		if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
			controlName = "OtherMobControl";
		
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
		
		String controlName = SafeCreeper.instance.getConfigManager().getControlName(s);
		if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
			controlName = "OtherMobControl";
		
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
		
		if(SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "KeepXPOnDeath", false, true, l)) {
			event.setKeepLevel(true);
		}
		if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "PlayerControl", "DropXPOnDeath", false, true, l)) {
			event.setDroppedExp(0);
		}
		
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
		if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
			controlName = "OtherMobControl";
		
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
			if(!SafeCreeper.instance.getConfigManager().isValidControl(controlName))
				controlName = "OtherMobControl";
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
