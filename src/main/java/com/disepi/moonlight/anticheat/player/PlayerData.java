package com.disepi.moonlight.anticheat.player;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import com.disepi.moonlight.utils.FakePlayer;

public class PlayerData {

    // Holds information about the players movement, statistics etc.
    public FakePlayer fake; // The fake player entity used for some checks
    public float lastX, lastY, lastZ, lastPitch, lastYaw, predictedFallAmount, currentSpeed, lastSpeed, balance = 0; // Last player position info and other movement stuff
    public int onGroundTicks, offGroundTicks, fallingTicks = 0; // Ticks
    public boolean onGround, onGroundAlternate = true; // onGround stores if the player is near ground, onGroundAlternate stores if the player is directly on ground
    public Vector3 startFallPos = null; // Position of when the player started falling
    public long lastTime = 0; // Last time when the player sent a move packet in milliseconds

    public int frictionLenientTicks = 0; // Ice blocks
    public int gravityLenientTicks = 0; // Ladders, lava, water, cobwebs, slimeblocks etc.
    public int blockAboveLenientTicks = 0; // Jumping below blocks
    public int staircaseLenientTicks = 0; // Jumping on staircases
    public int sprintingTicks = 0; // Will stay at 10 when player is sprinting and decrease over ticks if they are not
    public int jumpTicks = 0; // Increases to a fixed value when a player jumps and decreases after
    public int lerpTicks = 0; // Increases to a fixed value when a player's motion gets set by the server

    public float speedMultiplier = 1; // Speed potions affect this

    public boolean isTeleporting = false;
    public Vector3 teleportPos;

    // Constructor
    public PlayerData(Player player) {
        this.lastX = (float) player.x;
        this.lastY = (float) player.y;
        this.lastZ = (float) player.z;
        this.teleportPos = new Vector3(player.x, player.y, player.z);
        this.isTeleporting = false;
    }

    // Removes the instance of the fake player from the world and the class instance
    public void destructFakePlayer() {
        this.fake.despawnFromAll();
        this.fake = null;
    }

    public boolean isPlayerConsideredSprinting() {
        return this.sprintingTicks > 0;
    }

    public boolean isPlayerConsideredJumping() {
        return this.jumpTicks > 0;
    }

}
