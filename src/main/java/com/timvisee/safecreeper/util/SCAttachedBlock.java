package com.timvisee.safecreeper.util;

import com.timvisee.safecreeper.block.SCBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class SCAttachedBlock {

    public SCAttachedBlock() {
    }

    public static boolean isAttached(Block b) {
        return isAttached(new SCBlock(b));
    }

    public static boolean isAttached(SCBlock b) {
        return isAttached(b.getType());
    }

    public static boolean isAttached(Material type) {
        if(type == null)
            return false;

        Material[] mats = new Material[]{
                Material.WOOD_DOOR,
                Material.IRON_DOOR_BLOCK,

                Material.SAPLING,
                Material.SAND,
                Material.GRAVEL,
                Material.BED_BLOCK,
                Material.POWERED_RAIL,
                Material.DETECTOR_RAIL,
                Material.GRASS,
                Material.DEAD_BUSH,
                Material.PISTON_BASE,
                Material.PISTON_STICKY_BASE,
                Material.PISTON_EXTENSION,
                Material.YELLOW_FLOWER,
                Material.RED_ROSE,
                Material.BROWN_MUSHROOM,
                Material.RED_MUSHROOM,
                Material.TORCH,
                Material.FIRE,
                Material.REDSTONE_WIRE,
                Material.WHEAT,
                Material.SIGN_POST,
                Material.WOODEN_DOOR,
                Material.LADDER,
                Material.RAILS,
                Material.WALL_SIGN,
                Material.LEVER,
                Material.STONE_PLATE,
                Material.WOOD_PLATE,
                Material.REDSTONE_TORCH_OFF,
                Material.REDSTONE_TORCH_ON,
                Material.STONE_BUTTON,
                Material.SNOW,
                Material.CACTUS,
                Material.SUGAR_CANE,
                Material.CAKE_BLOCK,
                Material.DIODE_BLOCK_OFF,
                Material.DIODE_BLOCK_ON,
                Material.TRAP_DOOR,
                Material.PUMPKIN_STEM,
                Material.MELON_STEM,
                Material.VINE,
                Material.WATER_LILY,
                Material.NETHER_WARTS,
                Material.COCOA,
                Material.TRIPWIRE_HOOK,
                Material.TRIPWIRE,
                Material.FLOWER_POT,
                Material.CARROT,
                Material.POTATO,
                Material.WOOD_BUTTON,
                Material.ANVIL,
                Material.GOLD_PLATE,
                Material.IRON_PLATE,
                Material.REDSTONE_COMPARATOR_OFF,
                Material.REDSTONE_COMPARATOR_ON,
                Material.ACTIVATOR_RAIL
        };

        for(Material entry : mats)
            if(entry.equals(type))
                return true;

        return false;
    }

    public static List<Block> getBlockBase(Block b) {
        return getBlockBase(b, b.getType(), b.getData());
    }

    public static List<Block> getBlockBase(Block b, Material type, int data) {
        // Make sure the block or the type is not null
        if(b == null || type == null)
            return null;

        // Initialize the list
        List<Block> bases = new ArrayList<Block>();

        switch(type) {
            case SAPLING:
            case SAND:
            case GRAVEL:
            case POWERED_RAIL:
            case DETECTOR_RAIL:
            case GRASS:
            case DEAD_BUSH:
            case YELLOW_FLOWER:
            case RED_ROSE:
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
            case FIRE:
            case REDSTONE_WIRE:
            case WHEAT:
            case SIGN_POST:
            case WOODEN_DOOR:
            case RAILS:
            case LEVER:
            case STONE_PLATE:
            case WOOD_PLATE:
            case SNOW:
            case CACTUS:
            case SUGAR_CANE:
            case CAKE_BLOCK:
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
            case PUMPKIN_STEM:
            case MELON_STEM:
            case WATER_LILY:
            case NETHER_WARTS:
            case TRIPWIRE:
            case FLOWER_POT:
            case CARROT:
            case POTATO:
            case ANVIL:
            case GOLD_PLATE:
            case IRON_PLATE:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
            case ACTIVATOR_RAIL:
                bases.add(b.getRelative(BlockFace.DOWN));
                break;

            case WOOD_DOOR:
            case IRON_DOOR_BLOCK:
                if(data < 8)
                    bases.add(b.getRelative(BlockFace.UP));
                else
                    bases.add(b.getRelative(BlockFace.DOWN));
                break;

            case BED_BLOCK:
                bases.add(b.getRelative(BlockFace.DOWN));
                if(data == 0 || data == 4 || data == 10)
                    bases.add(b.getRelative(BlockFace.SOUTH));
                if(data == 1 || data == 5 || data == 11)
                    bases.add(b.getRelative(BlockFace.WEST));
                if(data == 2 || data == 6 || data == 8)
                    bases.add(b.getRelative(BlockFace.NORTH));
                if(data == 3 || data == 7 || data == 9)
                    bases.add(b.getRelative(BlockFace.EAST));
                break;

            case PISTON_BASE:
            case PISTON_STICKY_BASE:
                if(data == 6)
                    bases.add(b.getRelative(BlockFace.DOWN));
                if(data == 7)
                    bases.add(b.getRelative(BlockFace.UP));
                if(data == 8)
                    bases.add(b.getRelative(BlockFace.EAST));
                if(data == 9)
                    bases.add(b.getRelative(BlockFace.WEST));
                if(data == 10)
                    bases.add(b.getRelative(BlockFace.NORTH));
                if(data == 11)
                    bases.add(b.getRelative(BlockFace.SOUTH));
                break;

            case PISTON_EXTENSION:
                if(data == 0 || data == 6)
                    bases.add(b.getRelative(BlockFace.UP));
                if(data == 1 || data == 7)
                    bases.add(b.getRelative(BlockFace.DOWN));
                if(data == 2 || data == 8)
                    bases.add(b.getRelative(BlockFace.SOUTH));
                if(data == 3 || data == 9)
                    bases.add(b.getRelative(BlockFace.NORTH));
                if(data == 4 || data == 10)
                    bases.add(b.getRelative(BlockFace.EAST));
                if(data == 5 || data == 11)
                    bases.add(b.getRelative(BlockFace.WEST));
                break;

            case TORCH:
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON:
                if(data == 0)
                    bases.add(b.getRelative(BlockFace.DOWN));
                if(data == 1)
                    bases.add(b.getRelative(BlockFace.WEST));
                if(data == 2)
                    bases.add(b.getRelative(BlockFace.EAST));
                if(data == 3)
                    bases.add(b.getRelative(BlockFace.NORTH));
                if(data == 4)
                    bases.add(b.getRelative(BlockFace.SOUTH));
                if(data == 5)
                    bases.add(b.getRelative(BlockFace.DOWN));
                if(data == 6)
                    bases.add(b.getRelative(BlockFace.DOWN));
                break;

            case LADDER:
            case WALL_SIGN:
                if(data == 2)
                    bases.add(b.getRelative(BlockFace.SOUTH));
                if(data == 3)
                    bases.add(b.getRelative(BlockFace.NORTH));
                if(data == 4)
                    bases.add(b.getRelative(BlockFace.EAST));
                if(data == 5)
                    bases.add(b.getRelative(BlockFace.WEST));
                break;

            case STONE_BUTTON:
            case WOOD_BUTTON:
                if(data == 1 || data == 5)
                    bases.add(b.getRelative(BlockFace.WEST));
                if(data == 2 || data == 6)
                    bases.add(b.getRelative(BlockFace.EAST));
                if(data == 3 || data == 7)
                    bases.add(b.getRelative(BlockFace.NORTH));
                if(data == 4 || data == 8)
                    bases.add(b.getRelative(BlockFace.SOUTH));
                break;

            case TRIPWIRE_HOOK:
                if(data == 0 || data == 4 || data == 8)
                    bases.add(b.getRelative(BlockFace.NORTH));
                if(data == 1 || data == 5 || data == 9)
                    bases.add(b.getRelative(BlockFace.EAST));
                if(data == 2 || data == 6 || data == 10)
                    bases.add(b.getRelative(BlockFace.SOUTH));
                if(data == 3 || data == 7 || data == 11)
                    bases.add(b.getRelative(BlockFace.WEST));
                break;

            case TRAP_DOOR:
                if(data == 0 || data == 4 || data == 8 || data == 12)
                    bases.add(b.getRelative(BlockFace.SOUTH));
                if(data == 1 || data == 5 || data == 9 || data == 13)
                    bases.add(b.getRelative(BlockFace.NORTH));
                if(data == 2 || data == 6 || data == 10 || data == 14)
                    bases.add(b.getRelative(BlockFace.EAST));
                if(data == 3 || data == 7 || data == 11 || data == 15)
                    bases.add(b.getRelative(BlockFace.WEST));
                break;

            case VINE:
                if(data >= 8) {
                    bases.add(b.getRelative(BlockFace.SOUTH));
                    data -= 8;
                }
                if(data >= 4) {
                    bases.add(b.getRelative(BlockFace.NORTH));
                    data -= 4;
                }
                if(data >= 2) {
                    bases.add(b.getRelative(BlockFace.WEST));
                    data -= 2;
                }
                if(data >= 1)
                    bases.add(b.getRelative(BlockFace.EAST));
                bases.add(b.getRelative(BlockFace.UP));
                break;

            case COCOA:
                if(data == 0 || data == 4 || data == 8)
                    bases.add(b.getRelative(BlockFace.SOUTH));
                if(data == 1 || data == 5 || data == 9)
                    bases.add(b.getRelative(BlockFace.WEST));
                if(data == 2 || data == 6 || data == 10)
                    bases.add(b.getRelative(BlockFace.NORTH));
                if(data == 3 || data == 7 || data == 11)
                    bases.add(b.getRelative(BlockFace.EAST));
                break;

            default:
                break;
        }

        return bases;
    }
}
