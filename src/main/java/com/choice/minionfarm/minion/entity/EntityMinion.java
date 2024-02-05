package com.choice.minionfarm.minion.entity;

import com.choice.minionfarm.minion.di.enums.TypeFarm;
import com.choice.minionfarm.minion.repository.data.ArmorStandData;
import com.choice.minionfarm.minion.repository.local.MinionsRepository;
import com.choice.minionfarm.nbt.NBTDataHandler;
import com.choice.minionfarm.utils.function.BiFunction;
import com.choice.minionfarm.utils.constants.Constants;
import com.choice.minionfarm.utils.constants.MinionConfigConstants;
import com.choice.minionfarm.utils.function.Function;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTListCompound;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.SkullCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.choice.minionfarm.utils.constants.Constants.*;

public abstract class EntityMinion {

    @Getter
    private final MinionsRepository minionsRepository;


    public EntityMinion(
            MinionsRepository minionsRepository
    ) {
        this.minionsRepository = minionsRepository;
    }

    public CompMaterial getPlace(){
        if(minionsRepository.getPlace() == null || minionsRepository.getPlace() == CompMaterial.AIR) return CompMaterial.STONE;
        return minionsRepository.getPlace();
    }


    public ItemStack getItemHand(){
        if(minionsRepository.getItemHand() == null || minionsRepository.getItemHand() == CompMaterial.AIR) return ItemCreator.of(CompMaterial.STONE_PICKAXE).make();
        return ItemCreator.of(minionsRepository.getItemHand()).make();
    }

    public List<String> getLore(){
        return minionsRepository.getLore();
    }

    public abstract void action(ArmorStandData minion, ArmorStand armorStand, Location location, Function<Boolean> action);

    public abstract boolean checkBlock(Location location);
    public abstract Location checkBlockAround(Location location);
    public abstract Block getRandomBlock(Location location);
    public abstract void preExecuteAction(Location spawn, BiFunction<TypeFarm, Location> preExecution);
    public abstract void stop();

    public String getTitle(){
        return minionsRepository.getTitle();
    }

    public ItemStack getHead() {
        ItemStack skull = SkullCreator.createSkull();
        ItemMeta itemMeta = skull.getItemMeta();
        itemMeta.setDisplayName(getTitle());
        itemMeta.setLore(getLore());
        skull.setItemMeta(itemMeta);
        NBTDataHandler nbtItem = new NBTDataHandler(skull, Constants.MINION_HEAD_COMPOUND);
        nbtItem.setString(MinionConfigConstants.KEY, minionsRepository.getKey());
        nbtItem.setString(MinionConfigConstants.MINION_ID, UUID.randomUUID().toString());
        nbtItem.setString(MinionConfigConstants.MINION_TYPE, minionsRepository.getMinionType().name());
        nbtItem.setString(MinionConfigConstants.TITLE, minionsRepository.getTitle());
        NBTCompound nbtSkull = nbtItem.getNbtItem().addCompound("SkullOwner");
        nbtSkull.setString("Name", getTitle());
        NBTListCompound textures = nbtSkull.addCompound("Properties").getCompoundList("textures").addCompound();
        textures.setString("Value", minionsRepository.getHeadSkin());
        return nbtItem.getItem();
    }

    public Map<String, ItemStack> getArmor(){
        Map<String, ItemStack> map = new HashMap();
        NBTDataHandler nbtChestplate = new NBTDataHandler(ItemCreator.of(CompMaterial.LEATHER_CHESTPLATE).make());
        NBTDataHandler nbtLeggings = new NBTDataHandler(ItemCreator.of(CompMaterial.LEATHER_LEGGINGS).make());
        NBTDataHandler nbtBoots = new NBTDataHandler(ItemCreator.of(CompMaterial.LEATHER_BOOTS).make());
        nbtChestplate.setNBTContainer(minionsRepository.getArmorUrl());
        nbtLeggings.setNBTContainer(minionsRepository.getArmorUrl());
        nbtBoots.setNBTContainer(minionsRepository.getArmorUrl());

        map.put(ENTITY_CHESTPLATE, nbtChestplate.getItem());
        map.put(ENTITY_LEGGINGS, nbtLeggings.getItem());
        map.put(ENTITY_BOOTS, nbtBoots.getItem());
        return map;
    }

    public int getDelay() {
        return minionsRepository.getDelayFarm();
    }

}
