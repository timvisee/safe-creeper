package com.timvisee.safecreeper.manager;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.Configuration;

import com.timvisee.safecreeper.Metrics;
import com.timvisee.safecreeper.Metrics.Graph;
import com.timvisee.safecreeper.SafeCreeper;

public class SCMetricsManager {
	
	private Configuration config;
	private Logger log;
	
	/**
	 * Constructor
	 * @param log Logger
	 */
	public SCMetricsManager(Configuration config, Logger log) {
		this.config = config;
		this.log = log;
	}
	
	/**
	 * Set up Metrics
	 */
	public void setup() {
		if(!getConfig().getBoolean("statistics.enabled", true)) {
			this.log.info("MCStats.org Statistics disabled!");
			return;
		}
		
		// Metrics / MCStats.org
		try {
		    Metrics metrics = new Metrics(SafeCreeper.instance);
		    
		    // Add graph for nerfed creepers
		    // Construct a graph, which can be immediately used and considered as valid
		    Graph graph = metrics.createGraph("Activities Nerfed by Safe Creeper");
		    // Creeper explosions Nerfed
		    graph.addPlotter(new Metrics.Plotter("Creeper Explosions") {
	            @Override
	            public int getValue() {
	            	int i = SafeCreeper.instance.getStaticsManager().getCreeperExplosionsNerfed();
	            	SafeCreeper.instance.getStaticsManager().setCreeperExplosionNerved(0);
	            	return i;
	            }
		    });
		    graph.addPlotter(new Metrics.Plotter("TNT Explosions") {
	            @Override
	            public int getValue() {
	            	int i = SafeCreeper.instance.getStaticsManager().getTNTExplosionsNerfed();
	            	SafeCreeper.instance.getStaticsManager().setTNTExplosionNerved(0);
	            	return i;
	            }
		    });
		    graph.addPlotter(new Metrics.Plotter("TNT Damage") {
	            @Override
	            public int getValue() {
	            	int i = SafeCreeper.instance.getStaticsManager().getTNTDamageNerfed();
	            	SafeCreeper.instance.getStaticsManager().setTNTDamageNerved(0);
	            	return i;
	            }
		    });
		    // Used permissions systems
		    Graph graph2 = metrics.createGraph("Permisison Plugin Usage");
		    graph2.addPlotter(new Metrics.Plotter(SafeCreeper.instance.getPermissionsManager().getUsedPermissionsSystemType().getName()) {
	            @Override
	            public int getValue() {
	            	return 1;
	            }
		    });
		    
		    // Start metrics
		    metrics.start();
		    
		    // Show a status message
		    this.log.info("MCStats.org Statistics enabled.");
		} catch (IOException e) {
		    // Failed to submit the stats :-(
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the config
	 * @return Config
	 */
	public Configuration getConfig() {
		return this.config;
	}
	
	/**
	 * Set the config 
	 * @param config Config
	 */
	public void setConfiguration(Configuration config) {
		this.config = config;
	}
	
	/**
	 * Get the logger instance
	 * @return Logger instance
	 */
	public Logger getLogger() {
		return this.log;
	}
	
	/**
	 * Set the logger instance
	 * @param log Logger instance
	 */
	public void setLogger(Logger log) {
		this.log = log;
	}
}
