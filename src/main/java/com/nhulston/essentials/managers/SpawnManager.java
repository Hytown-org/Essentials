package com.nhulston.essentials.managers;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.spawn.GlobalSpawnProvider;
import com.hypixel.hytale.server.core.universe.world.spawn.ISpawnProvider;
import com.nhulston.essentials.models.Spawn;
import com.nhulston.essentials.util.ConfigManager;
import com.nhulston.essentials.util.Log;
import com.nhulston.essentials.util.StorageManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpawnManager {
    private final StorageManager storageManager;
    private final ConfigManager configManager;

    public SpawnManager(@Nonnull StorageManager storageManager, @Nonnull ConfigManager configManager) {
        this.storageManager = storageManager;
        this.configManager = configManager;
    }

    public void setSpawn(@Nonnull String world, double x, double y, double z, float yaw, float pitch) {
        Spawn spawn = new Spawn(world, x, y, z, yaw, pitch);
        storageManager.setSpawn(spawn);
    }

    @Nullable
    public Spawn getSpawn() {
        return storageManager.getSpawn();
    }

    public boolean hasSpawn() {
        return storageManager.getSpawn() != null;
    }

    /**
     * Syncs the saved Essentials spawn with the world's native spawn provider.
     * This updates the spawn marker on the map.
     * Should be called after worlds are loaded.
     * <p>
     * If the world already has a custom spawn provider (e.g., IndividualSpawnProvider
     * or a plugin-provided ISpawnProvider), it will NOT be replaced unless
     * {@code spawn.sync-spawn-provider} is set to {@code "force"} in config.
     * This prevents Essentials from overriding custom spawn logic used by
     * other plugins or custom PlayerStorageProviders.
     */
    public void syncWorldSpawnProvider() {
        if (!configManager.isSyncSpawnProviderEnabled()) {
            return;
        }

        Spawn spawn = getSpawn();
        if (spawn == null) {
            return;
        }

        World world = Universe.get().getWorld(spawn.getWorld());
        if (world == null) {
            Log.warning("Could not sync spawn provider: world '" + spawn.getWorld() + "' not found");
            return;
        }

        // Check if the world already has a custom (non-GlobalSpawnProvider) spawn provider.
        // Custom spawn providers (IndividualSpawnProvider, plugin-provided, etc.) should not
        // be replaced, as they manage per-player spawn logic that Essentials would destroy.
        ISpawnProvider existingProvider = world.getWorldConfig().getSpawnProvider();
        if (existingProvider != null && !(existingProvider instanceof GlobalSpawnProvider)) {
            Log.info("Skipping spawn provider sync for world '" + spawn.getWorld()
                    + "' - custom spawn provider detected (" + existingProvider.getClass().getSimpleName()
                    + "). Set spawn.sync-spawn-provider = \"force\" in config to override.");
            return;
        }

        Vector3d position = new Vector3d(spawn.getX(), spawn.getY(), spawn.getZ());
        Vector3f rotation = new Vector3f(0, spawn.getYaw(), 0);
        Transform spawnTransform = new Transform(position, rotation);
        
        world.getWorldConfig().setSpawnProvider(new GlobalSpawnProvider(spawnTransform));
        Log.info("Synced spawn provider for world '" + spawn.getWorld() + "'");
    }
}
