package com.choice.minionfarm.minion.entity.minion;

import com.choice.minionfarm.minion.animation.runnable.BlockBreakRunnable;
import com.choice.minionfarm.minion.di.enums.TypeFarm;
import com.choice.minionfarm.minion.di.model.MinionAction;
import com.choice.minionfarm.minion.entity.EntityMinion;
import com.choice.minionfarm.minion.repository.data.ArmorStandData;
import com.choice.minionfarm.minion.repository.local.MinionsRepository;
import com.choice.minionfarm.utils.BlockUtils;
import com.choice.minionfarm.utils.function.BiFunction;
import com.choice.minionfarm.utils.function.Function;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityMinionMiner extends EntityMinion {


    @Getter
    private final ItemCreator blockPlace;

    private ArrayList<Vector> haveSpaceAround = new ArrayList<>(Arrays.asList(new Vector(1, -1, 0), new Vector(0, -1, 1), new Vector(1, -1, 1), new Vector(-1, -1, 0), new Vector(0, -1, -1), new Vector(-1, -1, -1), new Vector(1, -1, -1), new Vector(-1, -1, 1), new Vector(2, -1, 0), new Vector(2, -1, -1), new Vector(2, -1, 1), new Vector(-2, -1, 0), new Vector(-2, -1, -1), new Vector(-2, -1, 1), new Vector(0, -1, 2), new Vector(1, -1, 2), new Vector(-1, -1, 2), new Vector(0, -1, -2), new Vector(1, -1, -2), new Vector(-1, -1, -2), new Vector(2, -1, -2), new Vector(2, -1, 2), new Vector(-2, -1, -2), new Vector(-2, -1, 2)));
    BlockBreakRunnable blockBreakRunnable;

    public EntityMinionMiner(MinionsRepository minionsRepository) {
        super(minionsRepository);
        this.blockPlace = ItemCreator.of(getPlace());
    }

    @Override
    public void action(ArmorStandData minion, ArmorStand armorStand, Location location, Function<Boolean> action) {
        if (armorStand == null || minion == null) return;
        MinionAction minionAction = minion.getMinionAction();
        switch (minionAction.getTypeFarm()) {
            case BREAK -> {
                blockBreakRunnable = new BlockBreakRunnable(minion, () -> {
                    Block block = minion.getMinionAction().getLocation().getBlock();
                    block.setType(Material.AIR);
                    action.callback(true);
                });
                blockBreakRunnable.start();
            }
            case PLACE -> {
                minionAction.getLocation().getBlock().setType(getPlace().toMaterial());
                action.callback(false);
            }

            default -> action.callback(false);
        }
    }

    @Override
    public boolean checkBlock(Location location) {
        return BlockUtils.isSpaceAvailable(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()), 2, false, getPlace());
    }

    @Override
    public Location checkBlockAround(Location location) {
        return BlockUtils.getAirBlockAround(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()), 2, false);
    }

    @Override
    public Block getRandomBlock(Location location) {
        Location minionLocation = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
        Block randomBlock = BlockUtils.getRandomBlock(minionLocation, 2, false);
        if (randomBlock.getLocation().equals(minionLocation)) {
            randomBlock = BlockUtils.getRandomBlock(minionLocation, 2, false);
        }
        return randomBlock;
    }

    @Override
    public void preExecuteAction(Location spawn, BiFunction<TypeFarm, Location> preExecution) {
        Block blockSpace = getRandomBlock(spawn);
        if (CompMaterial.fromBlock(blockSpace).equals(CompMaterial.AIR)) {
            preExecution.callback(TypeFarm.PLACE, blockSpace.getLocation());
            return;
        }
        Block block = getRandomBlock(spawn);
        if (CompMaterial.fromBlock(block).equals(CompMaterial.STONE) || CompMaterial.fromBlock(block).equals(getPlace())) {
            preExecution.callback(TypeFarm.BREAK, block.getLocation());
            return;
        }

        preExecution.callback(TypeFarm.NOTHING, spawn);
    }

    @Override
    public void stop() {
        if (blockBreakRunnable == null) return;
        blockBreakRunnable.cancel();
    }
}
