package com.disepi.moonlight.anticheat.check.motion.speed;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.MotionUtils;

public class SpeedC extends Check {
    // Constructor
    public SpeedC() {
        super("SpeedC", "Invalid strafe movement", 24);
    }

    public void check(PlayerAuthInputPacket e, PlayerData d, Player p) {
        reward(d, 0.2f); // Violate
        if (!d.onGround && d.offGroundTicks > 1 && d.currentSpeed > 0.355 && p.isSprinting()) {
            double expected = (MotionUtils.getExpectedSpeedValue(d.offGroundTicks) * d.speedMultiplier) * 1.75;

            double predictedVelX = d.viewVector.x * expected;
            double predictedVelZ = d.viewVector.z * expected;
            double velX = e.getPosition().x - p.lastX;
            double velZ = e.getPosition().z - p.lastZ;

            if (!(velZ <= 0.0f && velZ > predictedVelZ || velZ >= 0.0f && velZ < predictedVelZ || velX <= 0.0f && velX > predictedVelX || velX >= 0.0f && velX < predictedVelX)) {
                if(getViolationScale(d) > 8) {
                    fail(p, "expectedX=" + predictedVelX + ", expectedZ=" + predictedVelZ + ", x=" + velX + ", z=" + velZ + ", vl=" + (int) getViolationScale(d));
                }
                violate(p, d, 1, true); // Violate
            }
        }
    }

}
