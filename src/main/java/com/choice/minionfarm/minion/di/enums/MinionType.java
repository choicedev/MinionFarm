package com.choice.minionfarm.minion.di.enums;

import lombok.Getter;
import org.mineacademy.fo.remain.CompMaterial;

public enum MinionType {

    MINER(CompMaterial.STONE_PICKAXE),
    FARMER(CompMaterial.STONE_AXE),
    NONE(CompMaterial.AIR);

    @Getter
    private CompMaterial handItem;

    MinionType(CompMaterial handItem) {
        this.handItem = handItem;

    }

}
