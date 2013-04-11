package com.timvisee.safecreeper.task;

import com.timvisee.safecreeper.manager.DestructionRepairManager;

public abstract class SCDestructionRepairTask extends SCTask {
	
	private DestructionRepairManager drm;
	
	/**
	 * Constructor
	 * @param drm
	 */
	public SCDestructionRepairTask(DestructionRepairManager drm) {
		this.drm = drm;
	}
	
	/**
	 * Get the destruction repair manager
	 * @return Destruction repair manager
	 */
	public DestructionRepairManager getDestructionRepairManager() {
		return this.drm;
	}
	
	/**
	 * Set the destruction repair manager
	 * @param drm Destruction repair manager
	 */
	public void setDestructionRepairManager(DestructionRepairManager drm) {
		this.drm = drm;
	}
	
	/**
	 * Get the task name
	 * @return Task name
	 */
	public String getTaskName() {
		return "Safe Creeper destruction repair task";
	}
}
