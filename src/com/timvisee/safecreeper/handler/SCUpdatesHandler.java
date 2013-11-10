package com.timvisee.safecreeper.handler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import com.timvisee.safecreeper.SafeCreeper;

public class SCUpdatesHandler {
	
	private static String APP_NAME = "SafeCreeper";
	private static String BUKKIT_FEED_URL = "https://api.curseforge.com/servermods/files?projectIds=";
	private static int BUKKIT_FEED_PROJECT_ID = 34718;
	private static String MANUAL_DOWNLOAD_URL = "http://dev.bukkit.org/server-mods/safe-creeper/";
	
	private JSONArray feedData = null;
	
	/**
	 * Constructor
	 */
	public SCUpdatesHandler() {
		boolean checkUpdates = SafeCreeper.instance.getConfig().getBoolean("updateChecker.enabled", true);
		init(checkUpdates);
	}
	
	/**
	 * Constructor
	 * @param checkUpdates True to refresh updates data immediately
	 */
	public SCUpdatesHandler(boolean checkUpdates) {
		init(checkUpdates);
	}
	
	/**
	 * Initialize
	 * @param checkUpdates True to check for updates imediately
	 */
	private void init(boolean checkUpdates) {
		if(!checkUpdates)
			return;
		
		System.out.println("[" + APP_NAME + "] Checking for updates...");
		
		// Immediately refresh updates data
		refreshBukkitUpdatesFeedData();
		
		// Notify for updates
		boolean notifyUpdates = SafeCreeper.instance.getConfig().getBoolean("updateChecker.notifyForUpdatesInConsole", true);
		if(notifyUpdates && isUpdateAvaiable())
			System.out.println("[" + APP_NAME + "] A Safe Creeper update is available!");
		
		// Auto download and install updates
		boolean autoInstallUpdates = SafeCreeper.instance.getConfig().getBoolean("updateChecker.autoInstallUpdates", true);
		if(autoInstallUpdates && isUpdateAvaiable())
			downloadUpdate();
	}
	
	/**
	 * Refresh the bukkit updates feed data
	 * @return False if failed
	 */
	public boolean refreshBukkitUpdatesFeedData() {
		try {
			// Set up a connection to the Bukkit feed
			URL url = new URL(BUKKIT_FEED_URL + BUKKIT_FEED_PROJECT_ID);
	        final URLConnection conn = url.openConnection();
	        
	        conn.setConnectTimeout(5000);
	        conn.addRequestProperty("User-Agent", "SafeCreeper Update Checker (by Tim Visee)");
	        conn.setDoOutput(true);
	
	        final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        final String response = reader.readLine();
	
	        feedData = (JSONArray) JSONValue.parse(response);
	        
	        // Everything seems to be right, return true
	        return true;
	        
		} catch (final IOException e) {
            if (e.getMessage().contains("HTTP response code: 403"))
            	System.out.println("[" + APP_NAME + "] Invalid API key!");
            else
                System.out.println("[" + APP_NAME + "] Failed to connect to dev.bukkit.org!");
            e.printStackTrace();
        }
		
		// Something went wrong, return false
		return false;
	}
	
	/**
	 * Check whether any bukkit updates feed data is loaded
	 * @return False if not
	 */
	public boolean isBukkitUpdatesFeedDataLoaded() {
		return (this.feedData != null);
	}
	
	/**
	 * Check whether there's a compatible update available or not
	 * @return True if there's a newer compatible version available
	 */
	public boolean isUpdateAvaiable() {
		return isUpdateAvailable(true);
	}
	
	/**
	 * Check whether there's an update available or not
	 * @param compatibleOnly True to ignore incompatible updates
	 * @return True if there's a newer version available
	 */
	public boolean isUpdateAvailable(boolean compatibleOnly) {
		// Get the newest available versoin
		String newestVer = getNewestVersion(compatibleOnly);
		
		// Make sure the newest version is not just an empty string
		if(newestVer.trim().length() == 0)
			return false;
		
		// Compare the version numbers
		return isNewerVersion(getCurrentVersion(), newestVer);
	}
	
	/**
	 * Return the newest available version number.
	 * @param compatibleOnly True to return a compatible version only.
	 * @return Newest available version number, or null.
	 */
	public String getNewestVersion(boolean compatibleOnly) {
		List<String> vers = getAvailableVersions(compatibleOnly);
		
		// Make sure the list doesn't equal to null
		if(vers == null)
			return null;
		
		// Make sure the list contains any item
		if(vers.size() <= 0)
			return null;
		
		// Get the newest version number
		return vers.get(0);
	}
	
