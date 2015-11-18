package com.timvisee.safecreeper.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.timvisee.safecreeper.block.state.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Jukebox;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.block.SCRepairableBlock;
import com.timvisee.safecreeper.util.SCAttachedBlock;

public class SCDestructionRepairManager {
	
	private List<SCRepairableBlock> blocks = new ArrayList<SCRepairableBlock>();
	
	/**
	 * Constructor
	 */
	public SCDestructionRepairManager() { }

	/**
	 * Add a list of blocks that should be repaired.
	 *
	 * @param blocks List of block states to repair.
	 * @param listDelay Time in seconds to start repairing the first block of the list.
	 * @param blockDelay Delay between repairing blocks in seconds.
	 */
	public void addBlocks(List<Block> blocks, double listDelay, double blockDelay) {
		List<SCBlockState> states = new ArrayList<>();

		for(Block b : blocks) {
            // Don't rebuild TNT blocks
			if(b.getType().equals(Material.TNT))
				continue;
			
			if(b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN))
				states.add(new SCSignState(b));
            else if(b.getType().equals(Material.BEACON))
                states.add(new SCBeaconState(b));
			else if(b.getState() instanceof InventoryHolder)
				states.add(new SCContainerBlockState(b));
			else if(b.getType().equals(Material.MOB_SPAWNER))
				states.add(new SCSpawnerState(b));
			else if(b.getType().equals(Material.JUKEBOX))
				states.add(new SCJukeboxState(b));
			else if(b.getType().equals(Material.COMMAND))
				states.add(new SCCommandBlockState(b));
			else if(b.getType().equals(Material.SKULL))
				states.add(new SCSkullState(b));
			else
				states.add(new SCBlockState(b));
			
			// Clear the inventory of the containers and the jukebox, so the content won't drop
			if(b.getState() instanceof InventoryHolder) {
				Inventory inv = ((InventoryHolder) b.getState()).getInventory();
				inv.clear();
			} else if(b.getType().equals(Material.JUKEBOX)) {
				Jukebox j = ((Jukebox) b.getState());
				j.setPlaying(null);
			}
		}
		
