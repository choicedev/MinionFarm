package com.choice.minionfarm.utils.event;

import com.choice.minionfarm.api.FarmAPI;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class EventManager {

    public static void registerEvents(Listener listener) {
        PluginManager pm = FarmAPI.getInstance().getServer().getPluginManager();
        pm.registerEvents(listener, FarmAPI.getInstance());
    }
}
