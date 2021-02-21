package br.com.gmfonseca.mine.autominer.miner.listeners;

import br.com.gmfonseca.mine.autominer.miner.controller.MinerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        try {

            Player p = e.getPlayer();

            MinerManager.increaseMinerAmount(p, (byte) 1);
        }catch (Exception err){
            Bukkit.getConsoleSender().sendMessage("§c|method  PlayerEvent:onJoin() -> " + err.getMessage());
        }
    }

}
