package br.com.gmfonseca.api;

import br.com.gmfonseca.main.Controller;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.logging.Level;

public class Kit implements Listener{

    private static ItemStack default_iconB = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
    private static ItemStack default_iconY = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);

    public Kit() {
        ItemMeta m_item = default_iconB.getItemMeta();
        m_item.setDisplayName(" ");
        default_iconB.setItemMeta(m_item);
        default_iconY.setItemMeta(m_item);
    }

    public static void gen_default_inventory(Player p){
        try{
            Inventory inv = p.getInventory();
            inv.clear();

            ItemStack warps_icon = new ItemStack(Material.COMPASS, 1);
            ItemMeta m_warps = warps_icon.getItemMeta();
            m_warps.setDisplayName(ChatColor.AQUA + "Warps");
            ArrayList<String> warps_lore = new ArrayList<>();
            warps_lore.add(ChatColor.GRAY + "Viaje por locais predefinidos do servidor");
            m_warps.setLore(warps_lore);
            warps_icon.setItemMeta(m_warps);

            ItemStack kits_icon = new ItemStack(Material.CHEST, 1);
            ItemMeta m_kits = kits_icon.getItemMeta();
            m_kits.setDisplayName(ChatColor.YELLOW + "Kits");
            ArrayList<String> kits_lore = new ArrayList<>();
            kits_lore.add(ChatColor.GRAY + "Veja a sua lista de kits");
            m_kits.setLore(kits_lore);
            kits_icon.setItemMeta(m_kits);

            inv.setItem(0, default_iconB);
            for (int i = 1; i < 9; i+=2) {
                inv.setItem(i, default_iconY);
            }
            inv.setItem(8, default_iconB);

            inv.setItem(2, kits_icon);
            inv.setItem(6, warps_icon);

            if(p.hasPermission("gamma.staff.admin")){
                ItemStack admin_icon = new ItemStack(Material.REDSTONE, 1);
                ItemMeta a_kits = admin_icon.getItemMeta();
                a_kits.setDisplayName(ChatColor.RED + "ADMIN");
                ArrayList<String> admin_lore = new ArrayList<>();
                admin_lore.add(ChatColor.GRAY + "Entrar no modo Admin");
                a_kits.setLore(admin_lore);
                admin_icon.setItemMeta(a_kits);

                inv.setItem(4, admin_icon);
            }else{
                inv.setItem(4, default_iconB);
            }

        }catch (Exception e){
            Bukkit.getLogger().log(Level.SEVERE, "", e);
        }
    }



    public static void fill_soup(Inventory inv, Player p){

        if(inv.equals(p.getInventory())) {
            ItemStack bowl = new ItemStack(Material.BOWL, 64);
            ItemStack brown_mush = new ItemStack(Material.BROWN_MUSHROOM, 64);
            ItemStack red_mush = new ItemStack(Material.RED_MUSHROOM, 64);

            inv.setItem(12, brown_mush);
            inv.setItem(13, bowl);
            inv.setItem(14, red_mush);

        }

        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP);

        for(int i = inv.firstEmpty() ; i != -1 ; i=inv.firstEmpty()){
            inv.setItem(i, soup);
        }
    }

    @EventHandler
    private void soupRegen(PlayerInteractEvent e){
        if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getPlayer().getItemInHand().getType() == Material.MUSHROOM_SOUP){
            e.setCancelled(true);

            Player p = e.getPlayer();
            double health = p.getHealth(),
                   maxHealth = p.getMaxHealth();

            if(health < maxHealth) {
                p.setHealth((health+7>=maxHealth) ? maxHealth : health+7);
                p.getItemInHand().setType(Material.BOWL);

                p.playSound(p.getLocation(), Sound.BURP, 5, 5);
            }
        }
    }

    @EventHandler
    private void swordRepair(EntityDamageByEntityEvent e){
        try {
            if (e.getDamager() instanceof Player) {
                Player p = (Player) e.getDamager();
                ItemStack item = p.getItemInHand();

                if(item != null) {
                    switch (item.getType()) {
                        case DIAMOND_SWORD:
                        case IRON_SWORD:
                        case GOLD_SWORD:
                        case STONE_SWORD:
                        case WOOD_SWORD:
                            item.setDurability((short) 0);
                    }
                }
            }
        } catch (Exception err){
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Kit, swordRepair()");
        }
    }

    @EventHandler
    private void itemRepair(EntityDamageByEntityEvent e){
        try {
            if (e.getDamager() instanceof Player) {
                Player p = (Player) e.getDamager();
                ItemStack item = p.getItemInHand();

                if(item != null) {
                    switch (item.getType()) {
                        case DIAMOND_AXE:
                        case FISHING_ROD:
                        case BOW:
                        case WOOD_AXE:
                            item.setDurability((short) 0);
                    }
                }
            }
        } catch (Exception err){
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Kit, itemRepair()");
        }
    }

    @EventHandler
    private void helmetRepair(EntityDamageByEntityEvent e){
        try {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                ItemStack item = p.getInventory().getHelmet();

                if(item != null) {
                    switch (item.getType()) {
                        case DIAMOND_HELMET:
                        case IRON_HELMET:
                        case GOLD_HELMET:
                        case CHAINMAIL_HELMET:
                        case LEATHER_HELMET:
                            item.setDurability((short) 0);
                    }
                }
            }
        } catch (Exception err){
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Kit, helmetRepair()");
        }
    }

    @EventHandler
    private void chestplateRepair(EntityDamageByEntityEvent e){
        try {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                ItemStack item = p.getInventory().getChestplate();

                if(item != null) {
                    switch (item.getType()) {
                        case DIAMOND_CHESTPLATE:
                        case IRON_CHESTPLATE:
                        case GOLD_CHESTPLATE:
                        case CHAINMAIL_CHESTPLATE:
                        case LEATHER_CHESTPLATE:
                            item.setDurability((short) 0);
                    }
                }
            }
        } catch (Exception err){
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Kit, chestplateRepair()");
        }
    }

    @EventHandler
    private void leggingsRepair(EntityDamageByEntityEvent e){
        try {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                ItemStack item = p.getInventory().getLeggings();

                if(item != null) {
                    switch (item.getType()) {
                        case DIAMOND_LEGGINGS:
                        case IRON_LEGGINGS:
                        case GOLD_LEGGINGS:
                        case CHAINMAIL_LEGGINGS:
                        case LEATHER_LEGGINGS:
                            item.setDurability((short) 0);
                    }
                }
            }
        } catch (Exception err){
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Kit, leggingsRepair()");
        }
    }

    @EventHandler
    private void bootsRepair(EntityDamageByEntityEvent e){
        try {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                ItemStack item = p.getInventory().getBoots();

                if(item != null) {
                    switch (item.getType()) {
                        case DIAMOND_BOOTS:
                        case IRON_BOOTS:
                        case GOLD_BOOTS:
                        case CHAINMAIL_BOOTS:
                        case LEATHER_BOOTS:
                            item.setDurability((short) 0);
                    }
                }
            }
        } catch (Exception err){
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Kit, bootsRepair()");
        }
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent e){
        try {
            Player p = e.getEntity();

            Controller.player_kit.put(p, "none");
        } catch (Exception err){
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Kit, onDeath()");
        }
    }

    @EventHandler
    private void onDropKitItem(PlayerDropItemEvent e){
        try {
            Item item = e.getItemDrop();

            if (item.getItemStack().getItemMeta().getDisplayName() != null) {
                Player p = e.getPlayer();

                if (Controller.player_kit.get(p).equalsIgnoreCase("kangaroo") && item.getItemStack().getType() == Material.FIREWORK) {
                    e.setCancelled(true);
                }
            }
        } catch (Exception err){
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Kit, onDropKitItem()");
        }
    }

}
