package br.com.gmfonseca.kits;

import br.com.gmfonseca.api.Kit;
import br.com.gmfonseca.main.Controller;
import br.com.gmfonseca.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Kangaroo extends Kit implements Listener, CommandExecutor {

    private static HashMap<String, Boolean> players_voando = new HashMap<>();
    private static List<String> players_after_use = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("gamma.kit.kangaroo")) {
                if (!Controller.pHasKit(p)) {
                    giveItems(p);
                    Controller.player_kit.put(p, "kangaroo");
                    players_voando.put(p.getName(), false);

                    p.sendMessage(Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.getKit")
                            .replaceAll("&", "§")
                            .replaceAll("%kitname%", "KANGAROO"));
                } else {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "ERROR" + ChatColor.RED + ": Voce ja selecionou um kit.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "ERROR" + ChatColor.RED + ": Voce nao possui o kit kangaroo");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "ERROR" + ChatColor.RED + ": Voce nao e um player.");
        }

        return false;
    }

    private void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack kangaroo = new ItemStack(Material.FIREWORK);
        ItemMeta m_kangaroo = kangaroo.getItemMeta();
        m_kangaroo.setDisplayName(ChatColor.YELLOW + "Kangaroo");
        kangaroo.setItemMeta(m_kangaroo);

        inv.setItem(0, new ItemStack(Material.IRON_SWORD));
        inv.setItem(1, kangaroo);
        set_armor(p);
        fill_soup(p.getInventory(), p);
    }

    private void set_armor(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.setHelmet(new ItemStack(Material.IRON_HELMET));
        inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        inv.setBoots(new ItemStack(Material.GOLD_BOOTS));
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onUse(PlayerInteractEvent e) {
        if (e.getItem() != null &&
                (Controller.player_kit.get(e.getPlayer()).equalsIgnoreCase("kangaroo")) && (e.getItem().getType() == Material.FIREWORK)) {

            e.setCancelled(true);
            Player p = e.getPlayer();

            if (!players_voando.get(p.getName())) {

                if (p.isSneaking()) {
                    Vector v = p.getLocation().getDirection().multiply(1.5D).setY(0.45D);
                    p.setVelocity(v);
                    players_voando.put(p.getName(), true);
                } else {
                    Vector v = p.getLocation().getDirection().multiply(0.5D).setY(0.9D);
                    p.setVelocity(v);
                    players_voando.put(p.getName(), true);
                }
                if (!players_after_use.contains(p.getName())) {
                    players_after_use.add(p.getName());
                }
            }

        }
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (Controller.player_kit.get(p).equalsIgnoreCase("kangaroo") && players_voando.get(p.getName())) {
            Block block = p.getLocation().getBlock();

            if ((block.getType() != Material.AIR) || (block.getRelative(BlockFace.DOWN).getType() != Material.AIR)) {
                players_voando.put(p.getName(), false);
            }
        }

    }

    @EventHandler
    private void onFallAfterUse(EntityDamageEvent e) {
        if ((e.getEntity() instanceof Player)) {
            Player p = (Player) e.getEntity();

            if (e.getCause() == EntityDamageEvent.DamageCause.FALL && Controller.player_kit.get(e.getEntity()).equalsIgnoreCase("kangaroo") && players_after_use.contains(p.getName())) {

                if (players_after_use.contains(p.getName())) {
                    players_voando.put(p.getName(), false);
                    players_after_use.remove(p.getName());
                    e.setCancelled(true);
                }

            } else {

                if (players_after_use.contains(p.getName())) {
                    players_after_use.remove(p.getName());
                }
            }
        }
    }

    @EventHandler
    public void onDropKitItem(PlayerDropItemEvent e) {

        if (Controller.player_kit.get(e.getPlayer()).equalsIgnoreCase("kangaroo")) {
            try {
                Item item = e.getItemDrop();

                if (item.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW +"Kangaroo")
                        && item.getItemStack().getType().equals(Material.FIREWORK)) {
                    e.setCancelled(true);
                }

            } catch (Exception err) {
                Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Kangaroo, onDropKitItem()");
            }
        }

    }
}
