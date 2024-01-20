package com.choice.minionfarm.minion.repository.data;

import com.choice.minionfarm.api.FarmAPI;
import com.choice.minionfarm.minion.di.enums.MinionType;
import com.choice.minionfarm.minion.entity.EntityMinion;
import com.choice.minionfarm.player.entity.EntityPlayer;
import com.choice.minionfarm.settings.temporary.ActiveMinionsRepository;
import com.choice.minionfarm.utils.constants.Constants;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;
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

    @Getter
    private final UUID ownerUUID;

    @Getter
    private EntityMinion minion;

    public MinionData(UUID ownerUUID, String key, Location spawn){
        this.ownerUUID = ownerUUID;
        this.spawn = spawn.add(0.5, 1, 0.5);
        this.key = key;
        init();
    }


    public void init(){
        this.headSkin = "";
        this.minion = FarmAPI.getMinionManager().getMinion(this.key);
        this.minionType = minion.getMinionsRepository().getMinionType();
        this.enabled = minion.checkBlock(spawn);
    }

    public void spawn(){
        CompMaterial spawnBlock = CompMaterial.fromBlock(spawn.getBlock());
        if (!spawnBlock.equals(CompMaterial.BEDROCK) && !spawnBlock.equals(CompMaterial.ENDER_CHEST) &&
                !spawnBlock.equals(CompMaterial.CHEST) &&
                !spawnBlock.equals(CompMaterial.TRAPPED_CHEST)) {
            spawnBlock.setType(ItemCreator.of(CompMaterial.AIR).make());
        }

        ArrayList<Entity> entities = spawn.getWorld().getNearbyEntities(spawn, 0.5, 0.5, 0.5).stream()
                .filter(e -> e.getType().equals(EntityType.ARMOR_STAND)).collect(Collectors.toCollection(ArrayList::new));
        entities.forEach(Entity::remove);

        armorStand = spawn.getWorld().spawn(spawn, ArmorStand.class);
        this.uuid = armorStand.getUniqueId();
        configureArmorStand(armorStand);
        FarmAPI.getMinionManager().addActiveMinion(armorStand.getUniqueId(), this);
        this.enabled = this.minion.checkBlock(spawn);
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

        if(this.minion.getArmor().isEmpty()) return;

        entity.setChestplate(this.minion.getArmor().get(Constants.ENTITY_CHESTPLATE));
        entity.setLeggings(this.minion.getArmor().get(Constants.ENTITY_LEGGINGS));
        entity.setBoots(this.minion.getArmor().get(Constants.ENTITY_BOOTS));
    }

    public void removeFromWorld() {
        if(armorStand == null) return;
        deleteMinion();
        armorStand.remove();
     
    }

    private void deleteMinion(){
        FarmAPI.getMinionManager().removeMinionActive(armorStand.getUniqueId());
    }
}
