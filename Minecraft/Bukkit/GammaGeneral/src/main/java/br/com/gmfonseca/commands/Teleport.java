package br.com.gmfonseca.commands;

import br.com.gmfonseca.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.logging.Level;

public class Teleport implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        try {
            if (sender instanceof Player) {
                if (cmd.getName().equalsIgnoreCase("world")) {
                    if (args.length > 0) {
                        Player p = (Player) sender;

                        World world = Bukkit.getWorld(args[0]);

                        if (world != null) {
                            Location location = p.getLocation();
                            location.setWorld(world);

                            p.teleport(location);
                        } else
                            p.sendMessage("World called \'" + args[0] + "\' doesn't exists.");

                    } else {
                        sender.sendMessage("Incomplete Command. Try /world <world-name>");
                    }
                }
            } else {
                sender.sendMessage("You may be a Player.");
            }

        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('§', "§4[§lGamma.General§4] §c§lERROR§c: Ops! Algo deu errado -> " + e));
            Main.MAIN.getLogger().log(Level.SEVERE, "[" + new Date() + "] Error: ", e);
        }

        return false;
    }
}
