package com.timvisee.safecreeper.util;

import com.timvisee.safecreeper.SafeCreeper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SCEntityEquipment {

    private LivingEntity livingEntity;
    private EntityEquipment entityEquipment;

    public SCEntityEquipment(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
        this.entityEquipment = livingEntity.getEquipment();
    }

    public LivingEntity getLivingEntity() {
        return this.livingEntity;
    }

    public EntityEquipment getBukkitEquipment() {
        return this.entityEquipment;
    }

    public SCEntityEquipment getEquipment() {
        return this;
    }

    public void setEquipment(EntityEquipment from) {
        this.entityEquipment.setArmorContents(from.getArmorContents());
        this.entityEquipment.setBoots(from.getBoots());
        this.entityEquipment.setBootsDropChance(from.getBootsDropChance());
        this.entityEquipment.setChestplate(from.getChestplate());
        this.entityEquipment.setChestplateDropChance(from.getChestplateDropChance());
        this.entityEquipment.setHelmet(from.getHelmet());
        this.entityEquipment.setHelmetDropChance(from.getHelmetDropChance());
        this.entityEquipment.setItemInHand(from.getItemInHand());
        this.entityEquipment.setItemInHandDropChance(from.getItemInHandDropChance());
        this.entityEquipment.setLeggings(from.getLeggings());
        this.entityEquipment.setLeggingsDropChance(from.getLeggingsDropChance());
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

    public boolean applyEquipmentFromConfig() {
        Location l = this.livingEntity.getLocation();
        World w = l.getWorld();
        Random rand = new Random();

        // Get the current control name
        String controlName = SafeCreeper.instance.getConfigHandler().getControlName(this.livingEntity, "OtherMobControl");

        // Make sure the control is enabled
        if(!SafeCreeper.instance.getConfigHandler().isControlEnabled(w.getName(), controlName, false, l))
            return false;

        // Make sure the CustomEquipment feature is enabled
        if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CustomEquipment.Enabled", false, true, l))
            return false;

        // Get all equipment sets
        List<String> allSets = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets", new ArrayList<>(), true, l);
        List<String> sets = new ArrayList<>();
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
        if(Objects.equals(curSet.trim(), ""))
            return false;

        /*
         * Apply the hand items
        */
        // Get all the hand items
        List<String> allItems = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands", new ArrayList<>(), true, l);
        List<String> items = new ArrayList<>();
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
            if(!Objects.equals(curItem.trim(), "")) {
                // Get all item data
                int itemId = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".ItemId", 0, true, l);
                byte itemData = (byte) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".ItemData", -1, true, l);
                short itemDurability = (short) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".ItemDurability", -1, true, l);
                float dropChance = (float) ((Math.max(Math.min(SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".DropChance", -1, true, l), 100), -1) / 100));

                // Get all enchantments for the item
                List<String> allEnchantments = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".Enchantments", new ArrayList<>(), true, l);
                List<String> enchantments = new ArrayList<>();
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

                    // Add enchantments to item
                    for(String entry : enchantments) {
                        // Cast enchantment to enchantment name thingy!!!!!!!
                        String enchantmentName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".Enchantments." + entry + ".Enchantment", "", true, l).trim().toUpperCase().replace(" ", "_");
                        int enchantmentLevel = Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Hands." + curItem + ".Enchantments." + entry + ".Level", 1, true, l), 1);

                        if(Objects.equals(enchantmentName, "")) {
                            System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
                            continue;
                        }

                        Enchantment enchantment = Enchantment.getByName(enchantmentName);

                        // The enchantment may not be null
                        if(enchantment == null) {
                            System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
                            continue;
                        }

                        item.addUnsafeEnchantment(enchantment, enchantmentLevel);
                    }
                }

                // Add the item to the living entity equipment
                this.entityEquipment.setItemInHand(item);
                if(dropChance >= 0)
                    this.entityEquipment.setItemInHandDropChance(dropChance);
            }
        }

        /*
         * Apply the head items
         */
        // Get all the hand items
        allItems = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head", new ArrayList<>(), true, l);
        items = new ArrayList<>();
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
            if(!Objects.equals(curItem.trim(), "")) {
                // Get all item data
                int itemId = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".ItemId", 0, true, l);
                byte itemData = (byte) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".ItemData", -1, true, l);
                short itemDurability = (short) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".ItemDurability", -1, true, l);
                float dropChance = (float) ((Math.max(Math.min(SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".DropChance", -1, true, l), 100), -1) / 100));

                // Get all enchantments for the item
                List<String> allEnchantments = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".Enchantments", new ArrayList<>(), true, l);
                List<String> enchantments = new ArrayList<>();
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

                    // Add enchantments to item
                    for(String entry : enchantments) {
                        // Cast enchantment to enchantment name thingy!!!!!!!
                        String enchantmentName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".Enchantments." + entry + ".Enchantment", "", true, l).trim().toUpperCase().replace(" ", "_");
                        int enchantmentLevel = Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Head." + curItem + ".Enchantments." + entry + ".Level", 1, true, l), 1);

                        if(Objects.equals(enchantmentName, "")) {
                            System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
                            continue;
                        }

                        Enchantment enchantment = Enchantment.getByName(enchantmentName);

                        // The enchantment may not be null
                        if(enchantment == null) {
                            System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
                            continue;
                        }

                        item.addUnsafeEnchantment(enchantment, enchantmentLevel);
                    }
                }

                // Add the item to the living entity equipment
                this.entityEquipment.setHelmet(item);
                if(dropChance >= 0)
                    this.entityEquipment.setHelmetDropChance(dropChance);
            }
        }
		
		/*
		 * Appy the chest items
		 */
        // Get all the hand items
        allItems = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest", new ArrayList<>(), true, l);
        items = new ArrayList<>();
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
            if(!Objects.equals(curItem.trim(), "")) {
                // Get all item data
                int itemId = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".ItemId", 0, true, l);
                byte itemData = (byte) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".ItemData", -1, true, l);
                short itemDurability = (short) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".ItemDurability", -1, true, l);
                float dropChance = (float) ((Math.max(Math.min(SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".DropChance", -1, true, l), 100), -1) / 100));

                // Get all enchantments for the item
                List<String> allEnchantments = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".Enchantments", new ArrayList<>(), true, l);
                List<String> enchantments = new ArrayList<>();
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

                    // Add enchantments to item
                    for(String entry : enchantments) {
                        // Cast enchantment to enchantment name thingy!!!!!!!
                        String enchantmentName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".Enchantments." + entry + ".Enchantment", "", true, l).trim().toUpperCase().replace(" ", "_");
                        int enchantmentLevel = Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Chest." + curItem + ".Enchantments." + entry + ".Level", 1, true, l), 1);

                        if(Objects.equals(enchantmentName, "")) {
                            System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
                            continue;
                        }

                        Enchantment enchantment = Enchantment.getByName(enchantmentName);

                        // The enchantment may not be null
                        if(enchantment == null) {
                            System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
                            continue;
                        }

                        item.addUnsafeEnchantment(enchantment, enchantmentLevel);
                    }
                }

                // Add the item to the living entity equipment
                this.entityEquipment.setChestplate(item);
                if(dropChance >= 0)
                    this.entityEquipment.setChestplateDropChance(dropChance);
            }
        }
		
		/*
		 * Appy the leg items
		 */
        // Get all the hand items
        allItems = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs", new ArrayList<>(), true, l);
        items = new ArrayList<>();
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
            if(!Objects.equals(curItem.trim(), "")) {
                // Get all item data
                int itemId = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".ItemId", 0, true, l);
                byte itemData = (byte) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".ItemData", -1, true, l);
                short itemDurability = (short) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".ItemDurability", -1, true, l);
                float dropChance = (float) ((Math.max(Math.min(SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".DropChance", -1, true, l), 100), -1) / 100));

                // Get all enchantments for the item
                List<String> allEnchantments = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".Enchantments", new ArrayList<>(), true, l);
                List<String> enchantments = new ArrayList<>();
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

                    // Add enchantments to item
                    for(String entry : enchantments) {
                        // Cast enchantment to enchantment name thingy!!!!!!!
                        String enchantmentName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".Enchantments." + entry + ".Enchantment", "", true, l).trim().toUpperCase().replace(" ", "_");
                        int enchantmentLevel = Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Legs." + curItem + ".Enchantments." + entry + ".Level", 1, true, l), 1);

                        if(Objects.equals(enchantmentName, "")) {
                            System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
                            continue;
                        }

                        Enchantment enchantment = Enchantment.getByName(enchantmentName);

                        // The enchantment may not be null
                        if(enchantment == null) {
                            System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
                            continue;
                        }

                        item.addUnsafeEnchantment(enchantment, enchantmentLevel);
                    }
                }

                // Add the item to the living entity equipment
                this.entityEquipment.setLeggings(item);
                if(dropChance >= 0)
                    this.entityEquipment.setLeggingsDropChance(dropChance);
            }
        }
		
		/*
		 * Appy the feet items
		 */
        // Get all the hand items
        allItems = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet", new ArrayList<>(), true, l);
        items = new ArrayList<>();
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
            if(!Objects.equals(curItem.trim(), "")) {
                // Get all item data
                int itemId = SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".ItemId", 0, true, l);
                byte itemData = (byte) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".ItemData", -1, true, l);
                short itemDurability = (short) SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".ItemDurability", -1, true, l);
                float dropChance = (float) ((Math.max(Math.min(SafeCreeper.instance.getConfigHandler().getOptionDouble(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".DropChance", -1, true, l), 100), -1) / 100));

                // Get all enchantments for the item
                List<String> allEnchantments = SafeCreeper.instance.getConfigHandler().getOptionKeysList(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".Enchantments", new ArrayList<>(), true, l);
                List<String> enchantments = new ArrayList<>();
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

                    // Add enchantments to item
                    for(String entry : enchantments) {
                        // Cast enchantment to enchantment name thingy!!!!!!!
                        String enchantmentName = SafeCreeper.instance.getConfigHandler().getOptionString(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".Enchantments." + entry + ".Enchantment", "", true, l).trim().toUpperCase().replace(" ", "_");
                        int enchantmentLevel = Math.max(SafeCreeper.instance.getConfigHandler().getOptionInt(w, controlName, "CustomEquipment.EquipmentSets." + curSet + ".Feet." + curItem + ".Enchantments." + entry + ".Level", 1, true, l), 1);

                        if(Objects.equals(enchantmentName, "")) {
                            System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
                            continue;
                        }

                        Enchantment enchantment = Enchantment.getByName(enchantmentName);

                        // The enchantment may not be null
                        if(enchantment == null) {
                            System.out.println("[SafeCreeper] [ERROR] Unknown enchantment: " + enchantmentName);
                            continue;
                        }

                        item.addUnsafeEnchantment(enchantment, enchantmentLevel);
                    }
                }

                // Add the item to the living entity equipment
                this.entityEquipment.setBoots(item);
                if(dropChance >= 0)
                    this.entityEquipment.setBootsDropChance(dropChance);
            }
        }

        return false;
    }
}
