package com.timvisee.safecreeper;

import java.util.logging.Logger;

public class SCLogger {
	
	private Logger log;
	
	private final String prefix = "[SafeCreeper] ";
	private final String errorPrefix = "[SafeCreeper] [ERROR] ";
	private final String debugPrefix = "[SafeCreeper] [DEBUG] ";
	private boolean defShowPrefix = true;
	private boolean defShowErrorPrefix = true;
	private boolean defShowDebugPrefix = true;
	
	public SCLogger(Logger log) {
		if(log != null)
			this.log = log;
		
		setup();
	}
	
	public void setup() { }
	
	public String info(String text) {
		return info(text, this.defShowPrefix);
	}
	
	public String info(String text, boolean prefix) {
		String msg;
		
		if(prefix)
			msg = this.prefix + text;
		else
			msg = text;
		
		this.log.info(msg);
		return msg;
	}
	
	public String error(String text) {
		return error(text, this.defShowErrorPrefix);
	}
	
	public String error(String text, boolean prefix) {
		String msg;
		
		if(prefix)
			msg = this.errorPrefix + text;
		else
			msg = text;
		
		this.log.info(msg);
		return msg;
	}
	
	public String debug(String text) {
		return debug(text, this.defShowDebugPrefix);
	}
	
	public String debug(String text, boolean prefix) {
		String msg;
		
		if(prefix)
			msg = this.debugPrefix + text;
		else
			msg = text;
		
		this.log.info(msg);
		return msg;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public String getErrorPrefix() {
		return this.errorPrefix;
	}
	
	public String getDebugPrefix() {
		return this.debugPrefix;
	}
	
	public boolean getShowPrefixByDefault() {
		return this.defShowPrefix;
	}
	
	public void setShowPrefixByDefault(boolean showPrefix) {
		this.defShowPrefix = showPrefix;
	}
	
	public boolean getShowErrorPrefixByDefault() {
		return this.defShowErrorPrefix;
	}
	
	public void setShowErrorPrefixByDefault(boolean showErrorPrefix) {
		this.defShowErrorPrefix = showErrorPrefix;
	}
	
	public boolean getShowDebugPrefixByDefault() {
		return this.defShowDebugPrefix;
	}
	
	public void setShowDebugPrefixByDefault(boolean showDebugPrefix) {
		this.defShowErrorPrefix = showDebugPrefix;
	}
	
	public Logger getLogger() {
		return this.log;
	}
	
	public void setLogger(Logger log) {
		if(log != null)
			this.log = log;
	}
} 
