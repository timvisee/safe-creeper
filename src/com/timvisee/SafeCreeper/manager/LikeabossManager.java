package com.timvisee.safecreeper.manager;

import org.bukkit.entity.Entity;

import com.timvisee.safecreeper.SafeCreeper;

import cam.Likeaboss;
import cam.entity.LabEntityManager;

public class LikeabossManager {
	
	private boolean labEnabled = false;
	
	public LikeabossManager() {
		setup();
	}
	
	public void setup() {
		Likeaboss lab = (Likeaboss) SafeCreeper.instance.getServer().getPluginManager().getPlugin("Likeaboss");
        
        if(lab == null) {
        	SafeCreeper.instance.getSCLogger().info("Disabling Likeaboss usage, Likeaboss not found.");
        	this.labEnabled = false;
            return;
        }
        SafeCreeper.instance.getSCLogger().info("Hooked into Likeaboss!");
        this.labEnabled = true;
	}
	
	public boolean isBoss(Entity e) {
		if(this.labEnabled)
			return (LabEntityManager.getBoss(e) != null);
		return false;
	}
}
