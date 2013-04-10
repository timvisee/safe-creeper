package com.timvisee.safecreeper.task;

import com.timvisee.safecreeper.SafeCreeper;

public abstract class SCTask implements Runnable {
	
	/**
	 * Get the Safe Creeper instance
	 * @return Safe Creeper instance
	 */
	public SafeCreeper getSafeCreeper() {
		return SafeCreeper.instance;
	}
}