	/**
	 * Get a list of all available version nubmers.
	 * @return List of available version numbers.
	 */
	public List<String> getAvailableVersions() {
		return getAvailableVersions(false);
	}
	
	/**
	 * Get a list of all available version numbers. Returns the compatible versions if compatibleOnly equals to true. 
	 * @param compatibleOnly True to return the 
	 * @return List of available version numbers. Compatible versions only if compatibleOnly equals to true.
	 */
	public List<String> getAvailableVersions(boolean compatibleOnly) {
		// Make sure the bukkit feed data is loaded
		if(!isBukkitUpdatesFeedDataLoaded()) {
			boolean result = refreshBukkitUpdatesFeedData();
			
			if(!result)
				return null;
		}

        // Check whether any file was found in the feed
        if (feedData.size() == 0)
            return null;
        
        List<String> vers = new ArrayList<String>();
        
        // Add each version nubmer to the list
        for(int i = feedData.size() - 1; i >= 0; i--) {
        	// Get the file name and game version of the current entry
        	String fileName = (String) ((org.json.simple.JSONObject) feedData.get(i)).get("fileName");
        	String gameVer = (String) ((org.json.simple.JSONObject) feedData.get(i)).get("gameVersion");
        	
        	// Parse the current version of the file
        	String ver = fileName.replace("Safe", "").replace("Creeper", "").replace(".zip", "").replace(".jar","").replace("_", "").trim();
        	
        	// Make sure this version is compatible
        	if(compatibleOnly) {
        		String recBukkitVer = gameVer.replace("-SNAPSHOT", "").replace("CB", "").trim();
        		String curBukkitVer = getCurrentBukkitVersion().replace("-SNAPSHOT", "").replace("CB", "").trim();
        		
        		if(isOlderVersion(recBukkitVer, curBukkitVer))
        			continue;
        	}
        	
        	// Add the version to the list
        	vers.add(ver);
        }
        
        // Return the list of versions
        return vers;
	}
	
	/**
	 * Get the download URL of the newest compatible available version.
	 * @param compatibleOnly True to get the download link to the newest compatible version
	 * @return Download link to the newest compatible version. May return an empty string when no download URL was found.
	 */
	public String getNewestDownload() {
		return getNewestDownload(true);
	}
	
	/**
	 * Get the download URL of the newest available version.
	 * @param compatibleOnly True to get the download link to the newest compatible version
	 * @return Download link to the newest or newest compatible version. May return an empty string when no download URL was found.
	 */
	public String getNewestDownload(boolean compatibleOnly) {
		// Make sure the bukkit feed data is loaded
		if(!isBukkitUpdatesFeedDataLoaded()) {
			boolean result = refreshBukkitUpdatesFeedData();
			
			if(!result)
				return null;
		}

        // Check whether any file was found in the feed
        if (feedData.size() == 0)
            return null;
        
        // Add each version nubmer to the list
        for(int i = feedData.size() - 1; i >= 0; i--) {
        	// Get the game version and download URL of the current entry version
        	String gameVer = (String) ((org.json.simple.JSONObject) feedData.get(i)).get("gameVersion");
        	String downloadUrl = (String) ((org.json.simple.JSONObject) feedData.get(i)).get("downloadUrl");
        	
        	// Make sure this version is compatible
        	if(compatibleOnly) {
        		String recBukkitVer = gameVer.replace("-SNAPSHOT", "").replace("CB", "").trim();
        		String curBukkitVer = getCurrentBukkitVersion().replace("-SNAPSHOT", "").replace("CB", "").trim();
        		
        		if(isOlderVersion(recBukkitVer, curBukkitVer))
        			continue;
        	}
        	
        	// Return the download URL of the current file
        	return downloadUrl;
        }
        
        // No download found, return an empty string
        return "";
	}
	
	/**
	 * Get the current running Safe Creeper version
	 * @return current Safe Creeper version
	 */
	public String getCurrentVersion() {
		return SafeCreeper.instance.getVersion();
	}
	
	/**
	 * Get the current running (Craft)Bukkit version
	 * @return current (Craft)Bukkit version
	 */
	public String getCurrentBukkitVersion() {
		return Bukkit.getBukkitVersion();
	}
	
	/**
	 * Get the current Minecraft version the server is running on
	 * @return current Minecraft version
	 */
	public String getCurrentMinecraftVersion() {
		final String bukkitVer = Bukkit.getBukkitVersion().trim();
		final String mcVer = bukkitVer.split("-")[0];
		return mcVer;
	}
	
