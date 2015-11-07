package com.timvisee.safecreeper.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.timvisee.safecreeper.SafeCreeper;

public class SCEntityEquipment {

	LivingEntity le;
	EntityEquipment eq;
	
	public SCEntityEquipment(LivingEntity le) {
		this.le = le;
		this.eq = le.getEquipment();
	}
	
	public LivingEntity getLivingEntity() {
		return this.le;
	}
	
	public EntityEquipment getBukkitEquipment() {
		return this.eq;
	}
	
	public SCEntityEquipment getEquipment() {
		return this;
	}
	
	public void copyEquipment(SCEntityEquipment from) {
		setEquipment(from);
	}
	
	public void setEquipment(SCEntityEquipment from) {
		setEquipment(from.getBukkitEquipment());
	}
	
	public void copyEquipment(EntityEquipment from) {
		setEquipment(from);
	}
	
	public void setEquipment(EntityEquipment from) {
		this.eq.setArmorContents(from.getArmorContents());
		this.eq.setBoots(from.getBoots());
		this.eq.setBootsDropChance(from.getBootsDropChance());
		this.eq.setChestplate(from.getChestplate());
		this.eq.setChestplateDropChance(from.getChestplateDropChance());
		this.eq.setHelmet(from.getHelmet());
		this.eq.setHelmetDropChance(from.getHelmetDropChance());
		this.eq.setItemInHand(from.getItemInHand());
		this.eq.setItemInHandDropChance(from.getItemInHandDropChance());
		this.eq.setLeggings(from.getLeggings());
		this.eq.setLeggingsDropChance(from.getLeggingsDropChance());
	}
	
	public boolean applyEquipmentFromConfig() {
		Location l = this.le.getLocation();
		World w = l.getWorld();
		Random rand = new Random();
		
		// Get the current control name
		String controlName = SafeCreeper.instance.getConfigHandler().getControlName(this.le, "OtherMobControl");
		
		// Make sure the control is enabled
		if(!SafeCreeper.instance.getConfigHandler().isControlEnabled(w.getName(), controlName, false, l))
			return false;
		
		// Make sure the CustomEquipment feature is enabled
		if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.Enabled", false, true, l))
			return false;
		
		// Get all equipment sets
		List<String> allSets = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets", new ArrayList<String>(), true, l);
		List<String> sets = new ArrayList<String>();
		for(String set : allSets) {
			// Make sure this set is enabled and the set has a chance greater than zero
			if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.EquipmentSets." + set + ".Enabled", true, true, l))
				if(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + set + ".Chance", 1, true, l) > 0)
					sets.add(set);
		}
		
		// Make sure there's at least one sbet to use
		if(sets.size() <= 0)
			return false;
		
