package com.nhulston.essentials.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.inventory.UpdatePlayerInventory;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

/**
 * Pre-release server: {@code Player.sendInventory()} was removed; the client is updated via
 * {@link UpdatePlayerInventory} built from {@link InventoryComponent} sections.
 */
public final class PlayerInventorySync {

    private PlayerInventorySync() {
    }

    /**
     * Pushes the full player inventory to the client immediately (mirrors {@code PlayerSystems} join sync).
     */
    public static void syncInventoryToClient(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store) {
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) {
            return;
        }
        InventoryComponent.Storage storage = store.getComponent(ref, InventoryComponent.Storage.getComponentType());
        InventoryComponent.Armor armor = store.getComponent(ref, InventoryComponent.Armor.getComponentType());
        InventoryComponent.Hotbar hotbar = store.getComponent(ref, InventoryComponent.Hotbar.getComponentType());
        InventoryComponent.Utility utility = store.getComponent(ref, InventoryComponent.Utility.getComponentType());
        InventoryComponent.Tool tool = store.getComponent(ref, InventoryComponent.Tool.getComponentType());
        InventoryComponent.Backpack backpack = store.getComponent(ref, InventoryComponent.Backpack.getComponentType());
        playerRef.getPacketHandler()
                .writeNoCache(
                        new UpdatePlayerInventory(
                                storage != null ? storage.getInventory().toPacket() : null,
                                armor != null ? armor.getInventory().toPacket() : null,
                                hotbar != null ? hotbar.getInventory().toPacket() : null,
                                utility != null ? utility.getInventory().toPacket() : null,
                                tool != null ? tool.getInventory().toPacket() : null,
                                backpack != null ? backpack.getInventory().toPacket() : null
                        )
                );
    }
}
