package com.timvisee.safecreeper;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class SCLogger {
	
	private Logger log;
	
	/*private boolean writeToFile = false;
	private FileWriter writer;
	private final String writerPrefix = "[SafeCreeper] ";*/
	
	private final String prefix = "";
	private final String errorPrefix = "[ERROR] ";
	private final String debugPrefix = "[DEBUG] ";
	private boolean defShowPrefix = true;
	private boolean defShowErrorPrefix = true;
	private boolean defShowDebugPrefix = true;
	
	/**
	 * Constructor
	 * @param log Logger
	 */
	public SCLogger(Logger log) {
		this.log = log;
	}
	
	/**
	 * Get the logger
	 * @return
	 */
	public Logger getLogger() {
		return this.log;
	}
	
	/**
	 * Set the logger
	 * @param log Logger
	 */
	public void setLogger(Logger log) {
		this.log = log;
	}

	private void write(String msg) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
	}
	
	/**
	 * Info
	 * @param text Text
	 */
	public void info(String text) {
		info(text, this.defShowPrefix);
	}
	
	/**
	 * Info
	 * @param text Text
	 * @param prefix Prefix
	 * @return Output
	 */
	public String info(String text, boolean prefix) {
		String msg;
		
		if(prefix)
			msg = this.prefix + text;
		else
			msg = text;
		
		log.info(msg);
		
		// Write the message to the Safe Creeper log
		
		
		return msg;
	}
	
	/**
	 * Error
	 * @param text Text
	 */
	public void error(String text) {
		error(text, this.defShowErrorPrefix);
	}
	
	/**
	 * Error
	 * @param text Text
	 * @param prefix Prefix
	 * @return Output
	 */
	public String error(String text, boolean prefix) {
		String msg;
		
		if(prefix)
			msg = this.errorPrefix + text;
		else
			msg = text;
		
		log.info(msg);
		return msg;
	}
	
	/**
	 * Debug
	 * @param text Text
	 */
	public void debug(String text) {
		debug(text, this.defShowDebugPrefix);
	}
	
	/**
	 * Debug
	 * @param text Text
	 * @param prefix Prefix
	 * @return Output
	 */
	public String debug(String text, boolean prefix) {
		String msg;
		
		if(prefix)
			msg = this.debugPrefix + text;
		else
			msg = text;
		
		log.info(msg);
		return msg;
	}
	
	/**
	 * Get the prefix
	 * @return Prefix
	 */
	public String getPrefix() {
		return this.prefix;
	}
	
	/**
	 * Get the error prefix
	 * @return Error prefix
	 */
	public String getErrorPrefix() {
		return this.errorPrefix;
	}
	
	/**
	 * Get the debug prefix
	 * @return Debug prefix
	 */
	public String getDebugPrefix() {
		return this.debugPrefix;
	}
	
	/**
	 * Check if the prefix is shown by default
	 * @return True if shown by default
	 */
	public boolean getShowPrefixByDefault() {
		return this.defShowPrefix;
	}
	
	/**
	 * Set if the prefix is shown by default
	 * @param showPrefix Prefix shown by default
	 */
	public void setShowPrefixByDefault(boolean showPrefix) {
		this.defShowPrefix = showPrefix;
	}
	
	/**
	 * Get if the error prefix is shown by default
	 * @return Error prefix shown by default
	 */
	public boolean getShowErrorPrefixByDefault() {
		return this.defShowErrorPrefix;
	}
	
	/**
	 * Set if the error prefix is shown by default
	 * @param showErrorPrefix Error prefix shown by default
	 */
	public void setShowErrorPrefixByDefault(boolean showErrorPrefix) {
		this.defShowErrorPrefix = showErrorPrefix;
	}
	
	/**
	 * Get if the debug prefix is shown by default
	 * @return True if the debug prefix shown by default
	 */
	public boolean getShowDebugPrefixByDefault() {
		return this.defShowDebugPrefix;
	}
	
	/**
	 * Set if the debug prefix is shown by default
	 * @param showDebugPrefix Debug prefix shown by default
	 */
	public void setShowDebugPrefixByDefault(boolean showDebugPrefix) {
		this.defShowErrorPrefix = showDebugPrefix;
	}
} 
