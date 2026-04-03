package com.choice.minionfarm.settings.messages;

import com.choice.minionfarm.api.FileAPI;
import com.choice.minionfarm.settings.utils.FarmYamlConfig;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

import static com.choice.minionfarm.utils.constants.Constants.FILE_MESSAGES;

@Getter
public class MessageRepository extends FarmYamlConfig {

    private String insertMinionInWorld;

    private String removeMinionWorld;

    private String minionMaxInsert;


    //Holograms
    private List<String> noSpaceAround;
    private List<String> welcomeToWorld;
    private List<String> working;

    public MessageRepository(){
        this.setPrefix("messages");
        this.loadConfiguration(NO_DEFAULT, FILE_MESSAGES);
    }

    @Override
    protected void onLoad() {
        this.insertMinionInWorld = this.getStringPath("insertMinionInWorld");
        this.removeMinionWorld = this.getStringPath("removeMinionWorld");
        this.minionMaxInsert = this.getStringPath("minionMaxInsert");

        this.noSpaceAround = this.getStringList("holograms.noSpaceAround").stream()
                .map(originalString -> ChatColor.translateAlternateColorCodes('&', originalString))
                .collect(Collectors.toList());
        this.working = this.getStringList("holograms.working").stream()
                .map(originalString -> ChatColor.translateAlternateColorCodes('&', originalString))
                .collect(Collectors.toList());
        this.welcomeToWorld = this.getStringList("holograms.welcomeToWorld").stream()
                .map(originalString -> ChatColor.translateAlternateColorCodes('&', originalString))
                .collect(Collectors.toList());
    }

    public List<String> getWorking(String playerName, int amount){
        return this.working.stream().map(m ->
                m.replace("{amount}", ""+amount)
                        .replace("{player_name}", playerName)
        ).toList();
    }
}
