package com.timvisee.safecreeper.task;

import org.bukkit.configuration.Configuration;

import com.timvisee.safecreeper.manager.SCStatisticsManager;

public class SCStatisticsPostTask extends SCTask {

	Configuration config;
	SCStatisticsManager sm;
	
	/**
	 * Constructor
	 * @param config Config instance
	 * @param sm Statistics manager task
	 */
	public SCStatisticsPostTask(Configuration config, SCStatisticsManager sm) {
		this.config = config;
		this.sm = sm;
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
	 * Get the Statistics Manager instance
	 * @return Statistics Manager instance
	 */
	public SCStatisticsManager getStatisticsManager() {
		return this.sm;
	}
	
	/**
	 * Set the update checker instance
	 * @param sm Statistics Manager instance
	 */
	public void setStatisticsManager(SCStatisticsManager sm) {
		this.sm = sm;
	}
	
	/**
	 * Task
	 */
	@Override
	public void run() {
		if(getConfig().getBoolean("statistics.enabled", true)) {
			boolean showStatus = getConfig().getBoolean("tasks.statisticsPost.showStatusInConsole");
			
			if(showStatus)
				System.out.println("[SafeCreeper] Sending Safe Creeper statistics...");
			
			long t = System.currentTimeMillis();
			
			sm.postStatistics();
			
			long delay = System.currentTimeMillis() - t;
			
			if(showStatus)
				System.out.println("[SafeCreeper] Safe Creeper statistics send, took " + delay + " ms!");
		}
	}
	
	/**
	 * Get the task name
	 * @return Task name
	 */
	public String getTaskName() {
		return "Safe Creeper statistics post task";
	}
}
