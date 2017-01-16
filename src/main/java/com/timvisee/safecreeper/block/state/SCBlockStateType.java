package com.timvisee.safecreeper.block.state;

public enum SCBlockStateType {
    NORMAL("NORMAL"),
    BEACON("BEACON"),
    COMMAND_BLOCK("COMMAND_BLOCK"),
    CONTAINER_BLOCK("CONTAINER_BLOCK"),
    JUKEBOX("JUKEBOX"),
    SIGN("SIGN"),
    SKULL("SKULL"),
    SPAWNER("SPAWNER");

    private String name;

    /**
     * Constructor
     *
     * @param name State name
     */
    SCBlockStateType(String name) {
        this.name = name;
    }

    /**
     * Get the state name
     *
     * @return State name
     */
    public String getName() {
        return this.name;
    }
}
