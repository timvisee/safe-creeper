package com.timvisee.safecreeper.block.state;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.block.SCBlockLocation;
import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTManager;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffectType;

public class SCBeaconState extends SCBlockState {

    /**
     * The NBT tag name for the primary effect ID.
     */
    public static final String NBT_PRIMARY_EFFECT_TAG = "Primary";

    /**
     * The NBT tag name for the secondary effect ID.
     */
    public static final String NBT_SECONDARY_EFFECT_TAG = "Secondary";

    /**
     * The configuration key used to store the primary beacon effect.
     */
    public static final String CONFIG_PRIMARY_EFFECT_KEY = "effects.primary";

    /**
     * The configuration key used to store the secondary beacon effect.
     */
    public static final String CONFIG_SECONDARY_EFFECT_KEY = "effects.secondary";

    /**
     * The ID of the primary beacon effect.
     */
    private int primaryEffectId = 0;

    /**
     * The ID of the secondary beacon effect.
     */
    private int secondaryEffectId = 0;

    /**
     * Constructor
     *
     * @param b Beacon block
     */
    // TODO: Make a method for the data reading?
    public SCBeaconState(Beacon b) {
        // Construct the parent class
        super(b.getBlock());

        // Make sure the NBT API plugin is hooked
        if(!SafeCreeper.instance.getPowerNBTHandler().isHooked()) {
            SafeCreeper.instance.getSCLogger().debug("Unable to store Beacon block state, no supported NBT plugin available!");
            return;
        }

        // Get the NBT API manager
        NBTManager api = SafeCreeper.instance.getPowerNBTHandler().getNBTManager();

        // Get the NBT compound for the beacon block to allow NBT tag management
        NBTCompound c = api.read(b.getBlock());

        // Get and store the beacon effects
        primaryEffectId = c.getInt(NBT_PRIMARY_EFFECT_TAG);
        secondaryEffectId = c.getInt(NBT_SECONDARY_EFFECT_TAG);
    }

    /**
     * Constructor
     *
     * @param b Beacon block
     */
    public SCBeaconState(Block b) {
        this((Beacon) b.getState());
    }

    /**
     * Constructor.
     *
     * @param loc               The location of the beacon block.
     * @param primaryEffectId   The ID of the primary beacon effect.
     * @param secondaryEffectId The ID of the secondary beacon effect.
     */
    public SCBeaconState(SCBlockLocation loc, int primaryEffectId, int secondaryEffectId) {
        // Construct the parent class
        super(loc, Material.BEACON);

        // Store the effects
        this.primaryEffectId = primaryEffectId;
        this.secondaryEffectId = secondaryEffectId;
    }

    /**
     * Load the data in a configuration section.
     *
     * @param configSection Configuration section to store the data in.
     */
    public static SCBeaconState load(ConfigurationSection configSection) {
        // Make sure the param is not null
        if(configSection == null)
            return null;

        // Get the block location
        ConfigurationSection locSection = configSection.getConfigurationSection("loc");
        SCBlockLocation loc = SCBlockLocation.load(locSection);

        // Load the beacon effects from the configuration
        int primaryEffectId = configSection.getInt(CONFIG_PRIMARY_EFFECT_KEY);
        int secondaryEffectId = configSection.getInt(CONFIG_SECONDARY_EFFECT_KEY);

        // Construct the container state and return the instance
        return new SCBeaconState(loc, primaryEffectId, secondaryEffectId);
    }

    /**
     * Get the beacon block instance.
     *
     * @return Beacon block.
     */
    public Beacon getBeacon() {
        return (Beacon) getBlock().getState();
    }

    /**
     * Get the block state type
     */
    public SCBlockStateType getStateType() {
        return SCBlockStateType.BEACON;
    }

    /**
     * Get the ID of the primary the beacon effect.
     *
     * @return Effect ID.
     */
    public int getPrimaryEffectId() {
        return primaryEffectId;
    }

    /**
     * Set the ID of the primary beacon effect.
     *
     * @param primaryEffectId Effect ID.
     */
    public void setPrimaryEffectId(int primaryEffectId) {
        this.primaryEffectId = primaryEffectId;
    }

    /**
     * Get the ID of the secondary beacon effect.
     *
     * @return Effect ID.
     */
    public int getSecondaryEffectId() {
        return secondaryEffectId;
    }

    /**
     * Set the ID of the secondary beacon effect.
     *
     * @param secondaryEffectId Effect ID.
     */
    public void setSecondaryEffectId(int secondaryEffectId) {
        this.secondaryEffectId = secondaryEffectId;
    }

    /**
     * Set the primary beacon effect.
     *
     * @param potionEffect Effect ID.
     */
    public void setPrimaryEffect(PotionEffectType potionEffect) {
        this.primaryEffectId = potionEffect.getId();
    }

    /**
     * Set the secondary beacon effect.
     *
     * @param potionEffect Effect ID.
     */
    public void setSecondaryEffect(PotionEffectType potionEffect) {
        this.secondaryEffectId = potionEffect.getId();
    }

    /**
     * Apply the block state to the block
     *
     * @return True if succeed.
     */
    public boolean apply() {
        if(!super.apply())
            return false;

        // Make sure the NBT API plugin is hooked
        if(!SafeCreeper.instance.getPowerNBTHandler().isHooked()) {
            SafeCreeper.instance.getSCLogger().debug("Unable to restore Beacon block state, no supported NBT plugin available!");
            return true;
        }

        // Get the beacon
        Beacon b = getBeacon();

        // Get the NBT API manager
        NBTManager api = SafeCreeper.instance.getPowerNBTHandler().getNBTManager();

        // Get the NBT compound for the beacon block to allow NBT tag management
        NBTCompound c = api.read(b.getBlock());

        // Set the beacon effects using NBT
        c.put(NBT_PRIMARY_EFFECT_TAG, primaryEffectId);
        c.put(NBT_SECONDARY_EFFECT_TAG, secondaryEffectId);

        // Write the NBT to the block, and update the block afterwards
        api.write(b.getBlock(), c);
        b.update();

        // Return true
        return true;
    }

    /**
     * Save the data in a configuration section.
     *
     * @param configSection Configuration section to store the data in.
     */
    public void save(ConfigurationSection configSection) {
        // Make sure the param is not null
        if(configSection == null)
            return;

        // Save the main data from the parent class
        super.save(configSection);

        // Store the container contents
        configSection.set(CONFIG_PRIMARY_EFFECT_KEY, this.primaryEffectId);
        configSection.set(CONFIG_SECONDARY_EFFECT_KEY, this.secondaryEffectId);
    }
}
