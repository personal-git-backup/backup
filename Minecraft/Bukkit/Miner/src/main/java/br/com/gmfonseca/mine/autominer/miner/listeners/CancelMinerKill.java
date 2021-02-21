package br.com.gmfonseca.mine.autominer.miner.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;


public class CancelMinerKill implements Listener {

    @EventHandler
    private void cancelEntityKill(EntityDeathEvent e){

    }

}
