package com.timvisee.safecreeper.manager;

public class SCStaticsManager {

	private int creeperExplosionsNerfed = 0;
	private int TNTExplosionsNerfed = 0;
	private int TNTDamageNerfed = 0;
	
	private int blocksRebuild = 0;

	public int getCreeperExplosionsNerfed() {
		return this.creeperExplosionsNerfed;
	}
	
	/* Nerfed Creeper Explosions */
	public void addCreeperExplosionNerfed() {
		addCreeperExplosionNerfed(1);
	}
	
	public void addCreeperExplosionNerfed(int amount) {
		this.creeperExplosionsNerfed += amount;
	}
	
	public void setCreeperExplosionNerved(int amount) {
		this.creeperExplosionsNerfed = amount;
	}

	/* Nerfed TNT Explosions */
	public int getTNTExplosionsNerfed() {
		return this.TNTExplosionsNerfed;
	}
	
	public void addTNTExplosionNerfed() {
		addTNTExplosionNerfed(1);
	}
	
	public void addTNTExplosionNerfed(int amount) {
		this.TNTExplosionsNerfed += amount;
	}
	
	public void setTNTExplosionNerved(int amount) {
		this.TNTExplosionsNerfed = amount;
	}

	/* Nerfed TNT Damage */
	public int getTNTDamageNerfed() {
		return this.TNTDamageNerfed;
	}
	
	public void addTNTDamageNerfed() {
		addTNTDamageNerfed(1);
	}
	
	public void addTNTDamageNerfed(int amount) {
		this.TNTDamageNerfed += amount;
	}
	
	public void setTNTDamageNerved(int amount) {
		this.TNTDamageNerfed = amount;
	}
	
	/* Blocks rebuild */
	public int getBlocksRebuild() {
		return this.blocksRebuild;
	}
	
	public void addBlockRebuild() {
		addBlocksRebuild(1);
	}
	
	public void addBlocksRebuild(int amount) {
		this.blocksRebuild += amount;
	}
	
	public void setBlocksRebuild(int amount) {
		this.blocksRebuild = amount;
	}
}
