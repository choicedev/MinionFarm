package com.choice.minionfarm.minion.repository.data.local;

import com.choice.minionfarm.minion.di.enums.MinionType;
import com.choice.minionfarm.utils.constants.MinionConfigConstants;
import lombok.Getter;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;

public class MinionsYML extends YamlConfig {

    @Getter
    private String key;

    @Getter
    private String title;
    @Getter
    private String headSkin;
    @Getter
    private List<String> lore;
    @Getter
    private MinionType minionType;
    @Getter
    private CompMaterial itemHand;
    @Getter
    private CompMaterial place;

    private String fileName;

    public MinionsYML(String fileName){
        this.fileName = fileName.replaceAll(".yml", "");
        this.loadConfiguration(NO_DEFAULT, fileName);
    }


    @Override
    protected void onLoad() {
        this.key = getStringPath(MinionConfigConstants.KEY);
        this.title = getStringPath(MinionConfigConstants.TITLE);
        this.headSkin = getStringPath(MinionConfigConstants.HEAD_SKIN);
        this.lore = getStringList(MinionConfigConstants.LORE);
        this.itemHand = getMaterialPath(MinionConfigConstants.HAND_ITEM);
        this.minionType = getMinionTypePath(MinionConfigConstants.MINION_TYPE);
        this.place = getMaterialPath(MinionConfigConstants.PLACE);
    }

    private String getStringPath(String path) {
        return this.getString(this.fileName + "." + path, "");
    }

    private List<String> getListStringPath(String path) {
        return this.getStringList(this.fileName + "." + path);
    }

    private CompMaterial getMaterialPath(String path) {
        return this.get(this.fileName + "." + path, CompMaterial.class, CompMaterial.AIR);
    }

    private MinionType getMinionTypePath(String path) {
        String minion = getStringPath(path);
        try {
            return MinionType.valueOf(minion);
        } catch (Exception e) {
            return MinionType.NONE;
        }
    }
}
