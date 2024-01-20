package com.choice.minionfarm.minion.repository.managers;

import com.choice.minionfarm.api.FileAPI;
import com.choice.minionfarm.minion.di.enums.MinionType;
import com.choice.minionfarm.minion.entity.EntityMinion;
import com.choice.minionfarm.minion.entity.minion.EntityMinionMiner;
import com.choice.minionfarm.minion.repository.data.MinionData;
import com.choice.minionfarm.minion.repository.local.MinionsRepository;
import com.choice.minionfarm.settings.temporary.ActiveMinionsRepository;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.mineacademy.fo.Common;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MinionManager {

    ActiveMinionsRepository repository = new ActiveMinionsRepository();

    private HashMap<String, EntityMinion> minionsHashMap = new HashMap<>();
    private HashMap<UUID, MinionData> activeMinion = new HashMap<>();

    public MinionManager(){
        loadMinions();
    }

    public void loadMinions(){

        Arrays.stream(FileAPI.getFileMinionsFull().listFiles()).forEach(each -> {
            try {
                String fileName = each.getName().replaceAll(".yml", "");
                MinionsRepository minionConfig = new MinionsRepository(each.getName());
                MinionType minionType = minionConfig.getMinionType();
                String key = minionConfig.getKey();
               if (minionType.equals(MinionType.MINER)) {
                    minionsHashMap.put(key, new EntityMinionMiner(minionConfig));
                }
            }catch (Exception e){
                e.printStackTrace();
                Common.error(e);
            }
        });
    }

    public EntityMinion getMinion(String key){
        return minionsHashMap.get(key);
    }

    public void addActiveMinion(UUID uuid, MinionData minionData){
        activeMinion.put(uuid, minionData);
        repository.addMinionsActive(uuid);
        repository.save();
    }

    public MinionData getMinionActive(UUID uuid){
        return activeMinion.getOrDefault(uuid, null);
    }

    public void removeMinionActive(UUID uuid){
        activeMinion.remove(uuid);
        repository.removeMinion(uuid);
        repository.save();
    }

    public boolean containsMinionInBlock(Block block){
        Location location = block.getLocation().clone().add(0.5, 1, 0.5);
        boolean contains = false;
        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, 0.5, 1, 0.5);
        for(Entity entity : entities){
            if(entity instanceof ArmorStand){
                contains = repository.containsMinion(entity.getUniqueId());
                if(contains) break;
            }
        }
        return contains;
    }

}
