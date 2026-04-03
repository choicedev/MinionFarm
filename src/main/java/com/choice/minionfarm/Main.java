package com.choice.minionfarm;

import co.aikar.commands.BukkitCommandManager;
import com.choice.minionfarm.api.FileAPI;
import com.choice.minionfarm.minion.repository.data.ArmorStandData;
import com.choice.minionfarm.minion.repository.managers.MinionManager;
import com.choice.minionfarm.player.command.MinionCommand;
import com.choice.minionfarm.player.event.InteractEvent;
import com.choice.minionfarm.player.event.JoinEvent;
import com.choice.minionfarm.player.repository.data.PlayerDataStore;
import com.choice.minionfarm.settings.messages.MessageRepository;
import com.choice.minionfarm.utils.constants.Constants;
import org.mineacademy.fo.model.SimpleRunnable;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;
import java.util.HashSet;

import static com.choice.minionfarm.utils.constants.Constants.FILE_SEPARATOR;

public final class Main extends SimplePlugin {

    public static MinionManager minionManager;
    public static MessageRepository messageRepository;

    @Override
    protected void onPluginStart() {

        if (!new File(FileAPI.getFileMinionsFull().getPath(), "cobblestone.yml").exists()) {
            saveResource(Constants.FILE_MINIONS_CONFIG + FILE_SEPARATOR + "cobblestone.yml", false);
        }
        minionManager = new MinionManager();
        messageRepository = new MessageRepository();

        registerEvents(new InteractEvent());
        registerEvents(new JoinEvent());

        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new MinionCommand());


        new SimpleRunnable() {

            @Override
            public void run() {
                new HashSet<>(PlayerDataStore.getPlayers().values()).forEach(dataStore -> dataStore.getMinions().values().forEach(ArmorStandData::minionAction));
            }
        }.runTaskTimer(this, 20, 20L);
    }
}
