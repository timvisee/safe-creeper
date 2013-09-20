package com.timvisee.safecreeper.handler.plugin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

import com.timvisee.safecreeper.SCLogger;
import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.tvnlib.TVNLib;
import com.timvisee.tvnlib.api.TVNLibApi;

public class SCTVNLibHandler extends SCPluginHandler {
	
	private static final String PLUGIN_NAME = "TVNLib";
	
	public static TVNLibApi api;
	
	/**
	 * Constructor
	 * @param log Safe Creeper Logger instance
	 */
	public SCTVNLibHandler(SCLogger log) {
		super(PLUGIN_NAME, log);
	}
	
	/**
	 * Try to hook TVNLib
	 */
	public void hook() {
		Plugin TVNLibPlugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
		
        if (TVNLibPlugin == null && !(TVNLibPlugin instanceof TVNLib)) {
        	this.log.info("Disabling TVNativeLib usage, TVNLib not found.");
        	return;
        }
        
		// The TVNLib plugin has to be enabled
        try {
        	if(!TVNLibApi.isEnabled()) {
            	this.log.info("Disabling TVNLib usage, TVNLib not enabled!");
            	return;
        	}
        } catch(Exception ex) {
        	this.log.info("Disabling TVNLib usage, TVNLib not enabled!");
        	return;
        } catch(NoClassDefFoundError ex) {
        	this.log.info("Disabling TVNLib usage, TVNLib not enabled!");
        	return;
        }
        
        // Show a status message
        this.log.info("Hooked into TVNLib v" + TVNLibApi.getVersion() + "!");
        return;
	}
	
	/**
	 * Check if Safe Creeper is hooked into TVNLib
	 */
	public boolean isHooked() {
		return (api != null);
	}
	
	/**
	 * Unhook TVNLib
	 */
	public void unhook() {
        api = null;
        this.log.info("Unhooked TVNLib!");
	}
	
	/**
	 * Check if TVNLib is installed
	 * @return
	 */
	public static boolean isTVNLibInstalled() {
        File TVNLibFile = new File("plugins/TVNLib.jar");
        return TVNLibFile.exists();
	}
	
	/**
	 * Check if TVNLib is loaded
	 * @return
	 */
	public static boolean isTVNLibLoaded() {
		Plugin plugin = SafeCreeper.instance.getServer().getPluginManager().getPlugin(PLUGIN_NAME);
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
				plugin.getServer().getLogger().info("Failed to download TVNLib, please do it manually!");
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
		log.info("Downloading " + f.getName() + " (" + size / 1024 + "kb) ...");
		
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
				log.info("Downloading: " + ((int) ((double) downloaded / (double) size * 100d) + "%"));
				msgs++;
			}
		}
		
		// Close the streams
		in.close();
		out.close();
		
		// Show a status message
		log.info("" + f.getName() + " succesfully downloaded!");
	}
	
	public boolean isEnabled() {
		Plugin plugin = SafeCreeper.instance.getServer().getPluginManager().getPlugin(PLUGIN_NAME);
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
}
