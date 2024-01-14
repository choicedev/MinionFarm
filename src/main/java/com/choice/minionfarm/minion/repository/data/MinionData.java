package com.choice.minionfarm.minion.repository.data;

import com.choice.minionfarm.api.FarmAPI;
import com.choice.minionfarm.minion.di.enums.MinionType;
import com.choice.minionfarm.minion.entity.EntityMinion;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.SkullCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;


public class MinionData {

    @Getter
    @Setter
    private String key;

    @Getter
    @Setter
    private UUID uuid;

    @Getter
    @Setter
    private int level;

    @Getter
    @Setter
    private String headSkin;

    @Getter
    @Setter
    private int amount;

    @Getter
    @Setter
    private Location spawn;

    @Getter
    @Setter
    private MinionType minionType;

    @Getter
    @Setter
    private ArmorStand armorStand;

    @Getter
    @Setter
    private boolean enabled;

    private Player player;
    private EntityMinion minion;

    public MinionData(Player player, String key, Location spawn){
        this.player = player;
        this.spawn = spawn;
        this.uuid = UUID.randomUUID();
        this.key = key;
        init();
    }


    public void init(){
        this.headSkin = "";
        this.minion = FarmAPI.getMinionManager().getMinion(this.key);
        this.minionType = minion.getMinionsYML().getMinionType();
        this.enabled = minion.checkBlock(spawn);
    }

    public void spawn(){
        CompMaterial spawnBlock = CompMaterial.fromBlock(spawn.getBlock());
        if (!spawnBlock.equals(CompMaterial.BEDROCK) && !spawnBlock.equals(CompMaterial.ENDER_CHEST) &&
                !spawnBlock.equals(CompMaterial.CHEST) &&
                !spawnBlock.equals(CompMaterial.TRAPPED_CHEST)) {
            spawnBlock.setType(ItemCreator.of(CompMaterial.AIR).make());
        }


        ArrayList<Entity> entitys = spawn.getWorld().getNearbyEntities(spawn, 0.5, 0.5, 0.5).stream()
                .filter(e -> e.getType().equals(EntityType.ARMOR_STAND)).collect(Collectors.toCollection(ArrayList::new));
        entitys.forEach(Entity::remove);

        armorStand = spawn.getWorld().spawn(spawn, ArmorStand.class);
        configureArmorStand(armorStand);
    }

    private void configureArmorStand(ArmorStand entity) {
        entity.setArms(true);
        entity.setSmall(true);
        entity.setBasePlate(false);
        entity.setCustomNameVisible(false);
        addEquipment(entity);
    }

    private void addEquipment(ArmorStand entity) {

        entity.setItemInHand(minion.getItemHand());
        entity.setHelmet(minion.getHead());

    }

}
