package com.timvisee.SafeCreeper;

//import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
//import org.bukkit.event.world.WorldLoadEvent;

public class SafeCreeperWorldListener implements Listener {
	public static SafeCreeper plugin;

	public SafeCreeperWorldListener(SafeCreeper instance) {
		plugin = instance;
	}
	
	/*@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		World world = event.getWorld();
		
		// Check if there's a config file for this world
		File file = new File(plugin.getDataFolder(), "worlds/" + world.getName() + ".yml");
		if(file.exists() == false) {
			// World config not exists, create one (copy the global one)		
			plugin.createNewWorldConfiguration(world.getName());
		}
	}*/
}
