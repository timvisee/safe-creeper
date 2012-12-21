package com.timvisee.safecreeper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.logging.Logger;

import net.slipcor.pvparena.PVPArena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.MobArenaHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.timvisee.safecreeper.Metrics.Graph;
import com.timvisee.safecreeper.api.SafeCreeperApi;
import com.timvisee.safecreeper.command.CommandHandler;
import com.timvisee.safecreeper.entity.SCLivingEntityManager;
import com.timvisee.safecreeper.entity.SCLivingEntityReviveManager;
import com.timvisee.safecreeper.handler.TVNLibHandler;
import com.timvisee.safecreeper.listener.*;
import com.timvisee.safecreeper.manager.*;
import com.timvisee.safecreeper.util.SCFileUpdater;
import com.timvisee.safecreeper.util.SCUpdateChecker;

public class SafeCreeper extends JavaPlugin {
	// Loggers
	private SCLogger logger;
	
	// Safe Creeper static instance
	public static SafeCreeper instance;
	
	// Listeners
	private final SCBlockListener blockListener = new SCBlockListener();
	private final SCEntityListener entityListener = new SCEntityListener();
	private final SCPlayerListener playerListener = new SCPlayerListener();
	private final SCWorldListener worldListener = new SCWorldListener();
	private final SCTVNLibListener tvnlListener = new SCTVNLibListener();
	
	// Config file and folder paths
	private File globalConfigFile = new File("plugins/SafeCreeper/global.yml");
	private File worldConfigsFolder = new File("plugins/SafeCreeper/worlds");
	
	// Managers and Handlers
	private TVNLibHandler tvnlHandler;
	private PermissionsManager pm;
	private SCConfigManager cm = null;
	private SCLivingEntityManager lem;
	private SCLivingEntityReviveManager lerm;
	private MobArenaHandler maHandler;
	private LikeabossManager labHandler;
	private SCStaticsManager statics = new SCStaticsManager();
	
	// Update Checker
	public boolean isUpdateAvailable = false;
	public String newestVersion = "0.1";
	
	// Debug Mode
	boolean debug = false;
	
	// Variable to disable the other explosions for a little, little while (otherwise some explosions are going to be looped)
	public boolean disableOtherExplosions = false;
	
	public SafeCreeper() {
		// Define the Safe Creeper static instance variable
		instance = this;
	}
	
	public void onEnable() {
		long t = System.currentTimeMillis();
		
		// Setup the Safe Creeper logger
		setupSCLogger();
		
		// Setup the API
		setupApi();
		
		// Define the plugin manager
		PluginManager pm = getServer().getPluginManager();
		
		// Setup the file paths
		globalConfigFile = new File(getConfig().getString("GlobalConfigFilePath", globalConfigFile.getPath()));
		worldConfigsFolder = new File(getConfig().getString("WorldConfigsFolderPath", worldConfigsFolder.getPath()));
		
		// Check if all the config file exists
		try {
			checkConigFilesExist();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		// Setup the config manager before all other managers, to make the file updater work
	    setupConfigManager();
		
		// Update all existing config files if they aren't up-to-date
		((SCFileUpdater) new SCFileUpdater()).updateFiles();
		
		// Setup TVNativeLib
		setupTVNLibHandler();
		
		/*boolean autoDownloadTVNLib =getConfig().getBoolean("autoDownloadTVNLib", false);
		if(autoDownloadTVNLib) {
			getSCLogger().info("Downloading and installing TVNLib...");
			TVNLibHandler.downloadTVNLib();
			getSCLogger().info("TVNLib succesfully downloaded and installed!");
			setupTVNLibHandler();
		}*/
		
		// Setup managers and handlers
	    setupPermissionsManager();
	    setupLivingEntityManager();
	    setupLivingEntityReviveManager();
	    setupMobArenaHandler();
	    setupPVPArena();
	    setupFactions();
	    setupLabManager();
		setupMetrics();
		
		// Register event listeners
		pm.registerEvents(this.blockListener, this);
		pm.registerEvents(this.entityListener, this);
		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.worldListener, this);
		
		// Register the TVNLibListener if the TVNLib listener plugin is installed
		if(getTVNLibHandler().isEnabled())
			pm.registerEvents(this.tvnlListener, this);
		
		// Check for updates
		checkUpdates();
		
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
		
		// Plugin sucesfuly enabled, show console message
		PluginDescriptionFile pdfFile = getDescription();
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		getSCLogger().info("Safe Creeper v" + pdfFile.getVersion() + " enabled, took " + String.valueOf(duration) + " ms!");
	}
	
	public void onDisable() {
		// Save all entity data
		if(getLivingEntityManager() != null)
			getLivingEntityManager().save();
		
		// Cancel all running Safe Creeper tasks
		getSCLogger().info("Cancelling all Safe Creeper tasks...");
		SafeCreeper.instance.getServer().getScheduler().cancelTasks(SafeCreeper.instance);
		getSCLogger().info("All Safe Creeper tasks cancelled!");
		
		// Plugin disabled, show console message
		getSCLogger().info("Safe Creeper Disabled");
	}
    
	public String getVersion() {
		return getDescription().getVersion();
	}
	
	public void setupApi() {
		// Setup API
		SafeCreeperApi.setPlugin(this);
	}
	
	public void setupSCLogger() {
		this.logger = new SCLogger(Logger.getLogger("Minecraft"));
	}
	
	public SCLogger getSCLogger() {
		return this.logger;
	}
	
	public void setupTVNLibHandler() {
		// Setup TVNLib Handler
		this.tvnlHandler = new TVNLibHandler(this);
	}
	
	public TVNLibHandler getTVNLibHandler() {
		return this.tvnlHandler;
	}
	
