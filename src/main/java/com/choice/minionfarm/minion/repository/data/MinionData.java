package com.choice.minionfarm.minion.repository.data;

import com.choice.minionfarm.api.FarmAPI;
import com.choice.minionfarm.minion.di.enums.MinionType;
import com.choice.minionfarm.minion.di.enums.Parts;
import com.choice.minionfarm.minion.di.enums.TypeFarm;
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

import java.util.HashMap;
import java.util.UUID;

@AllArgsConstructor
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
    private boolean isEnabled;

    private boolean isLoaded = false;

    @Getter
    private final EntityPlayer player;

    @Getter
    private EntityMinion minion;

    private BodyPart bodyMinion;

    private int breakTimer = 0;

    public MinionData(EntityPlayer player, String key, Location spawn) {
        this.player = player;
        this.spawn = spawn;
        this.key = key;
        init();
    }

    public void init() {
        this.headSkin = "";
        this.minion = FarmAPI.getMinionManager().getMinion(this.key);
        this.minionType = minion.getMinionsRepository().getMinionType();
    }

    public void spawn() {
        CompMaterial blockBusy = CompMaterial.fromBlock(spawn.add(0, 1, 0).getBlock());

        if (blockBusy != CompMaterial.AIR) {
            isEnabled = false;
            return;
        }

        armorStand = spawn.getWorld().spawn(spawn.add(0.5, 0, 0.5), ArmorStand.class);
        this.uuid = armorStand.getUniqueId();
        this.bodyMinion = new BodyPart(armorStand);
        bodyMinion.setBodyPose(Parts.BODY);
        bodyMinion.rotateBody(player.getLocation());
        configureArmorStand(armorStand);
        FarmAPI.getMinionManager().addActiveMinion(armorStand.getUniqueId(), this);
        this.isEnabled = this.minion.checkBlock(spawn);
        isLoaded = true;
    }

    boolean isBusy = false;
    public void minionAction() {
        if (armorStand == null || !isLoaded) {
            return;
        }


        if(isBusy) return;

        breakTimer++;

        if (!isEnabled) {
            player.sendMessage("<red> No space around");
            return;
        }


        if (breakTimer == minion.getDelay() - 2) {
            preExecuteAction();
        }

        if (breakTimer >= minion.getDelay()) {
            breakTimer = 0;
            isBusy = true;
            this.minion.action(this, armorStand, spawn, (action) -> {
                breakTimer++;
                isBusy = false;
                bodyMinion.setBodyPose(Parts.RIGHT_ARM);
                bodyMinion.cancelAnimation();
            });

        }
    }

    public int getDamage(){
        return minion.getMinionsRepository().getDamageOnBlock();
    }

    private MinionAction minionAction;

    public MinionAction getMinionAction(){
        if(minionAction == null) return null;
        return minionAction;
    }

    private void preExecuteAction() {
        if (minion instanceof EntityMinionMiner) {
            minion.preExecuteAction(spawn, (type, location) -> {
                switch (type){
                    case BREAK -> {
                        armorStand.setItemInHand(minion.getItemHand());
                        bodyMinion.setBodyPose(Parts.BODY);
                        bodyMinion.rotateBody(location);
                        bodyMinion.animateRightArmAsync();
                    }
                    case PLACE -> {
                        armorStand.setItemInHand(minion.getPlace().toItem());
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
        entity.setItemInHand(minion.getItemHand());
        entity.setHelmet(minion.getHead());

        if (!this.minion.getArmor().isEmpty()) {
            entity.setChestplate(this.minion.getArmor().get(Constants.ENTITY_CHESTPLATE));
            entity.setLeggings(this.minion.getArmor().get(Constants.ENTITY_LEGGINGS));
            entity.setBoots(this.minion.getArmor().get(Constants.ENTITY_BOOTS));
        }
    }

    public void removeFromWorld() {
        if (armorStand != null) {
            deleteMinion();
            armorStand.remove();
        }
    }

    private void deleteMinion() {
        minion.stop();
        FarmAPI.getMinionManager().removeMinionActive(armorStand.getUniqueId());
    }
}
