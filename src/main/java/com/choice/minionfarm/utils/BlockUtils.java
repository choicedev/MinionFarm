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

    public static Block getRandomBlock(Location location, int y, int distance, boolean allow_vertical) {
        int randomX = location.getBlockX() - distance + ThreadLocalRandom.current().nextInt(2 * distance + 1);
        int randomY = location.getBlockY() - distance + ThreadLocalRandom.current().nextInt(2 * distance + 1);
        int randomZ = location.getBlockZ() - distance + ThreadLocalRandom.current().nextInt(2 * distance + 1);
        return location.getWorld().getBlockAt(randomX, allow_vertical ? randomY : y, randomZ);
    }


    public static boolean isSpaceAvailable(CompMaterial material, Location location, int distance, boolean allow_vertical) {
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
                    if (type != CompMaterial.AIR || type != material) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
