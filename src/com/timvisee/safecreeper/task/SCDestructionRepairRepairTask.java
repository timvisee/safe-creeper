package com.timvisee.safecreeper.task;

import com.timvisee.safecreeper.manager.DestructionRepairManager;

public class SCDestructionRepairRepairTask extends SCDestructionRepairTask {

	/**
	 * Constructor
	 * @param drm Destruction Repair Manager instance
	 */
	public SCDestructionRepairRepairTask(DestructionRepairManager drm) {
		super(drm);
	}

	/**
	 * Task
	 */
	@Override
	public void run() {
		// Repair blocks
		getDestructionRepairManager().repair();
	}
}
