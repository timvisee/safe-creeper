package com.timvisee.safecreeper.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SCBlock {
	
	private Block b;
	
	/**
	 * Constructor
	 * @param b The block
	 */
	public SCBlock(Block b) {
		this.b = b;
	}
	
	/**
	 * Constructor
	 * @param b The location of the block
	 */
	public SCBlock(Location loc) {
		this.b = loc.getBlock();
	}
	
	/**
	 * Get the block
	 * @return Block
	 */
	public Block getBlock() {
		return this.b;
	}
	
	/**
	 * Get the world the block is in
	 * @return World the block is in
	 */
	public World getWorld() {
		return this.b.getWorld();
	}
	
	/**
	 * Get the name of the world the block is in
	 * @return
	 */
	public String getWorldName() {
		return getWorld().getName();
	}
	
	/**
	 * Get the location of the block
	 * @return Block loation
	 */
	public Location getLocation() {
		return this.b.getLocation();
	}
	
	// TODO: setLocation(Location loc);
	
	/**
	 * Get the x coord of the block
	 * @return x coord
	 */
	public int getX() {
		return this.b.getX();
	}
	
	// TODO: setX(int x);
	
	/**
	 * Get the y coord of the block
	 * @return y coord
	 */
	public int getY() {
		return this.b.getY();
	}
	
	// TODO: setY(int y);
	
	/**
	 * Get the z coord of the block
	 * @return z coord
	 */
	public int getZ() {
		return this.b.getZ();
	}
	
	// TODO: setZ(int z);
	
	/**
	 * Get the blocks material type
	 * @return Block material type
	 */
	public Material getType() {
		return this.b.getType();
	}
	
	/**
	 * Set the type of the block
	 * @param type Block type
	 */
	public void setType(Material type) {
		this.b.setType(type);
	}
	
	/**
	 * Get the blocks type ID
	 * @return Type ID
	 */
	public int getTypeId() {
		return this.b.getTypeId();
	}
	
	/**
	 * Set the type ID of the block
	 * @param typeId Block type ID
	 */
	public void setTypeId(int typeId) {
		this.b.setTypeId(typeId);
	}
	
	/**
	 * Get the block data
	 * @return Block data
	 */
	public byte getData() {
		return this.b.getData();
	}
	
	/**
	 * Set the block data
	 * @param data Block data
	 */
	public void setData(byte data) {
		this.b.setData(data);
	}
}
