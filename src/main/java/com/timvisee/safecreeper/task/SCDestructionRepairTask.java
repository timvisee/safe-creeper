package com.timvisee.safecreeper.task;

import com.timvisee.safecreeper.manager.SCDestructionRepairManager;

public abstract class SCDestructionRepairTask extends SCTask {
	
	private SCDestructionRepairManager drm;
	
	/**
	 * Constructor
	 * @param drm
	 */
	public SCDestructionRepairTask(SCDestructionRepairManager drm) {
		this.drm = drm;
	}
	
	/**
	 * Get the destruction repair manager
	 * @return Destruction repair manager
	 */
	public SCDestructionRepairManager getDestructionRepairManager() {
		return this.drm;
	}
	
	/**
	 * Set the destruction repair manager
	 * @param drm Destruction repair manager
	 */
	public void setDestructionRepairManager(SCDestructionRepairManager drm) {
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
