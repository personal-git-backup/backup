package br.com.gmfonseca.mine.autominer.main;

import br.com.gmfonseca.mine.autominer.miner.controller.MinerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListMiner implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("List of miners: " + MinerManager.miners);
        Bukkit.getConsoleSender().sendMessage("");
        return false;
    }
}
