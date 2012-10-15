package com.timvisee.SafeCreeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.timvisee.SafeCreeper.Metrics.Graph;

public class SafeCreeper extends JavaPlugin {
	public static final Logger log = Logger.getLogger("Minecraft");
	
	// Listeners
	private final SafeCreeperBlockListener blockListener = new SafeCreeperBlockListener(this);
	private final SafeCreeperEntityListener entityListener = new SafeCreeperEntityListener(this);
	private final SafeCreeperPlayerListener playerListener = new SafeCreeperPlayerListener(this);
	private final SafeCreeperWorldListener worldListener = new SafeCreeperWorldListener(this);
	//private final SafeCreeperExplosionRepairCore explosionRepairCore = new SafeCreeperExplosionRepairCore(this);
	
	// Config files
	private File globalConfigFile = new File("plugins/SafeCreeper/global.yml");
	private File worldConfigsFolder = new File("plugins/SafeCreeper/worlds");
	private FileConfiguration globalConfig;
	private HashMap<String, FileConfiguration> worldConfigs = new HashMap<String, FileConfiguration>();
	
	// Update Checker
	boolean isUpdateAvailable = false;
	String newestVersion = "1.0";
	
	boolean debug = false;
	
	// variable to disable the other explosions for a little, little while (otherwise some explosions are going to be looped)
	public boolean disableOtherExplosions = false;
	
	/*
	 * 0 = none
	 * 1 = PermissionsEx
	 * 2 = PermissionsBukkit
	 * 3 = bPermissions
	 * 4 = Essentials Group Manager
	 * 5 = Permissions
	 */
	private int permissionsSystem = 0;
	private PermissionManager pexPermissions;
	private PermissionHandler defaultPermsissions;
	private GroupManager groupManagerPermissions;
	
	// Statics Manager
	private SafeCreeperStaticsManager statics = new SafeCreeperStaticsManager();
	
	public void onEnable() {
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
		
		// Setup permissions
		setupPermissions();
		
		// Register event listeners
		pm.registerEvents(this.blockListener, this);
		pm.registerEvents(this.entityListener, this);
		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.worldListener, this);
		