	public void setupConfigManager() {
		this.cm = new SCConfigManager(this, globalConfigFile, worldConfigsFolder);
	}
	
	public SCConfigManager getConfigManager() {
		return this.cm;
	}
	
	/**
	 * Setup the permissions manager
	 */
	public void setupPermissionsManager() {
		// Setup the permissions manager
		this.pm = new PermissionsManager(this.getServer(), this);
		this.pm.setup();
	}
	
	/**
	 * Get the permissions manager
	 * @return permissions manager
	 */
	public PermissionsManager getPermissionsManager() {
		return this.pm;
	}
	
	public SCStaticsManager getStaticsManager() {
		return this.statics;
	}

    protected WorldGuardPlugin getWorldGuard() {
        Plugin wg = getServer().getPluginManager().getPlugin("WorldGuard");
 
        // WorldGuard may not be loaded
        if (wg == null || !(wg instanceof WorldGuardPlugin))
            return null;
        return (WorldGuardPlugin) wg;
    }
    
    public boolean worldGuardEnabled() {
    	return (getWorldGuard() != null);
    }
   
    public void setupMobArenaHandler() {
        Plugin maPlugin = (MobArena) getServer().getPluginManager().getPlugin("MobArena");
        
        if (maPlugin == null) {
        	getSCLogger().info("Disabling MobArena usage, MobArena not found.");
            return;
        }
 
        maHandler = new MobArenaHandler();
        getSCLogger().info("Hooked into MobArena!");
    }
    
    public MobArenaHandler getMobArenaHandler() {
    	return this.maHandler;
    }
   
    public void setupPVPArena() {
        Plugin paPlugin = (PVPArena) getServer().getPluginManager().getPlugin("pvparena");
        
        if (paPlugin == null) {
        	getSCLogger().info("Disabling PVPArena usage, PVPArena not found.");
            return;
        }
 
        getSCLogger().info("Hooked into PVPArena!");
    }
   
    public void setupFactions() {
        Plugin fPlugin = (Plugin) getServer().getPluginManager().getPlugin("Factions");
        
        if (fPlugin == null) {
        	getSCLogger().info("Disabling Factions usage, Factions not found.");
            return;
        }
 
        getSCLogger().info("Hooked into Factions!");
    }
    
    public void setupLabManager() {
    	this.labHandler = new LikeabossManager();
    }
    
    public LikeabossManager getLabHandler() {
    	return this.labHandler;
    }
    
    public void setupLivingEntityManager() {
    	this.lem = new SCLivingEntityManager();
    	this.lem.load();
    }
    
    public SCLivingEntityManager getLivingEntityManager() {
    	return this.lem;
    }
    
    public void setupLivingEntityReviveManager() {
    	this.lerm = new SCLivingEntityReviveManager();
    }
    
    public SCLivingEntityReviveManager getLivingEntityReviveManager() {
    	return this.lerm;
    }
	
	public boolean checkUpdates() {
		SCUpdateChecker scuc = new SCUpdateChecker(this);
		isUpdateAvailable = scuc.checkUpdates();
		newestVersion = scuc.getLastVersion();
		
		if(isUpdateAvailable) {
			getSCLogger().info("New version available, version " + newestVersion + ".");
		}
		
		return isUpdateAvailable;
	}
	
	public void checkConigFilesExist() throws Exception {
		if(!getDataFolder().exists()) {
			getSCLogger().info("Creating new Safe Creeper folder");
			getDataFolder().mkdirs();
		}
		File configFile = new File(getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			getSCLogger().info("Generating new config file");
			copy(getResource("config.yml"), configFile);
		}
		if(!globalConfigFile.exists()) {
			getSCLogger().info("Generating new global file");
			copy(getResource("global.yml"), globalConfigFile);
		}
		if(!worldConfigsFolder.exists()) {
			getSCLogger().info("Generating new 'worlds' folder");
			worldConfigsFolder.mkdirs();
			copy(getResource("worlds/world_example.yml"), new File(worldConfigsFolder, "world_example.yml"));
			copy(getResource("worlds/world_example2.yml"), new File(worldConfigsFolder, "world_example2.yml"));
		}
	}
	
	private void copy(InputStream in, File file) {
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
	 * Setup metrics
	 */
	public void setupMetrics() {
		// Metrics / MCStats.org
		try {
		    Metrics metrics = new Metrics(this);
		    
		    // Add graph for nerfed creepers
		    // Construct a graph, which can be immediately used and considered as valid
		    Graph graph = metrics.createGraph("Activities Nerfed by Safe Creeper");
		    // Creeper explosions Nerfed
		    graph.addPlotter(new Metrics.Plotter("Creeper Explosions") {
	            @Override
	            public int getValue() {
	            	return statics.getCreeperExplosionsNerfed();
	            }
		    });
		    graph.addPlotter(new Metrics.Plotter("TNT Explosions") {
	            @Override
	            public int getValue() {
	            	return statics.getTNTExplosionsNerfed();
	            }
		    });
		    graph.addPlotter(new Metrics.Plotter("TNT Damage") {
	            @Override
	            public int getValue() {
	            	return statics.getTNTDamageNerfed();
	            }
		    });
		    // Used permissions systems
		    Graph graph2 = metrics.createGraph("Permisison Plugin Usage");
		    graph2.addPlotter(new Metrics.Plotter(getPermissionsManager().getUsedPermissionsSystemType().getName()) {
	            @Override
	            public int getValue() {
	            	return 1;
	            }
		    });
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
			e.printStackTrace();
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Run the command trough the command handler
		CommandHandler ch = new CommandHandler();
		return ch.onCommand(sender, cmd, commandLabel, args);
	}
}
