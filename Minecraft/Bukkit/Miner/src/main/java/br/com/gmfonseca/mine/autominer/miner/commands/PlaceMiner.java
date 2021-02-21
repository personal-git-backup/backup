package br.com.gmfonseca.mine.autominer.miner.commands;

import br.com.gmfonseca.mine.autominer.entity.Miner;
import br.com.gmfonseca.mine.autominer.miner.controller.MinerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaceMiner implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                if(args.length > 0){
                    int id = -1;

                    try{
                        id = Integer.parseInt(args[0]);
                    }catch (NumberFormatException e){
                        p.sendMessage("Id may be a number.");
                    }

                    if(id!=-1){
                        Miner miner = MinerManager.getMinerById(id, p);

                        if(miner != null){
                            if(!miner.isPlaced()) {
                                Location location = MinerManager.buildMinerLocation(p);

                                miner.spawn(location);
                                MinerManager.placeMiner(p, miner);
                            }else{
                                p.sendMessage("Can't place Miner #" + id + " because it is already placed.");
                            }
                        }else{
                            p.sendMessage("Miner #" + id + " not found.");
                        }
                    }

                }else{
                    p.sendMessage("Incomplete command. Use /placeminer <id>");
                }

            } else {
                sender.sendMessage("You may be a Player.");
            }
        }catch (Exception err){
            Bukkit.getConsoleSender().sendMessage("§c|command  Worker:onCommand() -> " + err.getMessage());
        }

        return false;
    }
}
