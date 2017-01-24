package com.timvisee.safecreeper.listener;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.safecreeper.handler.SCConfigHandler;
import com.timvisee.safecreeper.manager.SCDestructionRepairManager;
import com.timvisee.safecreeper.util.SCAttachedBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SCBlockListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block b = event.getBlock();
        Material bt = b.getType();
        Location l = b.getLocation();
        World w = event.getBlock().getWorld();

        switch(bt) {
            case WATER:
            case STATIONARY_WATER:
                // Could a player place water
                if(!hasBypassPermission(event.getPlayer(), "WaterControl", "CanPlaceWater", false))
                    if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "WaterControl", "CanPlaceWater", true, true, l))
                        event.setCancelled(true);
                break;

            case LAVA:
            case STATIONARY_LAVA:
                // Could a player place lava
                if(!hasBypassPermission(event.getPlayer(), "LavaControl", "CanPlaceLava", false))
                    if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "LavaControl", "CanPlaceLava", true, true, l))
                        event.setCancelled(true);
                break;

            case TNT:
                // Could a player place a TNT block
                if(!hasBypassPermission(event.getPlayer(), "TNTControl", "CanPlaceTNT", false))
                    if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "TNTControl", "CanPlaceTNT", true, true, l))
                        event.setCancelled(true);
                break;

            default:
                break;
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        Material bt = b.getType();
        Location l = b.getLocation();
        World w = event.getBlock().getWorld();

        switch(bt) {
            case TNT:
                // Could a player break a TNT block
                if(!hasBypassPermission(event.getPlayer(), "TNTControl", "CanBreakTNT", false))
                    if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "TNTControl", "CanBreakTNT", true, true, l))
                        event.setCancelled(true);
                break;

            default:
                break;
        }
    }

    @EventHandler
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        World w = event.getBlock().getWorld();
        Location l = event.getBlock().getLocation();
        Entity e = event.getEntity();
        BlockState newState = event.getNewState();

        // Get the current control name
        String controlName = SafeCreeper.instance.getConfigHandler().getControlName(e, "OtherMobControl");

        // Is the entity a snowman that can spawn snow layers
        if(e instanceof Snowman) {
            // The snowman tries to spawn snow
            if(newState.getType().equals(Material.SNOW)) {
                final boolean canCreateSnow = SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, controlName, "CanCreateSnow", true, true, l);
                if(!canCreateSnow)
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        // Get some constants
        final Block b = event.getBlock();
        final Location l = b.getLocation();
        final World w = b.getWorld();

        // Get the configuration handler
        final SCConfigHandler configHandler = SafeCreeper.instance.getConfigHandler();

        // Could fire burn/break blocks
        if(!configHandler.getOptionBoolean(w, "FireControl", "EnableBlockFire", true, true, l))
            event.setCancelled(true);

        else {
            // Check whether to rebuild blocks
            final boolean rebuildBlocks = configHandler.getOptionBoolean(w, "FireControl", "DestructionRebuild.Enabled", false, true, l);

            // Return if we shouldn't rebuild
            if(!rebuildBlocks)
                return;

            // Get the chance, return if the chance is false
            final boolean chance = configHandler.getOptionChance(w, "FireControl", "DestructionRebuild.RebuildChance", 1, true, l);
            if(!chance)
                return;

            // Get the rebuild delay
            double rebuildDelay = configHandler.getOptionDouble(w, "FireControl", "DestructionRebuild.RebuildDelay", 60, true, l);

            // Add the block to rebuild
            SafeCreeper.instance.getDestructionRepairManager().addBlock(b, rebuildDelay);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Block b = event.getBlock();
        Location l = b.getLocation();
        World w = b.getWorld();

        switch(event.getCause()) {
            case FIREBALL:
                // Could a player use flint and steel to ignite blocks
                if(event.getPlayer() == null)
                    break;

                if(!hasBypassPermission(event.getPlayer(), "FirechargeControl", "EnableFireCharge", false))
                    if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "FirechargeControl", "EnableFireCharge", true, true, l))
                        event.setCancelled(true);
                break;

            case FLINT_AND_STEEL:
                // Could a player use flint and steel to ignite blocks
                if(event.getPlayer() == null)
                    break;

                if(!hasBypassPermission(event.getPlayer(), "FlintAndSteelControl", "EnableFlintAndSteel", false))
                    if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "FlintAndSteelControl", "EnableFlintAndSteel", true, true, l))
                        event.setCancelled(true);
                break;

            case LAVA:
                // Could lava cause fire
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "LavaControl", "LavaFire", true, true, l))
                    event.setCancelled(true);
                break;

            case LIGHTNING:
                // Could lightning ignite blocks
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "LightningControl", "IgniteBlocks", true, true, l))
                    event.setCancelled(true);
                break;

            case SPREAD:
                // Could fire spread out
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "FireControl", "FireSpread", true, true, l))
                    event.setCancelled(true);
                break;

            default:
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block b = event.getBlock();
        Block toBlock = event.getToBlock();
        Location l = b.getLocation();
        World w = b.getWorld();

        SCDestructionRepairManager drm = SafeCreeper.instance.getDestructionRepairManager();

        switch(b.getType()) {
            case WATER:
            case STATIONARY_WATER:
                // This is water, check if water can spread
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "WaterControl", "WaterSpread", true, true, l))
                    event.setCancelled(true);
                else {
                    if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "WaterControl", "InfiniteWater", false, true, l))
                        toBlock.setData((byte) 0);

                    if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "WaterControl", "SpreadUpwards", false, true, l)) {
                        BlockFace[] faces = new BlockFace[]{
                                BlockFace.NORTH, BlockFace.EAST,
                                BlockFace.SOUTH, BlockFace.WEST};

                        for(BlockFace f : faces) {
                            Block relBlock = toBlock.getRelative(f);
                            Block aboveBlock = relBlock.getRelative(BlockFace.UP);

                            if(!relBlock.isLiquid() && !relBlock.isEmpty()) {
                                if(aboveBlock.isEmpty())
                                    aboveBlock.setType(b.getType());
                            }
                        }
                    }
                }
                break;

            case LAVA:
            case STATIONARY_LAVA:
                // This is lava, check if lava can spread
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "LavaControl", "LavaSpread", true, true, l))
                    event.setCancelled(true);
                else {
                    if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "LavaControl", "InfiniteLava", false, true, l))
                        toBlock.setData((byte) 0);

                    BlockFace[] faces = new BlockFace[]{
                            BlockFace.NORTH, BlockFace.EAST,
                            BlockFace.SOUTH, BlockFace.WEST};

                    if(SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "LavaControl", "SpreadUpwards", false, true, l)) {
                        for(BlockFace f : faces) {
                            Block relBlock = toBlock.getRelative(f);
                            Block aboveBlock = relBlock.getRelative(BlockFace.UP);

                            if(!relBlock.isLiquid() && !relBlock.isEmpty()) {
                                if(aboveBlock.isEmpty())
                                    aboveBlock.setType(b.getType());
                            }
                        }
                    }

				/*for(BlockFace f : faces) {
					Block relBlock = toBlock.getRelative(f);
					
					if(relBlock.getType().equals(Material.PORTAL)) {
						Por
					}
				}*/
                }
                break;

            default:
                break;
        }

        // If anything is moving into something that is going to be repaired, block the stream
        if(drm.isDestroyed(toBlock))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        Block b = event.getBlock();
        ItemStack item = event.getItem();
        Location l = b.getLocation();
        World w = b.getWorld();

        switch(item.getType()) {
            case WATER_BUCKET:
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "WaterControl", "CanPlaceWater", true, true, l))
                    event.setCancelled(true);
                break;

            case LAVA_BUCKET:
                // This is lava, check if lava can spread
                if(!SafeCreeper.instance.getConfigHandler().getOptionBoolean(w, "LavaControl", "CanPlaceLava", true, true, l))
                    event.setCancelled(true);
                break;

            default:
                break;
        }
    }

    @EventHandler
    public void onPhysics(BlockPhysicsEvent event) {
        Block b = event.getBlock();

        if(b == null)
            return;

        if(SCAttachedBlock.isAttached(b)) {
            SCDestructionRepairManager drm = SafeCreeper.instance.getDestructionRepairManager();

            List<Block> bases = SCAttachedBlock.getBlockBase(b);

            for(Block base : bases) {
                if(drm.isDestroyed(base)) {

                    if(b.getType().equals(Material.TORCH))
                        drm.addBlock(base, 5);
                    else
                        event.setCancelled(true);
                }
            }
        }
    }

    public boolean hasBypassPermission(Player player, String controlName, String bypassName, boolean def) {
        if(!SafeCreeper.instance.getPermissionsManager().isEnabled())
            return def;
        return SafeCreeper.instance.getPermissionsManager().hasPermission(player, "safecreeper.bypass." + controlName + "." + bypassName, def);
    }
}