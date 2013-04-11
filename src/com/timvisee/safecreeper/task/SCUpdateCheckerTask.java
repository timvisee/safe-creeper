package com.timvisee.safecreeper.task;

import org.bukkit.configuration.Configuration;

import com.timvisee.safecreeper.util.UpdateChecker;

public class SCUpdateCheckerTask extends SCTask {

	Configuration config;
	UpdateChecker uc;
	
	/**
	 * Constructor
	 * @param config Config instance
	 * @param uc Update checker instance
	 */
	public SCUpdateCheckerTask(Configuration config, UpdateChecker uc) {
		this.config = config;
		this.uc = uc;
	}
	
	/**
	 * Get the config instance
	 * @return Config instance
	 */
	public Configuration getConfig() {
		return this.config;
	}
	
	/**
	 * Set the config instance
	 * @param config Config instance
	 */
	public void setConfig(Configuration config) {
		this.config = config;
	}
	
	/**
	 * Get the update checker instance
	 * @return Update checker instance
	 */
	public UpdateChecker getUpdateChecker() {
		return this.uc;
	}
	
	/**
	 * Set the update checker instance
	 * @param uc Update checker instance
	 */
	public void setUpdateChecker(UpdateChecker uc) {
		this.uc = uc;
	}
	
	/**
	 * Task
	 */
	@Override
	public void run() {
		if(getConfig().getBoolean("updateChecker.enabled", true)) {
			getUpdateChecker().refreshUpdatesData();
			if(uc.isNewVersionAvailable()) {
				final String newVer = uc.getNewestVersion();
				System.out.println("[SafeCreeper] New Safe Creeper version available: v" + newVer);
				
				// Auto install updates if enabled
				if(getConfig().getBoolean("updateChecker.autoInstallUpdates", true) || getUpdateChecker().isImportantUpdateAvailable()) {
					if(uc.isNewVersionCompatibleWithCurrentBukkit()) {
						// Check if already update installed
						if(getUpdateChecker().isUpdateDownloaded())
							System.out.println("[SafeCreeper] Safe Creeper update installed, server reload required!");
						else {
							// Download the update and show some status messages
							System.out.println("[SafeCreeper] Automaticly installing SafeCreeper update...");
							getUpdateChecker().downloadUpdate();
							System.out.println("[SafeCreeper] Safe Creeper update installed, reload required!");
						}
					}
				} else {
					// Auto installing updates not enabled, show a status message
					System.out.println("[SafeCreeper] Use '/sc installupdate' to automaticly install the new update!");
				}
			}
		}
	}
	
	/**
	 * Get the task name
	 * @return Task name
	 */
	public String getTaskName() {
		return "Safe Creeper update checker task";
	}
}
