package br.com.gmfonseca.api;

import org.bukkit.ChatColor;

public enum Tags {

    DONO(ChatColor.DARK_RED),
    ADMIN(ChatColor.RED),
    DEV(ChatColor.BLUE),
    MODPLUS(ChatColor.DARK_PURPLE, ChatColor.ITALIC),
    MOD(ChatColor.DARK_PURPLE),
    TESTER(ChatColor.LIGHT_PURPLE),
    AJUDANTE(ChatColor.YELLOW),
    MVP(ChatColor.GOLD),
    VIP(ChatColor.GREEN),
    PADRAO(ChatColor.GRAY);

    final ChatColor color;
    final ChatColor special;

    Tags(ChatColor color){
        this.color = color;
        this.special=null;
    }

    Tags(ChatColor color, ChatColor special){
        this.color = color;
        this.special = special;
    }
}
