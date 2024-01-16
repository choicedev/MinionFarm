package com.choice.minionfarm.minion.entity.minion;

import com.choice.minionfarm.minion.entity.EntityMinion;
import com.choice.minionfarm.minion.repository.data.MinionData;
import com.choice.minionfarm.minion.repository.local.MinionsRepository;
import com.choice.minionfarm.utils.BlockUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import static com.choice.minionfarm.utils.BlockUtils.getRandomBlock;

public class EntityMinionMiner extends EntityMinion {


    @Getter
    private ItemCreator blockPlace;

    public EntityMinionMiner(MinionsRepository minionsRepository) {
        super(minionsRepository);
        this.blockPlace = ItemCreator.of(getPlace());
    }

    @Override
    public void update(MinionData minion, ArmorStand armorStand, Location location) {

    }

    @Override
    public boolean checkBlock(Location location) {
        Block block = getRandomBlock(location, (int) (location.getY() - 1), 2, false);
        CompMaterial material = CompMaterial.fromBlock(block);

        return material.equals(CompMaterial.AIR) || material.equals(getPlace());
    }

    @Override
    public boolean checkBlockAround(Location location) {
        return BlockUtils.isSpaceAvailable(getPlace(), location, 2, false);
    }


}