		// Add the block states
		addBlockStates(states, listDelay, blockDelay);
	}
	
	/**
	 * Add a blocks to repair
	 * @param b List of block states to repair
	 * @param delay Time in seconds to start repairing the first block
	 * @param delay Delay between reparing blocks in seconds
	 */
	public void addBlock(Block b, double delay) {
		List<Block> blocks = new ArrayList<Block>();
		blocks.add(b);
		
		addBlocks(blocks, delay, 1);
	}
	
	/**
	 * Add a list of blocks that should be repaired
	 * @param blockStates List of block states to repair
	 * @param listDelay Time in seconds to start repairing the first block
	 * @param blockDelay Delay between reparing blocks in seconds
	 */
	public void addBlockStates(List<SCBlockState> blockStates, double listDelay, double blockDelay) {
		Collections.shuffle(blockStates);
		Collections.sort(blockStates, new CustomComparator());
		
		long timestamp = System.currentTimeMillis();
		long time = timestamp + (int) (listDelay * 1000);
		
		for(SCBlockState bs : blockStates) {
			this.blocks.add(new SCRepairableBlock(bs, time));
			
			time += (blockDelay * 1000);
		}
	}
	
	/**
	 * Get the block list
	 * @return Block list
	 */
	public List<SCRepairableBlock> getBlockList() {
		return this.blocks;
	}
	
	/**
	 * Repair all blocks that should be repaired, based on their repair time
	 */
	public void repair() {
		long timestamp = System.currentTimeMillis();
		
		for(int i = 0; i < this.blocks.size(); i++) {
			SCRepairableBlock b = this.blocks.get(i);
			
			// The chunk the block is in has to be loaded
			if(!b.isChunkLoaded())
				continue;
			
			Material type = Material.getMaterial(b.getBlockState().getTypeId());
			
			if(b.getRepairAt() <= timestamp) {

				this.blocks.remove(i);
				i--;
				
				if(SCAttachedBlock.isAttached(type))
					repairBlocks(SCAttachedBlock.getBlockBase(b.getBlock(), type, b.getBlockState().getData()));
				
				// Drop the old block on that location
				b.getBlock().breakNaturally();
				
				// Repair/replace the block
				b.repair();
				
				// Unstuck all living entities
				unstuckLivingEntities(b.getBlock());
			}
		}
	}
	
	/**
	 * Immediately repair a specific block
	 * @param b Block to repair immediately
	 */
	public void repairBlock(Block b) {
		// Make sure the block is not null
		if(b == null)
			return;
		
		for(int i = 0; i < this.blocks.size(); i++) {
			SCRepairableBlock entry = this.blocks.get(i);
			
			// Check if this is the block it's all about
			if(entry.getBlock().equals(b)) {
				
				// Remove the block form the list
				this.blocks.remove(i);
				i--;
				
				// Get the material of the block
				Material type = Material.getMaterial(entry.getBlockState().getTypeId());
				
				// Check if this block was attached to another block, if so repair the base block first
				if(SCAttachedBlock.isAttached(type))
					repairBlocks(SCAttachedBlock.getBlockBase(entry.getBlock(), type, entry.getBlockState().getData()));

				// Drop the old block on that location
				b.breakNaturally();
				
				// Repair/replace the block
				entry.repair();
				
				// Unstuck all living entities
				unstuckLivingEntities(b);
				
				// Return
				return;
			}
		}
	}
	
	/**
	 * Immediately repair a list of blocks
	 * @param b List of blocks to repair
	 */
	public void repairBlocks(List<Block> b) {
		// Make sure the list is not null
		if(b == null)
			return;
		
		// Repair each block from the list
		for(Block entry : b)
			repairBlock(entry);
	}
	
	/**
	 * Repair all blocks
	 */
	public void repairAll() {
		// Repair all blocks
		for(int i = 0; i < this.blocks.size(); i++) {
			SCRepairableBlock entry = this.blocks.get(i);
				
			// Remove the block form the list
			this.blocks.remove(i);
			i--;
			
			// Get the material of the block
			Material type = Material.getMaterial(entry.getBlockState().getTypeId());
			
			// Check if this block was attached to another block, if so repair the base block first
			if(SCAttachedBlock.isAttached(type))
				repairBlocks(SCAttachedBlock.getBlockBase(entry.getBlock(), type, entry.getBlockState().getData()));

			// Drop the old block on that location
			entry.getBlock().breakNaturally();
			
			// Repair/replace the block
			entry.repair();
			
			// Unstuck all living entities
			unstuckLivingEntities(entry.getBlock());
		}
	}
	
	/**
	 * Is a block destroyed
	 * @param b The block to check for
	 * @return True if this block was destroyed and is going to be repared
	 */
	public boolean isDestroyed(Block b) {
		if(b == null)
			return false;
		
		for(SCRepairableBlock entry : this.blocks)
			if(entry.getBlock().equals(b))
				return true;
		return false;
	}
	
	/**
	 * Unstuck living entites from a block
	 * @param b The block that is going to be repaired
	 */
	public void unstuckLivingEntities(Block b) {
		List<Entity> entities = b.getWorld().getEntities();
		
		Block b2 = b.getRelative(BlockFace.UP);
		
		// Loop through the list of enties and check if the entity is in the way
		for(Entity e : entities) {
			if(e instanceof LivingEntity) { } else
				continue;
			
			// Get the living entity
			LivingEntity le = (LivingEntity) e;
			
			// Get the block location
			Block leb = le.getLocation().getBlock();
		
			// Is the living entity inside the block
			if(!leb.equals(b) && !leb.equals(b2))
				continue;
			
			final int x = b.getX();
			final int z = b.getZ();
			
			// The living entity is stuck, move the entity
			for(int y = b.getY(); y < 256; y++) {
				Block newb = b.getWorld().getBlockAt(x, y, z);
				
				if(!newb.getType().equals(Material.AIR))
					continue;
				
				if(y < 256) {
					Block newb2 = b.getWorld().getBlockAt(x, y + 1, z);
					
					if(!newb2.getType().equals(Material.AIR))
						continue;
				}
				
				Location newLoc = le.getLocation();
				newLoc.setY(y + 0.1);
				
				le.teleport(newLoc);
				break;
			}
		}
	}
	
	/**
	 * Clear the list of blocks
	 */
	public void clear() {
		this.blocks.clear();
	}
	
	/**
	 * Save the data
	 */
	public void save() {
		save(true);
	}
	
	/**
	 * Save the data
	 * @param showMsg boolean Show a message in the console
	 */
	public void save(boolean showMsg) {
		File f = new File(SafeCreeper.instance.getDataFolder(), "data/destruction_repair/blocks.yml");
		save(f, showMsg);
	}
	
	/**
	 * Save the data
	 * @param f File to save the data to
	 */
	public void save(File f) {
		save(f, true);
	}
	
	/**
	 * Save the data
	 * @param f File to save the data to
	 * @param showMsg boolean True to show a message in the console
	 */
	public void save(File f, boolean showMsg) {
		// Check if the file exists
		if(!f.exists())
			System.out.println("[SafeCreeper] Destruction repair data file does not exist. Creating a new file...");
		
		long t = System.currentTimeMillis();
		
		// Show an message in the console
		if(showMsg)
			System.out.println("[SafeCreeper] Saving destruction repair data...");
		
		// Define the new config file holder to put all the data in
		YamlConfiguration config = new YamlConfiguration();
		
		// Generate blank shops section first, to prevent bugs while loading if no shops where added
		config.createSection("blocks");
		
		// Store the current index
		int i = 0;
		
		// Put each block in the file
		for(SCRepairableBlock b : this.blocks) {
			// Get the index to use
			String indexStr = String.valueOf(i);
			
			// Create the section for the current repairable block
			ConfigurationSection blockSection = config.createSection("blocks." + indexStr);
			
			// Store the reparable block
			b.save(blockSection);
			
			// Increase the index
			i++;
		}
		
		final String scVer = SafeCreeper.instance.getVersionName();
		
		// Add the version code to the file
		config.set("version", scVer);
		
		// Add an information line to the top of the data file
		config.options().header("Safe Creeper Destruction Repair Data - Automaticly saved by Safe Creeper v" + scVer + ". Do not modify this file!");
		
		// Convert the file to a FileConfiguration and safe the file
		FileConfiguration fileConfig = config;
		try {
			fileConfig.save(f);
		} catch (IOException e) {
			System.out.println("[SafeCreeper] Error while saving destruction repair data!");
			e.printStackTrace();
			return;
		}

		// Calculate the save duration
		long duration = System.currentTimeMillis() - t;
		
		// Show an message in the console
		if(showMsg)
			System.out.println("[SafeCreeper] Destruction repair data saved, took " + String.valueOf(duration) + "ms!");
	}
	
	/**
	 * Load the old data list from an external file
	 */
	public void load() {
		File f = new File(SafeCreeper.instance.getDataFolder(), "data/destruction_repair/blocks.yml");
		load(f);
	}
	
	/**
	 * Load the old data list from an external file
	 * @param f external file to load the data from
	 */
	public void load(File f) {		
		// Check if the fiel exists
		if(!f.exists()) {
			System.out.println("[SafeCreeper] Destruction repair data file doesn't exist!");
			return;
		}
		
		long t = System.currentTimeMillis();
		
		// Show an message in the console
		System.out.println("[SafeCreeper] Loading destruction repair data...");
		
		// Load the shops file
		YamlConfiguration c = new YamlConfiguration();
		try {
			c.load(f);
		} catch (FileNotFoundException e) {
			System.out.println("[SafeCreeper] Error while loading destruction repair data file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("[SafeCreeper] Error while loading destruction repair data file!");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			System.out.println("[SafeCreeper] Error while loading destruction repair data file!");
			e.printStackTrace();
		}
		
		// Initialize the new list to store the loaded data in
		List<SCRepairableBlock> newBlocks = new ArrayList<SCRepairableBlock>();
		
		// Get all the items
		Set<String> keys = c.getConfigurationSection("blocks").getKeys(false);
		for(String bIndex : keys) {
			
			// Get the configuration section of the block
			ConfigurationSection blockSection = c.getConfigurationSection("blocks." + bIndex);
			
			// Make sure the section not null
			if(blockSection == null) {
				// Show an error message in the console
				SafeCreeper.instance.getLogger().info("Unable to load block from destruction repair data!");
				continue;
			}
			
			// Load the block
			SCRepairableBlock rb = SCRepairableBlock.load(blockSection);
			
			// Make sure the repairable block is not null
			if(rb == null) {
				// Show an error message in the console
				SafeCreeper.instance.getLogger().info("Unable to load block from destruction repair data!");
				continue;
			}
			
			// Add the block to the list
			newBlocks.add(rb);
		}
		
		// Clear the current list of repairable blocks
		clear();
		
		// Store the new list
		this.blocks = newBlocks;
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		// Show a message in the console
		System.out.println("[SafeCreeper] Destruction repair data loaded, took " + String.valueOf(duration) + "ms!");
	}
	
	/**
	 * Comparator to sort a list of blocks based on it's y coord
	 * @author Tim Vis�e
	 */
	public class CustomComparator implements Comparator<SCBlockState> {
		@Override
	    public int compare(SCBlockState b1, SCBlockState b2) {
	        return Integer.valueOf(b1.getY()).compareTo(Integer.valueOf(b2.getY()));
	    }
	}
}
