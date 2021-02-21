package br.com.gmfonseca.mine.autominer.miner.commands;

import br.com.gmfonseca.mine.autominer.entity.Miner;
import br.com.gmfonseca.mine.autominer.miner.controller.MinerManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;

public class GetMiner implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;

            MinerManager.ownerMiners.putIfAbsent(p.getUniqueId(), new HashSet<>());

            if(MinerManager.canCreateMiner(p) || MinerManager.canPlaceMiner(p)) {
                Miner miner = MinerManager.getUnplacedMiner(p);
                Location location = MinerManager.buildMinerLocation(p);

                if(miner == null){
                    miner = new Miner(p, location, 6 * 9, new ArrayList<>(), Material.IRON_PICKAXE);
                    MinerManager.placeMiner(p, miner);
                }else{
                    miner.spawn(location);
                }

                p.sendMessage(ChatColor.GREEN + "[Miner #" + miner.getId() + "] Miner placed at " + (int)miner.getLocation().getX() + " " + (int)miner.getLocation().getY() + " " + (int)miner.getLocation().getZ() + "."); //TODO: customize later, debug line

            }else{
                p.sendMessage("You don't have any miner to be placed.");
            }
        }else{
            sender.sendMessage("You may be a Player.");
        }

        return false;
    }
}
