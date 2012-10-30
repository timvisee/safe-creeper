package com.timvisee.SafeCreeper.ExplosionRepair;

import java.util.Date;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Painting;

public class SafeCreeperExplosionRepairPainting {
	private Painting painting;
	private Date date;
	private boolean fire;

	public SafeCreeperExplosionRepairPainting(Painting p, Date d, boolean f) {
		painting = p;
		date = d;
		fire = f;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date d) {
		date = d;
	}

	public boolean isBurnt() {
		return fire;
	}

	public Painting getPainting() {
		return painting;
	}

	public World getWorld() {
		return painting.getWorld();
	}

	public Location getLocation() {
		return painting.getLocation();
	}

	public void postPone(int delay) {
		date = new Date(date.getTime() + 1000 * delay);
	}
}
