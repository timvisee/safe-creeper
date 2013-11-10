package com.timvisee.safecreeper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.io.OutputStream;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.timvisee.safecreeper.api.SCApiController;
import com.timvisee.safecreeper.command.CommandHandler;
import com.timvisee.safecreeper.entity.SCLivingEntityReviveManager;
import com.timvisee.safecreeper.handler.SCConfigHandler;
import com.timvisee.safecreeper.handler.SCUpdatesHandler;
import com.timvisee.safecreeper.handler.plugin.SCCorruptionHandler;
import com.timvisee.safecreeper.handler.plugin.SCFactionsHandler;
import com.timvisee.safecreeper.handler.plugin.SCMobArenaHandler;
import com.timvisee.safecreeper.handler.plugin.SCPVPArenaHandler;
import com.timvisee.safecreeper.handler.plugin.SCTVNLibHandler;
import com.timvisee.safecreeper.handler.plugin.SCWorldGuardHandler;
import com.timvisee.safecreeper.listener.*;
import com.timvisee.safecreeper.manager.*;
import com.timvisee.safecreeper.task.SCDestructionRepairRepairTask;
import com.timvisee.safecreeper.task.SCDestructionRepairSaveDataTask;
import com.timvisee.safecreeper.task.SCStatisticsPostTask;
import com.timvisee.safecreeper.task.SCUpdateCheckerTask;
import com.timvisee.safecreeper.util.SCFileUpdater;

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
	
	// Controllers, Handlers and Managers
	private SCUpdatesHandler uh;
	private SCStatisticsManager sm;
	private SCApiController apic;
	private SCTVNLibHandler tvnlh;
	private SCPermissionsManager pm;
	private SCConfigHandler ch = null;
	private SCDestructionRepairManager drm;
	private SCLivingEntityReviveManager lerm;
	private SCCorruptionHandler corh;
	private SCMobArenaHandler mah;
	private SCPVPArenaHandler pah;
	private SCFactionsHandler fh;
	private SCWorldGuardHandler wgh;
	
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
		// Store the time Safe Creeper is starting on so the starting duration can be calculated
		long t = System.currentTimeMillis();
		
		// Get Bukkit's plugin manager
		PluginManager pm = getServer().getPluginManager();
		
		// Set up the file paths
		globalConfigFile = new File(getConfig().getString("GlobalConfigFilePath", globalConfigFile.getPath()));
		worldConfigsFolder = new File(getConfig().getString("WorldConfigsFolderPath", worldConfigsFolder.getPath()));

		// Set up the Safe Creeper logger
		setUpSCLogger();
		
		// Set up the API manager
		setUpApiManager();
		
		// Setup the config manager before all other managers, to make the file updater work
	    setUpConfigHandler();
		
		// Verify all the Safe Creeper files
		boolean anyFileUpdated = verifyExternalFiles(true);
		
		// Refresh the config files if any external file was updated
		if(anyFileUpdated)
			getConfigHandler().reloadAllConfigs();
		
		// Set up the updates handler
		setUpUpdatesHandler();
		
		// Set up the statistics manager
		setUpStatisticsManager();
		
		FileConfiguration c = getConfig();
		
		// Set up remaining managers
		setUpTVNLibManager();
	    setUpPermissionsManager();
	    setUpDestructionRepairManager();
	    setUpLivingEntityReviveManager();
	    setUpMobArenaHandler();
	    setUpPVPArenaHandler();
	    setUpFactionsManager();
	    setUpWorldGuardHandler();
	    setUpCorruptionHandler();
		
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
		if(c.getBoolean("tasks.destructionRepairRepair.enabled", true)) {
			int taskInterval = (int) c.getDouble("tasks.destructionRepairRepair.interval", 1) * 20;
			
			// Schedule the task
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new SCDestructionRepairRepairTask(getDestructionRepairManager()), taskInterval, taskInterval);
		} else {
			// Show an warning in the console
			getSCLogger().info("Scheduled task 'destructionRepairRepair' disabled in the config file!");
		}
		
		// Task to save the destruction repair data
		if(c.getBoolean("tasks.destructionRepairSave.enabled", true)) {
			int taskInterval = (int) c.getDouble("tasks.destructionRepairSave.interval", 300) * 20;
			
			// Schedule the task
			boolean showMsg = c.getBoolean("tasks.destructionRepairSave.showSaveStatus", true);
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new SCDestructionRepairSaveDataTask(getDestructionRepairManager(), showMsg), taskInterval, taskInterval);
		} else {
			// Show an warning in the console
			getSCLogger().info("Scheduled task 'destructionRepairSave' disabled in the config file!");
		}
		
		// Plugin sucesfuly enabled, show console message
		PluginDescriptionFile pdfFile = getDescription();
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		// Show a status message
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
		
		// Unhook all plugins hooked into Safe Creeper and remove/unregister their sessions
		if(getApiManager().getApiSessionsCount() > 0) {
			getSCLogger().info("Unhooking all hooked plugins...");
			getApiManager().unregisterAllApiSessions();
		}
		
		// Send the Safe Creeper statistics
		if(getConfig().getBoolean("statistics.enabled", true)) {
			boolean showStatus = getConfig().getBoolean("statistics.showStatusInConsole");
			
			if(showStatus)
				System.out.println("[SafeCreeper] Sending Safe Creeper statistics...");
			
			long t = System.currentTimeMillis();
			
			sm.postStatistics();
			
			long delay = System.currentTimeMillis() - t;
			
			if(showStatus)
				System.out.println("[SafeCreeper] Safe Creeper statistics send, took " + delay + " ms!");
		}
		
		// If any update was downloaded, install the update
		if(getUpdatesHandler().isUpdateDownloaded())
			getUpdatesHandler().installUpdate();
		
		// Remove all update files
		getUpdatesHandler().removeUpdateFiles();
		
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
	 * Set up the updates handler
	 */
	public void setUpUpdatesHandler() {
		this.uh = new SCUpdatesHandler(false);
		
		// Remove all (old) update files
		getUpdatesHandler().removeUpdateFiles();
		
		// Check for updates once
		FileConfiguration c = getConfig();
		if(c.getBoolean("updateChecker.enabled", true)) {
			System.out.println(ChatColor.YELLOW + "[SafeCreeper] Checking for updates...");
			uh.refreshBukkitUpdatesFeedData();
			
			// Check if any update exists
			if(uh.isUpdateAvailable(true)) {
				final String newVer = uh.getNewestVersion(true);
				
				if(uh.isUpdateDownloaded())
					System.out.println(ChatColor.YELLOW + "[SafeCreeper] New version already downloaded (v" + String.valueOf(newVer) + "). Server reload required!");
				else {
					if(c.getBoolean("updateChecker.autoInstallUpdates", true)) {
						System.out.println(ChatColor.YELLOW + "[SafeCreeper] Downloading new version (v" + String.valueOf(newVer) + ")");
						uh.downloadUpdate();
						System.out.println(ChatColor.YELLOW + "[SafeCreeper] Update downloaded, server reload required!");
					} else {
						System.out.println(ChatColor.YELLOW + "[SafeCreeper] New Safe Creeper version available! v: " + String.valueOf(newVer));
						System.out.println(ChatColor.YELLOW + "[SafeCreeper] use '/sc installupdates' to automaticly install this update");
					}
				}
				
			} else if(uh.isUpdateAvailable(false)) {
				final String newVer = uh.getNewestVersion(false);
				
				System.out.println(ChatColor.YELLOW + "[SafeCreeper] New incompatible Safe Creeper version available: v" + String.valueOf(newVer));
				System.out.println(ChatColor.YELLOW + "[SafeCreeper] Please update CraftBukkit to the latest available version!");
		
			} else {
				System.out.println(ChatColor.YELLOW + "[SafeCreeper] No Safe Creeper update available!");
			}
		}
		
		// Schedule update checker task
		if(c.getBoolean("tasks.updateChecker.enabled", true)) {
			int taskInterval = (int) c.getDouble("tasks.updateChecker.interval", 600) * 20;
			
			// Schedule the update checker task
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new SCUpdateCheckerTask(getConfig(), getUpdatesHandler()), taskInterval, taskInterval);
		} else {
			// Show an warning in the console
			getSCLogger().info("Scheduled task 'updateChecker' disabled in the config file!");
		}
	}
	
	/**
	 * Get the updates handler instance
	 * @return Updates handler instance
	 */
	public SCUpdatesHandler getUpdatesHandler() {
		return this.uh;
	}
	
	/**
	 * Set up the statistics manager
	 */
	public void setUpStatisticsManager() {
		this.sm = new SCStatisticsManager(this);
		
		FileConfiguration c = getConfig();
		
		// Schedule statistics post task
		if(c.getBoolean("tasks.statisticsPost.enabled", true)) {
			int taskInterval = (int) c.getDouble("tasks.statisticsPost.interval", 300) * 20;
			
			// Schedule the update checker task
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new SCStatisticsPostTask(getConfig(), getStatisticsManager()), taskInterval, taskInterval);
		} else {
			// Show an warning in the console
			getSCLogger().info("Scheduled task 'statisticsPost' disabled in the config file!");
		}
		
		if(getConfig().getBoolean("statistics.enabled", true)) {
			boolean showStatus = getConfig().getBoolean("statistics.showStatusInConsole");
			
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
	 * Get the statistics manager instance
	 * @return Statistics manager instance
	 */
	public SCStatisticsManager getStatisticsManager() {
		return this.sm;
	}
	
	/**
	 * Set up the Safe Creeper logger
	 */
	public void setUpSCLogger() {
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
	 * Set up the API Manager
	 */
	public void setUpApiManager() {
		// Construct the API Manager
		this.apic = new SCApiController(false);
		
		// Show a status message
		getSCLogger().info("Safe Creeper API started!");
		
		// Enable the API if it should be enabled
		if(getConfig().getBoolean("api.enabled", true))
			this.apic.setEnabled(true);
		else
			getSCLogger().info("Not enabling Safe Creeper API, disabled in config file!");
	}
	
	/**
	 * Get the API Manager instance
	 * @return API Manager instance
	 */
	public SCApiController getApiManager() {
		return this.apic;
	}
	
	/**
	 * Set up the TVNLib manager
	 */
	public void setUpTVNLibManager() {
		// Setup TVNLib Manager
		this.tvnlh = new SCTVNLibHandler(getSCLogger());
		this.tvnlh.setUp();
	}
	
	/**
	 * Get the TVNLib manager instance
	 * @return TVNLib manager instance
	 */
	public SCTVNLibHandler getTVNLibManager() {
		return this.tvnlh;
	}
	
	/**
	 * Set up the config manager
	 */
	public void setUpConfigHandler() {
		this.ch = new SCConfigHandler(globalConfigFile, worldConfigsFolder);
	}
	
	/**
	 * Get the config manager instance
	 * @return
	 */
	public SCConfigHandler getConfigHandler() {
		return this.ch;
	}
	
	/**
	 * Setup the permissions manager
	 */
	public void setUpPermissionsManager() {
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
	public void setUpDestructionRepairManager() {
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
	 * Set up the World Guard handler
	 */
	public void setUpWorldGuardHandler() {
		this.wgh = new SCWorldGuardHandler(getSCLogger());
		this.wgh.setUp();
	}
	
	/**
	 * Get the World Guard plugin instance
	 * @return
	 */
    public SCWorldGuardHandler getWorldGuardHandler() {
        return this.wgh;
    }
    
    /**
     * Set up the Mob Arena handler
     */
    public void setUpMobArenaHandler() {
    	// Set up the mob arena manager
    	this.mah = new SCMobArenaHandler(getSCLogger());
    	this.mah.setUp();
    }
    
    /**
     * Get the MobArena handler
     * @return MobArena handler
     */
    public SCMobArenaHandler getMobArenaHandler() {
    	return this.mah;
    }
   
    /**
     * Set up the PVP Arena handler
     */
    public void setUpPVPArenaHandler() {
    	// Set up the PVP Arena manager
    	this.pah = new SCPVPArenaHandler(getSCLogger());
    	this.pah.setUp();
    }
    
    /**
     * Get the PVP Arena handler instance
     * @return PVP Arena handler instnace
     */
    public SCPVPArenaHandler getPVPArenaHandler() {
    	return this.pah;
    }
   
    /**
     * Set up the Factions manager
     */
    public void setUpFactionsManager() {
    	this.fh = new SCFactionsHandler(getSCLogger());
    	this.fh.setUp();
    }
    
    /**
     * Get the Factions handler
     * @return Factions handler instance
     */
    public SCFactionsHandler getFactionsHandler() {
    	return this.fh;
    }
    
    /**
     * Set up the Corruption handler
     */
    public void setUpCorruptionHandler() {
    	this.corh = new SCCorruptionHandler(getSCLogger());
    	this.corh.setUp();
    }
    
    /**
     * Get the Corruption handler
     * @return Corruption handler
     */
    public SCCorruptionHandler getCorruptionHandler() {
    	return this.corh;
    }
    
    /**
     * Set up the living 
     */
    public void setUpLivingEntityReviveManager() {
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
     * Verify all the external Safe Creeper files, automatically fix invalid files if enabled
     * @param autoFix True to automatically fix and update invalid files
     * @return True when every file was fine, false if something was wrong
     */
    public boolean verifyExternalFiles(boolean autoFix) {
    	// Get the Safe Creeper data folder
    	File scDir = this.getDataFolder();
    	
    	// Keep track if everything was right
    	boolean invalid = false;
    	
    	// Make sure the Safe Creeper directory exists
    	if(!scDir.exists() || !scDir.isDirectory()) {
    		invalid = true;
    		
    		// Automatically fix this issue if enabled
    		if(autoFix) {
    			getSCLogger().info("Creating Safe Creeper directory...");
    			scDir.mkdirs();
    		}
    	}
    	
    	// Make sure the main config file exists
		File cfgFile = new File(scDir, "config.yml");
		if(!cfgFile.exists() || !cfgFile.isFile()) {
			invalid = true;
			
			// Automatically fix the file
			if(autoFix) {
				getSCLogger().info("Creating new config file...");
				copyFile(getResource("res/config.yml"), cfgFile);
			}
		}
		
		// Verify the config file version
		if(SCFileUpdater.updateConfig())
			invalid = true;
		
		// Make sure the global file exists
		if(!globalConfigFile.exists() || !globalConfigFile.isFile()) {
			invalid = true;
			
			// Automatically fix the file
			if(autoFix) {
				getSCLogger().info("Creating new global file...");
				copyFile(getResource("res/global.yml"), globalConfigFile);
			}
		}
		
		// Verify the config file version
		if(SCFileUpdater.updateGlobalConfig())
			invalid = true;
		
		// Make sure the worlds folder exists
		if(!worldConfigsFolder.exists() || !worldConfigsFolder.isDirectory()) {
			invalid = true;
			
			// Automatically fix the directory
			if(autoFix) {
				getSCLogger().info("Generating new worlds directory...");
				worldConfigsFolder.mkdirs();
				copyFile(getResource("res/worlds/world_example.yml"), new File(worldConfigsFolder, "world_example.yml"));
				copyFile(getResource("res/worlds/world_example2.yml"), new File(worldConfigsFolder, "world_example2.yml"));
			}
		}
    	
		// Verify all the world config files
		if(SCFileUpdater.updateAllWorldsConfig())
			invalid = true;
    	
		// Return the result
		return (!invalid);
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
	 * On command method, called when a command ran on the server
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Run the command trough the command handler
		CommandHandler ch = new CommandHandler();
		return ch.onCommand(sender, cmd, commandLabel, args);
	}
}
