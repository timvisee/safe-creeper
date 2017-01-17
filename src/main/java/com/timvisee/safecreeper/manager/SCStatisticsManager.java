package com.timvisee.safecreeper.manager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.*;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;

public class SCStatisticsManager {

    private static final int REPORT_APP_ID = 1;
    private static final String REPORT_APP_NAME = "SafeCreeper";

    private static final String REPORT_URL = "http://statistics.timvisee.com/post.php";
    private static final String REPORT_VER = "0.1";

    private static String sUID = "";

    private Plugin p;
    private boolean firstPost = true;

    /**
     * Constructor
     *
     * @param p Plugin
     */
    public SCStatisticsManager(Plugin p) {
        this.p = p;

        // Initialize
        init();
    }

    private static void encodeDataPair(final StringBuilder buffer, final String key, final String value) throws UnsupportedEncodingException {
        buffer.append('&').append(encode(key)).append('=').append(encode(value));
    }

    private static String encode(final String text) throws UnsupportedEncodingException {
        return URLEncoder.encode(text, "UTF-8");
    }

    /**
     * Initialize
     */
    public void init() {
        // Generate a new session UID
        if(SCStatisticsManager.sUID.length() == 0)
            generateNewSessionUID();
    }

    /**
     * Generate a new unique session ID
     */
    private void generateNewSessionUID() {
        Random r = new Random();

        StringBuilder uid = new StringBuilder();

        for(int i = 0; i < 4; i++)
            uid.append(10000000 + r.nextInt(99999999));

        SCStatisticsManager.sUID = uid.toString();
    }

    public String getSessionUID() {
        return SCStatisticsManager.sUID;
    }

    public Plugin getPlugin() {
        return this.p;
    }

    public void setPlugin(Plugin p) {
        this.p = p;
    }

    public boolean postStatistics() {
        try {
            // Get the plugins description
            final PluginDescriptionFile description = this.p.getDescription();

            // Construct the post data
            final StringBuilder data = new StringBuilder();

            data.append(encode("suid")).append('=').append(encode(this.getSessionUID()));
            encodeDataPair(data, "reportVersion", REPORT_VER);
            encodeDataPair(data, "firstPost", firstPost ? "1" : "0");
            encodeDataPair(data, "pluginId", Integer.toString(REPORT_APP_ID));
            encodeDataPair(data, "pluginName", REPORT_APP_NAME);
            encodeDataPair(data, "pluginVersion", description.getVersion());
            encodeDataPair(data, "serverVersion", Bukkit.getVersion());
            encodeDataPair(data, "onlinePlayers", Integer.toString(Bukkit.getServer().getOnlinePlayers().size()));

            // Send the server address with the post request if set in the configuration file
            if(this.p.getConfig().getBoolean("statistics.postServerAddress", false)) {
                encodeDataPair(data, "serverIp", this.p.getServer().getIp());
                encodeDataPair(data, "serverPort", Integer.toString(this.p.getServer().getPort()));
            }

            firstPost = false;

            // Create the url and connect to the site
            URL url = new URL(REPORT_URL);
            URLConnection conn;

            // Mineshafter creates a socks proxy which doesn't support POST requests, bypass the proxy
            if(isMineshafterPresent())
                conn = url.openConnection(Proxy.NO_PROXY);
            else
                conn = url.openConnection();

            conn.setConnectTimeout(5000);
            conn.addRequestProperty("User-Agent", "SafeCreeper Statistics Reporter (by Tim Visee)");
            conn.setDoOutput(true);

            // Write the data
            final OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data.toString());
            writer.flush();

            // Now read the response
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String response = reader.readLine();

            // close resources
            writer.close();
            reader.close();

            if(response == null || response.startsWith("ERR"))
                throw new IOException(response); //Throw the exception

            return true;

        } catch(Exception e) {
            System.out.println("[SafeCreeper] Failed to connect to the Safe Creeper statistics servers!");
            // TODO: Clarify error using e.getMessage();
            //e.printStackTrace();
        }

        return false;
    }

    private boolean isMineshafterPresent() {
        try {
            Class.forName("mineshafter.MineServer");
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
