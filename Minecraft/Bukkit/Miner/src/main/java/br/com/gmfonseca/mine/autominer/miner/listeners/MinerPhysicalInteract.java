package br.com.gmfonseca.mine.autominer.miner.listeners;

import br.com.gmfonseca.mine.autominer.entity.Miner;
import br.com.gmfonseca.mine.autominer.miner.controller.MinerManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MinerPhysicalInteract implements Listener {

    @EventHandler
    private void minerHitted(EntityDamageByEntityEvent e){

        if(e.getEntity() instanceof ArmorStand){
            Miner miner = MinerManager.getMinerByArmorStand((ArmorStand) e.getEntity());
            if(miner != null){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void cancelLavaKill(EntityCombustEvent e){
        if(e.getEntity() instanceof ArmorStand){
            Miner miner = MinerManager.getMinerByArmorStand((ArmorStand) e.getEntity());
            if(miner != null){
                e.setCancelled(true);
            }
        }
    }

}
