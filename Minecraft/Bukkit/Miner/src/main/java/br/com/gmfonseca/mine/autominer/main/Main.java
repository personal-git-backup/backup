package br.com.gmfonseca.mine.autominer.main;

import br.com.gmfonseca.mine.autominer.entity.Miner;
import br.com.gmfonseca.mine.autominer.miner.commands.GetMiner;
import br.com.gmfonseca.mine.autominer.miner.commands.GiveMiner;
import br.com.gmfonseca.mine.autominer.miner.commands.PlaceMiner;
import br.com.gmfonseca.mine.autominer.miner.commands.Worker;
import br.com.gmfonseca.mine.autominer.miner.controller.MinerManager;
import br.com.gmfonseca.mine.autominer.miner.listeners.CancelMinerKill;
import br.com.gmfonseca.mine.autominer.miner.listeners.MinerInteract;
import br.com.gmfonseca.mine.autominer.miner.listeners.MinerMenuInteract;
import br.com.gmfonseca.mine.autominer.miner.listeners.MinerPhysicalInteract;
import br.com.gmfonseca.mine.autominer.util.Config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

//TODO a way to don't kill miner when used a command to kill all entities (Ex. /kill @e)

public class Main extends JavaPlugin {

    public static Main MAIN;
    public static PluginManager PM = Bukkit.getServer().getPluginManager();

    public static HashMap<String, Config> CONFIGS = new HashMap<>();

    @Override
    public void onEnable() {
        try {
            MAIN = this;

            Bukkit.getConsoleSender().sendMessage("§6[§lGamma.Miner§6]§e Iniciando...");

            registerAll();

            Bukkit.getConsoleSender().sendMessage("§2[§lGamma.Miner§2]§a Iniciado com sucesso!");

        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§4[§lGamma.Miner§4] §c§lERROR§c: Ops! Algo deu errado -> " + e);
            Date now = new Date();
            Main.MAIN.getLogger().log(Level.SEVERE, "[" + now + "] Error: ", e);
        }

    }

    @Override
    public void onDisable() {
        try {
            Bukkit.getLogger().info("");
            Bukkit.getConsoleSender().sendMessage("§6[§lGamma.Miner§6]§e Finalizando...");

            HandlerList.unregisterAll(MAIN);
            saveAll();

            for (Miner miner : MinerManager.miners) {
                miner.removeMiner();
            }

            Bukkit.getConsoleSender().sendMessage("§4[§lGamma.Miner§4]§c Finalizado com sucesso!");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§4[§lGamma.Miner§4] §c§lERROR§c: Ops! Algo deu errado -> " + e);
            Date now = new Date();
            Main.MAIN.getLogger().log(Level.SEVERE, "[" + now + "] Error: ", e);
        }
    }

    private void registerAll() {
        registerEvents();
        registerCommands();
        registerConfigs();
        registerMiners();
    }

    private void registerEvents() {
        PM.registerEvents(new MinerMenuInteract(), MAIN);
        PM.registerEvents(new MinerInteract(), MAIN);
        PM.registerEvents(new MinerPhysicalInteract(), MAIN);
        PM.registerEvents(new CancelMinerKill() , MAIN);
    }

    private void registerCommands() {
        getCommand("getminer").setExecutor(new GetMiner());
        getCommand("placeminer").setExecutor(new PlaceMiner());
        getCommand("giveminer").setExecutor(new GiveMiner());
        getCommand("worker").setExecutor(new Worker());

        //debug
        getCommand("debug").setExecutor(new Debug());
        getCommand("listminer").setExecutor(new ListMiner());
    }

    private void registerConfigs() {
        registerConfig("miners");
    }

    private void registerConfig(String file_name) {
        Config cfg = new Config(file_name + ".yml");
        CONFIGS.put(file_name, cfg);
        if (!cfg.exists()) cfg.saveDefault();
        cfg.reload();
    }

    private void registerMiners() {
        //TODO: build fully logic
        for (Player p : Bukkit.getOnlinePlayers()) {
            MinerManager.minersAmount.put(p.getUniqueId(), (byte) 1);
            MinerManager.ownerMiners.put(p.getUniqueId(), new HashSet<>());
        }
    }

    private void saveAll(){
        saveMiners();
    }

    private void saveMiners(){
        FileConfiguration file = CONFIGS.get("miners").getConfig();
        //TODO: fix saving miners on file
        file.set("MINER_ID", MinerManager.MINER_ID);

        for (Player p : Bukkit.getOnlinePlayers()) {
            Set<Miner> miners = MinerManager.getPlayerMiners(p);
            int index=1;
            String path = p.getUniqueId() + ".";

            file.set(path + "amount", MinerManager.getMinersAmount(p));
            file.set(path + "placed", miners.size());

            for (Miner miner : miners) {
                saveMiner(miner, index++);
            }
        }
        CONFIGS.get("miners").saveCustomConfig();
    }

    private void saveMiner(Miner miner, int index){
        FileConfiguration file = CONFIGS.get("miners").getConfig();

        String minerNumber = miner.getOwner().getUniqueId() + ".miner" + index;

        //single
        file.set(minerNumber + ".id", miner.getId());
        file.set(minerNumber + ".tool", miner.getTool().name());
        file.set(minerNumber + ".isPlaced", miner.isPlaced());

        //location
        if(miner.getLocation() != null) {
            file.set(minerNumber + ".location.world", miner.getLocation().getWorld().getName());
            file.set(minerNumber + ".location.x", miner.getLocation().getX());
            file.set(minerNumber + ".location.y", miner.getLocation().getY());
            file.set(minerNumber + ".location.z", miner.getLocation().getZ());
            file.set(minerNumber + ".location.yaw", miner.getLocation().getYaw());
        }

        //backpack
        if(miner.getInventory() != null) {
            file.set(minerNumber + ".backpack.contents", miner.getInventory().getContents());
            file.set(minerNumber + ".backpack.maxStackSize", miner.getInventory().getMaxStackSize());
        }
    }

}