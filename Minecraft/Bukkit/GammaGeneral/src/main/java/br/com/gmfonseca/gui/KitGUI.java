package br.com.gmfonseca.gui;

import br.com.gmfonseca.main.Controller;
import br.com.gmfonseca.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class KitGUI implements Listener, CommandExecutor {

    private final int SIZE = 54;
    private final int MAX_KITS = 4;

    private ArrayList<String> kits = new ArrayList<>();
    private int OWNED_KITS;

    private void gui(Player p) {
        OWNED_KITS = 0;

        Inventory inv = Bukkit.createInventory(p, SIZE, "Seus kits");

        insert_default(inv, p);
        insert_kits(inv, p);

        p.openInventory(inv);
    }

    private void generate_gui(Player p) {
        OWNED_KITS = 0;

        Inventory inv = Bukkit.createInventory(p, SIZE, "Seus kits");

        insert_default(inv, p);
        insert_kits(inv, p);

        p.openInventory(inv);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("kits")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                if (p.hasPermission("gamma.kits")) {
                    if (args.length == 0) {
                        generate_gui(p);
                    }
                } else {
                    p.sendMessage(Main.CONFIGS.get("messages").getConfig().get("Personal.Command.noPermCmd").toString()
                            .replaceAll("&", "§")
                            .replaceAll("%cmd%", "kits"));
                }
            }
        }

        return false;
    }

    private void insert_default(Inventory inv, Player p) {
        double kills = Controller.player_kills.get(p);
        double deaths = Controller.player_deaths.get(p);
        double kdr = calculateKDRatio(kills, deaths);
        DecimalFormat df = new DecimalFormat("0.0#");

        //default icon
        ItemStack default_iconB = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemStack default_iconY = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta m_item = default_iconB.getItemMeta();
        m_item.setDisplayName(" ");
        default_iconB.setItemMeta(m_item);
        default_iconY.setItemMeta(m_item);

        //status icon
        ItemStack status_icon = new ItemStack(Material.BOOK, 1);
        ItemMeta m_status = status_icon.getItemMeta();
        m_status.setDisplayName("§bSTATUS");
        ArrayList<String> status_lore = new ArrayList<>();
        status_lore.add("§7Kills: " + kills);
        status_lore.add("§7Deaths: " + deaths);

        if (kdr < 1) {
            status_lore.add("§cK/D: " + df.format(kdr));
        } else if (kdr == 1) {
            status_lore.add("§eK/D: " + df.format(kdr));
        } else {
            status_lore.add("§aK/D: " + df.format(kdr));
        }

        status_lore.add("");

        m_status.setLore(status_lore);
        status_icon.setItemMeta(m_status);

        //shop icon
        ItemStack shop_icon = new ItemStack(Material.GOLD_INGOT, 1);

        //linha superior
        inv.setItem(0, default_iconB);
        inv.setItem(1, default_iconY);
        inv.setItem(2, default_iconB);
        inv.setItem(3, status_icon);
        inv.setItem(4, default_iconB);
        inv.setItem(5, shop_icon);
        inv.setItem(6, default_iconB);
        inv.setItem(7, default_iconY);
        inv.setItem(8, default_iconB);

        //colunas laterais
        for (int i = 9; i < 45; i += 9) {
            if (i % 2 == 0) {
                inv.setItem(i - 1, default_iconY);
                inv.setItem(i, default_iconB);
            } else {
                inv.setItem(i - 1, default_iconB);
                inv.setItem(i, default_iconY);
            }
        }

        //linha inferior
        for (int i = 45; i < 54; i++) {
            if (i % 2 == 0) {
                inv.setItem(i - 1, default_iconY);
                inv.setItem(i, default_iconB);
            } else {
                inv.setItem(i - 1, default_iconB);
                inv.setItem(i, default_iconY);
            }
        }
    }

    private double calculateKDRatio(double kills, double deaths) {
        if (deaths <= 0) deaths = 1;
        if (kills < 0) kills = 0;

        return kills / deaths;
    }

    private void insert_kits(Inventory inv, Player p) {
        insert_kit(Material.STONE_SWORD, 0, inv, p, "PVP", "Sem habilidade especial.", inv.firstEmpty());
        insert_kit(Material.FIREWORK, 0, inv, p, "KANGAROO", "Saia pulando feito um canguru.", inv.firstEmpty());
        insert_kit(Material.REDSTONE_TORCH_ON, 0, inv, p, "FLASH", "Seja tao rapido quanto o flash.", inv.firstEmpty());
        insert_kit(Material.FISHING_ROD, 0, inv, p, "FISHERMAN", "Fisgue seus inimigos e elimine-os.", inv.firstEmpty());

        // Shop Icon
        ItemStack shop = inv.getItem(5);
        ItemMeta m_shop = shop.getItemMeta();
        m_shop.setDisplayName("§6SHOP");
        ArrayList<String> shop_lore = new ArrayList<>();
        shop_lore.add("§7Adquira novos Kits aqui!");
        shop_lore.add("");
        shop_lore.add("§8Kits: " + OWNED_KITS + "/" + MAX_KITS);
        m_shop.setLore(shop_lore);
        shop.setItemMeta(m_shop);

        // NoKit
        ItemStack default_noKit = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta m_noKit = default_noKit.getItemMeta();
        m_noKit.setDisplayName("§cSEM KIT");
        default_noKit.setItemMeta(m_noKit);

        int i = inv.firstEmpty();
        while (i != -1) {
            inv.setItem(inv.firstEmpty(), default_noKit);
            i = inv.firstEmpty();
        }
    }

    private void insert_kit(Material item, int data, Inventory inv, Player p,
                            String kit_name, String habilidade, int position) {
        if (!kits.contains(kit_name))
            kits.add(kit_name);

        if (p.hasPermission("gamma.kit." + kit_name.toLowerCase())) {
            OWNED_KITS++;
            ItemStack icon = new ItemStack(item, 1, (short) data);
            ItemMeta iconM = icon.getItemMeta();

            ArrayList<String> lore = new ArrayList<>();
            lore.add("§7" + habilidade);
            lore.add("");
            lore.add("§eClique para selecionar.");

            iconM.setDisplayName("§a" + kit_name);
            iconM.setLore(lore);
            icon.setItemMeta(iconM);

            inv.addItem(icon);
        }
    }

    @EventHandler
    private void clickBau(PlayerInteractEvent e) {
        try {
            Player p = e.getPlayer();

            if (p.getItemInHand() != null && p.getItemInHand().getItemMeta() != null
                    && p.getItemInHand().getType() == Material.CHEST
                    && ChatColor.stripColor(p.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("kits")) {

                e.setCancelled(true);

//                gui(p);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            }
        } catch (Exception err) {
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class KitGUI, clickBau()");
        }
    }

    @EventHandler
    private void clickInventory(InventoryClickEvent e) {
        try {
            Player p = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();

            if (e.getInventory() != null && e.getInventory().getName().equalsIgnoreCase("Seus Kits") && item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null) {
                if (kits.contains(ChatColor.stripColor(item.getItemMeta().getDisplayName())) || item.getItemMeta().getDisplayName().equalsIgnoreCase("§6SHOP")) {
                    p.chat("/" + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase()));
                    p.closeInventory();
                }
                e.setCancelled(true);
            }
        } catch (Exception err) {
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class KitGUI, clickInventory()");
        }
    }

    @EventHandler
    public void onDropDefaultInventoryItem(PlayerDropItemEvent e) throws InterruptedException {
        try {
            Item item = e.getItemDrop();

            //Bloquear drop de itens inicias
            if (item.getItemStack().getItemMeta().getDisplayName() != null) {
                switch (item.getItemStack().getItemMeta().getDisplayName()) {
                    case "§eKits":
                        if (item.getItemStack().getType().equals(Material.CHEST)) {
                            e.setCancelled(true);
                        }
                        break;
                    case " ":
                        if (item.getItemStack().getType().equals(Material.STAINED_GLASS_PANE)) {
                            e.setCancelled(true);
                        }
                        break;
                    case "§bWarps":
                        if (item.getItemStack().getType().equals(Material.COMPASS)) {
                            e.setCancelled(true);
                        }
                        break;
                }
            }
        } catch (Exception err) {
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class KitGUI, onDropDefaultInventoryItem()");
        }

    }

}
