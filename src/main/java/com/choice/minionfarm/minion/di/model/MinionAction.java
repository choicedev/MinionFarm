package com.choice.minionfarm.minion.di.model;

import com.choice.minionfarm.minion.di.enums.TypeFarm;
import lombok.Data;
import org.bukkit.Location;

@Data
public class MinionAction {

    private TypeFarm typeFarm;
    private Location location;

    public MinionAction(TypeFarm type, Location location) {
        this.typeFarm = type;
        this.location = location;
    }
}
