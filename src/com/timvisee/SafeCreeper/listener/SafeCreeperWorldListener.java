package com.timvisee.safecreeper.listener;

//import org.bukkit.event.EventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
//import org.bukkit.event.world.WorldLoadEvent;

import com.timvisee.safecreeper.SafeCreeper;

public class SafeCreeperWorldListener implements Listener {
	
	@EventHandler
	public void onWorldLoad(WorldLoadEvent e) {
		if(SafeCreeper.instance.getLivingEntityManager() != null)
			SafeCreeper.instance.getLivingEntityManager().removeOldEntities();
	}
}
