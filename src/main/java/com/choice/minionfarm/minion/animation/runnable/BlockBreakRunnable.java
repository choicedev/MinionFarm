package com.choice.minionfarm.minion.animation.runnable;

import com.choice.minionfarm.api.FarmAPI;
import com.choice.minionfarm.minion.repository.data.MinionData;
import com.choice.minionfarm.packet.BlockPositionPacket;
import com.choice.minionfarm.utils.function.Callback;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Function;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.SimpleRunnable;
import org.mineacademy.fo.remain.CompMaterial;

public class BlockBreakRunnable extends SimpleRunnable {

    private MinionData minionData;
    private final Callback callback;
    private int damage = 0;

    public BlockBreakRunnable(MinionData minionData, Callback callback){
        this.minionData = minionData;
        this.damage = minionData.getDamage();
        this.callback = callback;
    }

    public void start(){
        this.runTaskTimer(FarmAPI.getInstance(), 1L, 8L);
    }

    private void cleanRunnable(){
        damage = -1;
        executeBreak();
        this.callback.callback();
        cancel();
    }

    @Override
    public void run() {
        Block blockFocused = minionData.getMinionAction().getLocation().getBlock();

        if(CompMaterial.fromBlock(blockFocused) == CompMaterial.AIR){
            cleanRunnable();
            return;
        }

        if(damage >= 10){
            cleanRunnable();
        }

        executeBreak();
        damage++;
    }

    private void executeBreak(){
        for (Entity ent : minionData.getSpawn().getWorld().getNearbyEntities(minionData.getSpawn(), 4, 4, 4)) {
            if (!(ent instanceof Player player)) continue;
            BlockPositionPacket.sendPacket(player, minionData.getMinionAction().getLocation(), damage);
        }
    }
}
