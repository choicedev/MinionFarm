package com.choice.minionfarm.player.event;

import com.choice.minionfarm.api.FarmAPI;
import com.choice.minionfarm.minion.entity.EntityMinion;
import com.choice.minionfarm.minion.repository.data.MinionData;
import com.choice.minionfarm.nbt.NBTDataHandler;
import com.choice.minionfarm.player.entity.EntityPlayer;
import com.choice.minionfarm.player.repository.data.PlayerDataStore;
import com.choice.minionfarm.utils.constants.Constants;
import com.choice.minionfarm.utils.constants.MinionConfigConstants;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
            player.sendMessage("<red>Você já colocou o maximo de minions!");
            return;
        }
        if (FarmAPI.getMinionManager().containsMinionInBlock(block)) {
            player.sendMessage("<red>Já contém minion nesse bloco.");
            return;
        }


        MinionData minion = new MinionData(
                player,
                keyMinion,
                placeLocation
        );

        playerData.addMinion(minion);
        minion.spawn();
        player.removeItemInventory(event.getItem());
        player.sendMessage("<green>Você spawnou um "+minion.getMinion().getTitle());
        player.sendMessage("<yellow>("+playerData.getMinions().size()+"/"+playerData.getMaxMinions()+")");
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
