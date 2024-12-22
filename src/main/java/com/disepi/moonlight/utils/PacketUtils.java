package com.disepi.moonlight.utils;

import cn.nukkit.Player;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.types.AuthInputAction;
import cn.nukkit.network.protocol.types.PlayerActionType;
import cn.nukkit.network.protocol.types.PlayerBlockActionData;

import java.util.ArrayList;
import java.util.List;

public class PacketUtils {
    public static final List<PlayerActionType> VALID_BLOCK_ACTIONS = List.of(
            PlayerActionType.START_DESTROY_BLOCK,
            PlayerActionType.ABORT_DESTROY_BLOCK,
            PlayerActionType.CONTINUE_DESTROY_BLOCK,
            PlayerActionType.PREDICT_DESTROY_BLOCK,
            PlayerActionType.STOP_DESTROY_BLOCK,
            PlayerActionType.CREATIVE_DESTROY_BLOCK
    );

    public static ArrayList<PlayerActionPacket> getPlayerActions(Player player, PlayerAuthInputPacket packet) {
        var list = new ArrayList<PlayerActionPacket>();
        if(packet.getInputData().contains(AuthInputAction.START_JUMPING)) {
            var actionPacket = new PlayerActionPacket();
            actionPacket.entityId = player.getId();
            actionPacket.face = -1;
            actionPacket.x = (int) packet.getPosition().x;
            actionPacket.y = (int) packet.getPosition().y;
            actionPacket.z = (int) packet.getPosition().z;
            actionPacket.resultPosition = new BlockVector3();
            actionPacket.action = PlayerActionType.START_JUMP.ordinal();
            list.add(actionPacket);
        }

        if(!packet.getInputData().contains(AuthInputAction.PERFORM_BLOCK_ACTIONS))
            return list;
        if(packet.getBlockActionData().isEmpty() || packet.getBlockActionData().size() >= 500)
            return list;

        for(PlayerBlockActionData blockAction : packet.getBlockActionData().values()) {
            var actionPacket = new PlayerActionPacket();
            actionPacket.entityId = player.getId();
            actionPacket.resultPosition = new BlockVector3();
            actionPacket.action = blockAction.getAction().ordinal();
            if(blockAction.getAction().equals(PlayerActionType.STOP_DESTROY_BLOCK)) {
                actionPacket.x = actionPacket.y = actionPacket.z = 0;
                actionPacket.face = 0;
            }
            else {
                actionPacket.x = blockAction.getPosition().x;
                actionPacket.y = blockAction.getPosition().y;
                actionPacket.z = blockAction.getPosition().z;
                actionPacket.face = blockAction.getFacing();
            }
            list.add(actionPacket);
        }

        return list;
    }
}
