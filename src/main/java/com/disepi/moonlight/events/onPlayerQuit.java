package com.disepi.moonlight.events;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class onPlayerQuit implements Listener {
    // Called upon player quit. It cleans up after the player has disconnected.
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerData data = Moonlight.getData(event.getPlayer());
        if (data.fake != null) {
            data.destructFakePlayer();
        }
        Moonlight.removeData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if(event.getReasonEnum().equals(PlayerKickEvent.Reason.FLYING_DISABLED))
            event.setCancelled(true); // Remove kicks for flying is not enabled, as this is supposed to be handled by the anticheat itself.
    }
}
