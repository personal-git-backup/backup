package br.com.gmfonseca.main;

import br.com.gmfonseca.api.Config;
import br.com.gmfonseca.api.Kit;
import br.com.gmfonseca.commands.Tag;
import br.com.gmfonseca.commands.Teleport;
import br.com.gmfonseca.events.Natural;
import br.com.gmfonseca.events.PlayerEv;
import br.com.gmfonseca.events.TagUpdater;
import br.com.gmfonseca.gui.KitGUI;
import br.com.gmfonseca.gui.WarpGUI;
import br.com.gmfonseca.kits.Fisherman;
import br.com.gmfonseca.kits.Flash;
import br.com.gmfonseca.kits.Kangaroo;
import br.com.gmfonseca.kits.Pvp;
import br.com.gmfonseca.staff.Admin;
import br.com.gmfonseca.staff.StaffChat;
import br.com.gmfonseca.warp.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Main MAIN;
    public static HashMap<String, Config> CONFIGS = new HashMap<>();

    public static PluginManager PM = Bukkit.getServer().getPluginManager();

    Connection connection;
    String host, database, username, password;
    int port;

    @Override
    public void onEnable() {
        try {
            Bukkit.getLogger().info("");
            MAIN = this;
            Bukkit.getConsoleSender().sendMessage("§6[§lGamma.General§6]§e Iniciando...");
            Controller.player_kit = new HashMap<>();
            //connect();
            registerAll();

            TagUpdater.start();

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                Controller.player_kit.put(player, "none");
                player.getInventory().clear();
                Kit.gen_default_inventory(player);

                long player_kills = CONFIGS.get("p_status").getConfig().getLong(player.getName() + ".kills");
                Controller.player_kills.put(player, player_kills);

                long player_deaths = CONFIGS.get("p_status").getConfig().getLong(player.getName() + ".deaths");
                Controller.player_deaths.put(player, player_deaths);

                if (player.hasPermission("gamma.staff.chat")) {
                    Controller.staff.put(player, true);
                }
            }
            //loadMiners();


            Bukkit.getConsoleSender().sendMessage("§2[§lGamma.General§2]§a Iniciado com sucesso!");
        /*} catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();*/
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§4[§lGamma.General§4] §c§lERROR§c: Ops! Algo deu errado -> " + e);

            Date now = new Date();

            Main.MAIN.getLogger().log(Level.SEVERE, "[" + now + "] Error: ", e);
        }

    }

    public void addScore(Player p, int score) throws SQLException {
        PreparedStatement stat = connection.prepareStatement("INSERT INTO PlayerScore(Player_Name,Score) VALUES (?, ?)");

        stat.setString(1, p.getName());
        stat.setInt(2, score);
        stat.executeQuery();

    }

    public void setScore(Player p, int score) throws SQLException {
        PreparedStatement stat = connection.prepareStatement("UPDATE PlayerScore SET Score = ? WHERE Player_Name = ?");

        stat.setString(1, p.getName());
        stat.setInt(2, score);
        stat.executeQuery();

    }

    public int getScore(Player p) throws SQLException {
        int score = 0;
        PreparedStatement stat = connection.prepareStatement("SELECT Score FROM PlayerScore WHERE Player_Name = ?");

        stat.setString(1, p.getName());
        ResultSet result = stat.executeQuery();

        while (result.next()) {
            score = result.getInt("Score");
        }

        return score;
    }

    public void connect() throws ClassNotFoundException, SQLException {

//        Class.forName("com.mysql.jdbc.Driver");
//        MysqlDataSource dataSource = new MysqlDataSource();
//        dataSource.setServerName(host);
//        dataSource.setPort(port);
//        dataSource.setDatabaseName(database);
//        dataSource.setUser(username);
//        dataSource.setPassword(password);
//
//        connection = dataSource.getConnection();
    }

    @Override
    public void onDisable() {
        try {
            Bukkit.getLogger().info("");
            Bukkit.getConsoleSender().sendMessage("§6[§lGamma.General§6]§e Finalizando...");

            HandlerList.unregisterAll(MAIN);

            registerData();
            //saveMiners();

            Bukkit.getConsoleSender().sendMessage("§4[§lGamma.General§4]§c Finalizado com sucesso!");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§4[§lGamma.General§4] §c§lERROR§c: Ops! Algo deu errado -> " + e);
            Date now = new Date();
            Main.MAIN.getLogger().log(Level.SEVERE, "[" + now + "] Error: ", e);
        }
    }

    private void registerAll() {
        registerConfigs();
        registerEvents();
        registerCommands();
    }

    private void registerConfigs() {
        registerConfig("messages");
        registerConfig("p_status");
        registerConfig("warps");
    }

    private void registerConfig(String file_name) {
        Config cfg = new Config(file_name + ".yml");
        CONFIGS.put(file_name, cfg);
        if (!cfg.exists()) cfg.saveDefault();
        cfg.reload();
    }

    private void registerEvents() {
        PM.registerEvents(new PlayerEv(), MAIN);
        PM.registerEvents(new KitGUI(), MAIN);
        PM.registerEvents(new WarpGUI(), MAIN);
        PM.registerEvents(new Natural(), MAIN);
        PM.registerEvents(new Tag(), MAIN);
        //STAFF
        PM.registerEvents(new Admin(), MAIN);

        //KITS
        PM.registerEvents(new Kit(), MAIN);
        PM.registerEvents(new Kangaroo(), MAIN);
        PM.registerEvents(new Flash(), MAIN);
        PM.registerEvents(new Fisherman(), MAIN);
    }

    private void registerCommands() {
        getCommand("tag").setExecutor(new Tag());
        getCommand("world").setExecutor(new Teleport());
        getCommand("setspawn").setExecutor(new Spawn());
        getCommand("spawn").setExecutor(new Spawn());

        //STAFF
        getCommand("admin").setExecutor(new Admin());
        getCommand("sc").setExecutor(new StaffChat());
        getCommand("scon").setExecutor(new StaffChat());
        getCommand("scoff").setExecutor(new StaffChat());

        //KITS
        getCommand("kits").setExecutor(new KitGUI());
        getCommand("warps").setExecutor(new WarpGUI());
        getCommand("pvp").setExecutor(new Pvp());
        getCommand("kangaroo").setExecutor(new Kangaroo());
        getCommand("flash").setExecutor(new Flash());
        getCommand("fisherman").setExecutor(new Fisherman());
    }

    private void registerData() {
        FileConfiguration file = CONFIGS.get("p_status").getConfig();
        for (Map.Entry<Player, Long> data : Controller.player_kills.entrySet()) {
            file.set(data.getKey().getName() + ".kills", data.getValue());
        }

        for (Map.Entry<Player, Long> data : Controller.player_deaths.entrySet()) {
            file.set(data.getKey().getName() + ".deaths", data.getValue());
        }

        file = CONFIGS.get("warps").getConfig();

        for (Map.Entry<String, Object[]> data : Controller.warps.entrySet()) {
            Object[] location = data.getValue();
            String warp_name = data.getKey();

            file.set(warp_name + ".world", ((org.bukkit.World) location[0]).getName());
            file.set(warp_name + ".x", location[1]);
            file.set(warp_name + ".y", location[2]);
            file.set(warp_name + ".z", location[3]);
        }

        CONFIGS.get("p_status").saveCustomConfig();
        CONFIGS.get("warps").saveCustomConfig();
    }

}
