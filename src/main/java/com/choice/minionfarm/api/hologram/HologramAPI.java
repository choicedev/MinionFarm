package com.choice.minionfarm.api.hologram;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public class HologramAPI {

    private final String HOLOGRAM_ID;
    private Hologram hologram;

    public HologramAPI(String HOLOGRAM_ID) {
        this.HOLOGRAM_ID = HOLOGRAM_ID;
    }

    public void createHologram(Location location, List<String> lines){
        Location hologramLocation;

        Block blockY = location.clone().getBlock();
        double hologramHeight = 1.2 + lines.size() * 0.3;
        hologramLocation = new Location(blockY.getWorld(), blockY.getX() + 0.5, blockY.getY() + hologramHeight, blockY.getZ() + 0.5);

        hologram = DHAPI.createHologram(HOLOGRAM_ID, hologramLocation, lines);
        hologram.setDisplayRange(20);
    }

    public void updateHologram(Location location, List<String> lines){
        DHAPI.removeHologram(HOLOGRAM_ID);
        createHologram(location, lines);
    }

    public void removeHologram(){
        DHAPI.removeHologram(HOLOGRAM_ID);
    }
}
