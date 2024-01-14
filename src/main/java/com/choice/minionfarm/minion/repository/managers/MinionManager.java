package com.choice.minionfarm.minion.repository.managers;

import com.choice.minionfarm.api.FarmAPI;
import com.choice.minionfarm.minion.di.enums.MinionType;
import com.choice.minionfarm.minion.entity.EntityMinion;
import com.choice.minionfarm.minion.entity.minion.EntityMinionMiner;
import com.choice.minionfarm.minion.repository.data.local.MinionsYML;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlConfig;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class MinionManager {

    private HashMap<String, EntityMinion> minionsHashMap = new HashMap<>();

    public void loadMinions(){

        Arrays.stream(FarmAPI.getFileMinions().listFiles()).forEach(each -> {
            String fileName = each.getName().replaceAll(".yml", "");
            MinionsYML minionConfig = new MinionsYML(each.getName());
            MinionType minionType = minionConfig.getMinionType();
            String key = minionConfig.getKey();
            Common.log("Loading minion: "+fileName+"..." );

            if(minionType.equals(MinionType.MINER)){
                minionsHashMap.put(key, new EntityMinionMiner(minionConfig));
            }
        });

    }

    public EntityMinion getMinion(String key){
        return minionsHashMap.get(key);
    }

}
