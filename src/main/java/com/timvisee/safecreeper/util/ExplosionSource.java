package com.timvisee.safecreeper.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

// TODO: Does this still have any function?

public class ExplosionSource {

    private Object source;

    /**
     * Constructor
     *
     * @param source Block source
     */
    public ExplosionSource(Block source) {
        this.source = source;
    }

    /**
     * Constructor
     *
     * @param source Entity source
     */
    public ExplosionSource(Entity source) {
        this.source = source;
    }

    /**
     * Constructor
     *
     * @param source Location source
     */
    public ExplosionSource(Location source) {
        this.source = source;
    }

    /**
     * Get the source object
     *
     * @return Source object
     */
    public Object getSource() {
        return this.source;
    }

    /**
     * Get the source as block
     *
     * @return Source as block
     */
    public Block getSourceBlock() {
        if(this.source instanceof Block)
            return (Block) this.source;

        return null;
    }

    /**
     * Get the source as entity
     *
     * @return Source as entity
     */
    public Entity getSourceEntity() {
        if(this.source instanceof Entity)
            return (Entity) this.source;

        return null;
    }

    /**
     * Get the source as location
     *
     * @return Source as location
     */
    public Location getSourceLocation() {
        if(this.source instanceof Location)
            return (Location) this.source;

        return null;
    }

    /**
     * Get the source type
     *
     * @return Explosion source type
     */
    public ExplosionSourceType getSourceType() {
        if(this.source instanceof Block)
            return ExplosionSourceType.BLOCK;

        if(this.source instanceof Entity)
            return ExplosionSourceType.ENTITY;

        if(this.source instanceof Location)
            return ExplosionSourceType.LOCATION;

        return ExplosionSourceType.CUSTOM;
    }

    public enum ExplosionSourceType {
        ENTITY,
        BLOCK,
        LOCATION,
        CUSTOM;
    }
}
