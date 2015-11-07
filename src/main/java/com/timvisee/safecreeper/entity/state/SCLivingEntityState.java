package com.timvisee.safecreeper.entity.state;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;

public class SCLivingEntityState {
	
	private Collection<PotionEffect> potionEffects;
	private boolean canPickupItems;
	private String customName;
	private EntityEquipment equip;
	private int fireTicks;
	private double health;
	private double maxHealth;
	private Player killer;
	private double lastDmg;
	private EntityDamageEvent lastDmgCause;
	private Location loc;
	private int maxFireTicks;
	private int maxAir;
	private int maxNoDmgTicks;
	private int noDmgTicks;
	private Entity passenger;
	private int remainingAir;
	private boolean removeWhenFarAway;
	private int ticksLived;
	private EntityType type;
	private boolean customNameVisible;
	private boolean death;
	
	/**
	 * Constructor
	 * @param le Living entity
	 */
	public SCLivingEntityState(LivingEntity le) {
		// Store the state of the living entity
		setState(le);
	}
	
	/**
	 * Set the state of the living entity
	 * @param le Living entit
	 */
	public void setState(LivingEntity le) {
		this.potionEffects = le.getActivePotionEffects();
		this.canPickupItems = le.getCanPickupItems();
		this.customName = le.getCustomName();
		this.equip = le.getEquipment();
		this.fireTicks = le.getFireTicks();
		this.health = le.getHealth();
		this.maxHealth = le.getMaxHealth();
		this.killer = le.getKiller();
		this.lastDmg = le.getLastDamage();
		this.lastDmgCause = le.getLastDamageCause();
		this.loc = le.getLocation().clone();
		this.maxFireTicks = le.getMaxFireTicks();
		this.maxAir = le.getMaximumAir();
		this.maxNoDmgTicks = le.getMaximumNoDamageTicks();
		// TODO: Metadata
		this.noDmgTicks = le.getNoDamageTicks();
		this.passenger = le.getPassenger();
		this.remainingAir = le.getRemainingAir();
		this.removeWhenFarAway = le.getRemoveWhenFarAway();
		this.ticksLived = le.getTicksLived();
		this.type = le.getType();
		this.customNameVisible = le.isCustomNameVisible();
		this.death = le.isDead();
	}
	
	public boolean applyState(LivingEntity le) {
		// Make sure the living entity is not null
		if(le == null)
			return false;
		
		// Apply the states
		try {
			le.addPotionEffects(this.potionEffects);
			le.setCanPickupItems(this.canPickupItems);
			le.setCustomName(this.customName);
			// TODO: Equipment
			le.setFireTicks(this.fireTicks);
			le.setMaxHealth(this.maxHealth);
			le.setHealth(this.health);
			le.setCustomNameVisible(this.customNameVisible);
			// TODO: Killer
			// TODO: Is death!?
			le.setLastDamage(this.lastDmg);
			le.setLastDamageCause(this.lastDmgCause);
			le.teleport(this.loc);
			le.setMaximumAir(this.maxAir);
			// TODO: Max fire ticks
			le.setMaximumNoDamageTicks(this.maxNoDmgTicks);
			le.setPassenger(this.passenger);
			le.setRemainingAir(this.remainingAir);
			le.setRemoveWhenFarAway(this.removeWhenFarAway);
			le.setTicksLived(this.ticksLived);
			return true;
			
		} catch(Exception e) {
			return false;
		}
	}
	
	public Collection<PotionEffect> getActivePotionEffects() {
		return this.potionEffects;
	}
	
	public void setActivePotionEffects(Collection<PotionEffect> potionEffects) {
		this.potionEffects = potionEffects;
	}
	
	public boolean getCanPickupItems() {
		return this.canPickupItems;
	}
	
	public void setCanPickupItems(boolean canPickupItems) {
		this.canPickupItems = canPickupItems;
	}
	
	public String getCustomName() {
		return this.customName;
	}
	
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	
	public EntityEquipment getEquipment() {
		return this.equip;
	}
	
	public void setEntityEquipment(EntityEquipment equip) {
		this.equip = equip;
	}
	
	public int getFireTicks() {
		return this.fireTicks;
	}
	
	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}
	
	public double getHealht() {
		return this.health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public double getMaxHealth() {
		return this.maxHealth;
	}
	
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public Player getKiller() {
		return this.killer;
	}
	
	public void setKiller(Player killer) {
		this.killer = killer;
	}
	
	public double getLastDamage() {
		return this.lastDmg;
	}
	
	public void setLastDamage(int lastDmg) {
		this.lastDmg = lastDmg;
	}
	
	public EntityDamageEvent getLastDamageCause() {
		return this.lastDmgCause;
	}
	
	public void setLastDamageCasue(EntityDamageEvent lastDmgCause) {
		this.lastDmgCause = lastDmgCause;
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public int getMaxFireTicks() {
		return this.maxFireTicks;
	}
	
	public void setMaxFireTicks(int maxFireTicks) {
		this.maxFireTicks = maxFireTicks;
	}
	
	public int getMaximumAir() {
		return this.maxAir;
	}
	
	public void setMaximumAir(int maxAir) {
		this.maxAir = maxAir;
	}
	
	public int getMaximumNoDamageTicks() {
		return this.maxNoDmgTicks;
	}
	
	public void setMaximumNoDamageTicks(int maxNoDmgTicks) {
		this.maxNoDmgTicks = maxNoDmgTicks;
	}
	
	public int getNoDamageTicks() {
		return this.noDmgTicks;
	}
	
	public void setNoDamageTicks(int noDmgTicks) {
		this.noDmgTicks = noDmgTicks;
	}
	
	public Entity getPassenger() {
		return this.passenger;
	}
	
	public void setPassenger(Entity passenger) {
		this.passenger = passenger;
	}
	
	public int getRemainingAir() {
		return this.remainingAir;
	}
	
	public void setRemainingAir(int remainingAir) {
		this.remainingAir = remainingAir;
	}
	
	public boolean getRemoveWhenFarAway() {
		return this.removeWhenFarAway;
	}
	
	public void setRemoveWhenFarAway(boolean removeWhenFarAway) {
		this.removeWhenFarAway = removeWhenFarAway;
	}
	
	public int getTicksLived() {
		return this.ticksLived;
	}
	
	public void setTicksLived(int ticksLived) {
		this.ticksLived = ticksLived;
	}
	
	public EntityType getType() {
		return this.type;
	}
	
	public void setEntityType(EntityType type) {
		this.type = type;
	}
	
	public boolean isCustomNameVisible() {
		return this.customNameVisible;
	}
	
	public void setCustomNameVisible(boolean customNameVisible) {
		this.customNameVisible = customNameVisible;
	}
	
	public boolean isDead() {
		return this.death;
	}
	
	public void setDead(boolean dead) {
		this.death = dead;
	}
}
