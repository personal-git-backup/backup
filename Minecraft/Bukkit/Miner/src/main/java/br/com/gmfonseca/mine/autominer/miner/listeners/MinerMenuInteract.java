package br.com.gmfonseca.mine.autominer.miner.listeners;

import br.com.gmfonseca.mine.autominer.entity.Miner;
import br.com.gmfonseca.mine.autominer.miner.controller.MinerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashSet;
import java.util.Set;

public class MinerMenuInteract implements Listener {

    //TODO inventario do usuario eh validado tambem, cancelando evento quando manipulado na tela do menu do mineiro

    @EventHandler
    private void menuInteract(InventoryClickEvent e){
        try{

            // if the inventory doesn't exists or holder is not a miner
            if(e.getClickedInventory() == null || !(e.getClickedInventory().getHolder() instanceof Miner)) return;

            // if the clicked item is NULL or AIR
            if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

            // if the inventory's size isn't 9
            if(e.getClickedInventory().getSize() != 9) return;

            // logic begin
            e.setCancelled(true);

            //if who clicked is a player
            if(e.getWhoClicked() instanceof Player){

                Player p = (Player) e.getWhoClicked();
                Miner miner = (Miner) e.getClickedInventory().getHolder();

                switch (e.getCurrentItem().getType()){
                    case INK_SACK: // ON/OFF
                        byte data = e.getCurrentItem().getData().getData();
                        String message = null;

                        if (data == 1) { // if data is red
                            if(miner.stopMining())
                                message = ChatColor.RED + "[Miner #" + miner.getId() + "] Stopping mining.";
                            else
                                message = ChatColor.RED + "[Miner #" + miner.getId() + "] Can't stop mining.";
                        }else if (data == 10) {// if data is green
                            if(miner.startMining())
                                message = ChatColor.GREEN + "[Miner #" + miner.getId() + "] Starting mining.";
                            else
                                message = ChatColor.RED + "[Miner #" + miner.getId() + "] Can't start mining.";
                        }

                        if(message != null && !p.equals(miner.getOwner())) {
                            p.sendMessage(message);
                        }
                        break;

                    case CHEST: // BACKPACK
                        p.sendMessage("abrindo backpack"); //TODO: remove later, debug line
                        miner.openBackpack(p);
                        break;

                    case BED: // REMOVE
                        MinerManager.removeMiner(p, miner);
                        if(!p.equals(miner.getOwner()))
                            p.sendMessage(ChatColor.RED + "[Miner #" + miner.getId() + "] Removed from World.");
                        miner.getOwner().sendMessage(ChatColor.RED + "[Miner #" + miner.getId() + "] Removed from World.");
                        break;

                    default: // INFO
                        return;
                }

                //close inventory for all viewers
                //TODO NOT WORKING BECAUSE MENU ARE NOT STATIC FOR ALL
                Set<HumanEntity> viewers = new HashSet<>(e.getClickedInventory().getViewers());
                for (HumanEntity viewer : viewers) {
                    if(viewer!=null) {
                        Bukkit.getConsoleSender().sendMessage("Viewer: " + viewer.getName());
                        viewer.closeInventory();
                    }
                }

            }else{
                e.getWhoClicked().sendMessage("You may be a Player to interact with miner's menu interface.");
            }
        }catch (Exception err){
            Bukkit.getConsoleSender().sendMessage("§c|listener  MinerMenuInteract:menuInteract() -> " + err.getMessage());
        }
    }
}
