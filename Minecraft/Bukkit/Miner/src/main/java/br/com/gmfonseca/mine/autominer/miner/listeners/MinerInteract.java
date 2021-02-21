package br.com.gmfonseca.mine.autominer.miner.listeners;

import br.com.gmfonseca.mine.autominer.entity.Miner;
import br.com.gmfonseca.mine.autominer.miner.controller.MinerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MinerInteract implements Listener {

    @EventHandler
    public void minerInteract(PlayerArmorStandManipulateEvent e){
        if(e.getRightClicked() != null){
            ArmorStand stand = e.getRightClicked();
            Player p = e.getPlayer();
            Miner miner = MinerManager.getMinerByArmorStand(stand);

            boolean isStaff = (p.isOp() || p.hasPermission("gamma.staff.miner"));

            if(miner != null){
                e.setCancelled(true);

                if(isStaff || miner.getOwner() == p){

                    p.openInventory(buildMenuInventory(miner));

                }else{
                    p.sendMessage("You don't have permission to access " + miner.getOwner().getName() + "'s miner.");
                }

            }else{
                p.sendMessage("This miner is broken. Ask help for some admin.");
            }
        }
    }

    //TODO build a static menu inventory on Miner's profile
    private Inventory buildMenuInventory(Miner miner){
        ItemStack item;
        ItemMeta meta;
        List<String> lore = new ArrayList<>();
        byte data;
        String name;

        Inventory menu = Bukkit.createInventory(miner, 9, miner.getOwner().getName() + "'s miner");

        // Start/Stop mining
        data = (byte) ((miner.isMining())? 1 : 10);
        name = (data==1)? ChatColor.RED + "Stop" : ChatColor.GREEN + "Start";
        item = new ItemStack(Material.INK_SACK, 1, (short)0, data);
        meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        menu.setItem(0, item);

        // Info
        item = new ItemStack(Material.BOOK, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Info");
        lore = buildInfoLore(miner);
        meta.setLore(lore);
        item.setItemMeta(meta);
        menu.setItem(4, item);

        // Backpack
        item = new ItemStack(Material.CHEST, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Backpack");
        item.setItemMeta(meta);
        menu.setItem(6, item);

        // Remove
        item = new ItemStack(Material.BED, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Remove");
        item.setItemMeta(meta);
        menu.setItem(8, item);

        return menu;
    }

    private List<String> buildInfoLore(Miner miner){
        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.GRAY + "ID #" + ChatColor.WHITE + miner.getId());
        lore.add("");
        lore.add(ChatColor.GRAY + "Owner: " + ChatColor.WHITE + miner.getOwner().getName());
        lore.add(ChatColor.GRAY + "Delay: " + ChatColor.WHITE + miner.getDelaySeconds() + " seconds");
        lore.add("");
        lore.add(ChatColor.GRAY + "Mining: " + ChatColor.WHITE + miner.isMining());
        lore.add(ChatColor.GRAY + "Direction: " + ChatColor.WHITE + miner.getDirection());
        lore.add(ChatColor.GRAY + "Current Target: " + ChatColor.WHITE + miner.getTarget().getType().name());

        return lore;
    }

}
