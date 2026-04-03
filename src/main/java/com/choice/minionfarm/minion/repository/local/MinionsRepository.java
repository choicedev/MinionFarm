package com.choice.minionfarm.minion.repository.local;

import com.choice.minionfarm.api.FileAPI;
import com.choice.minionfarm.minion.di.enums.MinionType;
import com.choice.minionfarm.utils.constants.MinionConfigConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;
import java.util.stream.Collectors;

import static com.choice.minionfarm.utils.constants.Constants.FILE_SEPARATOR;

@NoArgsConstructor
public class MinionsRepository extends YamlConfig {

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
    @Getter
    private String armorUrl;
    @Getter
    private int delayFarm;
    @Getter
    private int damageOnBlock;
    @Getter
    private CompMaterial giveItem;
    private String fileName;

    public MinionsRepository(String fileName) {
        this.fileName = fileName.replaceAll(".yml", "");
        this.loadConfiguration(NO_DEFAULT, FileAPI.getFileMinions().getPath() + FILE_SEPARATOR + fileName);
    }


    @Override
    protected void onLoad() {
        this.key = fileName;
        this.title = getStringPath(MinionConfigConstants.TITLE);
        this.headSkin = getStringPath(MinionConfigConstants.HEAD_SKIN);
        this.lore = getListStringPath(MinionConfigConstants.LORE);
        this.itemHand = getMaterialPath(MinionConfigConstants.HAND_ITEM);
        this.minionType = getMinionTypePath();
        this.place = getMaterialPath(MinionConfigConstants.PLACE);
        this.armorUrl = getStringPath(MinionConfigConstants.ARMOR_URL);
        this.delayFarm = getIntegerPath(MinionConfigConstants.DELAY);
        this.giveItem = getMaterialPath(MinionConfigConstants.GIVE_ITEM);
        this.damageOnBlock = getIntegerPath(MinionConfigConstants.DAMAGE);
    }

    private String getStringPath(String path) {
        return ChatColor.translateAlternateColorCodes('&', this.getString(this.fileName + "." + path, ""));
    }

    private int getIntegerPath(String path) {
        return this.getInteger(this.fileName + "." + path, 0);
    }

    private List<String> getListStringPath(String path) {
        return this.getStringList(this.fileName + "." + path).stream()
                .map(originalString -> ChatColor.translateAlternateColorCodes('&', originalString))
                .collect(Collectors.toList());
    }

    private CompMaterial getMaterialPath(String path) {
        return this.get(this.fileName + "." + path, CompMaterial.class, CompMaterial.AIR);
    }

    private MinionType getMinionTypePath() {
        String minion = getStringPath(MinionConfigConstants.MINION_TYPE);
        try {
            return MinionType.valueOf(minion);
        } catch (Exception e) {
            return MinionType.NONE;
        }
    }
}
