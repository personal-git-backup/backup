package br.com.gmfonseca.events;

import br.com.gmfonseca.api.Kit;
import br.com.gmfonseca.main.Controller;
import br.com.gmfonseca.main.Main;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import static br.com.gmfonseca.main.Controller.player_kit;

public class PlayerEv implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    private void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Kit.gen_default_inventory(p);

        FileConfiguration p_status = Main.CONFIGS.get("p_status").getConfig();
        if (p_status.get(p.getName()) == null) {
            p_status.set(p.getName() + ".kills", 0);
            p_status.set(p.getName() + ".deaths", 0);
            Main.CONFIGS.get("p_status").saveCustomConfig();
        }

        FileConfiguration messages = Main.CONFIGS.get("messages").getConfig();
        e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(messages.get("Server.join"))
                .replaceAll("%name", e.getPlayer().getDisplayName())));

        //Registrando o jogador na relação de player e seu kit
        player_kit.put(p, "none");

        //Recuperando numero de kills do jogador
        long player_kills = p_status.getLong(p.getName() + ".kills");
        Controller.player_kills.put(p, player_kills);

        //Recuperando numero de kills do jogador
        long player_deaths = p_status.getLong(p.getName() + ".deaths");
        Controller.player_deaths.put(p, player_deaths);

        if (p.hasPermission("gamma.staff.admin") && !Controller.admin.containsKey(p)) {
            Controller.admin.put(p, false);
        }

    }


    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(Main.CONFIGS.get("messages").getConfig().get("Server.quit"))
                .replaceAll("%name", e.getPlayer().getDisplayName())));

        Player p = e.getPlayer();

        //Removendo o jogador na relação de player e seu kit
        player_kit.remove(p);

        //Salvando numero de kills do jogador
        if (Controller.player_kills.containsKey(p)) {
            long player_kills = Controller.player_kills.remove(p);
            Main.CONFIGS.get("p_status").getConfig().set(p.getName() + ".kills", player_kills);
        }

        //Salvando numero de kills do jogador
        if (Controller.player_deaths.containsKey(p)) {
            long player_deaths = Controller.player_deaths.remove(p);
            Main.CONFIGS.get("p_status").getConfig().set(p.getName() + ".deaths", player_deaths);
        }
    }

    @EventHandler
    private void onMessage(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (p.hasPermission("gamma.chatcolor") || p.isOp()) {
            String msg = e.getMessage();

            msg = ChatColor.translateAlternateColorCodes('&', msg);
            e.setMessage(msg);
        }

        e.setFormat(ChatColor.translateAlternateColorCodes('&', String.valueOf(Main.CONFIGS.get("messages").getConfig().get("Server.chatFormat"))
                .replace("%name", p.getDisplayName())
                .replace("%message", e.getMessage())));
    }

    @EventHandler
    private void onKilling(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            long player_kills = Controller.player_kills.get(killer);
            Controller.player_kills.put(killer, player_kills + 1);

            killer.playSound(killer.getLocation(), Sound.ANVIL_BREAK, 1.0F, 1.0F);
        }
    }

    @EventHandler
    private void onDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            Player dead = (Player) e.getEntity();

            Bukkit.broadcastMessage(ChatColor.WHITE + "[" + ChatColor.RED + "✞" + ChatColor.WHITE + "] " + ChatColor.RED + "" + e.getEntity().getName() + " " + ChatColor.DARK_RED + ChatColor.BOLD + "☠ " + dead.getLastDamageCause().getCause().name());

            long player_deaths = Controller.player_deaths.get(dead);
            Controller.player_deaths.put(dead, player_deaths + 1);

        }
    }

    @EventHandler
    private void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();

        Kit.gen_default_inventory(p);

    }

    @EventHandler
    private void onDrop(PlayerDropItemEvent e) {
        Item item = e.getItemDrop();

        new BukkitRunnable() {

            @Override
            public void run() {
                Location item_location = item.getLocation();
                item.remove();
                item.getWorld().playEffect(item_location, Effect.SMOKE, BlockFace.UP, 4);
            }

        }.runTaskLaterAsynchronously(Main.MAIN, 10);

    }
}
