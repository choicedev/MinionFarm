package com.choice.minionfarm.player.entity;

import com.choice.minionfarm.api.FarmAPI;
import com.choice.minionfarm.utils.PlaceholdersFunction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mineacademy.fo.PlayerUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityPlayer {

    private final Player player;

    public EntityPlayer(Player player) {
        this.player = player;
    }

    public UUID getUniqueId(){
        return this.player.getUniqueId();
    }

    public Location getLocation(){
        return this.player.getLocation();
    }

    public PlayerInventory getInventory(){
        return this.player.getInventory();
    }

    public ItemStack getItemInMainHand(){
        return this.getInventory().getItemInMainHand();
    }

    public ItemStack getItemInSecondHand(){
        return this.getInventory().getItemInOffHand();
    }

    public Map<Integer, ItemStack> removeItemInventory(ItemStack itemStack){
        return getInventory().removeItem(itemStack);
    }

    public boolean hasEmptyInventory(){
        return PlayerUtil.hasEmptyInventory(this.player);
    }

    public Map<Integer, ItemStack> addItems(Collection<ItemStack> items){
        return PlayerUtil.addItems(getInventory(), items.toArray(new ItemStack[items.size()]));
    }

    public Map<Integer, ItemStack> addItems(ItemStack... items){
        return PlayerUtil.addItems(getInventory(), items);
    }

    public boolean addItemsOrDrop(ItemStack... items){
        return PlayerUtil.addItemsOrDrop(this.player, items);
    }

    public ItemStack getItemHand(){
        return this.player.getItemInHand();
    }

    public void sendMessage(String message) {
        sendMessage(message, null);
    }


    public void sendMessage(String message, PlaceholdersFunction placeholders) {
        Map<String, String> map = new HashMap<>();
        if (placeholders != null) {
            placeholders.invoke(map);
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        Component legacyComponent = colorize(message);

        FarmAPI.getAudiences().player(player).sendMessage(
                deserialize(legacyComponent)
        );
    }

    MiniMessage miniMessage = MiniMessage.miniMessage();
    private Component colorize(String text) {
        Component legacyComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(text);

        String serializedLegacy = LegacyComponentSerializer.legacySection().serialize(legacyComponent);
        legacyComponent = LegacyComponentSerializer.legacySection().deserialize(serializedLegacy);

        String miniMessageText = miniMessage.serialize(legacyComponent)
                .replace("\\<", "<")
                .replace("\\\\<", "<");

        return miniMessage.deserialize(miniMessageText);
    }

    private Component deserialize(Component legacyComponent) {

        String message = miniMessage.serialize(legacyComponent);

        return miniMessage.deserialize(message);
    }


}
