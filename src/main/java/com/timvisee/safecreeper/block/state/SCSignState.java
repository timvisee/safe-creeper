package com.timvisee.safecreeper.block.state;

import com.timvisee.safecreeper.block.SCBlockLocation;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;

public class SCSignState extends SCBlockState {

    /**
     * Sign contents.
     */
    private String[] lines;

    /**
     * Constructor.
     *
     * @param block Sign.
     */
    public SCSignState(Block block) {
        // Construct the super
        super(block);

        // Remember the text lines
        this.lines = ((Sign) block.getState()).getLines();
    }

    /**
     * Constructor.
     *
     * @param sign Sign.
     */
    public SCSignState(Sign sign) {
        // Construct the super
        super(sign.getBlock());

        // Remember the text lines
        this.lines = sign.getLines();
    }

    /**
     * Constructor.
     *
     * @param loc    Block location.
     * @param type   Block material.
     * @param data   Block data.
     * @param lines  Sign lines.
     */
    public SCSignState(SCBlockLocation loc, Material type, byte data, String[] lines) {
        // Construct the parent class
        super(loc, type, data);

        // Store the sign lines
        this.lines = lines.clone();
    }

    /**
     * Load the data in a configuration section
     *
     * @param configSection Configuration section to store the data in
     */
    public static SCSignState load(ConfigurationSection configSection) {
        // Make sure the param is not null
        if(configSection == null)
            return null;

        // Get the block location
        final ConfigurationSection locSection = configSection.getConfigurationSection("loc");
        final SCBlockLocation loc = SCBlockLocation.load(locSection);

        // Create a variable for the block material
        Material type;

        // Load the material if the proper key is available
        if(configSection.isString("type"))
            type = Material.getMaterial(configSection.getString("type"));

        else if(configSection.isInt("typeId"))
            //noinspection deprecation
            type = Material.getMaterial(configSection.getInt("typeId"));

        else {
            // Show an error message, and return null
            System.out.println("Failed to load stored block state, type is missing.");
            return null;
        }

        // Get the block type ID and data
        byte data = (byte) configSection.getInt("data", 0);

        // Get the block name and the selected command
        List<String> linesList = configSection.getStringList("signLines");

        // Convert the lines list into an array
        String[] lines = linesList.toArray(new String[]{});

        // Construct the sign state and return the instance
        return new SCSignState(loc, type, data, lines);
    }

    /**
     * Get the sign block
     *
     * @return Sign block
     */
    public Sign getSign() {
        return (Sign) getBlock().getState();
    }

    /**
     * Get the lines of the sign
     *
     * @return Sign lines
     */
    public String[] getLines() {
        return this.lines;
    }

    /**
     * Set the sign lines
     *
     * @param lines Sign lines array
     */
    public void setLines(String[] lines) {
        this.lines = lines;
    }

    /**
     * Get the content of a line on the sign
     *
     * @param line Line index
     * @return Content
     */
    public String getLine(int line) {
        return this.lines[line];
    }

    /**
     * Set the text on a line
     *
     * @param line Line index
     * @param text Text to put on the line
     */
    public void setLine(int line, String text) {
        this.lines[line] = text;
    }

    /**
     * Check if a sign is empty or not
     *
     * @return True if the sign was empty
     */
    public boolean isEmptySign() {
        for(String line : this.lines)
            if(!line.equals(""))
                return false;
        return true;
    }

    /**
     * Get the block state type
     */
    public SCBlockStateType getStateType() {
        return SCBlockStateType.SIGN;
    }

    /**
     * Apply the block state to the block
     *
     * @return True if succeed
     */
    public boolean apply() {
        if(!super.apply())
            return false;

        // Get the sign
        Sign s = getSign();

        // Make sure the sign instance is not null
        if(s == null)
            return false;

        // Put the text on the sign
        for(int i = 0; i < 4; i++)
            s.setLine(i, getLine(i));

        // Update the sign to the client
        s.update();

        // Return true
        return true;
    }

    /**
     * Save the data in a configuration section
     *
     * @param configSection Configuration section to store the data in
     */
    public void save(ConfigurationSection configSection) {
        // Make sure the param is not null
        if(configSection == null)
            return;

        // Save the main data from the parent class
        super.save(configSection);

        // Store the sign lines
        configSection.set("signLines", Arrays.asList(this.lines));
    }
}
