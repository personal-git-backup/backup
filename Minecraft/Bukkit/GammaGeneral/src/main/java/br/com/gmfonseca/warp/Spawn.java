package br.com.gmfonseca.warp;

import br.com.gmfonseca.api.Warp;
import br.com.gmfonseca.main.Controller;
import br.com.gmfonseca.main.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Spawn extends Warp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("setspawn")) {
                if (p.hasPermission("gamma.warp.setspawn")) {
                    this.world = p.getLocation().getWorld();

                    Location loc = p.getLocation();
                    this.x = loc.getX();
                    this.y = loc.getY();
                    this.z = loc.getZ();

                    Object[] location = {world, x, y, z};

                    Controller.warps.put("spawn", location);
                    world.setSpawnLocation(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.CONFIGS.get("messages").getConfig().get("Personal.Command.noPermCmd").toString()
                            .replaceAll("%cmd%", "setspawn")));

                }

            } else { /* if(cmd.getName().equalsIgnoreCase("spawn")) */

                if (!Controller.warps.containsKey("spawn")) {
                    FileConfiguration cfg = Main.CONFIGS.get("warps").getConfig();
                    this.world = Bukkit.getWorld(String.valueOf(cfg.get("spawn.world")));
                    this.x = cfg.getDouble("spawn.x");
                    this.y = cfg.getDouble("spawn.y");
                    this.z = cfg.getDouble("spawn.z");
                    Object[] location = {world, x, y, z};

                    Controller.warps.put("spawn", location);
                } else {
                    world = (World) Controller.warps.get("spawn")[0];
                    x = (Double) Controller.warps.get("spawn")[1];
                    y = (Double) Controller.warps.get("spawn")[2];
                    z = (Double) Controller.warps.get("spawn")[3];
                }

                Location place = new Location(world, x, y + 0.5, z);
                p.teleport(place);
                p.sendMessage(ChatColor.translateAlternateColorCodes('§', "§aTeleportado para o §lSpawn"));
                p.playSound(place, Sound.ENDERMAN_TELEPORT, 0.5F, 0.5F);
                p.getWorld().playEffect(place, Effect.POTION_SWIRL_TRANSPARENT, 10);
            }


        }

        return false;
    }


}