	/**
	 * Get the manual download URL
	 * @return manual download URL
	 */
	public String getManualDownloadUrl() {
		return MANUAL_DOWNLOAD_URL;
	}
	
	/**
	 * Download the newest available compatible update
	 * @return True if any update was downloaded
	 */
	public boolean downloadUpdate() {
		return downloadUpdate(true);
	}
	
	/**
	 * Download the newest available update
	 * @param compatibleOnly True to ignore incompatible files
	 * @return True if any file was downloaded
	 */
	public boolean downloadUpdate(boolean compatibleOnly) {
		// Get the download URL of the newest available compatible version
		String downloadUrl = getNewestDownload(true);
		
		// The download URL may not be an empty string
		if(downloadUrl.equals(""))
			return false;
		
		// Define the file path to save the update to and get the logger
		String fileExtention = getPathExtension(downloadUrl);
		
		// Make sure any file extension is set
		if(fileExtention.equals(""))
			fileExtention = ".jar";
		
		File updatePath = new File("plugins/SafeCreeper/updates/SafeCreeper" + fileExtention);
		Logger log = SafeCreeper.instance.getServer().getLogger();
		
		// Download the update
		try {
			
			// Make sure the download is hosted on a allowed host (Required cause of Bukkit rules)
			/*if(!ALLOWED_DOWNLOAD_HOSTS.contains(getDomainName(downloadUrl))) {
				log.info("[" + APP_NAME + "] The host the update is hosted on is not allowed, can't download update!");
				return;
			}*/
			
			downloadFile(log, downloadUrl, updatePath);
			
			// TODO: Make sure the file was downloaded correctly!
			
			return true;
			
		} catch (IOException e) {
			log.info("[" + APP_NAME + "] An error occured while downloading an update!");
			e.printStackTrace();
			
		}/* catch (URISyntaxException e) {
			log.info("[" + APP_NAME + "] An error occured while downloading update!");
			e.printStackTrace();
		}*/
		
		// Something went wrong, return false
		return false;
	}
	
	/**
	 * Check if any update has been downloaded
	 * @return False if not
	 */
	public boolean isUpdateDownloaded() {
		File updatedFilePath = new File("plugins/SafeCreeper/updates/SafeCreeper.jar");
		File updatedFilePathZip = new File("plugins/SafeCreeper/updates/SafeCreeper.zip");
		
		return (updatedFilePath.exists() || updatedFilePathZip.exists());
	}
	
