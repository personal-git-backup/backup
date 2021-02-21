package br.com.gmfonseca.commands;

import br.com.gmfonseca.main.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Tag implements Listener, CommandExecutor {

    public static Map<UUID, String> playerTag = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equals("tag")) {
            Player p = (Player) sender;

            if (args.length == 0) {
                p.sendMessage("");
                p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "SUAS TAGS" + ChatColor.GREEN + ":");
                TextComponent tag;

                //TAG DONO
                if (p.hasPermission("gamma.tag.dono")) {
                    tag = new TextComponent(ChatColor.GRAY + "- " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Dono");
                    tag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag dono"));
                    tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Selecionar a tag " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Dono").create()));
                    p.spigot().sendMessage(tag);
                }

                //TAG ADMIN
                if (p.hasPermission("gamma.tag.admin")) {
                    tag = new TextComponent(ChatColor.GRAY + "- " + ChatColor.RED + "" + ChatColor.BOLD + "Admin");
                    tag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag admin"));
                    tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Selecionar a tag" + ChatColor.RED + "" + ChatColor.BOLD + "Admin").create()));
                    p.spigot().sendMessage(tag);
                }

                //TAG DEV
                if (p.hasPermission("gamma.tag.dev")) {
                    tag = new TextComponent(ChatColor.GRAY + "- " + ChatColor.BLUE + "" + ChatColor.BOLD + "Dev");
                    tag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag dev"));
                    tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Selecionar a tag " + ChatColor.BLUE + "" + ChatColor.BOLD + "Dev").create()));
                    p.spigot().sendMessage(tag);
                }

                //TAG MOD+
                if (p.hasPermission("gamma.tag.mod+")) {
                    tag = new TextComponent(ChatColor.GRAY + "- " + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Mod" + ChatColor.GOLD + "+");
                    tag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag mod+"));
                    tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Selecionar a tag " + ChatColor.BOLD + "Mod" + ChatColor.GOLD + "+").create()));
                    p.spigot().sendMessage(tag);
                }

                //TAG MOD
                if (p.hasPermission("gamma.tag.mod")) {
                    tag = new TextComponent(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Mod");
                    tag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag mod"));
                    tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Selecionar a tag " + ChatColor.BOLD + "Mod").create()));
                    p.spigot().sendMessage(tag);
                }

                //TAG TESTER
                if (p.hasPermission("gamma.tag.tester")) {
                    tag = new TextComponent(ChatColor.GRAY + "- " + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Tester");
                    tag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag tester"));
                    tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Selecionar a tag " + ChatColor.BOLD + "Tester").create()));
                    p.spigot().sendMessage(tag);
                }

                //TAG AJUDANTE
                if (p.hasPermission("gamma.tag.ajudante")) {
                    tag = new TextComponent(ChatColor.GRAY + "- " + ChatColor.YELLOW + "" + ChatColor.BOLD + "Ajudante");
                    tag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag ajudante"));
                    tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Selecionar a tag " + ChatColor.BOLD + "Ajudante").create()));
                    p.spigot().sendMessage(tag);
                }

                //TAG MVP
                if (p.hasPermission("gamma.tag.mvp")) {
                    tag = new TextComponent(ChatColor.GRAY + "- " + ChatColor.GOLD + "" + ChatColor.BOLD + "Mvp");
                    tag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag mvp"));
                    tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Selecionar a tag " + ChatColor.BOLD + "Mvp").create()));
                    p.spigot().sendMessage(tag);
                }

                //TAG VIP
                if (p.hasPermission("gamma.tag.vip")) {
                    tag = new TextComponent(ChatColor.GRAY + "- " + ChatColor.GREEN + "" + ChatColor.BOLD + "VIP");
                    tag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag vip"));
                    tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Selecionar a tag " + ChatColor.BOLD + "VIP").create()));
                    p.spigot().sendMessage(tag);
                }

                //TAG PADRAO
                if (p.hasPermission("gamma.tag.padrao")) {
                    tag = new TextComponent(ChatColor.GRAY + "- " + ChatColor.BOLD + "Padrao");
                    tag.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag padrao"));
                    tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Selecionar a tag " + ChatColor.BOLD + "Padrao").create()));
                    p.spigot().sendMessage(tag);
                }

                p.sendMessage("");

            } else {
                String tag = args[0];
                boolean tagAlterada = false;

                switch (tag.toLowerCase()) {
                    case "dono":
                        if (p.hasPermission("gamma.tag.dono")) {
                            p.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Dono" + ChatColor.DARK_RED + p.getName());
                            playerTag.put(p.getUniqueId(), ChatColor.DARK_RED + "" + ChatColor.BOLD + "Dono" + ChatColor.DARK_RED + p.getName());
                            tagAlterada = true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.noPermTag")
                                            .replaceAll("%tagname%", "dono")));
                        }
                        break;

                    case "admin":
                        if (p.hasPermission("gamma.tag.admin")) {
                            p.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + p.getName());
                            playerTag.put(p.getUniqueId(), ChatColor.RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + p.getName());
                            tagAlterada = true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.noPermTag")
                                    .replaceAll("%tagname%", "admin")));
                        }
                        break;

                    case "dev":
                        if (p.hasPermission("gamma.tag.dev")) {
                            p.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Dev" + ChatColor.BLUE + p.getName());
                            playerTag.put(p.getUniqueId(), ChatColor.BLUE + "" + ChatColor.BOLD + "Dev" + ChatColor.BLUE + p.getName());
                            tagAlterada = true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.noPermTag")
                                    .replaceAll("%tagname%", "dev")));
                        }
                        break;
                    case "mod+":
                        if (p.hasPermission("gamma.tag.mod+")) {
                            p.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Mod" + ChatColor.DARK_PURPLE + "+" + ChatColor.ITALIC + p.getName());
                            playerTag.put(p.getUniqueId(), ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Mod" + ChatColor.DARK_PURPLE + "+" + ChatColor.ITALIC + p.getName());
                            tagAlterada = true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.noPermTag")
                                    .replaceAll("%tagname%", "mod+")));
                        }
                        break;
                    case "mod":
                        if (p.hasPermission("gamma.tag.mod")) {
                            p.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Mod" + ChatColor.DARK_PURPLE + p.getName());
                            playerTag.put(p.getUniqueId(), ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Mod" + ChatColor.DARK_PURPLE + p.getName());
                            tagAlterada = true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.noPermTag")
                                    .replaceAll("%tagname%", "mod")));
                        }
                        break;
                    case "tester":
                        if (p.hasPermission("gamma.tag.tester")) {
                            p.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Tester" + ChatColor.LIGHT_PURPLE + p.getName());
                            playerTag.put(p.getUniqueId(), ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Tester" + ChatColor.LIGHT_PURPLE + p.getName());
                            tagAlterada = true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.noPermTag")
                                    .replaceAll("%tagname%", "tester")));
                        }
                        break;
                    case "ajudante":
                        if (p.hasPermission("gamma.tag.ajudante")) {
                            p.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Ajudante" + ChatColor.YELLOW + p.getName());
                            playerTag.put(p.getUniqueId(), ChatColor.YELLOW + "" + ChatColor.BOLD + "Ajudante" + ChatColor.YELLOW + p.getName());
                            tagAlterada = true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.noPermTag")
                                    .replaceAll("%tagname%", "ajudante")));
                        }
                        break;
                    case "mvp":
                        if (p.hasPermission("gamma.tag.mvp")) {
                            p.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "MVP" + ChatColor.GOLD + p.getName());
                            playerTag.put(p.getUniqueId(), ChatColor.GOLD + "" + ChatColor.BOLD + "MVP" + ChatColor.GOLD + p.getName());
                            tagAlterada = true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.noPermTag")
                                    .replaceAll("%tagname%", "mvp")));
                        }
                        break;
                    case "vip":
                        if (p.hasPermission("gamma.tag.vip")) {
                            p.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.GREEN + p.getName());
                            playerTag.put(p.getUniqueId(), ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.GREEN + p.getName());
                            tagAlterada = true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.noPermTag")
                                    .replaceAll("%tagname%", "vip")));
                        }
                        break;
                    case "padrao":
                        if (p.hasPermission("gamma.tag.padrao")) {
                            p.setDisplayName(ChatColor.GRAY + p.getName());
                            playerTag.put(p.getUniqueId(), ChatColor.GRAY + p.getName());
                            tagAlterada = true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.noPermTag")
                                    .replaceAll("%tagname%", "padrao")));
                        }
                        break;
                    default:
                        p.sendMessage("");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(Main.CONFIGS.get("messages").getConfig().get("Personal.Command.unknown"))));
                        p.sendMessage("");
                }

                if (tagAlterada) {
                    p.setPlayerListName(p.getDisplayName());

                    p.sendMessage("");
                    p.sendMessage(ChatColor.GREEN + "Sua tag foi alterada para: " + p.getDisplayName());
                    p.sendMessage("");
                }
            }
        }

        return false;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        e.setFormat(ChatColor.translateAlternateColorCodes('&', String.valueOf(Main.CONFIGS.get("messages").getConfig().get("Server.chatFormat"))
                .replaceAll("%name", p.getDisplayName())
                .replaceAll("%message", e.getMessage())));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (p.hasPermission("gamma.tag.dono")) {
            p.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Dono" + ChatColor.DARK_RED + p.getName());

        } else if (p.hasPermission("gamma.tag.admin")) {
            p.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + p.getName());

        } else if (p.hasPermission("gamma.tag.dev")) {
            p.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Dev" + ChatColor.BLUE + p.getName());

        } else if (p.hasPermission("gamma.tag.mod+")) {
            p.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Mod" + ChatColor.GOLD + "+" + ChatColor.GREEN + p.getName());

        } else if (p.hasPermission("gamma.tag.mod")) {
            p.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Mod" + ChatColor.GREEN + p.getName());

        } else if (p.hasPermission("gamma.tag.tester")) {
            p.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Tester" + ChatColor.LIGHT_PURPLE + p.getName());

        } else if (p.hasPermission("gamma.tag.ajudante")) {
            p.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Ajudante" + ChatColor.YELLOW + p.getName());

        } else if (p.hasPermission("gamma.tag.mvp+")) {
            p.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "MVP" + ChatColor.GOLD + "+" + ChatColor.BLUE + p.getName());

        } else if (p.hasPermission("gamma.tag.mvp")) {
            p.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "MVP" + ChatColor.BLUE + p.getName());

        } else if (p.hasPermission("gamma.tag.vip+")) {
            p.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.WHITE + "+" + ChatColor.GREEN + p.getName());

        } else if (p.hasPermission("gamma.tag.vip")) {
            p.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.GREEN + p.getName());
        } else {
            p.setDisplayName(ChatColor.GRAY + p.getName());
        }

        p.setPlayerListName(p.getDisplayName());
        e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(Main.CONFIGS.get("messages").getConfig().get("Server.join"))
                .replaceAll("%name", p.getDisplayName())));
    }
}