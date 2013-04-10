package com.timvisee.safecreeper.task;

import com.timvisee.safecreeper.manager.DestructionRepairManager;

public class SCDestructionRepairSaveDataTask extends SCDestructionRepairTask {

	/**
	 * Constructor
	 * @param drm Destruction Repair Manager instance
	 */
	public SCDestructionRepairSaveDataTask(DestructionRepairManager drm) {
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
}
