package com.choice.minionfarm.api;

import com.choice.minionfarm.Main;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

public class MinionFarmAPI {

    public static Main getInstance(){
        try{
            return (Main) SimplePlugin.getInstance();
        }catch (Exception e){
            e.printStackTrace();
            Common.error(e);
            return null;
        }
    }


    public static BukkitAudiences getAudiences(){
        try{
            return BukkitAudiences.create(SimplePlugin.getInstance());
        }catch (Exception e){
            e.printStackTrace();
            Common.error(e);
            return null;
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
            e.printStackTrace();
            Common.error(e);
            return null;
        }
    }

}