		// Calculate the total sets chance
		int totalSetsChance = 0;
		for(String set : sets) {
			totalSetsChance += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + set + ".Chance", 1, true, l), 0);
		}
		
		// Pick a random set
		int randomSetNumber = rand.nextInt(totalSetsChance) + 1;
		int currentChanceIndex = 0;
		String curSet = "";
		for(String set : sets) {
			currentChanceIndex += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + set + ".Chance", 1, true, l), 0);
			
			if(currentChanceIndex >= randomSetNumber) {
				curSet = set;
				break;
			}
		}
		if(curSet.trim() == "" || curSet == null)
			return false;
		
		/*
		 * Apply the hand items
		 */
		// Get all the hand items
		List<String> allItems = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands", new ArrayList<String>(), true, l);
		List<String> items = new ArrayList<String>();
		for(String item : allItems) {
			// Make sure there's anything inside the hands node, these must be enabled and must have a chance
			if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + item + ".Enabled", true, true, l))
				if(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + item + ".Chance", 1, true, l) > 0)
					items.add(item);
		}
		
		// Make sure there's at least one item in the list
		if(items.size() > 0) {
			// Calculate the total items chance
			int totalItemsChance = 0;
			for(String item : items) {
				totalItemsChance += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + item + ".Chance", 1, true, l), 0);
			}
			
			// Pick a random set
			int randomItemNumber = rand.nextInt(totalItemsChance) + 1;
			currentChanceIndex = 0;
			String curItem = "";
			for(String item : items) {
				currentChanceIndex += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + item + ".Chance", 1, true, l), 0);
				
				if(currentChanceIndex >= randomItemNumber) {
					curItem = item;
					break;
				}
			}
			if(curItem.trim() != "" && curItem != null) {
				// Get all item data
				int itemId = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".ItemId", 0, true, l);
				byte itemData = (byte) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".ItemData", -1, true, l);
				short itemDurability = (short) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".ItemDurability", -1, true, l);
				String itemName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".ItemName", "", true, l);
				float dropChance = (float) ((Math.max(Math.min(SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".DropChance", -1, true, l), 100), -1) / 100));
				
				// Get all enchantments for the item
				List<String> allEnchantments = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".Enchantments", new ArrayList<String>(), true, l);
				List<String> enchantments = new ArrayList<String>();
				for(String enchantment : allEnchantments) {
					// Make sure there's anything inside the hands node, these must be enabled and must have a chance
					if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".Enchantments." + enchantment + ".Enabled", true, true, l)) {
						double enchantmentChance = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".Enchantments." + enchantment + ".Chance", 0, true, l);
						if(enchantmentChance > 0) {
							if(((int) enchantmentChance * 10) > rand.nextInt(1000))
								enchantments.add(enchantment);
						}
					}
				}
				
				ItemStack item;
				if(itemId <= 0)
					item = null;
				else {
					item = new ItemStack(itemId);
					if(itemData >= 0)
						item.setData(new MaterialData(itemId, itemData));
					if(itemDurability >= 0)
						item.setDurability(itemDurability);
					if(itemName.trim() != "") {}
						// Add this!
					
					// Add enchantments to item
					for(String entry : enchantments) {					
						// Cast enchantment to enchantment name thingy!!!!!!!
						String enchantmentName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".Enchantments." + entry + ".Enchantment", "", true, l).trim().toUpperCase().replace(" ", "_");
						int enchantmentLevel = Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".Enchantments." + entry + ".Level", 1, true, l), 1);
						
						if(enchantmentName == "") {
							System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
			    			continue;
			    		}
						
						Enchantment enchantment = Enchantment.getByName(enchantmentName);
			    		
			    		// The enchantment may not be null
			    		if(enchantment == null) {
							System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
			    			continue;
			    		}
			    		
			    		if(item != null)
			    			item.addUnsafeEnchantment(enchantment, enchantmentLevel);
					}
				}	
				
				// Add the item to the living entity equipment
				this.eq.setItemInHand(item);
				if(dropChance >= 0)
					this.eq.setItemInHandDropChance(dropChance);
			}
		}
		
		/*
		 * Appy the head items
		 */
		// Get all the hand items
		allItems = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head", new ArrayList<String>(), true, l);
		items = new ArrayList<String>();
		for(String item : allItems) {
			// Make sure there's anything inside the hands node, these must be enabled and must have a chance
			if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + item + ".Enabled", true, true, l))
				if(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + item + ".Chance", 1, true, l) > 0)
					items.add(item);
		}
		
		// Make sure there's at least one item in the list
		if(items.size() > 0) {
			// Calculate the total items chance
			int totalItemsChance = 0;
			for(String item : items) {
				totalItemsChance += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + item + ".Chance", 1, true, l), 0);
			}
			
			// Pick a random set
			int randomItemNumber = rand.nextInt(totalItemsChance) + 1;
			currentChanceIndex = 0;
			String curItem = "";
			for(String item : items) {
				currentChanceIndex += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + item + ".Chance", 1, true, l), 0);
				
				if(currentChanceIndex >= randomItemNumber) {
					curItem = item;
					break;
				}
			}
			if(curItem.trim() != "" && curItem != null) {
				// Get all item data
				int itemId = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".ItemId", 0, true, l);
				byte itemData = (byte) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".ItemData", -1, true, l);
				short itemDurability = (short) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".ItemDurability", -1, true, l);
				String itemName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".ItemName", "", true, l);
				float dropChance = (float) ((Math.max(Math.min(SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".DropChance", -1, true, l), 100), -1) / 100));
				
				// Get all enchantments for the item
				List<String> allEnchantments = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".Enchantments", new ArrayList<String>(), true, l);
				List<String> enchantments = new ArrayList<String>();
				for(String enchantment : allEnchantments) {
					// Make sure there's anything inside the hands node, these must be enabled and must have a chance
					if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".Enchantments." + enchantment + ".Enabled", true, true, l)) {
						double enchantmentChance = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".Enchantments." + enchantment + ".Chance", 0, true, l);
						if(enchantmentChance > 0) {
							if(((int) enchantmentChance * 10) > rand.nextInt(1000))
								enchantments.add(enchantment);
						}
					}
				}
				
				ItemStack item;
				if(itemId <= 0)
					item = null;
				else {
					item = new ItemStack(itemId);
					if(itemData >= 0)
						item.setData(new MaterialData(itemId, itemData));
					if(itemDurability >= 0)
						item.setDurability(itemDurability);
					if(itemName.trim() != "") {}
						// Add this!
					
					// Add enchantments to item
					for(String entry : enchantments) {					
						// Cast enchantment to enchantment name thingy!!!!!!!
						String enchantmentName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".Enchantments." + entry + ".Enchantment", "", true, l).trim().toUpperCase().replace(" ", "_");
						int enchantmentLevel = Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".Enchantments." + entry + ".Level", 1, true, l), 1);
						
						if(enchantmentName == "") {
							System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
			    			continue;
			    		}
						
						Enchantment enchantment = Enchantment.getByName(enchantmentName);
			    		
			    		// The enchantment may not be null
			    		if(enchantment == null) {
							System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
			    			continue;
			    		}

			    		if(item != null)
			    			item.addUnsafeEnchantment(enchantment, enchantmentLevel);
					}
				}
				
				// Add the item to the living entity equipment
				this.eq.setHelmet(item);
				if(dropChance >= 0)
					this.eq.setHelmetDropChance(dropChance);
			}
		}
		
		/*
		 * Appy the chest items
		 */
		// Get all the hand items
		allItems = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest", new ArrayList<String>(), true, l);
		items = new ArrayList<String>();
		for(String item : allItems) {
			// Make sure there's anything inside the hands node, these must be enabled and must have a chance
			if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + item + ".Enabled", true, true, l))
				if(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + item + ".Chance", 1, true, l) > 0)
					items.add(item);
		}
		
		// Make sure there's at least one item in the list
		if(items.size() > 0) {
			// Calculate the total items chance
			int totalItemsChance = 0;
			for(String item : items) {
				totalItemsChance += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + item + ".Chance", 1, true, l), 0);
			}
			
			// Pick a random set
			int randomItemNumber = rand.nextInt(totalItemsChance) + 1;
			currentChanceIndex = 0;
			String curItem = "";
			for(String item : items) {
				currentChanceIndex += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + item + ".Chance", 1, true, l), 0);
				
				if(currentChanceIndex >= randomItemNumber) {
					curItem = item;
					break;
				}
			}
			if(curItem.trim() != "" && curItem != null) {
				// Get all item data
				int itemId = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".ItemId", 0, true, l);
				byte itemData = (byte) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".ItemData", -1, true, l);
				short itemDurability = (short) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".ItemDurability", -1, true, l);
				String itemName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".ItemName", "", true, l);
				float dropChance = (float) ((Math.max(Math.min(SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".DropChance", -1, true, l), 100), -1) / 100));
				
				// Get all enchantments for the item
				List<String> allEnchantments = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".Enchantments", new ArrayList<String>(), true, l);
				List<String> enchantments = new ArrayList<String>();
				for(String enchantment : allEnchantments) {
					// Make sure there's anything inside the hands node, these must be enabled and must have a chance
					if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".Enchantments." + enchantment + ".Enabled", true, true, l)) {
						double enchantmentChance = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".Enchantments." + enchantment + ".Chance", 0, true, l);
						if(enchantmentChance > 0) {
							if(((int) enchantmentChance * 10) > rand.nextInt(1000))
								enchantments.add(enchantment);
						}
					}
				}
				
				ItemStack item;
				if(itemId <= 0)
					item = null;
				else {
					item = new ItemStack(itemId);
					if(itemData >= 0)
						item.setData(new MaterialData(itemId, itemData));
					if(itemDurability >= 0)
						item.setDurability(itemDurability);
					if(itemName.trim() != "") {}
						// Add this!
					
					// Add enchantments to item
					for(String entry : enchantments) {					
						// Cast enchantment to enchantment name thingy!!!!!!!
						String enchantmentName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".Enchantments." + entry + ".Enchantment", "", true, l).trim().toUpperCase().replace(" ", "_");
						int enchantmentLevel = Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".Enchantments." + entry + ".Level", 1, true, l), 1);
						
						if(enchantmentName == "") {
							System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
			    			continue;
			    		}
						
						Enchantment enchantment = Enchantment.getByName(enchantmentName);
			    		
			    		// The enchantment may not be null
			    		if(enchantment == null) {
							System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
			    			continue;
			    		}

			    		if(item != null)
			    			item.addUnsafeEnchantment(enchantment, enchantmentLevel);
					}
				}
				
				// Add the item to the living entity equipment
				this.eq.setChestplate(item);
				if(dropChance >= 0)
					this.eq.setChestplateDropChance(dropChance);
			}
		}
		
		/*
		 * Appy the leg items
		 */
		// Get all the hand items
		allItems = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs", new ArrayList<String>(), true, l);
		items = new ArrayList<String>();
		for(String item : allItems) {
			// Make sure there's anything inside the hands node, these must be enabled and must have a chance
			if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + item + ".Enabled", true, true, l))
				if(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + item + ".Chance", 1, true, l) > 0)
					items.add(item);
		}
		
		// Make sure there's at least one item in the list
		if(items.size() > 0) {
			// Calculate the total items chance
			int totalItemsChance = 0;
			for(String item : items) {
				totalItemsChance += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + item + ".Chance", 1, true, l), 0);
			}
			
			// Pick a random set
			int randomItemNumber = rand.nextInt(totalItemsChance) + 1;
			currentChanceIndex = 0;
			String curItem = "";
			for(String item : items) {
				currentChanceIndex += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + item + ".Chance", 1, true, l), 0);
				
				if(currentChanceIndex >= randomItemNumber) {
					curItem = item;
					break;
				}
			}
			if(curItem.trim() != "" && curItem != null) {
				// Get all item data
				int itemId = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".ItemId", 0, true, l);
				byte itemData = (byte) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".ItemData", -1, true, l);
				short itemDurability = (short) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".ItemDurability", -1, true, l);
				String itemName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".ItemName", "", true, l);
				float dropChance = (float) ((Math.max(Math.min(SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".DropChance", -1, true, l), 100), -1) / 100));
				
				// Get all enchantments for the item
				List<String> allEnchantments = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".Enchantments", new ArrayList<String>(), true, l);
				List<String> enchantments = new ArrayList<String>();
				for(String enchantment : allEnchantments) {
					// Make sure there's anything inside the hands node, these must be enabled and must have a chance
					if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".Enchantments." + enchantment + ".Enabled", true, true, l)) {
						double enchantmentChance = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".Enchantments." + enchantment + ".Chance", 0, true, l);
						if(enchantmentChance > 0) {
							if(((int) enchantmentChance * 10) > rand.nextInt(1000))
								enchantments.add(enchantment);
						}
					}
				}
				
				ItemStack item;
				if(itemId <= 0)
					item = null;
				else {
					item = new ItemStack(itemId);
					if(itemData >= 0)
						item.setData(new MaterialData(itemId, itemData));
					if(itemDurability >= 0)
						item.setDurability(itemDurability);
					if(itemName.trim() != "") {}
						// Add this!
					
					// Add enchantments to item
					for(String entry : enchantments) {					
						// Cast enchantment to enchantment name thingy!!!!!!!
						String enchantmentName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".Enchantments." + entry + ".Enchantment", "", true, l).trim().toUpperCase().replace(" ", "_");
						int enchantmentLevel = Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".Enchantments." + entry + ".Level", 1, true, l), 1);
						
						if(enchantmentName == "") {
							System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
			    			continue;
			    		}
						
						Enchantment enchantment = Enchantment.getByName(enchantmentName);
			    		
			    		// The enchantment may not be null
			    		if(enchantment == null) {
							System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
			    			continue;
			    		}

			    		if(item != null)
			    			item.addUnsafeEnchantment(enchantment, enchantmentLevel);
					}
				}
				
				// Add the item to the living entity equipment
				this.eq.setLeggings(item);
				if(dropChance >= 0)
					this.eq.setLeggingsDropChance(dropChance);
			}
		}
		
		/*
		 * Appy the feet items
		 */
		// Get all the hand items
		allItems = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet", new ArrayList<String>(), true, l);
		items = new ArrayList<String>();
		for(String item : allItems) {
			// Make sure there's anything inside the hands node, these must be enabled and must have a chance
			if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + item + ".Enabled", true, true, l))
				if(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + item + ".Chance", 1, true, l) > 0)
					items.add(item);
		}
		
		// Make sure there's at least one item in the list
		if(items.size() > 0) {
			// Calculate the total items chance
			int totalItemsChance = 0;
			for(String item : items) {
				totalItemsChance += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + item + ".Chance", 1, true, l), 0);
			}
			
			// Pick a random set
			int randomItemNumber = rand.nextInt(totalItemsChance) + 1;
			currentChanceIndex = 0;
			String curItem = "";
			for(String item : items) {
				currentChanceIndex += Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + item + ".Chance", 1, true, l), 0);
				
				if(currentChanceIndex >= randomItemNumber) {
					curItem = item;
					break;
				}
			}
			if(curItem.trim() != "" && curItem != null) {
				// Get all item data
				int itemId = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".ItemId", 0, true, l);
				byte itemData = (byte) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".ItemData", -1, true, l);
				short itemDurability = (short) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".ItemDurability", -1, true, l);
				String itemName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".ItemName", "", true, l);
				float dropChance = (float) ((Math.max(Math.min(SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".DropChance", -1, true, l), 100), -1) / 100));
				
				// Get all enchantments for the item
				List<String> allEnchantments = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".Enchantments", new ArrayList<String>(), true, l);
				List<String> enchantments = new ArrayList<String>();
				for(String enchantment : allEnchantments) {
					// Make sure there's anything inside the hands node, these must be enabled and must have a chance
					if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".Enchantments." + enchantment + ".Enabled", true, true, l)) {
						double enchantmentChance = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".Enchantments." + enchantment + ".Chance", 0, true, l);
						if(enchantmentChance > 0) {
							if(((int) enchantmentChance * 10) > rand.nextInt(1000))
								enchantments.add(enchantment);
						}
					}
				}
				
				ItemStack item;
				if(itemId <= 0)
					item = null;
				else {
					item = new ItemStack(itemId);
					if(itemData >= 0)
						item.setData(new MaterialData(itemId, itemData));
					if(itemDurability >= 0)
						item.setDurability(itemDurability);
					if(itemName.trim() != "") {}
						// Add this!
					
					// Add enchantments to item
					for(String entry : enchantments) {					
						// Cast enchantment to enchantment name thingy!!!!!!!
						String enchantmentName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".Enchantments." + entry + ".Enchantment", "", true, l).trim().toUpperCase().replace(" ", "_");
						int enchantmentLevel = Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".Enchantments." + entry + ".Level", 1, true, l), 1);
						
						if(enchantmentName == "") {
							System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
			    			continue;
			    		}
						
						Enchantment enchantment = Enchantment.getByName(enchantmentName);
			    		
			    		// The enchantment may not be null
			    		if(enchantment == null) {
							System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
			    			continue;
			    		}

			    		if(item != null)
			    			item.addUnsafeEnchantment(enchantment, enchantmentLevel);
					}
				}
				
				// Add the item to the living entity equipment
				this.eq.setBoots(item);
				if(dropChance >= 0)
					this.eq.setBootsDropChance(dropChance);
			}
		}
		
		return false;
	}
}
