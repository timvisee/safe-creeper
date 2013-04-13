package com.timvisee.safecreeper.task;

import com.timvisee.safecreeper.manager.SCDestructionRepairManager;

public class SCDestructionRepairSaveDataTask extends SCDestructionRepairTask {

	/**
	 * Constructor
	 * @param drm Destruction Repair Manager instance
	 */
	public SCDestructionRepairSaveDataTask(SCDestructionRepairManager drm) {
		super(drm);
	}

	/**
	 * Task
	 */
	@Override
	public void run() {
		// Save the destruction repair data
		getDestructionRepairManager().save();
	}
	
	/**
	 * Get the task name
	 * @return Task name
	 */
	public String getTaskName() {
		return "Safe Creeper destruction repair save task";
	}
}
