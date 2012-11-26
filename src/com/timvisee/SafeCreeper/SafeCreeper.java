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
import com.timvisee.safecreeper.command.CommandHandler;
import com.timvisee.safecreeper.entity.SCLivingEntityManager;
import com.timvisee.safecreeper.listener.*;
import com.timvisee.safecreeper.manager.*;
import com.timvisee.safecreeper.update.FileUpdater;
import com.timvisee.safecreeper.update.UpdateChecker;

public class SafeCreeper extends JavaPlugin {
	public static final Logger log = Logger.getLogger("Minecraft");
	
	// Safe Creeper static instance
	public static SafeCreeper instance;
	
	// Listeners
	private final SafeCreeperBlockListener blockListener = new SafeCreeperBlockListener();
	private final SafeCreeperEntityListener entityListener = new SafeCreeperEntityListener();
	private final SafeCreeperPlayerListener playerListener = new SafeCreeperPlayerListener();
	private final SafeCreeperWorldListener worldListener = new SafeCreeperWorldListener();
	
	// Config file and folder paths
	private File globalConfigFile = new File("plugins/SafeCreeper/global.yml");
	private File worldConfigsFolder = new File("plugins/SafeCreeper/worlds");
	
	// Managers and Handlers
	private PermissionsManager pm;
	private SafeCreeperConfigManager cm = null;
	private SCLivingEntityManager lem;
	private MobArenaHandler maHandler;
	private LikeabossManager labHandler;
	private SafeCreeperStaticsManager statics = new SafeCreeperStaticsManager();
	
	// Update Checker
	public boolean isUpdateAvailable = false;
	public String newestVersion = "1.0";
	
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
		((FileUpdater) new FileUpdater()).updateFiles();
		
		// Setup managers and handlers
	    setupPermissionsManager();
	    setupLivingEntityManager();
	    setupMobArenaHandler();
	    setupPVPArena();
	    setupFactions();
	    setupLabHandler();
		setupMetrics();
		
		// Register event listeners
		pm.registerEvents(this.blockListener, this);
		pm.registerEvents(this.entityListener, this);
		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.worldListener, this);
		
		// Check for updates
		checkUpdates();
		
		// Test - Beginning of custom mob abilities!
		/*getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
					List<Player> onlinePlayers = Arrays.asList(getServer().getOnlinePlayers());
					if(onlinePlayers.size() > 0) {
						Player p = onlinePlayers.get(0);
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
			}, 20, 20);*/
				
		// Plugin sucesfuly enabled, show console message
		PluginDescriptionFile pdfFile = getDescription();
		
		// Calculate the load duration
		long duration = System.currentTimeMillis() - t;
		
		log.info("[SafeCreeper] Safe Creeper v" + pdfFile.getVersion() + " enabled, took " + String.valueOf(duration) + " ms!");
	}
	
	public void onDisable() {
		// Save all entity data
		if(getLivingEntityManager() != null)
			getLivingEntityManager().save();
		
		// Cancel all running tasks
		getServer().getScheduler().cancelTasks(this);
		
		// Plugin disabled, show console message
		log.info("[SafeCreeper] Safe Creeper Disabled");
	}
    
	public void debug(String text) {
		if(this.debug) {
			log.info("[SafeCreeper] [Debug] " + text);
		}
	}
	
	public void setupConfigManager() {
		this.cm = new SafeCreeperConfigManager(this, globalConfigFile, worldConfigsFolder);
	}
	
	public SafeCreeperConfigManager getConfigManager() {
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
	
	public SafeCreeperStaticsManager getStaticsManager() {
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
        	log.info("[SafeCreeper] Disabling MobArena usage, MobArena not found.");
            return;
        }
 
        maHandler = new MobArenaHandler();
        log.info("[SafeCreeper] Hooked into MobArena!");
    }
    
    public MobArenaHandler getMobArenaHandler() {
    	return this.maHandler;
    }
   
    public void setupPVPArena() {
        Plugin paPlugin = (PVPArena) getServer().getPluginManager().getPlugin("pvparena");
        
        if (paPlugin == null) {
        	log.info("[SafeCreeper] Disabling PVPArena usage, PVPArena not found.");
            return;
        }
 
        log.info("[SafeCreeper] Hooked into PVPArena!");
    }
   
    public void setupFactions() {
        Plugin fPlugin = (Plugin) getServer().getPluginManager().getPlugin("Factions");
        
        if (fPlugin == null) {
        	log.info("[SafeCreeper] Disabling Factions usage, Factions not found.");
            return;
        }
 
        log.info("[SafeCreeper] Hooked into Factions!");
    }
    
    public void setupLabHandler() {
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
	
	public boolean checkUpdates() {
		UpdateChecker scuc = new UpdateChecker(this);
		isUpdateAvailable = scuc.checkUpdates();
		newestVersion = scuc.getLastVersion();
		
		if(isUpdateAvailable) {
			log.info("[SafeCreeper] New version available, version " + newestVersion + ".");
		}
		
		return isUpdateAvailable;
	}
	
	public void checkConigFilesExist() throws Exception {
		if(!getDataFolder().exists()) {
			log.info("[SafeCreeper] Creating new Safe Creeper folder");
			getDataFolder().mkdirs();
		}
		File configFile = new File(getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			log.info("[SafeCreeper] Generating new config file");
			copy(getResource("config.yml"), configFile);
		}
		if(!globalConfigFile.exists()) {
			log.info("[SafeCreeper] Generating new global file");
			copy(getResource("global.yml"), globalConfigFile);
		}
		if(!worldConfigsFolder.exists()) {
			log.info("[SafeCreeper] Generating new 'worlds' folder");
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