	/**
	 * Install a downloaded update
	 */
	public void installUpdate() {
		// Make sure any update has been downloaded
		if(!isUpdateDownloaded()) {
			System.out.println("[" + APP_NAME + "] No update available to install!");
			return;
		}
		
		// Keep track of the time
		long t = System.currentTimeMillis();
		
		// Show a status message
		System.out.println("[" + APP_NAME + "] Installing update...");

		File pluginFile = new File("plugins/SafeCreeper.jar");
		File updatedFilePath = new File("plugins/SafeCreeper/updates/SafeCreeper.jar");
		File updatedFilePathZip = new File("plugins/SafeCreeper/updates/SafeCreeper.zip");
		
		if(updatedFilePath.exists()) {
			// Copy the update
			copyFile(updatedFilePath, pluginFile);
			
			// Calculate installation duration
			long duration = System.currentTimeMillis() - t;
			
			// Show a status message
			System.out.println("[" + APP_NAME + "] Update installed, took " + String.valueOf(duration) + " ms!");
			System.out.println("[" + APP_NAME + "] Server reload required!");
			
		} else if(updatedFilePathZip.exists()) {
			try {
				ZipFile updatedFileZip = new ZipFile(updatedFilePathZip);
				InputStream fileFromZip = updatedFileZip.getInputStream(updatedFileZip.getEntry("SafeCreeper.jar"));
				
				// Copy the update
				copyFile(fileFromZip, pluginFile);
				
				// Close the zip file
				updatedFileZip.close();
				
				// Calculate installation duration
				long duration = System.currentTimeMillis() - t;
				
				// Show a status message
				System.out.println("[" + APP_NAME + "] Update installed, took " + String.valueOf(duration) + " ms!");
				System.out.println("[" + APP_NAME + "] Server reload required!");
				
			} catch (ZipException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Remove all update files
	 */
	public void removeUpdateFiles() {
		File updatesFolder = new File("plugins/SafeCreeper/updates");
		
		// Remove all files
		try {
			delete(updatesFolder);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Download a file from an URL
	 * @param log logger to log to
	 * @param url URL to download the file from
	 * @param f file path to download the file to
	 * @throws IOException
	 */
	private void downloadFile(Logger log, String url, File f) throws IOException {
		downloadFile(log, new URL(url), f);
	}
	
	/**
	 * Download a file from an URL
	 * @param log logger to log to
	 * @param url URL to download the file from
	 * @param f file path to download the file to
	 * @throws IOException
	 */
	private void downloadFile(Logger log, URL url, File f) throws IOException {
		long t = System.currentTimeMillis();
		
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
		log.info("[" + APP_NAME + "] Downloading " + f.getName() + " (" + size / 1024 + "kb) ...");
		
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
				log.info("[" + APP_NAME + "] Downloading... " + ((int) ((double) downloaded / (double) size * 100d) + "%"));
				msgs++;
			}
		}
		
		// Close the streams
		in.close();
		out.close();

		// Calculate download duration
		long duration = System.currentTimeMillis() - t;
		
		// Show a status message
		if(duration >= 1000)
			log.info("[" + APP_NAME + "] " + f.getName() + " succesfully downloaded, took " + String.valueOf((double) (duration) / 1000) + " s!");
		else
			log.info("[" + APP_NAME + "] " + f.getName() + " succesfully downloaded, took " + String.valueOf(duration) + " ms!");
	}

	/**
	 * Copy a file
	 * @param from file to copy
	 * @param to path to copy file to
	 */
	private void copyFile(File from, File to) {
		InputStream in = null;
		try {
			in = new FileInputStream(from);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		copyFile(in, to);
	}

	/**
	 * Copy an input stream into a file
	 * @param in input stream to copy from
	 * @param file path to copy input stream to
	 */
	private void copyFile(InputStream in, File to) {
		// Make sure the input isn't null
		if(in == null)
			return;
		
	    try {
	        OutputStream out = new FileOutputStream(to);
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
	 * Delete a file or folder
	 * @param file file or folder to delete
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void delete(File file) throws FileNotFoundException, IOException {
		if(!file.exists())
			return;
		
		// Checks if file is a directory
		if (file.isDirectory()) {
		   // Gathers files in directory
		   List<File> b = Arrays.asList(file.listFiles());
		   
		   // Recursively deletes all files and sub-directories
		   for (File entry : b)
			   delete(entry);
		   
		   // Deletes original sub-directory file
		   file.delete();
		} else
		   file.delete();
	}
	
	/**
	 * Get the extension of a path
	 * @return URL Extension
	 */
	private String getPathExtension(String url) {
		return url.substring(url.lastIndexOf("."));
	}
	
	/**
	 * Compare two version numbers
	 * @param current first version number
	 * @param lastCheck second version number
	 * @return true if second version number is larger
	 */
	private boolean isNewerVersion(String current, String lastCheck) {
        String s1 = normalisedVersion(current);
        String s2 = normalisedVersion(lastCheck);
        int cmp = s1.compareTo(s2);
        return (cmp < 0);
    }
	
	/**
	 * Compare two version numbers
	 * @param current first version number
	 * @param lastCheck second version number
	 * @return true if first version number is larger
	 */
	private boolean isOlderVersion(String current, String lastCheck) {
		return isNewerVersion(lastCheck, current);
	}

	/**
	 * Compare two version numbers
	 * @param current first version number
	 * @param lastCheck second version number
	 * @return true if server versions equals
	 */
	public boolean isSameVersion(String current, String lastCheck) {
		String s1 = normalisedVersion(current);
        String s2 = normalisedVersion(lastCheck);
        int cmp = s1.compareTo(s2);
        return (cmp == 0);
	}
	
	/**
	 * Normalize version number
	 * @param version version number to normalize
	 * @return normalized version number
	 */
	private String normalisedVersion(String version) {
        return normalisedVersion(version, ".", 6);
    }

	/**
	 * Normalize version number
	 * @param version version number to normalize
	 * @param sep seperation char
	 * @param maxWidth max width
	 * @return normalized version number
	 */
	private String normalisedVersion(String version, String sep, int maxWidth) {
        String[] split = Pattern.compile(sep, Pattern.LITERAL).split(version);
        StringBuilder sb = new StringBuilder();
        for (String s : split)
            sb.append(String.format("%" + maxWidth + 's', s));
        return sb.toString();
    }
}
