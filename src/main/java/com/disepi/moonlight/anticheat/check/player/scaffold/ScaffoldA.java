package com.disepi.moonlight.anticheat.check.player.scaffold;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.inventory.transaction.data.UseItemData;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class ScaffoldA extends Check {
    // Constructor
    public ScaffoldA() {
        super("ScaffoldA", "Checks for invalid selections", 6);
    }

    public void check(MobEquipmentPacket e, PlayerData d, Player p) {
        if (e.windowId == 0 && (e.hotbarSlot > 8 || e.inventorySlot > 8))
            punish(p, d);
    }

    public void check(InventoryTransactionPacket e, PlayerData d, Player p) {
        if(e.transactionType == InventoryTransactionPacket.TYPE_USE_ITEM && e.transactionData instanceof UseItemData data) {
            if(data.actionType == InventoryTransactionPacket.USE_ITEM_ACTION_CLICK_BLOCK) {
                if (data.hotbarSlot > 8)
                    punish(p, d);
                else if(data.hotbarSlot != p.getInventory().getHeldItemSlot()) {
                    if(p.getInventory().getItem(data.hotbarSlot).getBlock().getId() != Block.AIR) {
                        if(getViolationScale(d) > 1.f)
                            fail(p, "slot=" + data.hotbarSlot + ", held=" + p.getInventory().getHeldItemSlot());
                        violate(p, d, 1, true);
                    }
                }
            }
        }
    }
}