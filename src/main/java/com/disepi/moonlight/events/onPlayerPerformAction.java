package com.disepi.moonlight.events;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.types.AuthInputAction;
import cn.nukkit.network.protocol.types.PlayerActionType;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.PacketUtils;

import java.util.List;

public class onPlayerPerformAction implements Listener {

    // Listens for PlayerActionPackets. These are packets that get sent when you start sprinting, sneaking, etc.

    public void handleActions(Player player, List<PlayerActionPacket> packets) {
        PlayerData data = Moonlight.getData(player);
        for(var packet : packets) {
            if(packet.action == PlayerActionType.START_JUMP.ordinal()) {
                data.jumpTicks = 20;
            }

            Moonlight.checks.forEach(check -> check.check(packet, data, player));
        }
    }

    @EventHandler
    public void onPlayerPerformAction(DataPacketReceiveEvent event) {
        PlayerData data = Moonlight.getData(event.getPlayer());
        if (data == null) return;

        if(event.getPacket() instanceof PlayerAuthInputPacket packet) {
            var actions = PacketUtils.getPlayerActions(event.getPlayer(), packet);
            for(var action : actions) {
                if(!PacketUtils.VALID_BLOCK_ACTIONS.contains(PlayerActionType.fromOrNull(action.action))) {
                    event.setCancelled(true); //drop packet if it appears to be invalid
                    return;
                }
            }
            handleActions(event.getPlayer(), actions);
        }
        else if(event.getPacket() instanceof PlayerActionPacket packet) {
            if(PlayerActionType.fromOrNull(packet.action) == null || packet.entityId != event.getPlayer().getId()) {
                event.setCancelled(true);
                event.getPlayer().kick("Invalid Packet.", false);
                return;
            }
            handleActions(event.getPlayer(), List.of(packet));
        }
    }
}
