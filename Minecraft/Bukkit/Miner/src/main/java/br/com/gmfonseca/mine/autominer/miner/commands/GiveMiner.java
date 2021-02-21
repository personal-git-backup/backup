package br.com.gmfonseca.mine.autominer.miner.commands;

import br.com.gmfonseca.mine.autominer.miner.controller.MinerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveMiner implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(cmd.getName().equalsIgnoreCase("giveminer") && args.length > 0){
            Player p = Bukkit.getPlayer(args[0]);

            if(p != null){
                byte value = 1;

                try{
                    if(args.length>1) value = Byte.parseByte(args[1]);

                    if(value > 0) {
                        MinerManager.increaseMinerAmount(p, value);
                        sender.sendMessage(ChatColor.GREEN + "Successfully increased " + p.getName() + "\'s miner amount.");
                    }else{
                        sender.sendMessage(ChatColor.RED + "Invalid amount value. (" + value + ")");
                    }

                    String s = value > 1? "s" : "";
                    p.sendMessage(ChatColor.GREEN + "You received " + value + " miners" + s + ".");

                }catch (NumberFormatException e){
                    sender.sendMessage("Informe um valor valido. /giveminer <playername> [value]");
                }

            }else{
                sender.sendMessage("User \'" + args[0] + "\' couldn't be found.");
            }
        }else{
            sender.sendMessage("Incomplete command. Try: /giveminer <playername> [value]");
        }

        return false;
    }
}