		// Load all config files
		log.info("[SafeCreeper] Loading config files...");
		reloadAllConfigs();
		log.info("[SafeCreeper] Global config file loaded");
		log.info("[SafeCreeper] " + String.valueOf(worldConfigs.size()) + " world config files loaded");
		
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
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
			e.printStackTrace();
		}
		
		// Plugin sucesfully enabled, show console message
		PluginDescriptionFile pdfFile = getDescription();
		log.info("[SafeCreeper] Safe Creeper v" + pdfFile.getVersion() + " Enabled");
	}
	
	public void onDisable() {
		// Plugin disabled, show console message
		log.info("[Safe Creeper] Safe Creeper Disabled");
	}
	
	public SafeCreeperStaticsManager getStaticsManager() {
		return this.statics;
	}
	
	public void debug(String text) {
		if(this.debug) {
			log.info("[SafeCreeper] [Debug] " + text);
		}
	}
	
	public boolean checkUpdates() {
		SafeCreeperUpdateChecker scuc = new SafeCreeperUpdateChecker(this);
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
	
	private void setupPermissions() {
		// Reset permissions
		permissionsSystem = 0;
		
		if(!getConfig().getBoolean("usePermissions", true)) {
			permissionsSystem = 0;
			System.out.println("[SafeCreeper] Permissions usage disabled in config file!");
			if(useBypassPermissions()) {
				System.out.println("[SafeCreeper] Bypass permissions auto disabled!");
			} else {
				System.out.println("[SafeCreeper] Bypass permissions disabled!");
			}
			return;
		}
		
		// Check PermissionsEx system
		Plugin testPex = this.getServer().getPluginManager().getPlugin("PermissionsEx");
		if(testPex != null) {
			pexPermissions = PermissionsEx.getPermissionManager();
			if(pexPermissions != null) {
				permissionsSystem = 1;
				
				System.out.println("[SafeCreeper] Hooked into PermissionsEx!");
				if(useBypassPermissions()) {
					System.out.println("[SafeCreeper] Bypass permissions enabled!");
				} else {
					System.out.println("[SafeCreeper] Bypass permissions disabled!");
				}
				return;
			}
		}
		
		// Check PermissionsBukkit system
		Plugin testBukkitPerms = this.getServer().getPluginManager().getPlugin("PermissionsBukkit");
		if(testBukkitPerms != null) {
			permissionsSystem = 2;
			System.out.println("[SafeCreeper] Hooked into PermissionsBukkit!");
			if(useBypassPermissions()) {
				System.out.println("[SafeCreeper] Bypass permissions enabled!");
			} else {
				System.out.println("[SafeCreeper] Bypass permissions disabled!");
			}
			return;
		}
		
		// Check bPermissions system
		// Not available yet!
		
		// Check Essentials Group Manager system
		final PluginManager pluginManager = getServer().getPluginManager();
		final Plugin GMplugin = pluginManager.getPlugin("GroupManager");
		if (GMplugin != null && GMplugin.isEnabled())
		{
			permissionsSystem = 4;
			groupManagerPermissions = (GroupManager)GMplugin;
            System.out.println("[SafeCreeper] Hooked into Essentials Group Manager!");
			if(useBypassPermissions()) {
				System.out.println("[SafeCreeper] Bypass permissions enabled!");
			} else {
				System.out.println("[SafeCreeper] Bypass permissions disabled!");
			}
            return;
		}
		
		// Check Permissions system
	    Plugin testPerms = this.getServer().getPluginManager().getPlugin("Permissions");
	    if (this.defaultPermsissions == null) {
	        if (testPerms != null) {
	        	permissionsSystem = 5;
	            this.defaultPermsissions = ((Permissions) testPerms).getHandler();
	            System.out.println("[SafeCreeper] Hooked into Permissions!");
	            return;
	        }
	    }
	    
	    // None of the permissions systems worked >:c.
	    permissionsSystem = 0;
	    System.out.println("[SafeCreeper] No Permissions system found! Permissions disabled!");
		if(useBypassPermissions()) {
			System.out.println("[SafeCreeper] Bypass permissions auto disabled!");
		} else {
			System.out.println("[SafeCreeper] Bypass permissions disabled!");
		}
	}
	
	public boolean usePermissions() {
		if(getConfig().getBoolean("usePermissions", true)) {
			return true;
		}
		return false;
	}
	
	public boolean useBypassPermissions() {
		if(getConfig().getBoolean("useBypassPermissions", false)) {
			return true;
		}
		return false;
	}
	
	public boolean isPermissionsSystemEnabled() {
		if(permissionsSystem == 0) {
			return false;
		}
		return true;
	}
	
	public int getPermissionsSystem() {
		return permissionsSystem;
	}
	
	public boolean hasPermission(Player player, String permissionNode) {
		return hasPermission(player, permissionNode, player.isOp());
	}
	
	public boolean hasPermission(Player player, String permissionNode, boolean def) {
		if(!usePermissions()) {
			return def;
		}
		if(!isPermissionsSystemEnabled()) {
			return def;
		}
		
		// Using PermissionsEx
		if(getPermissionsSystem() == 1) {
			PermissionUser user  = PermissionsEx.getUser(player);
			return user.has(permissionNode);
		}
		
		// Using PermissionsBukkit
		if(getPermissionsSystem() == 2) {
			return player.hasPermission(permissionNode);
		}
		
		// Using bPemissions
		// Comming soon!
		
		// Using Essentials Group Manager
		if(getPermissionsSystem() == 4) {
			final AnjoPermissionsHandler handler = groupManagerPermissions.getWorldsHolder().getWorldPermissions(player);
			if (handler == null)
			{
				return false;
			}
			return handler.has(player, permissionNode);
		}
		
		// Using Permissions
		if(getPermissionsSystem() == 5) {
			return this.defaultPermsissions.has(player, permissionNode);
		}

		return def;
	}
	
	/*private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
     
        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }
        
        return (WorldGuardPlugin) plugin;
    }
    
    public boolean worldGuardEnabled() {
    	if(getWorldGuard() == null) {
    		return false;
    	} else {
    		return true;
    	}
    }*/
	
    public boolean reloadAllConfigs() {
    	// Load all the needed config files
    	// Load global config file
    	if(reloadGlobalConfig() == false) {
    		return false;
    	}
    	// Load world config files
    	if(reloadWorldConfigs() == false) {
    		return false;
    	}
    	return true;
    }
    
    private boolean reloadGlobalConfig() {
    	// Load the global config file into the global config file variable
    	globalConfig = getConfigFromPath(globalConfigFile.getPath(), false);
    	return true;
    }
    
    public FileConfiguration getGlobalConfig() {
    	return globalConfig;
    }
    
    public void setGlobalConfig(FileConfiguration config) {
    	this.globalConfig = config;
    }
    
    public void setWorldConfig(String worldName, FileConfiguration config) {
    	if(this.worldConfigs.containsKey(worldName)) {
    		this.worldConfigs.remove(worldName);
    	}
    	this.worldConfigs.put(worldName, config);
    }
    
    public boolean saveGlobalConfig() {
    	try {
			this.globalConfig.save(globalConfigFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
    }
    
    public boolean saveWorldConfig(String worldName) {
    	try {
    		if(this.worldConfigs.containsKey(worldName)) {
    			File worldFile = new File(worldConfigsFolder, worldName + ".yml");
        		if(!worldFile.exists()) {
        			worldFile.createNewFile();
        		}
        		
    			this.worldConfigs.get(worldName).save(worldFile);
    		}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
    }
    
    private boolean reloadWorldConfigs() {
    	// Load all the world config files
    	// Clear the world config files variable first
    	worldConfigs.clear();
    	
    	File dir = worldConfigsFolder;

    	List<File> worldConfigFolderFiles = Arrays.asList(dir.listFiles());
        
        for(File item : worldConfigFolderFiles) {
    		if(getFileExtention(item.getAbsolutePath()).equals("yml") || getFileExtention(item.getAbsolutePath()).equals("yaml")) {
    			worldConfigs.put(getFileName(item.getAbsolutePath()), getConfigFromPath(item));
    		}
    	}

        return true;
    }
    
    public FileConfiguration getWorldConfig(String worldName) {
    	if(worldConfigExist(worldName)) {
    		return worldConfigs.get(worldName);
    	}
    	return getGlobalConfig();
    }
    
    public boolean worldConfigExist(String worldName) {
    	return worldConfigs.containsKey(worldName);
    }
    
    public List<FileConfiguration> getWorldConfigs() {
    	List<FileConfiguration> worldConfigs = new ArrayList<FileConfiguration>();
    	for(FileConfiguration item : worldConfigs) {
    		worldConfigs.add(item);
    	}
    	return worldConfigs;
    }
    
	// Fuctnion to get a constum configuration file
	public FileConfiguration getConfigFromPath(String filePath, boolean insideDataFolder) {
		if(insideDataFolder) {
			File file = new File(getDataFolder(), filePath);
			return getConfigFromPath(file);
		} else {
			File file = new File(filePath);
			return getConfigFromPath(file);
		}
	}
	
	// Function to get a costum configuration file
	public FileConfiguration getConfigFromPath(File file) {
		FileConfiguration c;
		
		if (file == null) {
		    return null;
		}

	    c = YamlConfiguration.loadConfiguration(file);
	    
	    return c;
	}
	
    public static String getFileExtention(String filename) {
        String fileext;

        // Remove the path upto the filename.
        int lastSeparatorIndex = filename.lastIndexOf(".");
        if (lastSeparatorIndex == -1) {
        	fileext = filename;
        } else {
        	fileext = filename.substring(lastSeparatorIndex + 1);
        }
        return fileext;
    }
    
    public static String getFileName(String s) {
        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return filename;

        return filename.substring(0, extensionIndex);
    }

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if(commandLabel.equalsIgnoreCase("safecreeper") || commandLabel.equalsIgnoreCase("sc")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
				sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("config") || args[0].equalsIgnoreCase("c")) {
				// Check wrong command values
				if(args.length < 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				if(args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("s")) {
					
					// Check permissions
					if(sender instanceof Player) {
						if(!hasPermission((Player) sender, "safecreeper.command.config.set")) {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
							return true;
						}
					}
					
					if(args[2].equalsIgnoreCase("global") || args[2].equalsIgnoreCase("g")) {
						if(args.length != 5) {
							sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
							return true;
						}
						if(getGlobalConfig().isBoolean(args[3].toString())) {
							FileConfiguration config = getGlobalConfig();
							config.set(args[3].toString(), Boolean.parseBoolean(args[4].toString()));
							setGlobalConfig(config);
							saveGlobalConfig();
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + String.valueOf(Boolean.parseBoolean(args[4].toString())) + ChatColor.GOLD + " (boolean)");
							return true;
						} else if(getGlobalConfig().isInt(args[3].toString())) {
							FileConfiguration config = getGlobalConfig();
							config.set(args[3].toString(), Integer.parseInt(args[4].toString()));
							setGlobalConfig(config);
							saveGlobalConfig();
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + String.valueOf(Integer.parseInt(args[4].toString())) + ChatColor.GOLD + " (integer)");
							return true;
						} else if(getGlobalConfig().isString(args[3].toString())) {
							FileConfiguration config = getGlobalConfig();
							config.set(args[3].toString(), args[4].toString());
							setGlobalConfig(config);
							saveGlobalConfig();
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " (string)");
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + args[3].toString());
							sender.sendMessage(ChatColor.DARK_RED + "Invalid path!");
							return true;
						}
						
					} else if(args[2].equalsIgnoreCase("world") || args[2].equalsIgnoreCase("w")) {
						if(args.length != 6) {
							sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
							return true;
						}
						String wname = args[3].toString();
						if(getGlobalConfig().isBoolean(args[4].toString())) {
							if(!worldConfigExist(args[3].toString())) {
								FileConfiguration c;
							    c = new YamlConfiguration();
							    this.worldConfigs.put(args[3].toString(), c);
							    sender.sendMessage(ChatColor.GOLD + "The config file for the world " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " did not exists.");
							    sender.sendMessage(ChatColor.GOLD + "A new file has been created.");
							}
							FileConfiguration config = getWorldConfig(wname);
							config.set(args[4].toString(), Boolean.parseBoolean(args[5].toString()));
							setWorldConfig(wname, config);
							saveWorldConfig(wname);
							sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + String.valueOf(Boolean.parseBoolean(args[5].toString())) + ChatColor.GOLD + " (boolean)");
							return true;
							
						} else if(getGlobalConfig().isInt(args[3].toString())) {
							if(!worldConfigExist(args[3].toString())) {
								FileConfiguration c;
							    c = new YamlConfiguration();
							    this.worldConfigs.put(args[3].toString(), c);
							    sender.sendMessage(ChatColor.GOLD + "The config file for the world " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " did not exists.");
							    sender.sendMessage(ChatColor.GOLD + "A new file has been created.");
							}
							FileConfiguration config = getWorldConfig(wname);
							config.set(args[4].toString(), Integer.parseInt(args[5].toString()));
							setWorldConfig(wname, config);
							saveWorldConfig(wname);
							sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + String.valueOf(Integer.parseInt(args[5].toString())) + ChatColor.GOLD + " (integer)");
							return true;
						} else if(getGlobalConfig().isString(args[3].toString())) {
							if(!worldConfigExist(args[3].toString())) {
								FileConfiguration c;
							    c = new YamlConfiguration();
							    this.worldConfigs.put(args[3].toString(), c);
							    sender.sendMessage(ChatColor.GOLD + "The config file for the world " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " did not exists.");
							    sender.sendMessage(ChatColor.GOLD + "A new file has been created.");
							}
							FileConfiguration config = getWorldConfig(wname);
							config.set(args[4].toString(), args[5].toString());
							setWorldConfig(wname, config);
							saveWorldConfig(wname);
							sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " changed to " + ChatColor.YELLOW + args[5].toString() + ChatColor.GOLD + " (string)");
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + args[4].toString());
							sender.sendMessage(ChatColor.DARK_RED + "Invalid path!");
							return true;
						}
					}
					
				} else if(args[1].equalsIgnoreCase("get") || args[1].equalsIgnoreCase("g")) {
					
					// Check permissions
					if(sender instanceof Player) {
						if(!hasPermission((Player) sender, "safecreeper.command.config.get")) {
							sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
							return true;
						}
					}
					
					if(args[2].equalsIgnoreCase("global") || args[2].equalsIgnoreCase("g")) {
						if(args.length != 4) {
							sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
							return true;
						}
						if(getGlobalConfig().isBoolean(args[3].toString())) {
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + String.valueOf(getGlobalConfig().getBoolean(args[3].toString())) + ChatColor.GOLD + " (boolean)");
							return true;
						} else if(getGlobalConfig().isInt(args[3].toString())) {
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + String.valueOf(getGlobalConfig().getInt(args[3].toString())) + ChatColor.GOLD + " (integer)");
							return true;
						} else if(getGlobalConfig().isString(args[3].toString())) {
							sender.sendMessage(ChatColor.GOLD + "Global: " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + args[3].toString() + ChatColor.GOLD + " (string)");
							return true;
						} else {
							sender.sendMessage(ChatColor.DARK_RED + args[3].toString());
							sender.sendMessage(ChatColor.DARK_RED + "Invalid path!");
							return true;
						}
						
					} else if(args[2].equalsIgnoreCase("world") || args[2].equalsIgnoreCase("w")) {
						if(args.length != 5) {
							sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
							return true;
						}
						if(worldConfigExist(args[3].toString())) {
							String wname = args[3].toString();
							
							if(getWorldConfig(wname).isBoolean(args[4].toString())) {
								sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + String.valueOf(getWorldConfig(wname).getBoolean(args[4].toString())) + ChatColor.GOLD + " (boolean)");
								return true;
							} else if(getWorldConfig(wname).isInt(args[4].toString())) {
								sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + String.valueOf(getWorldConfig(wname).getInt(args[4].toString())) + ChatColor.GOLD + " (integer)");
								return true;
							} else if(getWorldConfig(wname).isString(args[4].toString())) {
								sender.sendMessage(ChatColor.GOLD + wname + ": " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " equals to " + ChatColor.YELLOW + args[4].toString() + ChatColor.GOLD + " (string)");
								return true;
							} else {
								sender.sendMessage(ChatColor.DARK_RED + args[4].toString());
								sender.sendMessage(ChatColor.DARK_RED + "Invalid path! This path doesn't exist.");
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "No world config file found for the world " + ChatColor.GOLD + args[3].toString());
							return true;
						}
					}
				}
				sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("v")) {
				// Check wrong command values
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				PluginDescriptionFile pdfFile = getDescription();
				sender.sendMessage(ChatColor.YELLOW + "This server is running Safe Creeper v" + pdfFile.getVersion());
				sender.sendMessage(ChatColor.YELLOW + "Safe Creeper is made by Tim Visee - timvisee.com");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reload")) {
				// Check wrong command values
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				// Check permission
				if(sender instanceof Player) {
					if(!hasPermission((Player) sender, "safecreeper.command.reload")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				// Setup permissions
				setupPermissions();
				
				// Load all the config files again
				log.info("[SafeCreeper] Loading config files...");
				boolean b = reloadAllConfigs();
				
				// Show a succes message
				if(b) {
					log.info("[SafeCreeper] Global config file loaded");
					log.info("[SafeCreeper] " + String.valueOf(worldConfigs.size()) + " world config files loaded");
					sender.sendMessage(ChatColor.YELLOW + "[Safe Creeper] " + ChatColor.GREEN + "Safe Creeper reloaded!");
				} else {
					log.info("[SafeCreeper] Error while loading configs!");
					sender.sendMessage(ChatColor.YELLOW + "[Safe Creeper] " + ChatColor.DARK_RED + "Error while reloading!");
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reloadpermissions") || args[0].equalsIgnoreCase("reloadperms")) {
				// Check wrong command values
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				// Check permission
				if(sender instanceof Player) {
					if(!hasPermission((Player) sender, "safecreeper.command.reloadperms")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				// Setup permissions
				setupPermissions();
				
				// Show a succes message
				sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] " + ChatColor.GREEN + "Permissions reloaded!");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("checkupdates")) {
				// Check wrong command values
				if(args.length != 1) {
					sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
					return true;
				}
				
				// Check permission
				if(sender instanceof Player) {
					if(!hasPermission((Player) sender, "safecreeper.command.checkupdates")) {
						sender.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
						return true;
					}
				}
				
				// Setup permissions
				sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] Checking for updates...");
				if(checkUpdates()) {
					sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] " + ChatColor.GREEN + "New version found! (v" + newestVersion + ")");
				} else {
					sender.sendMessage(ChatColor.YELLOW + "[SafeCreeper] No new version found!");
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("?")) {
				if(args.length == 2) {
					
					if(args[1].equals("config") || args[1].equals("c")) {
						// View the help
						sender.sendMessage(ChatColor.GREEN + "==========[ SAFE CREEPER HELP ]==========");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " config get global <path> " + ChatColor.WHITE + ": Get global config value");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " config get world <world> <path> " + ChatColor.WHITE + ": Get world config value");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " config set global <path> <value> " + ChatColor.WHITE + ": Set global config value");
						sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " config set world <world> <path> <value> " + ChatColor.WHITE + ": Set world config value");
						sender.sendMessage(" ");
						sender.sendMessage(ChatColor.GOLD + "path " + ChatColor.WHITE + ": The value path. Example: CreeperControl.Enabled");
						sender.sendMessage(ChatColor.GOLD + "world " + ChatColor.WHITE + ": The world name");
						sender.sendMessage(ChatColor.GOLD + "value " + ChatColor.WHITE + ": The new value. A boolean, integer or string.");
						
						return true;
					}
					
				} else {
					// Check wrong command values
					if(args.length != 1) {
						sender.sendMessage(ChatColor.DARK_RED + "Wrong command values!");
						sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
						return true;
					}
					
					// View the help
					sender.sendMessage(ChatColor.GREEN + "==========[ SAFE CREEPER HELP ]==========");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " <help/h/?> " + ChatColor.WHITE + ": View help");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " <help/h/?> config " + ChatColor.WHITE + ": Config command help");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " reload " + ChatColor.WHITE + ": Reload config files");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " reloadperms " + ChatColor.WHITE + ": Reload permissions system");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " <checkupdates/check> " + ChatColor.WHITE + ": Check for updates");
					sender.sendMessage(ChatColor.GOLD + "/" + commandLabel + " <version/ver/v> " + ChatColor.WHITE + ": Check plugin version");
					
					return true;
				}
			}
			
			sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
			sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GOLD + "/" + commandLabel + " help " + ChatColor.YELLOW + "to view help");
			return true;
		}
		
		return false;
	}
}
