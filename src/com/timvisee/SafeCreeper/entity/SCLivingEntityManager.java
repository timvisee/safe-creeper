package com.timvisee.safecreeper.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.timvisee.safecreeper.SafeCreeper;

public class SCLivingEntityManager {
	
	File dataFile;
	
	List<SCLivingEntity> livingEntities = new ArrayList<SCLivingEntity>();
	
	public SCLivingEntityManager() {
		dataFile = new File(SafeCreeper.instance.getDataFolder(), "data/SCEntities.yml");
	}
	
	public void addLivingEntity(SCLivingEntity le) {
		this.livingEntities.add(le);
	}
	
	public void damageLivingEntity(SCLivingEntity le, int damage) {
		le.setHealth(le.getHealth() - damage);
	}
	
	public boolean isSCLivingEntity(LivingEntity le) {
		for(SCLivingEntity scle : this.livingEntities)
			if(scle.equals(le))
				return true;
		return false;
	}
	
	public SCLivingEntity getLivingEntity(LivingEntity le) {
		for(SCLivingEntity scle : this.livingEntities)
			if(scle.equals(le))
				return scle;
		return null;
	}
	
	public List<SCLivingEntity> getLivingEntities() {
		return this.livingEntities;
	}
	
	public void removeOldEntities() {
		List<SCLivingEntity> oldEntities = new ArrayList<SCLivingEntity>();
		for(SCLivingEntity scle : this.livingEntities) {
			if(scle.isOldEntity())
				oldEntities.add(scle);
		}
		this.livingEntities.removeAll(oldEntities);
	}
	
	public int size() {
		return this.livingEntities.size();
	}
	
	public void clear() {
		this.livingEntities.clear();
	}
	
	public void onEntityDamage(EntityDamageEvent event) {
		// The event may not be cancelled
		if(event.isCancelled())
			return;
		
		// Ignore events with a zero or negative damage
		if(event.getDamage() <= 0)
			return;
		
		Entity e = event.getEntity();
		int d = event.getDamage();
		
		// The entity has to be a living entity
		if(e instanceof LivingEntity) {} else
			return;
		
		// Cast the entity to a living entity
		LivingEntity le = (LivingEntity) event.getEntity();
		
		// The living entity has to be a SCLivingEntity
		if(!isSCLivingEntity(le))
			return;
		
		// Set the event damage to zero to prevent the mob from dieing
		event.setDamage(0);
		
		// Damage the mob
		SCLivingEntity scle = getLivingEntity(le);
		scle.damage(d);
		
		// If the mob died, set the damage to the mobs max health, to instantly kill the mob
		if(scle.getHealth() <= 0)
			event.setDamage(le.getMaxHealth());
		
		// Test / Debuging
		// Show some health messages to the player
		/*if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent eventEntity = (EntityDamageByEntityEvent) event;
			Entity damager = eventEntity.getDamager();
			
			if(damager instanceof Projectile) {
				Projectile projectile = (Projectile) damager;
				if(projectile.getShooter() instanceof Player)
					damager = (Player) projectile.getShooter();
			}
			
			if(damager instanceof Player) {
				Player p = (Player) damager;
				if(scle.getHealth() > 0)
					p.sendMessage(ChatColor.GOLD + "Health: " + ChatColor.YELLOW + String.valueOf(scle.getHealth()) + ChatColor.GRAY + " (" + String.valueOf(d) + " damage)");
				else
					p.sendMessage(ChatColor.GOLD + "Health: " + ChatColor.YELLOW + "died" + ChatColor.GRAY + " (" + String.valueOf(d) + " damage)");
			}
		}
		
		if(le instanceof Player) {
			Player p = (Player) le;
			if(scle.getHealth() > 0)
				p.sendMessage(ChatColor.GOLD + "Your Health: " + ChatColor.YELLOW + String.valueOf(scle.getHealth()) + ChatColor.GRAY + " (" + String.valueOf(d) + " damage)");
			else
				p.sendMessage(ChatColor.GOLD + "Your Health: " + ChatColor.YELLOW + "died" + ChatColor.GRAY + " (" + String.valueOf(d) + " damage)");
		}*/
	}
	
