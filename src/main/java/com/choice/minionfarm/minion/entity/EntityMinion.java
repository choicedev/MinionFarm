package com.choice.minionfarm.minion.entity;

import com.choice.minionfarm.minion.repository.data.MinionData;
import com.choice.minionfarm.minion.repository.data.local.MinionsYML;
import com.choice.minionfarm.nbt.NBTDataHandler;
import com.choice.minionfarm.utils.constants.Constants;
import com.choice.minionfarm.utils.constants.MinionConfigConstants;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.SkullCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.List;

public abstract class EntityMinion {

    @Getter
    private final MinionsYML minionsYML;


    public EntityMinion(
            MinionsYML minionsYML
    ) {
        this.minionsYML = minionsYML;
    }

    public CompMaterial getPlace(){
        if(minionsYML.getPlace() == null || minionsYML.getPlace() == CompMaterial.AIR) return CompMaterial.STONE;
        return minionsYML.getPlace();
    }


    public ItemStack getItemHand(){
        if(minionsYML.getPlace() == null || minionsYML.getPlace() == CompMaterial.AIR) return ItemCreator.of(CompMaterial.STONE_PICKAXE).make();
        return ItemCreator.of(minionsYML.getItemHand()).make();
    }

    public List<String> getLore(){
        return minionsYML.getLore();
    }

    public abstract void update(MinionData minion, ArmorStand armorStand, Location location);

    public abstract boolean checkBlock(Location location);
    public abstract boolean checkBlockAround(Location location);

    public ItemStack getHead() {
        ItemStack skull = SkullCreator.itemFromUrl(minionsYML.getHeadSkin());
        NBTDataHandler nbtItem = new NBTDataHandler(skull, Constants.MINION_HEAD_COMPOUND);
        nbtItem.setString(MinionConfigConstants.KEY, minionsYML.getKey());
        nbtItem.setString(MinionConfigConstants.MINION_TYPE, minionsYML.getMinionType().name());
        nbtItem.setString(MinionConfigConstants.TITLE, minionsYML.getTitle());
        return nbtItem.getItem();
    }
}
