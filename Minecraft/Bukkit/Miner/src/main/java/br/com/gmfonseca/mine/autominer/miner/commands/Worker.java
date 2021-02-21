package br.com.gmfonseca.mine.autominer.miner.commands;

import br.com.gmfonseca.mine.autominer.entity.Miner;
import br.com.gmfonseca.mine.autominer.miner.controller.MinerManager;
import org.bukkit.Bukkit;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class Worker implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        try {
            if(sender instanceof Player){

                Player p = (Player) sender;
                int amount = MinerManager.minersAmount.getOrDefault(p.getUniqueId(), (byte) 0);
                Set<Miner> miners = MinerManager.getPlayerMiners(p);
                int placedAmount = MinerManager.getPlacedMinersAmount(p);
                int registeredAmount = MinerManager.getRegisteredMinersAmount(p);

                p.sendMessage("");
                p.sendMessage(p.getName() + "\'s miners information");
                p.sendMessage("");
                p.sendMessage("- Total Amount: " + amount);
                p.sendMessage("- Placed Amount: " + placedAmount);
                p.sendMessage("- Registered Amount: " + registeredAmount);
                p.sendMessage("- Not registered Amount: " + (amount-registeredAmount));
                p.sendMessage("- Miners: [");
                miners.forEach(m -> {
                    p.sendMessage("");
                    p.sendMessage("** #" + m.getId());
                    p.sendMessage("** mining: " + m.isMining());
                    p.sendMessage("** placed: " + m.isPlaced());
                    if(m.isPlaced()) {
                        p.sendMessage("** x: " + m.getLocation().getX());
                        p.sendMessage("** y: " + m.getLocation().getY());
                        p.sendMessage("** z: " + m.getLocation().getZ());
                        p.sendMessage("** direction: " + m.getDirection().name());
                    }
                });
                p.sendMessage("");
                p.sendMessage("]");
                p.sendMessage("");

            }else{
                sender.sendMessage("You may be a Player.");
            }
        }catch (Exception err){
            Bukkit.getConsoleSender().sendMessage("§c|command  Worker:onCommand() -> " + err.getMessage());
        }

        return false;
    }
}
