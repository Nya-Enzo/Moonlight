package com.disepi.moonlight.anticheat.check.motion.motion;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.types.AuthInputAction;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.MotionUtils;
import com.disepi.moonlight.utils.Util;

public class MotionA extends Check {
    // Constructor
    public MotionA() {
        super("MotionA", "Invalid vertical jump movement", 8);
    }

    public void doFailCheck(Player p, PlayerData d, float value, float expected) {
        fail(p, "height=" + value + ", expected=" + expected + ", ticks=" + d.offGroundTicks);
        violate(p, d, 1, true);
    }

    public void check(PlayerAuthInputPacket e, PlayerData d, Player p) {
        reward(d, 0.25f); // Violate

        if(e.getInputData().contains(AuthInputAction.START_JUMPING)) {
            d.jumpMotion = e.getPosition().y - d.lastY;
        }
        else if(d.offGroundTicks > 1 && d.jumpMotion > 0.f) {
            d.jumpMotion -= MotionUtils.DRAG;
            d.jumpMotion *= MotionUtils.GRAVITY;
            if(d.jumpMotion <= 0.f || d.blockAboveLenientTicks > 0 || d.lerpTicks > 0) {
                d.jumpMotion = 0.f;
                return;
            }

            float value = e.getPosition().y - d.lastY;
            if(value <= 0.f) {
                if(d.jumpMotion > 0.1f) {
                    fail(p, "falling=true, expected=" + d.jumpMotion + ", ticks=" + d.offGroundTicks);
                    violate(p, d, 0.5f, true);
                }
                d.jumpMotion = 0.f;
            }
            else if(!Util.isRoughlyEqual(value, d.jumpMotion, 0.01)) {
                doFailCheck(p, d, value, d.jumpMotion);
            }
        }
    }
}
