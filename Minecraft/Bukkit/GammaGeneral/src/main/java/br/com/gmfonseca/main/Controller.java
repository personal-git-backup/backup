package br.com.gmfonseca.main;

import br.com.gmfonseca.api.Tags;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Controller {

    //General
    public static HashMap<String, Object[]> warps = new HashMap<>();
    public static HashMap<String, Tags> tags = new HashMap<>();

    //Staff
    public static HashMap<Player, Boolean> admin = new HashMap<>();
    public static HashMap<Player, Boolean> staff = new HashMap<>();

    //Player stats
    public static HashMap<Player, String> player_kit = new HashMap<>();
    public static HashMap<Player, Long> player_kills = new HashMap<>();
    public static HashMap<Player, Long> player_deaths = new HashMap<>();

    public Controller(){

    }

    public static boolean pHasKit(Player p){
        boolean has=false;
        if(!player_kit.get(p).equalsIgnoreCase("none")){
            has = true;
        }

        return has;
    }

    public static boolean pRemoveKit(Player p){
        boolean removed=false;
        if(pHasKit(p)){
            player_kit.put(p, "none");
            removed = true;
        }

        return removed;
    }

    public static boolean pSetKit(Player p, String kitname){
        boolean set=false;
        if(!pHasKit(p)){
            player_kit.put(p, kitname);
            set = true;
        }

        return set;
    }

}
