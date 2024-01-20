package com.choice.minionfarm.player.repository.data;

import com.choice.minionfarm.minion.repository.data.MinionData;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataStore {

    @Getter
    private final HashMap<UUID, MinionData> minions = new HashMap<>();

    @Setter
    @Getter
    private UUID uuid;

    @Setter
    @Getter
    private int maxMinions;

    @Setter
    @Getter
    private String lastLogin;

    private static final HashMap<UUID, PlayerDataStore> players = new HashMap<>();

    public PlayerDataStore(UUID uuid) {
        if (players.containsKey(uuid)) return;
        this.uuid = uuid;
        this.maxMinions = 2;
        players.put(uuid, this);
    }


    public void addMinion(MinionData data) {
        minions.put(data.getUuid(), data);
    }

    public static PlayerDataStore getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public void removeMinion(UUID uuid) {
        minions.remove(uuid);
    }
}
