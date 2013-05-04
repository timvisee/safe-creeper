package com.timvisee.safecreeper.event.config;

import java.io.File;

import com.timvisee.safecreeper.event.SCEvent;

public class SCGlobalConfigLoadEvent extends SCEvent {
	
	private File configFile;
	
	/**
	 * Constructor
	 * @param configFile Global config file
	 */
	public SCGlobalConfigLoadEvent(File configFile) {
		this.configFile = configFile;
	}
	
	/**
	 * Get the path of the global config file
	 * @return Global config file path
	 */
	public File getConfigFile() {
		return this.configFile;
	}
	
	/**
	 * Set the path of the global config file to load
	 * @param configFile Global config file path
	 */
	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}
}
