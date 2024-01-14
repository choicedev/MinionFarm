package com.choice.minionfarm.api;

import com.choice.minionfarm.Main;
import com.choice.minionfarm.minion.repository.managers.MinionManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;

import static com.choice.minionfarm.utils.constants.Constants.FILE_MINIONS_CONFIG;
import static com.choice.minionfarm.utils.constants.Constants.FILE_SEPARATOR;

public class FarmAPI {

    public static Main getInstance(){
        try{
            return (Main) SimplePlugin.getInstance();
        }catch (Exception e){
            Common.error(e);
            throw e;
        }
    }


    public static BukkitAudiences getAudiences(){
        try{
            return BukkitAudiences.create(SimplePlugin.getInstance());
        }catch (Exception e){
            Common.error(e);
            throw e;
        }
    }

    public static LegacyComponentSerializer getLegacyComponentSerializer(){
        try{
            return LegacyComponentSerializer.builder()
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .character(LegacyComponentSerializer.SECTION_CHAR)
                    .character(LegacyComponentSerializer.AMPERSAND_CHAR)
                    .character(LegacyComponentSerializer.HEX_CHAR)
                    .build();
        }catch (Exception e){
            Common.error(e);
            throw e;
        }
    }


    public static File getFileMinions(){
        try {
            File file = new File(getInstance().getDataFolder()+FILE_SEPARATOR+FILE_MINIONS_CONFIG);
            if(!file.exists()) {
                file.mkdirs();
            }
            return file;
        }catch (Exception e){
            e.printStackTrace();
            Common.error(e);
            throw e;
        }
    }

    public static MinionManager getMinionManager(){
        try {
            return Main.minionManager;
        }catch (Exception e){
            e.printStackTrace();
            Common.error(e);
            throw e;
        }
    }
}
