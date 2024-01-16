package com.choice.minionfarm.player.command;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.choice.minionfarm.api.FarmAPI;
import com.choice.minionfarm.minion.entity.EntityMinion;
import com.choice.minionfarm.player.entity.EntityPlayer;
import org.bukkit.entity.Player;

@CommandAlias("minion")
public class MinionCommand extends BaseCommand {

    @Subcommand("cobblestone")
    public static void onGiveFarmCobbleStone(Player player){
        EntityPlayer entityPlayer = new EntityPlayer(player);
        EntityMinion minion = FarmAPI.getMinionManager().getMinion("cobblestone");
        entityPlayer.addItems(minion.getHead());
    }

}
