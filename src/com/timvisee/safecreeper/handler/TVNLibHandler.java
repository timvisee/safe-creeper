package com.timvisee.safecreeper.handler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.tvnlib.TVNLib;
import com.timvisee.tvnlib.api.TVNLibApi;

public class TVNLibHandler {
	
	public static Plugin plugin;
	public static TVNLibApi api;
	private static final String TVNLIB_PLUGIN_NAME = "TVNLib";
	
	public TVNLibHandler(Plugin instance) {
		plugin = instance;
		setup();
	}
	
	public void setup() {
		Plugin TVNLibPlugin = plugin.getServer().getPluginManager().getPlugin(TVNLIB_PLUGIN_NAME);
        if (TVNLibPlugin == null && !(TVNLibPlugin instanceof TVNLib)) {
        	plugin.getLogger().info("[" + plugin.getName() + "] Disabling TVNativeLib usage, TVNLib not found.");
        	return;
        }
        
        // The TVNLib plugin has to be enabled
        try {
        	if(!TVNLibApi.isEnabled()) {
            	plugin.getLogger().info("[" + plugin.getName() + "] Disabling TVNLib usage, TVNLib not enabled!");
            	return;
        	}
        } catch(Exception ex) {
        	plugin.getLogger().info("[" + plugin.getName() + "] Disabling TVNLib usage, TVNLib not enabled!");
        	return;
        } catch(NoClassDefFoundError ex) {
        	plugin.getLogger().info("[" + plugin.getName() + "] Disabling TVNLib usage, TVNLib not enabled!");
        	return;
        }
        
        // Show a status message
        plugin.getLogger().info("[" + plugin.getName() + "] Hooked into TVNLib v" + TVNLibApi.getVersion() + "!");
	}
	
	public static boolean isTVNLibInstalled() {
        File TVNLibFile = new File("plugins/TVNLib.jar");
        return TVNLibFile.exists();
	}
	
	public static boolean isTVNLibLoaded() {
		Plugin plugin = SafeCreeper.instance.getServer().getPluginManager().getPlugin(TVNLIB_PLUGIN_NAME);
        if (plugin == null && !(plugin instanceof TVNLib))
        	return false;
        return true;
	}
	
	/*public static void downloadTVNLib() {
		// Make sure TVNLib isn't already installed
		if(isTVNLibInstalled())
			return;
		
		/*String LibDataUrl = "http://updates.timvisee.com/check.php?app=tvnlib&";
		
		InputStream inputStream;
		try {
			URL test = new URL(LibDataUrl);
			inputStream = test.openStream();
			XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
			
			while (xmlStreamReader.hasNext()) {
				printEventInfo(xmlStreamReader);	
		    }
			xmlStreamReader.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		}* /
		
		PluginManager pm = plugin.getServer().getPluginManager();
		if (pm.getPlugin(TVNLIB_PLUGIN_NAME) == null) {
			try {
				File TVNLibFile = new File("plugins/TVNLib.jar");
				download(plugin.getServer().getLogger(), new URL("http://updates.timvisee.com/check.php?app=tvnlib"), TVNLibFile);
				pm.loadPlugin(TVNLibFile);
				pm.enablePlugin(pm.getPlugin(TVNLIB_PLUGIN_NAME));
			} catch (Exception exception) {
				plugin.getServer().getLogger().info("[" + plugin.getName() + "] Failed to download TVNLib, please do it manually!");
			}
		}
	}*/
	
	/*public static void printEventInfo(XMLStreamReader reader) {
		int eventCode;
		try {
			eventCode = reader.next();
			
			if(reader.isStartElement()) {
				System.out.println("IS START: " + reader.getLocalName());
				System.out.println("IS START: " + reader.getElementText());
			}
 			
			
		    switch (eventCode) {
		    case 1 :
		    	System.out.println("event = START_ELEMENT");
		    	System.out.println("Localname = "+reader.getLocalName());
		    	break;
		    	
		    case 2 :
		    	System.out.println("event = END_ELEMENT");
		    	System.out.println("Localname = " + reader.getLocalName());
		    	break;
		    
		    case 3 :
		    	System.out.println("event = PROCESSING_INSTRUCTION");
		    	System.out.println("PIData = " + reader.getPIData());
		    	break;
		    }
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}*/
	
	public static void download(Logger log, URL url, File f) throws IOException {
		// Make sure the parent folder does exist
		if (!f.getParentFile().exists())
			f.getParentFile().mkdir();
		
		// Delete previous versions of the file
		if (f.exists())
			f.delete();
		
		// Create a new file
		f.createNewFile();
		
		// Show a status message
		final int size = url.openConnection().getContentLength();
		log.info("[" + plugin.getName() + "] Downloading " + f.getName() + " (" + size / 1024 + "kb) ...");
		
		// Download the file
		final InputStream in = url.openStream();
		final OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
		
		final byte[] buffer = new byte[1024];
		int len, downloaded = 0, msgs = 0;
		final long start = System.currentTimeMillis();
		
		// Download the file
		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
			downloaded += len;
			
			// Show downloading process
			if ((int) ((System.currentTimeMillis() - start) / 500) > msgs) {
				log.info("[" + plugin.getName() + "] Downloading: " + ((int) ((double) downloaded / (double) size * 100d) + "%"));
				msgs++;
			}
		}
		
		// Close the streams
		in.close();
		out.close();
		
		// Show a status message
		log.info("[" + plugin.getName() + "] " + f.getName() + " succesfully downloaded!");
	}
	
	public boolean isEnabled() {
		Plugin plugin = SafeCreeper.instance.getServer().getPluginManager().getPlugin(TVNLIB_PLUGIN_NAME);
		try {
			if (plugin == null && !(plugin instanceof TVNLib))
	        	return false;
	        else
	        	return TVNLibApi.isEnabled();
		} catch(Exception ex) {
			return false;
		} catch(NoClassDefFoundError ex) {
			return false;
		}
	}
	
	public boolean livingEntityTargetTo(LivingEntity livingEntity, double x, double y, double z) {
		return TVNLibApi.livingEntityTargetTo(livingEntity, x, y, z);
	}
	
	public boolean livingEntityTargetTo(LivingEntity livingEntity, double x, double y, double z, float speed) {
		return TVNLibApi.livingEntityTargetTo(livingEntity, x, y, z, speed);
	}
	
	@Deprecated
	public void dressMonster(LivingEntity monster, Material head, Material chest, Material legs, Material boots, Material weapon) {
		TVNLibApi.setMonsterEquipment(monster, head, chest, legs, boots, weapon);
	}
}
