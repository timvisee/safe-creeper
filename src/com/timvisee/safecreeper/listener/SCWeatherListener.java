package com.timvisee.safecreeper.listener;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.handler.SCConfigHandler;

public class SCWeatherListener implements Listener {
	
	@EventHandler
	public void onLightningStrike(LightningStrikeEvent e) {
		World w = e.getWorld();
		Location l = w.getSpawnLocation();
		SCConfigHandler cm = SafeCreeper.instance.getConfigHandler();
		
		// Make sure it's thundering in this world
		if(!w.isThundering())
			return;
		
		// Check if lightning is allowed in this world while it's thundering
		if(!cm.getOptionBoolean(w, "WeatherControl", "LightningInThunderStorm", true, true, l))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		World w = e.getWorld();
		Location l = w.getSpawnLocation();
		SCConfigHandler cm = SafeCreeper.instance.getConfigHandler();
		Random rand = new Random();
		
		// Should it always rain
		if(cm.getOptionBoolean(w, "WeatherControl", "AlwaysRain", false, true, l)) {
			// Make sure the thunder will not stop if it was thundering
			if(!e.toWeatherState()) {
				e.setCancelled(true);
				return;
			}
		}
		
		// Is rain and clear is allowed in this world
		boolean clearAllowed = true;
		boolean rainAllowed = true;
		if(cm.getOptionBoolean(w, "WeatherControl", "CustomWeatherTypes.Enabled", false, true, l)) {
			clearAllowed = cm.getOptionBoolean(w, "WeatherControl", "CustomWeatherTypes.Clear.Allowed", true, true, l);
			rainAllowed = cm.getOptionBoolean(w, "WeatherControl", "CustomWeatherTypes.Rain.Allowed", true, true, l);
		}
		
		// Check if the weather is changing to rain
		if(e.toWeatherState()) {
			// Make sure rain is allowed, if not cancel the event
			if(!rainAllowed) {
				e.setCancelled(true);
				return;
			}
			
			// Get the default weather duration
			int duration = w.getWeatherDuration();
			
			// Apply the custom weather duration
			if(cm.getOptionBoolean(w, "WeatherControl", "CustomDurations.Enabled", false, true, l)) {
				if(cm.getOptionBoolean(w, "WeatherControl", "CustomDurations.Weather.Enabled", false, true, l)) {
					int minDuration = cm.getOptionInt(w, "WeatherControl", "CustomDurations.Weather.MinDuration", 720, true, l);
					int maxDuration = cm.getOptionInt(w, "WeatherControl", "CustomDurations.Weather.MaxDuration", 1080, true, l);
					
					duration = Math.max(minDuration + rand.nextInt(maxDuration - minDuration), 1);
				}
			}
			
			// Set the weather duration
			w.setWeatherDuration(duration);
		
		} else {
			// The weather is clearing
			// Make sure weather clearing is allowed
			if(!clearAllowed) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onThunderChange(ThunderChangeEvent e) {
		World w = e.getWorld();
		Location l = w.getSpawnLocation();
		SCConfigHandler cm = SafeCreeper.instance.getConfigHandler();
		Random rand = new Random();
		
		// Should it always thunder
		if(cm.getOptionBoolean(w, "WeatherControl", "AlwaysThunderStorm", false, true, l)) {
			// Make sure the thunder will not stop if it was thundering
			if(!e.toThunderState()) {
				e.setCancelled(true);
				return;
			}
		}
		
		// Is thunder storm allowed in this world
		boolean thunderAllowed = true;
		if(cm.getOptionBoolean(w, "WeatherControl", "CustomWeatherTypes.Enabled", false, true, l))
			thunderAllowed = cm.getOptionBoolean(w, "WeatherControl", "CustomWeatherTypes.ThunderStorm.Allowed", true, true, l);
		
		// Check if the weather is changing to thunder
		if(e.toThunderState()) {
			// Make sure thunder is allowed, if not cancel the event
			if(!thunderAllowed) {
				e.setCancelled(true);
				return;
			}
			
			// Get the default thunder duration
			int duration = w.getThunderDuration();
			
			// Apply the custom thunder duration
			if(cm.getOptionBoolean(w, "WeatherControl", "CustomDurations.Enabled", false, true, l)) {
				if(cm.getOptionBoolean(w, "WeatherControl", "CustomDurations.ThunderStorm.Enabled", false, true, l)) {
					int minDuration = cm.getOptionInt(w, "WeatherControl", "CustomDurations.ThunderStorm.MinDuration", 240, true, l);
					int maxDuration = cm.getOptionInt(w, "WeatherControl", "CustomDurations.ThunderStorm.MaxDuration", 360, true, l);
					
					duration = Math.max(minDuration + rand.nextInt(maxDuration - minDuration), 1);
				}
			}
			
			// Set the thunder duration
			w.setThunderDuration(duration);
		}
	}
}
