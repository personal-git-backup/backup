package br.com.gmfonseca.staff;

import br.com.gmfonseca.api.Kit;
import br.com.gmfonseca.main.Controller;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Admin implements Listener, CommandExecutor {

    public static void gen_admin_inventory(Player p) {
        Inventory inv = p.getInventory();
        inv.setItem(1, new ItemStack(Material.STICK));

        ItemStack admin_icon = new ItemStack(Material.REDSTONE, 1);
        ItemMeta a_meta = admin_icon.getItemMeta();
        a_meta.setDisplayName("§cADMIN");
        ArrayList<String> admin_lore = new ArrayList<>();
        admin_lore.add("§7Sair do modo Admin");
        a_meta.setLore(admin_lore);
        admin_icon.setItemMeta(a_meta);

        ItemStack invsee_icon = new ItemStack(Material.BOOK, 1);
        ItemMeta inv_meta = admin_icon.getItemMeta();
        inv_meta.setDisplayName("§eINVSEE");
        ArrayList<String> invsee_lore = new ArrayList<>();
        invsee_lore.add("§7Acessar inventario ao clicar em um usuario");
        inv_meta.setLore(invsee_lore);
        invsee_icon.setItemMeta(inv_meta);

        ItemStack minerremover_icon = new ItemStack(Material.STICK, 1);
        ItemMeta minerremover_meta = minerremover_icon.getItemMeta();
        minerremover_meta.setDisplayName("§cMinerRemover");
        ArrayList<String> minerremover_lore = new ArrayList<>();
        minerremover_lore.add("§7Remover um §6Miner §7do mundo");
        minerremover_meta.setLore(minerremover_lore);
        minerremover_icon.setItemMeta(minerremover_meta);

        inv.setItem(1, minerremover_icon);
        inv.setItem(4, admin_icon);
        inv.setItem(7, invsee_icon);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if ((sender instanceof Player)) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("admin")) {
                if (p.hasPermission("gamma.staff.admin")) {
                    if (!Controller.admin.containsKey(p)) {
                        Controller.admin.put(p, false);
                    }

                    boolean active = Controller.admin.get(p);

                    p.getInventory().clear();
                    if (active) {
                        Kit.gen_default_inventory(p);
                        p.setGameMode(GameMode.SURVIVAL);

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.showPlayer(p);
                        }

                        if (args.length == 0 || !args[0].equalsIgnoreCase("ignore_message")) {
                            p.sendMessage("");
                            p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Modo ADMIN desativado");
                            p.sendMessage(ChatColor.RED + "Agora voce esta visivel aos outros jogadores");
                            p.sendMessage("");
                        }

                        Controller.admin.put(p, false);
                    } else {
                        gen_admin_inventory(p);
                        p.setGameMode(GameMode.CREATIVE);

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.hasPermission("gamma.staff.admin") && Controller.admin.get(player)) {
                                player.showPlayer(p);
                            } else {
                                player.hidePlayer(p);
                            }
                        }

                        if (args.length == 0 || !args[0].equalsIgnoreCase("ignore_message")) {

                            p.sendMessage("");
                            p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Modo ADMIN ativado");
                            p.sendMessage(ChatColor.GREEN + "Agora voce esta invisivel aos outros jogadores");
                            p.sendMessage("");

                        }

                        Controller.admin.put(p, true);
                    }

                    p.updateInventory();

                    Controller.admin.put(p, !active);


                } else {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "ERROR" + ChatColor.RED + ": Sem permissao para o comando /" + ChatColor.ITALIC + "admin");
                }
            }
        }

        return false;
    }

    @EventHandler
    private void onDefaultItemClick(PlayerInteractEvent e) {
        try {
            Player p = e.getPlayer();
            ItemStack item = e.getItem();

            if (item != null && item.getItemMeta() != null) {
                ItemMeta meta = item.getItemMeta();
                if (item.getItemMeta().getDisplayName() != null) {

                    switch (ChatColor.stripColor(meta.getDisplayName()).toLowerCase()) {
                        case "admin":
                            switch (e.getAction()) {
                                case RIGHT_CLICK_AIR:
                                case RIGHT_CLICK_BLOCK:
                                    e.setCancelled(true);
                                    p.chat("/admin");
                            }
                            break;

                    }
                }
            }
        } catch (Exception err) {
            Bukkit.getLogger().warning(err.toString() + " | Class Admin, onDefaultItemClick()");
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEntityEvent e) {
        try {
            Player p = e.getPlayer();
            if (p.hasPermission("gamma.staff.admin")) {
                ItemStack item = p.getItemInHand();
                if (item != null && item.getItemMeta() != null
                        && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("invsee")) {

                    switch (e.getRightClicked().getClass().getSimpleName()) {
                        case "CraftPlayer":
                            Player user = (Player) e.getRightClicked();
                            p.openInventory(user.getInventory());

                            p.sendMessage(ChatColor.GREEN + "Acessando o inventario de " + ChatColor.BOLD + user.getName());

                            break;
                        default:
                            p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Ops! " + ChatColor.RED + "Voce precisa clicar em um jogador para ver o inventario");
                    }

                }
            }
        } catch (Exception err) {
            Bukkit.getLogger().warning(err.toString() + " | Class Admin, onPlayerInteract()");
        }
    }
}
