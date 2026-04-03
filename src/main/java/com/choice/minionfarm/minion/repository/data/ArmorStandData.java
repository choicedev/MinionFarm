package com.choice.minionfarm.minion.repository.data;

import com.choice.minionfarm.api.FarmAPI;
import com.choice.minionfarm.api.FileAPI;
import com.choice.minionfarm.api.hologram.HologramAPI;
import com.choice.minionfarm.minion.di.enums.MinionType;
import com.choice.minionfarm.minion.di.enums.Parts;
import com.choice.minionfarm.minion.di.model.MinionAction;
import com.choice.minionfarm.minion.entity.EntityMinion;
import com.choice.minionfarm.minion.entity.minion.EntityMinionMiner;
import com.choice.minionfarm.minion.util.BodyPart;
import com.choice.minionfarm.player.entity.EntityPlayer;
import com.choice.minionfarm.utils.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.UUID;

@AllArgsConstructor
public class ArmorStandData {

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
    private boolean spaceAround;

    private boolean isLoaded = false;

    @Getter
    private final EntityPlayer player;

    @Getter
    private EntityMinion entityMinion;

    private BodyPart bodyMinion;

    boolean isBusy = false;
    private int breakTimer = 0;
    private HologramAPI minionHologram;

    public ArmorStandData(EntityPlayer player, String key, Location spawn) {
        this.player = player;
        this.spawn = spawn;
        this.key = key;
        init();
    }

    public void init() {
        this.headSkin = "";
        this.entityMinion = FarmAPI.getMinionManager().getMinion(this.key);
        this.minionType = entityMinion.getMinionsRepository().getMinionType();
    }

    public void spawn() {
        CompMaterial blockBusy = CompMaterial.fromBlock(spawn.add(0, 1, 0).getBlock());

        if (blockBusy != CompMaterial.AIR) {
            spaceAround = false;
            return;
        }

        armorStand = spawn.getWorld().spawn(spawn.add(0.5, 0, 0.5), ArmorStand.class);
        this.uuid = armorStand.getUniqueId();
        this.bodyMinion = new BodyPart(armorStand);
        bodyMinion.setBodyPose(Parts.BODY);
        bodyMinion.rotateBody(player.getLocation());
        configureArmorStand(armorStand);
        minionHologram = new HologramAPI(armorStand.getUniqueId().toString());
        minionHologram.createHologram(spawn, FileAPI.getMessagesRepository().getWelcomeToWorld().stream().map(map -> map.replace("{player_name}", player.displayName())).toList());
        FarmAPI.getMinionManager().addActiveMinion(armorStand.getUniqueId(), this);
        //this.spaceAround = this.entityMinion.checkBlock(spawn);
        isLoaded = true;
    }

    public void minionAction() {
        if (armorStand == null || !isLoaded || isBusy) {
            return;
        }
        spawn = armorStand.getLocation();
        this.spaceAround = this.entityMinion.checkBlock(spawn);

        if (!spaceAround) {
            minionHologram.updateHologram(spawn, FileAPI.getMessagesRepository().getNoSpaceAround());
        }else{
            minionHologram.updateHologram(spawn, FileAPI.getMessagesRepository().getWorking(player.displayName(), getAmount()));
        }


        breakTimer++;

        if (breakTimer == entityMinion.getDelay() - 2) {
            preExecuteAction();
        }

        if (breakTimer >= entityMinion.getDelay()) {
            breakTimer = 0;
            isBusy = true;
            this.entityMinion.action(this, armorStand, spawn, (action) -> {
                breakTimer++;
                isBusy = false;
                bodyMinion.setBodyPose(Parts.RIGHT_ARM);
                bodyMinion.cancelAnimation();
            });

        }
    }

    public int getDamage(){
        return entityMinion.getMinionsRepository().getDamageOnBlock();
    }

    private MinionAction minionAction;

    public MinionAction getMinionAction(){
        if(minionAction == null) return null;
        return minionAction;
    }

    private void preExecuteAction() {
        if (entityMinion instanceof EntityMinionMiner) {
            entityMinion.preExecuteAction(spawn, (type, location) -> {
                switch (type){
                    case BREAK -> {
                        armorStand.setItemInHand(entityMinion.getItemHand());
                        bodyMinion.setBodyPose(Parts.BODY);
                        bodyMinion.rotateBody(location);
                        bodyMinion.animateRightArmAsync();
                    }
                    case PLACE -> {
                        armorStand.setItemInHand(entityMinion.getPlace().toItem());
                        bodyMinion.setBodyPose(Parts.BODY);
                        bodyMinion.rotateBody(location);
                        bodyMinion.animateRightArmAsync();
                    }
                }
                minionAction = new MinionAction(type, location);
            });
        }
    }

    private void configureArmorStand(ArmorStand entity) {
        entity.setArms(true);
        entity.setSmall(true);
        entity.setBasePlate(false);
        entity.setCustomNameVisible(false);
        addEquipment(entity);
    }

    private void addEquipment(ArmorStand entity) {
        entity.setItemInHand(entityMinion.getItemHand());
        entity.setHelmet(entityMinion.getHead());

        if (!this.entityMinion.getArmor().isEmpty()) {
            entity.setChestplate(this.entityMinion.getArmor().get(Constants.ENTITY_CHESTPLATE));
            entity.setLeggings(this.entityMinion.getArmor().get(Constants.ENTITY_LEGGINGS));
            entity.setBoots(this.entityMinion.getArmor().get(Constants.ENTITY_BOOTS));
        }
    }

    public void removeFromWorld() {
        if (armorStand != null) {
            deleteMinion();
            armorStand.remove();
        }
    }

    private void deleteMinion() {
        entityMinion.stop();
        minionHologram.removeHologram();
        FarmAPI.getMinionManager().removeMinionActive(armorStand.getUniqueId());
    }
}
