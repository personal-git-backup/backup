package br.com.gmfonseca.mine.autominer.main;

import br.com.gmfonseca.mine.autominer.entity.Miner;
import br.com.gmfonseca.mine.autominer.miner.controller.MinerManager;
import br.com.gmfonseca.mine.autominer.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Debug implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;

            if (MinerManager.canCreateMiner(p)) {

                FileConfiguration cfg = Main.CONFIGS.get("miners").getConfig();
                String minerNumber = p.getUniqueId() + ".miner1";

                //tool
                Material tool = Material.getMaterial(cfg.getString(minerNumber + ".tool"));

                int id = cfg.getInt(minerNumber + ".id");
                //location
                World world = Bukkit.getWorld(cfg.getString(minerNumber + ".location.world"));
                double x = cfg.getDouble(minerNumber + ".location.x");
                double y = cfg.getDouble(minerNumber + ".location.y");
                double z = cfg.getDouble(minerNumber + ".location.z");
                float yaw = (float) cfg.getDouble(minerNumber + ".location.yaw");

                //backpack
                ArrayList<ItemStack> list = (ArrayList<ItemStack>) cfg.getList(minerNumber + ".backpack.contents");
                int maxStack = cfg.getInt(minerNumber + ".backpack.maxStackSize");

                p.sendMessage("id: " + id);
                p.sendMessage("tool: " + tool);
                p.sendMessage("world: " + world.getName());
                p.sendMessage("x: " + x);
                p.sendMessage("y: " + y);
                p.sendMessage("z: " + z);
                p.sendMessage("yaw: " + yaw);
                p.sendMessage("maxStack: " + maxStack);
                p.sendMessage("list: " + Util.arrayToSet(list.toArray()));

                //building miner
                Location loc = new Location(world, x, y, z);
                loc.setYaw(yaw);

                Miner miner = new Miner(p, loc, 54, list, tool);
                miner.setId(id);
                MinerManager.placeMiner(p, miner);
            } else {
                sender.sendMessage("Can't place a miner.");
            }
        }else{
            sender.sendMessage("You may be a Player.");
        }

        return false;
    }
}
