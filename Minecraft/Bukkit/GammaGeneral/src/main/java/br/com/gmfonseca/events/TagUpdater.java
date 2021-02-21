package br.com.gmfonseca.events;

import br.com.gmfonseca.commands.Tag;
import br.com.gmfonseca.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TagUpdater {

    private static int id = -1;

    public static void start(){
        id = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.MAIN, new Runnable(){
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    String tag = Tag.playerTag.getOrDefault(p.getUniqueId(), ChatColor.GRAY + p.getName());

                    p.setPlayerListName(tag);
                    p.setDisplayName(tag);
                }
            }
        }, 120, 120).getTaskId();
    }

    public static void stop(){
        if(id != -1){
            Bukkit.getScheduler().cancelTask(id);
            id = -1;
        }
    }

}