	public void onEntityDeath(EntityDeathEvent event) {
		for(SCLivingEntity le : this.livingEntities) {
			if(le.equals(event.getEntity())) {
				this.livingEntities.remove(le);
				return;
			}
		}
	}
	
	public File getDataFile() {
		return this.dataFile;
	}
	
	public void setDataFile(File f) {
		this.dataFile = f;
	}
	
	public void load() {
		load(this.dataFile);
	}
	
	public void load(File f) {
		// Check if the shops file exists
		if(!f.exists()) {
			System.out.println("[SafeCreeper] Entities data file doesn't exist!");
			return;
		}
		
		long t = System.currentTimeMillis();
		
		// Remove all entities
		clear();
		
		// Show an message in the console
		System.out.println("[SafeCreeper] Loading entities data...");
		
		// Load the shops file
		YamlConfiguration c = new YamlConfiguration();
		try {
			c.load(f);
		} catch (FileNotFoundException e) {
			System.out.println("[SafeCreeper] Error while loading entities data file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("[SafeCreeper] Error while loading entities data file!");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			System.out.println("[SafeCreeper] Error while loading entities data file!");
			e.printStackTrace();
		}
		
		// Create a list to put the entity data in
		List<SCLivingEntity> newList = new ArrayList<SCLivingEntity>();
		
		// Get all the items
		Set<String> keys = c.getConfigurationSection("SCLivingEntities").getKeys(false);
		for(String entityId : keys) {
			int entityHealth = c.getInt("SCLivingEntities." + entityId + ".health", 1);
			
			if(entityId == "")
				System.out.println("[SafeCreeper] [ERROR] Invalid entity ID: " + String.valueOf(entityId));
			
			// Cast the living entity to a SCLivingEntity and put it into the new list
			SCLivingEntity curScle = new SCLivingEntity(entityId);
			curScle.setHealth(entityHealth);
			newList.add(curScle);
		}
		
		// Overwrite the current list with the new, loaded list
		this.livingEntities = newList;
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		// Remove all old/died entities
		removeOldEntities();
		
		// Show a message in the console
		if(size() == 1)
			System.out.println("[SafeCreeper] 1 entity loaded, took " + String.valueOf(duration) + "ms!");
		else
			System.out.println("[SafeCreeper] " + String.valueOf(size()) + " entities loaded, took " + String.valueOf(duration) + "ms!");
	}
	
	public void save() {
		save(this.dataFile);
	}
	
	public void save(File f) {
		// Check if the shops file exists
		if(!f.exists()) {
			System.out.println("[SafeCreeper] Entity data file doesn't exists. Creating a new file...");
			
			// Create the parent folders
			(new File(f.getParent())).mkdir();
			
			// Create the file
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		long t = System.currentTimeMillis();
		
		// Show an message in the console
		System.out.println("[SafeCreeper] Saving entities data...");
		
		// Define the new config file holder to put all the shops in
		YamlConfiguration config = new YamlConfiguration();
		
		// Generate blank shops section first, to prevent bugs while loading if no shops where added
		config.createSection("SCLivingEntities");
		
		// Put each entity into the file
		for(SCLivingEntity scle : this.livingEntities) {
			// The entity has to be exists
			if(scle.isOldEntity())
				continue;
			
			// Put all the shop settings into the file
			String entityId = String.valueOf(scle.getEntityId());
			config.set("SCLivingEntities." + entityId + ".health", scle.getHealth());
		}
		
		// Add the version code to the file
		config.set("version", SafeCreeper.instance.getDescription().getVersion());
		
		// Convert the file to a FileConfiguration and safe the file
		FileConfiguration fileConfig = config;
		try {
			fileConfig.save(f);
		} catch (IOException e) {
			System.out.println("[SafeCreeper] Error while saving the entity data file!");
			e.printStackTrace();
			return;
		}

		// Calculate the save size
		int count = config.getConfigurationSection("SCLivingEntities").getKeys(false).size();
		
		// Calculate the save duration
		long duration = System.currentTimeMillis() - t;
		
		// Show an message in the console
		if(count == 1)
			System.out.println("[SafeCreeper] 1 entity saved, took " + String.valueOf(duration) + "ms!");
		else
			System.out.println("[SafeCreeper] " + String.valueOf(count) + " entities saved, took " + String.valueOf(duration) + "ms!");
	}
}
