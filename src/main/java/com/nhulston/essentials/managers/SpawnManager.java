package com.nhulston.essentials.managers;

import com.nhulston.essentials.models.Spawn;
import com.nhulston.essentials.util.StorageManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpawnManager {
    private final StorageManager storageManager;

    public SpawnManager(@Nonnull StorageManager storageManager) {
        this.storageManager = storageManager;
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
}
