package com.timvisee.safecreeper.listener;

import com.timvisee.safecreeper.SafeCreeper;
import com.timvisee.tvnlib.event.entity.TVNLibEntityLostTargetEvent;
import com.timvisee.tvnlib.event.entity.TVNLibEntityReachedTargetEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SCTVNLibListener implements Listener {

    @EventHandler
    public void onTVNLibEntityTargetReached(TVNLibEntityReachedTargetEvent event) {
        Entity e = event.getLivingEntity();

        if(e instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) e;

            if(SafeCreeper.instance.getLivingEntityReviveManager().isReviver(le)) {
                SafeCreeper.instance.getLivingEntityReviveManager().onReviverReachedTarget(le);
            }
        }
    }

    @EventHandler
    public void onTVNLibEntityTargetReached(TVNLibEntityLostTargetEvent event) {
        Entity e = event.getLivingEntity();

        if(e instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) e;

            if(SafeCreeper.instance.getLivingEntityReviveManager().isReviver(le)) {
                SafeCreeper.instance.getLivingEntityReviveManager().onReviverLostTarget(le);
            }
        }
    }
}
