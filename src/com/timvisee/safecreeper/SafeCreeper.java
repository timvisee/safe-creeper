package com.timvisee.safecreeper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.io.OutputStream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.timvisee.safecreeper.api.SafeCreeperApi;
import com.timvisee.safecreeper.command.CommandHandler;
import com.timvisee.safecreeper.entity.SCLivingEntityReviveManager;
import com.timvisee.safecreeper.listener.*;
import com.timvisee.safecreeper.manager.*;
import com.timvisee.safecreeper.task.SCDestructionRepairRepairTask;
import com.timvisee.safecreeper.task.SCDestructionRepairSaveDataTask;
import com.timvisee.safecreeper.task.SCUpdateCheckerTask;
import com.timvisee.safecreeper.util.SCFileUpdater;
import com.timvisee.safecreeper.util.SCUpdateChecker;

public class SafeCreeper extends JavaPlugin {
	
	// Safe Creeper static instance
	public static SafeCreeper instance;
	
	// Logger
	private SCLogger log;
	
	// Listeners
	private final SCBlockListener blockListener = new SCBlockListener();
	private final SCEntityListener entityListener = new SCEntityListener();
	private final SCPlayerListener playerListener = new SCPlayerListener();
	private final SCPluginListener pluginListener = new SCPluginListener();
	private final SCHangingListener hangingListener = new SCHangingListener();
	private final SCTVNLibListener tvnlListener = new SCTVNLibListener();
	private final SCWeatherListener weatherListener = new SCWeatherListener();
	private final SCWorldListener worldListener = new SCWorldListener();
	
	// Config file and folder paths
	private File globalConfigFile = new File("plugins/SafeCreeper/global.yml");
	private File worldConfigsFolder = new File("plugins/SafeCreeper/worlds");
	
	// Managers and Handlers
	private SCTVNLibManager tvnlManager;
	private SCPermissionsManager pm;
	private SCConfigManager cm = null;
	private SCDestructionRepairManager drm;
	private SCLivingEntityReviveManager lerm;
	private SCCorruptionManager corHandler;
	private SCMobArenaManager mam;
	private SCPVPArenaManager pam;
	private SCFactionsManager fm;
	private SCWorldGuardManager wgm;
	private SCMetricsManager mm;
	private SCStaticsManager statics = new SCStaticsManager();
	
	// Update Checker
	private SCUpdateChecker uc = null;
	
	// Debug Mode
	boolean debug = false;
	
	// Variable to disable the other explosions for a little, little while (otherwise some explosions are going to be looped)
	public boolean disableOtherExplosions = false;
	
	/**
	 * Constructor
	 */
	public SafeCreeper() {
		// Define the Safe Creeper static instance variable
		instance = this;
	}
	
