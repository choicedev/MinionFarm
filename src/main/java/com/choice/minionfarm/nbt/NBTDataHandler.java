package com.choice.minionfarm.nbt;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class NBTDataHandler {

    @Getter
    private final NBTCompound nbtCompound;

    @Getter
    private final NBTItem nbtItem;

    public NBTDataHandler(ItemStack item) {
        this.nbtItem = new NBTItem(item);
        this.nbtCompound = null;
    }

    public NBTDataHandler(ItemStack item, String compound) {
        NBTItem nbtItem = new NBTItem(item);
        this.nbtItem = nbtItem;
        if(nbtItem.getCompound(compound) == null) {
            this.nbtCompound = nbtItem.addCompound(compound);
            return;
        }
        this.nbtCompound = nbtItem.getCompound(compound);
    }

    public ItemStack getItem(){
        return nbtItem.getItem();
    }

    public String getString(String keyName) {
        return nbtCompound.getString(keyName);
    }

    public void setString(String keyName, String value) {
        nbtCompound.setString(keyName, value);
    }

    public double getDouble(String keyName) {
        return nbtCompound.getDouble(keyName);
    }

    public void setDouble(String keyName, double value) {
        nbtCompound.setDouble(keyName, value);
    }

    public long getLong(String keyName) {
        return nbtCompound.getLong(keyName);
    }

    public void setLong(String keyName, long value) {
        nbtCompound.setLong(keyName, value);
    }

    public int getInt(String keyName) {
        return nbtCompound.getInteger(keyName);
    }

    public void setInt(String keyName, int value) {
        nbtCompound.setInteger(keyName, value);
    }

    public boolean getBoolean(String keyName) {
        return nbtCompound.getBoolean(keyName);
    }

    public void setBoolean(String keyName, boolean value) {
        nbtCompound.setBoolean(keyName, value);
    }

    public boolean hasCompound(String compound){
        return nbtItem.getCompound(compound) != null;
    }

    public void setNBTContainer(String container){
        nbtItem.mergeCompound(new NBTContainer(container));
    }

    public void setLocation(@NonNull Location location, @Nullable String compound){
        NBTCompound nbt = nbtCompound.addCompound(Objects.requireNonNullElse(compound, NBT_LOCATION));
        nbt.setString(NBT_LOCATION_WORLD_UUID, location.getWorld().getUID().toString());
        nbt.setDouble(NBT_LOCATION_X, location.getX());
        nbt.setDouble(NBT_LOCATION_Y, location.getY());
        nbt.setDouble(NBT_LOCATION_Z, location.getZ());
    }

    public Location getLocation(){
        NBTCompound nbt = nbtCompound.getCompound(NBT_LOCATION);
        String world = nbt.getString(NBT_LOCATION_WORLD_UUID);
        double x = nbt.getDouble(NBT_LOCATION_X);
        double y = nbt.getDouble(NBT_LOCATION_Y);
        double z = nbt.getDouble(NBT_LOCATION_Z);
        return new Location(Bukkit.getWorld(UUID.fromString(world)), x, y, z);
    }

    public static final String NBT_LOCATION = "NBT_LOCATION";
    public static final String NBT_LOCATION_WORLD_UUID = "NBT_LOCATION_WORLD_UUID";
    public static final String NBT_LOCATION_X = "NBT_LOCATION_X";
    public static final String NBT_LOCATION_Y = "NBT_LOCATION_Y";
    public static final String NBT_LOCATION_Z = "NBT_LOCATION_Z";
}
