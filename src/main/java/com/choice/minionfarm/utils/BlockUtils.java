package com.choice.minionfarm.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BlockUtils {

    public static Block getRandomBlock(Location location, int distance, boolean allowVertical) {
        int randomX = location.getBlockX() - distance + ThreadLocalRandom.current().nextInt(2 * distance + 1);
        int randomY = location.getBlockY() - distance + ThreadLocalRandom.current().nextInt(2 * distance + 1);
        int randomZ = location.getBlockZ() - distance + ThreadLocalRandom.current().nextInt(2 * distance + 1);
        return location.getWorld().getBlockAt(randomX, allowVertical ? randomY : location.getBlockY(), randomZ);
    }



    public static Location isSpaceAvailable(Location location, int distance, boolean allow_vertical) {
        World world = location.getWorld();

        int minX = location.getBlockX() - distance;
        int maxX = location.getBlockX() + distance;
        int minY = allow_vertical ? location.getBlockY() - distance : location.getBlockY();
        int maxY = allow_vertical ? location.getBlockY() + distance : location.getBlockY();
        int minZ = location.getBlockZ() - distance;
        int maxZ = location.getBlockZ() + distance;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    CompMaterial type = CompMaterial.fromBlock(block);
                    if (type == CompMaterial.AIR) {
                        return block.getLocation();
                    }
                }
            }
        }

        return null;
    }
}