	/**
	 * On enable method, called when plugin is being enabled
	 */
	public void onEnable() {
		long t = System.currentTimeMillis();
		
		// Define the plugin manager
		PluginManager pm = getServer().getPluginManager();
		
		// Setup the file paths
		globalConfigFile = new File(getConfig().getString("GlobalConfigFilePath", globalConfigFile.getPath()));
		worldConfigsFolder = new File(getConfig().getString("WorldConfigsFolderPath", worldConfigsFolder.getPath()));

		// Setup the Safe Creeper logger
		setupSCLogger();
		
		// Check if all the config file exists
		try {
			checkConigFilesExist();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		// Setup the config manager before all other managers, to make the file updater work
	    setupConfigManager();
		
		// Initialize the update checker
		setupUpdateChecker();
		
		// Remove all (old) update files
		getUpdateChecker().removeUpdateFiles();
		
		// Check if any update exists
		if(getConfig().getBoolean("updateChecker.enabled", true)) {
			if(uc.isNewVersionAvailable()) {
				final String newVer = uc.getNewestVersion();
				System.out.println("[SafeCreeper] New Safe Creeper version available: v" + newVer);
				
				// Auto install updates if enabled
				if(getConfig().getBoolean("updateChecker.autoInstallUpdates", true) || getUpdateChecker().isImportantUpdateAvailable()) {
					if(!uc.isNewVersionCompatibleWithCurrentBukkit()) {
						System.out.println("[SafeCreeper] The newest Safe Creeper version is not compatible with the current Bukkit version!");
						System.out.println("[SafeCreeper] Please update to Bukkit " + uc.getRequiredBukkitVersion() + " or higher!");
					} else {
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
		
		// Schedule update checker task
		FileConfiguration config = getConfig();
		if(config.getBoolean("tasks.updateChecker.enabled", true)) {
			int taskInterval = (int) config.getDouble("tasks.updateChecker.interval", 3600) * 20;
			
			// Schedule the update checker task
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new SCUpdateCheckerTask(getConfig(), getUpdateChecker()), taskInterval, taskInterval);
		} else {
			// Show an warning in the console
			getSCLogger().info("Scheduled task 'updateChecker' disabled in the config file!");
		}
		
		// Setup the API
		setupApi();
		
		// Update all existing config files if they aren't up-to-date
		((SCFileUpdater) new SCFileUpdater()).updateFiles();
		
		// Setup TVNativeLib
		setupTVNLibManager();
		
		// Setup managers and handlers
	    setupPermissionsManager();
	    setupDestructionRepairManager();
	    setupLivingEntityReviveManager();
	    setupMobArenaManager();
	    setupPVPArenaManager();
	    setupFactionsManager();
	    setupWorldGuardManager();
	    setupCorruptionManager();
		setupMetricsManager();
		
		// Load destruction repair data
		getDestructionRepairManager().load();
		
		// Register event listeners
		pm.registerEvents(this.blockListener, this);
		pm.registerEvents(this.entityListener, this);
		pm.registerEvents(this.hangingListener, this);
		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.pluginListener, this);
		pm.registerEvents(this.weatherListener, this);
		pm.registerEvents(this.worldListener, this);
		
		// Register the TVNLibListener if the TVNLib listener plugin is installed
		if(getTVNLibManager().isEnabled())
			pm.registerEvents(this.tvnlListener, this);
		
		/* // Test - Beginning of custom mob abilities!
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
					List<Player> onlinePlayers = Arrays.asList(getServer().getOnlinePlayers());
					if(onlinePlayers.size() > 0) {
						for(Player p : onlinePlayers) {
							//Player p = onlinePlayers.get(0);
							for(LivingEntity e : p.getWorld().getLivingEntities()) {
								if(e instanceof Creature) {
									/*Creature c = (Creature) e;
									c.setTarget(p);* /
									
									//c.launchProjectile();
									
									if(getLivingEntityManager().isSCLivingEntity(e)) {
										SCLivingEntity scle = getLivingEntityManager().getLivingEntity(e);
										
										if(scle.getLivingEntity().getLocation().distance(p.getLocation()) > 15)
											continue;
										
										scle.shootProjectile(p);
									}
								}
							}
						}
					}
				}
			}, 20, 20);*/
		
		// Task to repair blocks from the destruction repair manager// Schedule update checker task
		if(config.getBoolean("tasks.destructionRepairRepair.enabled", true)) {
			int taskInterval = (int) config.getDouble("tasks.destructionRepairRepair.interval", 1) * 20;
			
			// Schedule the task
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new SCDestructionRepairRepairTask(getDestructionRepairManager()), taskInterval, taskInterval);
		} else {
			// Show an warning in the console
			getSCLogger().info("Scheduled task 'destructionRepairRepair' disabled in the config file!");
		}
		
		// Task to save the destruction repair data
		if(config.getBoolean("tasks.destructionRepairSave.enabled", true)) {
			int taskInterval = (int) config.getDouble("tasks.destructionRepairSave.interval", 300) * 20;
			
			// Schedule the task
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new SCDestructionRepairSaveDataTask(getDestructionRepairManager()), taskInterval, taskInterval);
		} else {
			// Show an warning in the console
			getSCLogger().info("Scheduled task 'destructionRepairSave' disabled in the config file!");
		}
		
		// Plugin sucesfuly enabled, show console message
		PluginDescriptionFile pdfFile = getDescription();
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		getSCLogger().info("Safe Creeper v" + pdfFile.getVersion() + " enabled, took " + String.valueOf(duration) + " ms!");
	}
	
	/**
	 * On disable method, called when plugin is being disabled
	 */
	public void onDisable() {
		// Save the destruction repair data
		getDestructionRepairManager().save();
		
		// Cancel all running Safe Creeper tasks
		stopTasks();
		
		// If any update was downloaded, install the update
		if(getUpdateChecker().isUpdateDownloaded())
			getUpdateChecker().installUpdate();
		
		// Remove all update files
		getUpdateChecker().removeUpdateFiles();
		
		// Plugin disabled, show console message
		getSCLogger().info("Safe Creeper Disabled");
	}
	
	/**
	 * Stop all scheduled Safe Creeper tasks
	 */
	public void stopTasks() {
		getSCLogger().info("Cancelling all Safe Creeper tasks...");
		SafeCreeper.instance.getServer().getScheduler().cancelTasks(SafeCreeper.instance);
		getSCLogger().info("All Safe Creeper tasks cancelled!");
	}
    
	/**
	 * Fetch the Safe Creeper version from the plugin.yml file
	 * @return Fetch the Safe Creeper version from the plugin.yml file
	 */
	public String getVersion() {
		return getDescription().getVersion();
	}
	
	/**
	 * Set up the Safe Creeper API layer
	 */
	public void setupApi() {
		// Setup API
		SafeCreeperApi.setPlugin(this);
	}
	
	/**
	 * Set up the update checker
	 */
	public void setupUpdateChecker() {
		this.uc = new SCUpdateChecker();
	}
	
	/**
	 * Get the update checker instance
	 * @return Update checker instance
	 */
	public SCUpdateChecker getUpdateChecker() {
		return this.uc;
	}
	
	/**
	 * Set up the Safe Creeper logger
	 */
	public void setupSCLogger() {
		this.log = new SCLogger(getLogger());
	}
	
	/**
	 * Get the Safe Creeper logger instance
	 * @return Safe Creeper logger instance
	 */
	public SCLogger getSCLogger() {
		return this.log;
	}
	
	/**
	 * Get the plugin listener
	 * @return SCPluginListener instance
	 */
	public SCPluginListener getPluginListener() {
		return this.pluginListener;
	}
	
	/**
	 * Set up the TVNLib manager
	 */
	public void setupTVNLibManager() {
		// Setup TVNLib Manager
		this.tvnlManager = new SCTVNLibManager(getSCLogger());
		this.tvnlManager.setUp();
	}
	
	/**
	 * Get the TVNLib manager instance
	 * @return TVNLib manager instance
	 */
	public SCTVNLibManager getTVNLibManager() {
		return this.tvnlManager;
	}
	
	/**
	 * Set up the config manager
	 */
	public void setupConfigManager() {
		this.cm = new SCConfigManager(globalConfigFile, worldConfigsFolder);
	}
	
	/**
	 * Get the config manager instance
	 * @return
	 */
	public SCConfigManager getConfigManager() {
		return this.cm;
	}
	
	/**
	 * Setup the permissions manager
	 */
	public void setupPermissionsManager() {
		// Setup the permissions manager
		this.pm = new SCPermissionsManager(this.getServer(), this, getSCLogger());
		this.pm.setup();
	}
	
	/**
	 * Get the permissions manager
	 * @return permissions manager
	 */
	public SCPermissionsManager getPermissionsManager() {
		return this.pm;
	}
	
	/**
	 * Setup the destruction repair manager
	 */
	public void setupDestructionRepairManager() {
		// Setup the  destruction repair manager
		this.drm = new SCDestructionRepairManager();
	}
	
	/**
	 * Get the destruction repair manager
	 * @return destruction repair manager
	 */
	public SCDestructionRepairManager getDestructionRepairManager() {
		return this.drm;
	}
	
	/**
	 * Get the statics manager instnace
	 * @return
	 */
	public SCStaticsManager getStaticsManager() {
		return this.statics;
	}

	/**
	 * Set up the World Guard manager
	 */
	public void setupWorldGuardManager() {
		this.wgm = new SCWorldGuardManager(getSCLogger());
		this.wgm.setUp();
	}
	
	/**
	 * Get the World Guard plugin instance
	 * @return
	 */
    public SCWorldGuardManager getWorldGuardManager() {
        return this.wgm;
    }
    
    /**
     * Set up the Mob Arena Manager
     */
    public void setupMobArenaManager() {
    	// Set up the mob arena manager
    	this.mam = new SCMobArenaManager(getSCLogger());
    	this.mam.setUp();
    }
    
    /**
     * Get the MobArena manager
     * @return MobArena manager
     */
    public SCMobArenaManager getMobArenaManager() {
    	return this.mam;
    }
   
    /**
     * Set up the PVP Arena manager
     */
    public void setupPVPArenaManager() {
    	// Set up the PVP Arena manager
    	this.pam = new SCPVPArenaManager(getSCLogger());
    	this.pam.setUp();
    }
    
    /**
     * Get the PVP Arena manager instance
     * @return PVP Arena manager instnace
     */
    public SCPVPArenaManager getPVPArenaManager() {
    	return this.pam;
    }
   
    /**
     * Set up the Factions manager
     */
    public void setupFactionsManager() {
    	this.fm = new SCFactionsManager(getSCLogger());
    	this.fm.setUp();
    }
    
    /**
     * Get the Factions manager
     * @return Factions manager instance
     */
    public SCFactionsManager getFactionsManager() {
    	return this.fm;
    }
    
    /**
     * Set up the Corruption manager
     */
    public void setupCorruptionManager() {
    	this.corHandler = new SCCorruptionManager(getSCLogger());
    	this.corHandler.setUp();
    }
    
    /**
     * Get the Corruption handler
     * @return Corruption manager
     */
    public SCCorruptionManager getCorruptionHandler() {
    	return this.corHandler;
    }
    
    /**
     * Set up the living 
     */
    public void setupLivingEntityReviveManager() {
    	this.lerm = new SCLivingEntityReviveManager();
    }
    
    /**
     * Get the living entity revive manager instance
     * @return Living entity revive manager instance
     */
    public SCLivingEntityReviveManager getLivingEntityReviveManager() {
    	return this.lerm;
    }
	
    /**
     * Check if the config file exists
     * @throws Exception
     */
	public void checkConigFilesExist() throws Exception {
		if(!getDataFolder().exists()) {
			getSCLogger().info("Creating new Safe Creeper folder");
			getDataFolder().mkdirs();
		}
		File configFile = new File(getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			getSCLogger().info("Generating new config file");
			copyFile(getResource("res/config.yml"), configFile);
		}
		if(!globalConfigFile.exists()) {
			getSCLogger().info("Generating new global file");
			copyFile(getResource("res/global.yml"), globalConfigFile);
		}
		if(!worldConfigsFolder.exists()) {
			getSCLogger().info("Generating new 'worlds' folder");
			worldConfigsFolder.mkdirs();
			copyFile(getResource("res/worlds/world_example.yml"), new File(worldConfigsFolder, "world_example.yml"));
			copyFile(getResource("res/worlds/world_example2.yml"), new File(worldConfigsFolder, "world_example2.yml"));
		}
	}
	
	/**
	 * Copy a file
	 * @param in Input stream (file)
	 * @param file File to copy the file to
	 */
	private void copyFile(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Set up the metrics manager
	 */
	public void setupMetricsManager() {
		this.mm = new SCMetricsManager(getConfig(), getSCLogger());
		this.mm.setup();
	}
	
	/**
	 * Get the metrics manager
	 * @return Metrics manager instance
	 */
	public SCMetricsManager getMetricsManager() {
		return this.mm;
	}

	/**
	 * On command method, called when a command ran on the server
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Run the command trough the command handler
		CommandHandler ch = new CommandHandler();
		return ch.onCommand(sender, cmd, commandLabel, args);
	}
}
