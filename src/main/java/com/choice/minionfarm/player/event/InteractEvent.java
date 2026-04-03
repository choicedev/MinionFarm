package com.choice.minionfarm.player.event;

import com.choice.minionfarm.api.FarmAPI;
import com.choice.minionfarm.api.FileAPI;
import com.choice.minionfarm.minion.entity.EntityMinion;
import com.choice.minionfarm.minion.repository.data.ArmorStandData;
import com.choice.minionfarm.nbt.NBTDataHandler;
import com.choice.minionfarm.player.entity.EntityPlayer;
import com.choice.minionfarm.player.repository.data.PlayerDataStore;
import com.choice.minionfarm.utils.constants.Constants;
import com.choice.minionfarm.utils.constants.MinionConfigConstants;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompMaterial;

public class InteractEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractEvent(PlayerInteractEvent event) {
        if (!isValidInteraction(event)) return;
        event.setCancelled(true);

        EntityPlayer player = new EntityPlayer(event.getPlayer());
        ItemStack item = event.getItem();
        ItemStack mainHand = player.getItemInMainHand();
        ItemStack secondHand = player.getItemInSecondHand();
        Location placeLocation = event.getClickedBlock().getLocation();
        Block block = event.getClickedBlock();
        String keyMinion = new NBTDataHandler(item, Constants.MINION_HEAD_COMPOUND).getString(MinionConfigConstants.KEY);
        PlayerDataStore playerData = PlayerDataStore.getPlayer(player.getUniqueId());
        if (playerData.getMinions().size() >= playerData.getMaxMinions()) {
            player.sendMessage(FileAPI.getMessagesRepository().getMinionMaxInsert());
            return;
        }
        if (FarmAPI.getMinionManager().containsMinionInBlock(block)) {
            player.sendMessage("<red>Já contém minion nesse bloco.");
            return;
        }


        ArmorStandData minion = new ArmorStandData(
                player,
                keyMinion,
                placeLocation
        );

        minion.spawn();
        playerData.addMinion(minion);
        player.removeItemInventory(event.getItem());
        player.sendMessage(FileAPI.getMessagesRepository().getInsertMinionInWorld(), map -> {
            map.put("{minion_name}", minion.getEntityMinion().getTitle());
            map.put("{onWorld}", ""+playerData.getMinions().size());
            map.put("{maxMinion}", ""+playerData.getMaxMinions());
        });
    }

    @EventHandler
    public void onIntenractWithMinion(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player damager)) return;
        if(!(event.getEntity() instanceof ArmorStand armorStand)) return;

        EntityPlayer player = new EntityPlayer(damager);
        ArmorStandData armorStandData = FarmAPI.getMinionManager().getMinionActive(armorStand.getUniqueId());

        if(armorStandData == null) return;
        if(!(armorStandData.getPlayer().getUniqueId().equals(player.getUniqueId()) || player.isOp())) return;
        event.setCancelled(true);
       PlayerDataStore playerDataStore = PlayerDataStore.getPlayer(armorStandData.getPlayer().getUniqueId());
        EntityMinion entityMinion = FarmAPI.getMinionManager().getMinion(armorStandData.getKey());

        armorStandData.removeFromWorld();
        playerDataStore.removeMinion(armorStandData.getUuid());

        player.addItems(entityMinion.getHead());
        player.sendMessage(FileAPI.getMessagesRepository().getRemoveMinionWorld(), map -> {
            map.put("{minion_name}", armorStandData.getEntityMinion().getTitle());
            map.put("{onWorld}", ""+playerDataStore.getMinions().size());
            map.put("{maxMinion}", ""+playerDataStore.getMaxMinions());
        });

    }

    private boolean isValidInteraction(PlayerInteractEvent event) {
        return event.getClickedBlock() != null &&
                event.hasItem() &&
                event.hasBlock() &&
                event.getItem().getType() == CompMaterial.PLAYER_HEAD.toMaterial() &&
                event.getClickedBlock().getType() != CompMaterial.AIR.toMaterial() &&
                event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                new NBTDataHandler(event.getItem()).hasCompound(Constants.MINION_HEAD_COMPOUND);
    }




}
