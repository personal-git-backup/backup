package br.com.gmfonseca.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpGUI implements Listener, CommandExecutor {

    private final int SIZE = 9;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            gui(p);
        }

        return false;
    }

    private void gui(Player p) {

        ItemStack spawn = new ItemStack(Material.BEACON);
        ItemMeta m_spawn = spawn.getItemMeta();

        m_spawn.setDisplayName(ChatColor.YELLOW + "Spawn");
        spawn.setItemMeta(m_spawn);

        Inventory warps = Bukkit.createInventory(p, SIZE, "Warps");
        warps.setItem(4, spawn);

        p.openInventory(warps);
    }

    @EventHandler
    private void clickInventory(InventoryClickEvent e){
        try {
            Player p = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();

            if (e.getInventory() != null && e.getInventory().getName().equalsIgnoreCase("Warps") && item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null) {

                p.chat("/" + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase()));
                p.closeInventory();
                e.setCancelled(true);
            }
        }catch (Exception err){
            Bukkit.getLogger().warning(err.toString() + " | Class WarpGUI, clickInventory()");
        }
    }

    @EventHandler(priority= EventPriority.HIGH)
    private void clickCompass(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getItemInHand();

        if (item != null && item.getType() == Material.COMPASS){
            if(item.getItemMeta() != null && item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "WARPS")
                && e.getAction() != Action.PHYSICAL){

                e.setCancelled(true);

                gui(p);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            }
        }
    }


}
