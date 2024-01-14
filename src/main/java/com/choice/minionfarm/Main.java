package com.choice.minionfarm;

import com.choice.minionfarm.minion.repository.managers.MinionManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.fo.plugin.SimplePlugin;

public final class Main extends SimplePlugin {

    public static MinionManager minionManager;
    @Override
    protected void onPluginStart() {
        minionManager = new MinionManager();
    }
}
