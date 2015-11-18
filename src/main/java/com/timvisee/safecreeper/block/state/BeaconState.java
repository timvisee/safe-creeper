package com.timvisee.safecreeper.block.state;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Foodyling
 */
public class BeaconState {
    public static class ReflectionUtil {
        private static final String MC_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        public static Class<?> forClassName(String name) {
            try {
                return Class.forName(name.replace("%MC_VERSION%", MC_VERSION));
            } catch (Throwable error) {
                return null;
            }
        }

        public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
            try {
                return clazz.getMethod(methodName, params);
            } catch (Throwable error) {
                return null;
            }
        }

        public static Field getField(Class<?> clazz, String fieldName) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (Throwable error) {
                return null;
            }
        }
    }

    private static final Class<?> beaconState = ReflectionUtil.forClassName("net.minecraft.server.%MC_VERSION%.TileEntityBeacon"),
            craftBeacon = ReflectionUtil.forClassName("org.bukkit.craftbukkit.%MC_VERSION%.block.CraftBeacon"),
            entityHuman = ReflectionUtil.forClassName("net.minecraft.server.%MC_VERSION%.EntityHuman");

    private static final Field tileEntityBeacon = ReflectionUtil.getField(craftBeacon, "beacon"),
            primary = ReflectionUtil.getField(beaconState, "f"),
            secondary = ReflectionUtil.getField(beaconState, "g");

    private static final Method getName = ReflectionUtil.getMethod(beaconState, "getName"),
            setName = ReflectionUtil.getMethod(beaconState, "a", String.class),
            tier = ReflectionUtil.getMethod(beaconState, "l"),
            getItem = ReflectionUtil.getMethod(beaconState, "getItem", int.class),
            setItem = ReflectionUtil.getMethod(beaconState, "setItem", int.class, ItemStack.class);

    static {
        tileEntityBeacon.setAccessible(true);
        primary.setAccessible(true);
        secondary.setAccessible(true);
    }

    private final Block block;
    private final Object blockState;

    public BeaconState(Block block) {
        this(block.getState());
    }

    public BeaconState(BlockState state) {
        if (craftBeacon.isInstance(state)) {
            try {
                this.block = state.getBlock();
                this.blockState = tileEntityBeacon.get(craftBeacon.cast(state));
            } catch (Throwable error) {
                throw new IllegalArgumentException("BlockState must be a instance of org.bukkit.craftbukkit.block.CraftBeacon");
            }
        } else {
            throw new IllegalArgumentException("BlockState must be a instance of org.bukkit.craftbukkit.block.CraftBeacon");
        }
    }

    public static BeaconState asBeacon(Block block) {
        return new BeaconState(block);
    }

    public static BeaconState asBeacon(BlockState state) {
        return new BeaconState(state);
    }

    public Block getBlock() {
        return block;
    }

    public String getName() {
        try {
            return (String) getName.invoke(blockState);
        } catch (Throwable error) {
            return null;
        }
    }

    public void setName(String name) {
        try {
            setName.invoke(blockState, name);
        } catch (Throwable error) {
            error.printStackTrace();
        }
    }

    public PotionEffectType getPrimary() {
        try {
            return PotionEffectType.getById(primary.getInt(blockState));
        } catch (Throwable error) {
            return null;
        }
    }

    public PotionEffectType getSecondary() {
        try {
            return PotionEffectType.getById(secondary.getInt(blockState));
        } catch (Throwable error) {
            return null;
        }
    }

    public void setPrimary(PotionEffectType type) {
        try {
            primary.setInt(blockState, type.getId());
        } catch (Throwable error) {
            error.printStackTrace();
        }
    }

    public void setSecondary(PotionEffectType type) {
        try {
            secondary.setInt(blockState, type.getId());
        } catch (Throwable error) {
            error.printStackTrace();
        }
    }

    public void setBoth(PotionEffectType type) {
        setPrimary(type);
        setSecondary(type);
    }

    public int getTier() {
        try {
            return ((Integer) tier.invoke(blockState)).intValue();
        } catch (Throwable error) {
            error.printStackTrace();
            return 0;
        }
    }

    public ItemStack getItem() {
        try {
            return (ItemStack) getItem.invoke(blockState, 0);
        } catch (Throwable error) {
            return null;
        }
    }

    public void setItem(ItemStack stack) {
        try {
            setItem.invoke(blockState, 0, stack);
        } catch (Throwable error) {

        }
    }

    public boolean applicableFor(LivingEntity entity) {
        if (entityHuman.isInstance(entity)) {
            return entity.getLocation().distanceSquared(block.getLocation()) <= 4096;
        }
        return false;
    }
}