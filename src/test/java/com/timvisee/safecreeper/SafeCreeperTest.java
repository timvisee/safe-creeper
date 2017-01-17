package com.timvisee.safecreeper;

import org.junit.Test;

import org.junit.Assert;

public class SafeCreeperTest {

    @Test
    public void shouldHavePluginName() {
        Assert.assertTrue(SafeCreeper.PLUGIN_NAME.length() > 0);
    }

    @Test
    public void shouldHaveVersionName() {
        Assert.assertTrue(SafeCreeper.PLUGIN_VERSION_NAME.length() > 0);
    }

    @Test
    public void shouldHaveVersionCode() {
        //noinspection ConstantConditions
        Assert.assertTrue(SafeCreeper.PLUGIN_VERSION_CODE > 0);
    }
}
