package br.com.gmfonseca.mine.autominer.util;

import br.com.gmfonseca.mine.autominer.main.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class Config {

    private FileConfiguration config;
    private File file;
    private String filename;

    public Config(String filename) {
        this.file = new File(Main.MAIN.getDataFolder(), filename);
        this.filename = filename;
    }

    public void reload() {
        if (file == null) {
            file = new File(Main.MAIN.getDataFolder(), filename);
        }
        config = YamlConfiguration.loadConfiguration(file);
        config.getKeys(true);

        // Look for defaults in the jar
        try {
            Reader defConfigStream = new InputStreamReader(Main.MAIN.getResource(filename), StandardCharsets.UTF_8);

            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config.setDefaults(defConfig);

        }catch (Exception e){
            Main.MAIN.getLogger().log(Level.SEVERE, "Could not save config to " + file, e);
        }
    }

    public void saveCustomConfig() {
        if (config == null || file == null) {
            return;
        }
        try {
            this.config.save(file);
        } catch (IOException ex) {
            Main.MAIN.getLogger().log(Level.SEVERE, "Could not save config to " + file, ex);
        }
    }

    public void saveDefault() {
        if (file == null) {
            file = new File(Main.MAIN.getDataFolder(), filename);
        }
        if (!file.exists()) {
            Main.MAIN.saveResource(filename, false);
        }
    }

    public boolean exists(){
        return getFile().exists();
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reload();
        }
        return config;
    }

    public File getFile() {
        return file;
    }

    public String getFilename() {
        return filename;
    }
}
