package com.choice.minionfarm.settings.temporary;

import com.choice.minionfarm.utils.constants.Constants;
import lombok.Getter;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;
import java.util.UUID;

public class ActiveMinionsRepository extends YamlConfig {

    @Getter
    private List<String> minionsActive;

    public ActiveMinionsRepository() {
        this.loadConfiguration(NO_DEFAULT, Constants.FILE_ACTIVE_MINIONS);
    }

    @Override
    protected void onLoad() {
        this.minionsActive = this.getStringList("temp");
    }

    @Override
    protected void onSave() {
        this.set("temp", this.minionsActive);
    }

    public void addMinionsActive(UUID uuid){
        this.minionsActive.add(uuid.toString());
    }

    public boolean containsMinion(UUID uuid){
        return this.minionsActive.contains(uuid.toString());
    }

    public boolean removeMinion(UUID uuid){
        return this.minionsActive.remove(uuid.toString());
    }
}
