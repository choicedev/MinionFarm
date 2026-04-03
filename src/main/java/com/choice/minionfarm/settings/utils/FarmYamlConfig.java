package com.choice.minionfarm.settings.utils;

import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;
import java.util.stream.Collectors;

public abstract class FarmYamlConfig extends YamlConfig {

    @Setter
    public String prefix;

    public String getStringPath(String path) {
        return ChatColor.translateAlternateColorCodes('&', this.getString(this.prefix + "." + path, ""));
    }

    public int getIntegerPath(String path) {
        return this.getInteger(this.prefix + "." + path, 0);
    }

    public List<String> getListStringPath(String path) {
        return this.getStringList(this.prefix + "." + path).stream()
                .map(originalString -> ChatColor.translateAlternateColorCodes('&', originalString))
                .collect(Collectors.toList());
    }

    public CompMaterial getMaterialPath(String path) {
        return this.get(this.prefix + "." + path, CompMaterial.class, CompMaterial.AIR);
    }
}
