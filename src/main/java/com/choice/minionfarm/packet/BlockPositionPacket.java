package com.choice.minionfarm.packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BlockPositionPacket {

    @SuppressWarnings("CallToPrintStackTrace")
    private static Object buildPacket(
            Location block,
            int status
    ) {
        try {
            Constructor<?> constructor = ReflectionUtil.getNMSClass("BlockPosition", "N/A").getConstructor(
                    int.class, int.class, int.class
            );
            Object blockPosition = constructor.newInstance(block.getBlockX(), block.getBlockY(), block.getBlockZ());
            Constructor<?> packetConstructor = ReflectionUtil.getNMSClass("PacketPlayOutBlockBreakAnimation", "N/A").getConstructor(
                    int.class, ReflectionUtil.getNMSClass("BlockPosition"), int.class);

            return packetConstructor.newInstance(block.getBlockX(), blockPosition, status);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void sendPacket(Player player, Location block, int status) {
        try {
            Object packet = buildPacket(block, status);
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", ReflectionUtil.getNMSClass("Packet")).invoke(playerConnection, packet);

        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
