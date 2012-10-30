package com.timvisee.safecreeper;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
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
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.entity.SlimeSplitEvent;

import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.material.MaterialData;

public class SafeCreeperEntityListener implements Listener {
	public static SafeCreeper plugin;

	public SafeCreeperEntityListener(SafeCreeper instance) {
		plugin = instance;
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		EntityType entityType = event.getEntityType();
		SpawnReason reason = event.getSpawnReason();
		World world = event.getLocation().getWorld();
		
		if(entityType == EntityType.BAT) {
			// Could a blaze spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "BatControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "BatControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "BatControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "BatControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "BatControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		
		} else if(entityType == EntityType.BLAZE) {
			// Could a blaze spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "BlazeControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "BlazeControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "BlazeControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "BlazeControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "BlazeControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		
		} else if(entityType == EntityType.CAVE_SPIDER) {
			// Could a blaze spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "CaveSpiderControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "CaveSpiderControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "CaveSpiderControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "CaveSpiderControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "CaveSpiderControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		
		} else if(entityType == EntityType.CHICKEN) {
			// Could a blaze spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "ChickenControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "ChickenControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "ChickenControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "ChickenControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "ChickenControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		
		} else if(entityType == EntityType.COW) {
			// Could a blaze spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "CowControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "CowControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "CowControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "CowControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "CowControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		
		} else if(entityType == EntityType.CREEPER) {
			// Could a creeper spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "CreeperControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "CreeperControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "CreeperControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "CreeperControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "CreeperControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.ENDER_DRAGON) {
			// Could an enderdragon spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "EnderDragonControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "EnderDragonControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "EnderDragonControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "EnderDragonControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "EnderDragonControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.ENDERMAN) {
			// Could an enderman spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "EndermanControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "EndermanControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "EndermanControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "EndermanControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "EndermanControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.GHAST) {
			// Could a ghast spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "GhastControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "GhastControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "GhastControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "GhastControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "GhastControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.GIANT) {
			// Could a ghast spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "GiantControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "GiantControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "GiantControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "GiantControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "GiantControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.MAGMA_CUBE) {
			// Could a ghast spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "MagmaCubeControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "MagmaCubeControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "MagmaCubeControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "MagmaCubeControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "MagmaCubeControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.MUSHROOM_COW) {
			// Could a ghast spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "MushroomCowControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "MushroomCowControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "MushroomCowControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "MushroomCowControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "MushroomCowControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.OCELOT) {
			// Could a ghast spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "OcelotControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "OcelotControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "OcelotControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "OcelotControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "OcelotControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.PIG) {
			// Could a ghast spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "PigControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "PigControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "PigControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "PigControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "PigControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.PIG_ZOMBIE) {
			// Could a ghast spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "PigZombieControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "PigZombieControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "PigZombieControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "PigZombieControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "PigZombieControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.SHEEP) {
			// Could a sheep spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "SheepControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "SheepControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "SheepControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "SheepControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "SheepControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		} else if(entityType == EntityType.SKELETON) {
			// Could a skeleton spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "SkeletonControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "SkeletonControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "SkeletonControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "SkeletonControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "SkeletonControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.SLIME) {
			// Could a skeleton spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "SlimeControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "SlimeControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "SlimeControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "SlimeControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "SlimeControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.SNOWMAN) {
			// Could a skeleton spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "SnowmanControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "SnowmanControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "SnowmanControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "SnowmanControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "SnowmanControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.SILVERFISH) {
			// Could a skeleton spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "SilverfishControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "SilverfishControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "SilverfishControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "SilverfishControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "SilverfishControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.SPIDER) {
			// Could a skeleton spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "SpiderControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "SpiderControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "SpiderControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "SpiderControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "SpiderControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.SQUID) {
			// Could a skeleton spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "SquidControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "SquidControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "SquidControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "SquidControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "SquidControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.VILLAGER) {
			// Could a skeleton spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "VillagerControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "VillagerControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "VillagerControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "VillagerControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "VillagerControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.WITCH) {
			// Could a ghast spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "WitchControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "WitchControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "WitchControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "WitchControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "WitchControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.WITHER) {
			// Could a ghast spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "WitherControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "WitherControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "WitherControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "WitherControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "WitherControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entityType == EntityType.WOLF) {
			// Could a wolf spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "WolfControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "WolfControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "WolfControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "WolfControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "WolfControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		} else if(entityType == EntityType.ZOMBIE) {
			// Could a zombie spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "ZombieControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "ZombieControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "ZombieControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "ZombieControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "ZombieControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		} else {
			// Could a other mob spawn
			if(reason == SpawnReason.CHUNK_GEN) {
				if(!getConfigSettings(world, "OtherMobControl", "Spawning.CanSpawnWithWorldGeneration", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.NATURAL) {
				if(!getConfigSettings(world, "OtherMobControl", "Spawning.CanSpawnNaturally", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER) {
				if(!getConfigSettings(world, "OtherMobControl", "Spawning.CanSpawnFromSpawner", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else if(reason == SpawnReason.SPAWNER_EGG) {
				if(!getConfigSettings(world, "OtherMobControl", "Spawning.CanSpawnFromSpawnerEgg", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			} else {
				if(!getConfigSettings(world, "OtherMobControl", "Spawning.CanSpawnFromOther", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		World world = entity.getWorld();
		DamageCause damageCause = event.getCause();
		
		if(damageCause == DamageCause.ENTITY_EXPLOSION || damageCause == DamageCause.ENTITY_ATTACK) {
			EntityDamageByEntityEvent eventEntity = (EntityDamageByEntityEvent) event;
			Entity damager = eventEntity.getDamager();
			if(damager instanceof Blaze) {
				if(entity instanceof HumanEntity) {
					// Could a blaze damage players
					if(!getConfigSettings(world, "BlazeControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could a blaze damage mobs
					if(!getConfigSettings(world, "BlazeControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof CaveSpider) {
				if(entity instanceof HumanEntity) {
					// Could a cavespider damage players
					if(!getConfigSettings(world, "CaveSpiderControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could a cavespider damage mobs
					if(!getConfigSettings(world, "CaveSpiderControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Creeper) {
				if(entity instanceof HumanEntity) {
					// Could a creeper damage players
					if(!getConfigSettings(world, "CreeperControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could a creeper damage mobs
					if(!getConfigSettings(world, "CreeperControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Enderman) {
				if(entity instanceof HumanEntity) {
					// Could a enderman damage players
					if(!getConfigSettings(world, "EndermanControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could an enderman damage mobs
					if(!getConfigSettings(world, "EndermanControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof EnderDragon) {
				if(entity instanceof HumanEntity) {
					// Could an enderdragon damage players
					if(!getConfigSettings(world, "EnderDragonControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could an enderdragon damage mobs
					if(!getConfigSettings(world, "EnderDragonControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Fireball) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "FireballControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "FireballControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Ghast) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "GhastControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "GhastControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Giant) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "GiantControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "GiantControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof MagmaCube) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "MagmaCubeControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "MagmaCubeControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof PigZombie) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "PigZombieControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "PigZombieControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Silverfish) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "SilverfishControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "SilverfishControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Skeleton) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "SkeletonControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "SkeletonControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Slime) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "SlimeControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "SlimeControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Spider) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "SpiderControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "SpiderControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof TNTPrimed) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "TNTControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) {
						event.setCancelled(true);
						plugin.getStaticsManager().addTNTDamageNerfed();
					}
					
				} else if(entity instanceof LivingEntity) {
					if(!getConfigSettings(world, "TNTControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Witch) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "WitchControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "WitchControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Wither) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "WitherControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "WitherControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Wither) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "WitherSkullControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "WitherSkullControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Wolf) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "WolfControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "WolfControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else if(damager instanceof Zombie) {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "ZombieControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "ZombieControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			} else {
				if(entity instanceof HumanEntity) {
					// Could TNT damage players
					if(!getConfigSettings(world, "OtherMobControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					
				} else if(entity instanceof LivingEntity) {
					// Could TNT damage mobs
					if(!getConfigSettings(world, "OtherMobControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				}
			}
		} else if(damageCause == DamageCause.DROWNING) {
			if(entity instanceof HumanEntity) {
				// Could players drown
				if(!getConfigSettings(world, "WaterControl", "PlayerDrowning", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				
			} else if(entity instanceof LivingEntity) {
				// Could mobs drown
				if(!getConfigSettings(world, "WaterControl", "MobDowning", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
				
		} else if(damageCause == DamageCause.FIRE || damageCause == DamageCause.FIRE_TICK) {
			if(entity instanceof HumanEntity) {
				// Could fire hurt players
				if(!getConfigSettings(world, "FireControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				
			} else if(entity instanceof LivingEntity) {
				// Could fire hurt mobs
				if(!getConfigSettings(world, "FireControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
				
		} else if(damageCause == DamageCause.LAVA) {
			if(entity instanceof HumanEntity) {
				// Could lava hurt players
				if(!getConfigSettings(world, "LavaControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				
			} else if(entity instanceof LivingEntity) {
				// Could lava hurt mobs
				if(!getConfigSettings(world, "LavaControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
				
		} else if(damageCause == DamageCause.LIGHTNING) {
			if(entity instanceof HumanEntity) {
				// Could lightning hurt players
				if(!getConfigSettings(world, "LightningControl", "DamagePlayers", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
				
			} else if(entity instanceof LivingEntity) {
				// Could lightning hurt mobs
				if(!getConfigSettings(world, "LightningControl", "DamageMobs", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		World world = event.getLocation().getWorld();
		Location location = event.getLocation();
		Entity entity = event.getEntity();
		
		// The entity is a creeper
		if(event.getEntity() instanceof Creeper) {
			// Could a creeper destroy the world
			//event.setCancelled(getConfigSettings(world, "CreeperControl", "DestroyWorld", false, true, true, entity.getLocation().getBlockY()));
			boolean b = getConfigSettings(world, "CreeperControl", "DestroyWorld", true, true, true, entity.getLocation().getBlockY());
			boolean costumExplosionStrengthEnabled = getConfigSettings(world, "CreeperControl", "CostumExplosionStrength.Enabled", false, true, true, entity.getLocation().getBlockY());
			int costumExplosionStrength = configGetInt(world.getName(), "CreeperControl", "CostumExplosionStrength.ExplosionStrength", 3);
			if(!costumExplosionStrengthEnabled) {
				if(!b) {
					event.setCancelled(true);
					plugin.getStaticsManager().addCreeperExplosionNerfed();
				}
			} else {
				event.setCancelled(true);
				plugin.getStaticsManager().addCreeperExplosionNerfed();
			}
			if(!b) {
				if(getConfigSettings(world, "CreeperControl", "EnableExplosionSound", true, true, true, entity.getLocation().getBlockY())) {
					createExplosionSound(world, location);
				}
				if(getConfigSettings(world, "CreeperControl", "EnableExplosionSmoke", true, true, true, entity.getLocation().getBlockY())) {
					world.playEffect(location, Effect.SMOKE, 3);
				}
			} else {
				if(costumExplosionStrengthEnabled) {
					plugin.disableOtherExplosions = true;
					world.createExplosion(location, costumExplosionStrength);
				}
			}
			
		} else if(event.getEntity() instanceof EnderDragon) {
			// Could an enderdragon destroy the world
			//event.setCancelled(getConfigSettings(world, "EnderDragonControl", "DestroyWorld", true, true, true, entity.getLocation().getBlockY()));
			boolean b = !getConfigSettings(world, "EnderDragonControl", "DestroyWorld", true, true, true, entity.getLocation().getBlockY());
			if(b) {
				event.setCancelled(true);
				if(getConfigSettings(world, "EnderDragonControl", "EnableExplosionSound", true, true, true, entity.getLocation().getBlockY())) {
					createExplosionSound(world, location);
				}
				if(getConfigSettings(world, "EnderDragonControl", "EnableExplosionSmoke", true, true, true, entity.getLocation().getBlockY())) {
					world.playEffect(location, Effect.SMOKE, 3);
				}
			}
			
		} else if(event.getEntity() instanceof Fireball) {
			// Could a fireball destroy the world
			boolean b = !getConfigSettings(world, "FireballControl", "DestroyWorld", true, true, true, entity.getLocation().getBlockY());
			if(b) {
				event.setCancelled(true);
				if(getConfigSettings(world, "FireballControl", "EnableExplosionSound", true, true, true, entity.getLocation().getBlockY())) {
					createExplosionSound(world, location);
				}
				if(getConfigSettings(world, "FireballControl", "EnableExplosionSmoke", true, true, true, entity.getLocation().getBlockY())) {
					world.playEffect(location, Effect.SMOKE, 3);
				}
			}
			
		} else if(event.getEntity() instanceof TNTPrimed) {
			// Could TNT destroy the world
			boolean b = getConfigSettings(world, "TNTControl", "DestroyWorld", false, true, true, entity.getLocation().getBlockY());
			boolean costumExplosionStrengthEnabled = getConfigSettings(world, "TNTControl", "CostumExplosionStrength.Enabled", false, true, true, entity.getLocation().getBlockY());
			int costumExplosionStrength = configGetInt(world.getName(), "TNTControl", "CostumExplosionStrength.ExplosionStrength", 4);
			if(!costumExplosionStrengthEnabled) {
				if(!b) {
					event.setCancelled(true);
					plugin.getStaticsManager().addTNTExplosionNerfed();
				}
			} else {
				event.setCancelled(true);
				plugin.getStaticsManager().addTNTExplosionNerfed();
			}
			if(!b) {
				if(getConfigSettings(world, "TNTControl", "EnableExplosionSound", true, true, true, entity.getLocation().getBlockY())) {
					createExplosionSound(world, location);
				}
				if(getConfigSettings(world, "TNTControl", "EnableExplosionSmoke", true, true, true, entity.getLocation().getBlockY())) {
					world.playEffect(location, Effect.SMOKE, 3);
				}
			} else {
				if(costumExplosionStrengthEnabled) {
					plugin.disableOtherExplosions = true;
					world.createExplosion(location, costumExplosionStrength);
				}
			}
			
		} else if(event.getEntity() instanceof Wither) {
			// Could a creeper destroy the world
			//event.setCancelled(getConfigSettings(world, "CreeperControl", "DestroyWorld", false, true, true, entity.getLocation().getBlockY()));
			boolean b = getConfigSettings(world, "WitherControl", "DestroyWorld", true, true, true, entity.getLocation().getBlockY());
			boolean costumExplosionStrengthEnabled = getConfigSettings(world, "WitherControl", "CostumExplosionStrength.Enabled", false, true, true, entity.getLocation().getBlockY());
			int costumExplosionStrength = configGetInt(world.getName(), "WitherControl", "CostumExplosionStrength.ExplosionStrength", 3);
			if(!costumExplosionStrengthEnabled) {
				if(!b) {
					event.setCancelled(true);
					//plugin.getStaticsManager().addCreeperExplosionNerfed();
				}
			} else {
				event.setCancelled(true);
				//plugin.getStaticsManager().addCreeperExplosionNerfed();
			}
			if(!b) {
				if(getConfigSettings(world, "WitherControl", "EnableExplosionSound", true, true, true, entity.getLocation().getBlockY())) {
					createExplosionSound(world, location);
				}
				if(getConfigSettings(world, "WitherControl", "EnableExplosionSmoke", true, true, true, entity.getLocation().getBlockY())) {
					world.playEffect(location, Effect.SMOKE, 3);
				}
			} else {
				if(costumExplosionStrengthEnabled) {
					plugin.disableOtherExplosions = true;
					world.createExplosion(location, costumExplosionStrength);
				}
			}
			
		} else if(event.getEntity() instanceof WitherSkull) {
			// Could a WitherSkull destroy the world
			boolean b = !getConfigSettings(world, "WitherSkullControl", "DestroyWorld", true, true, true, entity.getLocation().getBlockY());
			if(b) {
				event.setCancelled(true);
				if(getConfigSettings(world, "WitherSkullControl", "EnableExplosionSound", true, true, true, entity.getLocation().getBlockY())) {
					createExplosionSound(world, location);
				}
				if(getConfigSettings(world, "WitherSkullControl", "EnableExplosionSmoke", true, true, true, entity.getLocation().getBlockY())) {
					world.playEffect(location, Effect.SMOKE, 3);
				}
			}
			
		} else {
			if(plugin.disableOtherExplosions == false) {
				// Could other explosions destroy the world
				boolean b = !getConfigSettings(world, "OtherExplosions", "DestroyWorld", true, true, true, location.getBlockY());
				if(b) {
					event.setCancelled(true);
					if(getConfigSettings(world, "OtherExplosions", "EnableExplosionSound", true, true, true, location.getBlockY())) {
						createExplosionSound(world, location);
					}
					if(getConfigSettings(world, "OtherExplosions", "EnableExplosionSmoke", true, true, true, location.getBlockY())) {
						world.playEffect(location, Effect.SMOKE, 3);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityTeleport(EntityTeleportEvent event) {
		EntityType entityType = event.getEntityType();
		Entity entity = event.getEntity();
		World world = entity.getWorld();
		
		if(entityType == EntityType.ENDERMAN) {
			// Could a creeper spawn
			if(!getConfigSettings(world, "EndermanControl", "CanTeleport", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			
		}
	}
	
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		EntityType entityType = event.getEntityType();
		Entity entity = event.getEntity();
		Block from = event.getBlock();
		Material to = event.getTo();
		World world = entity.getWorld();
		
		if(entityType == EntityType.ENDERMAN) {
			if(from.getTypeId() == 0) {
				if(to != Material.AIR) {
					boolean b = !getConfigSettings(world, "EndermanControl", "CanPlaceBlock", true, true, true, entity.getLocation().getBlockY());
					if(!getConfigSettings(world, "EndermanControl", "CanPlaceBlock", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
					if(b) {
						boolean clearHands = getConfigSettings(world, "EndermanControl", "ClearHandsOnPlace", false, true, true, entity.getLocation().getBlockY());
						if(clearHands) {
							Enderman enderman = (Enderman) entity;
							enderman.eject();
						}
					}
				}
			}
			
			if(from.getTypeId() != 0) {
				if(to == Material.AIR) {
					boolean b = !getConfigSettings(world, "EndermanControl", "CanPickupBlock", true, true, true, entity.getLocation().getBlockY());
					if(b) {
						event.setCancelled(true);
						boolean cloneBlock = getConfigSettings(world, "EndermanControl", "CanCloneBlock", false, true, true, entity.getLocation().getBlockY());
						if(cloneBlock) {
							Enderman enderman = (Enderman) entity;
							enderman.setCarriedMaterial(new MaterialData(to));
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPigZap(PigZapEvent event) {
		Entity entity = event.getEntity();
		World world = entity.getWorld();
		
		if(!getConfigSettings(world, "PigControl", "TurnIntoPigZombieOnLightning", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
	}
	
	@EventHandler
	public void onSheepRegrowWool(SheepRegrowWoolEvent event) {
		Entity entity = event.getEntity();
		World world = entity.getWorld();
		
		if(!getConfigSettings(world, "SheepControl", "RegrowWool", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
	}

	/*
	 * Doesn't work with 1.2.3 anymore, needs to be fixed!
	 */
	/*@EventHandler
	public void onEndermanPickup(EndermanPickupEvent event) {
		World world = event.getBlock().getWorld();
		Block block = event.getBlock();
		Entity entity = event.getEntity();
		Enderman enderman = (Enderman) entity;
		
		if(configGetBoolean(world.getName(), "EndermanControl", "Enabled", true)) {
			// Destroy blocks or not?
			if(configGetBoolean(world.getName(), "EndermanControl", "CanPickupBlock", false) == false) {
				
				boolean useSettings = true;
				
				// Check if the user want to use this only between two layers
				if(configGetBoolean(world.getName(), "EndermanControl", "EnableBetweenLevels", "Enabled", false)) {
					int entityLevel = block.getLocation().getBlockY();
					int minLevel = configGetInt(world.getName(), "EndermanControl", "EnableBetweenLevels", "MinLevel", 0);
					int maxLevel = configGetInt(world.getName(), "EndermanControl", "EnableBetweenLevels", "MaxLevel", 127);
					if(entityLevel >= minLevel && entityLevel <= maxLevel) {
						useSettings = true;
					} else {
						useSettings = false;
					}
				} else {
					useSettings = true;
				}
				
				if(useSettings) {
					// Cancel the event to safe all the blocks from the world
					event.setCancelled(true);
					
					if(configGetBoolean(world.getName(), "EndermanControl", "CanCloneBlock", true)) {
						MaterialData newBlockInHand = new MaterialData(block.getTypeId(), block.getData());
						enderman.setCarriedMaterial(newBlockInHand);
					}
				}
			}
		}
	}

	public void onEntityBlockForm(EntityBlockFormEvent event) {
		plugin.getServer().broadcastMessage("[Safe Creeper] EntityBlockForm: " + event.getEntity().toString());
	}
	
	/*@EventHandler
	public void onEndermanPlace(EndermanPlaceEvent event) {
		World world = event.getLocation().getWorld();
		Entity entity = event.getEntity();
		Enderman enderman = (Enderman) entity;
		
		if(configGetBoolean(world.getName(), "EndermanControl", "Enabled", true)) {
			// Destroy blocks or not?
			if(configGetBoolean(world.getName(), "EndermanControl", "CanPlaceBlock", false) == false) {
				
				boolean useSettings = true;
				
				// Check if the user want to use this only between two layers
				if(configGetBoolean(world.getName(), "EndermanControl", "EnableBetweenLevels", "Enabled", false)) {
					int entityLevel = event.getLocation().getBlockY();
					int minLevel = configGetInt(world.getName(), "EndermanControl", "EnableBetweenLevels", "MinLevel", 0);
					int maxLevel = configGetInt(world.getName(), "EndermanControl", "EnableBetweenLevels", "MaxLevel", 127);
					if(entityLevel >= minLevel && entityLevel <= maxLevel) {
						useSettings = true;
					} else {
						useSettings = false;
					}
				} else {
					useSettings = true;
				}
				
				if(useSettings) {
					// Cancel the event to safe all the blocks from the world
					event.setCancelled(true);
					
					if(configGetBoolean(world.getName(), "EndermanControl", "ClearHandsOnPlace", true)) {
						MaterialData newBlockInHand = new MaterialData(0);
						enderman.setCarriedMaterial(newBlockInHand);
					}
				}
			}
		}
	}*/

	@EventHandler
	public void onSlimeSplit(SlimeSplitEvent event) {
		Entity entity = event.getEntity();
		EntityType entityType = event.getEntityType();
		World world = entity.getWorld();
		
		if(entityType == EntityType.SLIME) {
			if(!getConfigSettings(world, "SlimeControl", "CanSplit", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			
			boolean costumSplitAmount = getConfigSettings(world, "SlimeControl", "SplitIntoCostumAmount.Enabled", false, true, true, entity.getLocation().getBlockY());
			if(costumSplitAmount) {
				int min = configGetInt(world.getName(), "SlimeControl", "SplitIntoCostumAmount.SplitIntoMin", 2);
				int max = configGetInt(world.getName(), "SlimeControl", "SplitIntoCostumAmount.SplitIntoMax", 4);
				Random rand = new Random();
				int newAmount = rand.nextInt(max-min) + min;
				event.setCount(newAmount);
			}
		} else if(entityType == EntityType.MAGMA_CUBE) {
			if(!getConfigSettings(world, "MagmaCubeControl", "CanSplit", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			
			boolean costumSplitAmount = getConfigSettings(world, "MagmaCubeControl", "SplitIntoCostumAmount.Enabled", false, true, true, entity.getLocation().getBlockY());
			if(costumSplitAmount) {
				int min = configGetInt(world.getName(), "MagmaCubeControl", "SplitIntoCostumAmount.SplitIntoMin", 2);
				int max = configGetInt(world.getName(), "MagmaCubeControl", "SplitIntoCostumAmount.SplitIntoMax", 4);
				Random rand = new Random();
				int newAmount = rand.nextInt(max-min) + min;
				event.setCount(newAmount);
			}
		}
	}
	
	@EventHandler
	public void onEntityTame(EntityTameEvent event) {
		Entity entity = event.getEntity();
		EntityType entityType = event.getEntityType();
		World world = entity.getWorld();
		
		if(entityType == EntityType.WOLF) {
			if(!getConfigSettings(world, "WolfControl", "CanBeTamed", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
		} else if(entityType == EntityType.OCELOT) {
			if(!getConfigSettings(world, "OcelotControl", "CanBeTamed", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
		}
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Projectile projectile = event.getEntity();
		LivingEntity shooter = projectile.getShooter();
		World world = projectile.getWorld();
		
		if(shooter instanceof Blaze) {
			if(!getConfigSettings(world, "BlazeControl", "CanLaunchProjectile", true, true, true, projectile.getLocation().getBlockY())) { event.setCancelled(true); }
		} else if(shooter instanceof Ghast) {
			if(!getConfigSettings(world, "GhastControl", "CanLaunchProjectile", true, true, true, projectile.getLocation().getBlockY())) { event.setCancelled(true); }
		} else if(shooter instanceof Skeleton) {
			if(!getConfigSettings(world, "SkeletonControl", "CanLaunchProjectile", true, true, true, projectile.getLocation().getBlockY())) { event.setCancelled(true); }
		} else if(shooter instanceof Snowman) {
			if(!getConfigSettings(world, "SnowmanControl", "CanLaunchProjectile", true, true, true, projectile.getLocation().getBlockY())) { event.setCancelled(true); }
		} else if(shooter instanceof Witch) {
			if(!getConfigSettings(world, "WitchControl", "CanLaunchProjectile", true, true, true, projectile.getLocation().getBlockY())) { event.setCancelled(true); }
		} else if(shooter instanceof Wither) {
			if(!getConfigSettings(world, "WitherControl", "CanLaunchProjectile", true, true, true, projectile.getLocation().getBlockY())) { event.setCancelled(true); }
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		World world = player.getWorld();
		
		if(getConfigSettings(world, "PlayerControl", "KeepXPOnDeath", false, true, true, player.getLocation().getBlockY())) {
			event.setKeepLevel(true);
		}
		if(!getConfigSettings(world, "PlayerControl", "DropXPOnDeath", false, true, true, player.getLocation().getBlockY())) {
			event.setDroppedExp(0);
		}
	}
	
	@EventHandler
	public void onEntityBreakDoor(EntityBreakDoorEvent event) {
		Entity entity = event.getEntity();
		Block door = event.getBlock();
		World world = door.getWorld();
		
		if(entity instanceof Zombie) {
			if(!getConfigSettings(world, "ZombieControl", "CanBreakDoor", true, true, true, door.getLocation().getBlockY())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityCreatePortal(EntityCreatePortalEvent event) {
		Entity entity = event.getEntity();
		PortalType type = event.getPortalType();
		World world = entity.getWorld();
		
		if(entity instanceof Player) {
			if(type == PortalType.NETHER) {
				if(!getConfigSettings(world, "PlayerControl", "CanCreateNetherPortal", true, true, true, entity.getLocation().getBlockY())) {
					event.setCancelled(true);
				}
			}
			if(type == PortalType.ENDER) {
				if(!getConfigSettings(world, "PlayerControl", "CanCreateEndPortal", true, true, true, entity.getLocation().getBlockY())) {
					event.setCancelled(true);
				}
			}
			if(type == PortalType.CUSTOM) {
				if(!getConfigSettings(world, "PlayerControl", "CanCreateOtherPortal", true, true, true, entity.getLocation().getBlockY())) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		Entity entity = event.getEntity();
		World world = entity.getWorld();
		
		if(entity instanceof Player) {
			if(getConfigSettings(world, "PlayerControl", "LockFoodmeter", false, true, true, entity.getLocation().getBlockY())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		Entity entity = event.getEntity();
		Entity target = event.getTarget();
		World world = event.getEntity().getWorld();
		
		if(entity instanceof Bat) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "BatControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "BatControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Blaze) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "BlazeControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "BlazeControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof CaveSpider) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "CaveSpiderControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "CaveSpiderControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Chicken) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "ChickenControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "ChickenControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Cow) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "CowControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "CowControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Creeper) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "CreeperControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "CreeperControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof EnderDragon) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "EnderDragonControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "EnderDragonControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Enderman) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "EndermanControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "EnderDragonControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Ghast) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "GhastControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "GhastControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Giant) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "GiantControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "GiantControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof MagmaCube) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "MagmaCubeControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "MagmaCubeControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof MushroomCow) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "MushroomCowControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "MushroomCowControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof MushroomCow) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "PigControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "PigControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof PigZombie) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "PigZombieControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "PigZombieControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Sheep) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "SheepControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "SheepControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Silverfish) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "SilverfishControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "SilverfishControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Skeleton) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "SkeletonControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "SkeletonControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Slime) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "SlimeControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "SlimeControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Snowman) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "SnowmanControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "SnowmanControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Spider) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "SpiderControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "SpiderControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Squid) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "SquidControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "SquidControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Villager) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "VillagerControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "VillagerControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Witch) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "WitchControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "WitchControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Wither) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "WitherControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "WitherControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			
		} else if(entity instanceof Wolf) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "WolfControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "WolfControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		} else if(entity instanceof Zombie) {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "ZombieControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "ZombieControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		} else {
			if(target instanceof HumanEntity) {
				if(!getConfigSettings(world, "OtherMobControl", "CanTargetPlayer", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
			if(target instanceof LivingEntity) {
				if(!getConfigSettings(world, "OtherMobControl", "CanTargetMob", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
			}
		}
	}

	@EventHandler
	public void onCreeperPower(CreeperPowerEvent event) {
		World world = event.getEntity().getWorld();
		Entity entity = event.getEntity();
		
		if(getConfigSettings(world, "CreeperControl", "PoweredByLightning", true, true, true, entity.getLocation().getBlockY())) { event.setCancelled(true); }
	}
	
	public void createExplosionSound(World world, Location location) {
		plugin.disableOtherExplosions = true;
		world.createExplosion(location, 0, false);
		plugin.disableOtherExplosions = false;
	}
	
	public boolean getConfigSettings(World world, String controlName, String keyName, boolean def, boolean checkEnabled, boolean checkLevel, int currentLevel) {
		if(checkEnabled) {
			if(!configGetBoolean(world.getName(), controlName, "Enabled", false)) {
				return def;
			}
		}
			
		// Check if the user want to use this only between two layers
		if(checkLevel) {
			if(configGetBoolean(world.getName(), controlName + ".EnableBetweenLevels", "Enabled", false)) {
				String levelsString = configGetString(world.getName(), controlName + ".EnableBetweenLevels", "Levels", "0-256");
				List<String> levelsStringSplitted = Arrays.asList(levelsString.split(","));
				for(String levelsStringItem : levelsStringSplitted) {
					List<String> values = Arrays.asList(levelsStringItem.split("-"));
					if(values.size() == 1) {
						int level = Integer.parseInt(values.get(0));
						if(currentLevel == level) {
							return configGetBoolean(world.getName(), controlName, keyName, def);
						}
					} else if(values.size() == 2) {
						int minLevel = Math.min(Integer.parseInt(values.get(0)), Integer.parseInt(values.get(1)));
						int maxLevel = Math.max(Integer.parseInt(values.get(0)), Integer.parseInt(values.get(1)));
						if(currentLevel >= minLevel && currentLevel <= maxLevel) {
							return configGetBoolean(world.getName(), controlName, keyName, def);
						}
					}
				}
				return def;
			} else {
				return configGetBoolean(world.getName(), controlName, keyName, def);
			}
		} else {
			return configGetBoolean(world.getName(), controlName, keyName, def);
		}
	}
	
	// Some YAML config functions
	public boolean configGetBoolean(String world, String node, String key, boolean def) {
		FileConfiguration globalConfig = plugin.getGlobalConfig();
		
		if(plugin.worldConfigExist(world)) {
			FileConfiguration worldConfig =  plugin.getWorldConfig(world);
			
			boolean globalSetting = globalConfig.getBoolean(node + "." + key, def);
			boolean worldSetting = worldConfig.getBoolean(node + "." + key, globalSetting);
			
			return worldSetting;
		} else {			
			boolean globalSetting = globalConfig.getBoolean(node + "." + key, def);
			return globalSetting;
		}
	}
	
	public int configGetInt(String world, String node, String key, int def) {
		FileConfiguration globalConfig = plugin.getGlobalConfig();
		
		if(plugin.worldConfigExist(world)) {
			FileConfiguration worldConfig =  plugin.getWorldConfig(world);
			
			int globalSetting = globalConfig.getInt(node + "." + key, def);
			int worldSetting = worldConfig.getInt(node + "." + key, globalSetting);
			
			return worldSetting;
		} else {
			int globalSetting = globalConfig.getInt(node + "." + key, def);
			return globalSetting;
		}
	}
	
	public String configGetString(String world, String node, String key, String def) {
		FileConfiguration globalConfig = plugin.getGlobalConfig();
		
		if(plugin.worldConfigExist(world)) {
			FileConfiguration worldConfig =  plugin.getWorldConfig(world);
			
			String globalSetting = globalConfig.getString(node + "." + key, def);
			String worldSetting = worldConfig.getString(node + "." + key, globalSetting);
			
			return worldSetting;
		} else {
			String globalSetting = globalConfig.getString(node + "." + key, def);
			return globalSetting;
		}
	}
}
