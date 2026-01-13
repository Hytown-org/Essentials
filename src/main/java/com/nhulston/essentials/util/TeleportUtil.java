package com.nhulston.essentials.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class TeleportUtil {

    private TeleportUtil() {}

    /**
     * Teleports an entity to the specified location.
     * @return null if successful, error message if failed
     */
    @Nullable
    public static String teleport(@Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref,
                                  @Nonnull String worldName, double x, double y, double z,
                                  float yaw, float pitch) {
        World targetWorld = Universe.get().getWorld(worldName);
        if (targetWorld == null) {
            return "World '" + worldName + "' is not loaded.";
        }

        Vector3d position = new Vector3d(x, y, z);
        Vector3f rotation = new Vector3f(pitch, yaw, 0.0F);

        Teleport teleport = new Teleport(targetWorld, position, rotation);
        store.putComponent(ref, Teleport.getComponentType(), teleport);

        return null;
    }
}
