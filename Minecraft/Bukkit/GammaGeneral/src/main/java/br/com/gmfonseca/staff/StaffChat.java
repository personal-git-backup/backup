package br.com.gmfonseca.staff;

import br.com.gmfonseca.main.Controller;
import br.com.gmfonseca.main.Main;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Map;

public class StaffChat implements Listener, CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(sender.hasPermission("gamma.staff.chat")){
            if(cmd.getName().equalsIgnoreCase("sc")) {
                if (args.length != 0) {
                    StringBuilder message = new StringBuilder();

                    for (String arg : args) {
                        message.append(arg);
                        message.append(" ");
                    }

                    for (Map.Entry<Player, Boolean> staff : Controller.staff.entrySet()) {
                        if (staff.getValue()) {
                            Player p = staff.getKey();
                            p.sendMessage("§c§lSTAFF CHAT §7" + sender.getName() + " §a> §7" + message.toString());
                            p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 0.5f, 0.5f);
                        }
                    }

                } else {
                    sender.sendMessage("");
                    sender.sendMessage("§c§lOps! §cVoce precisa inserir uma mensagem para enviar");
                    sender.sendMessage("§cUse /sc <mensagem>");
                }
            }else{
                if(sender instanceof Player) {
                    Player p = (Player) sender;
                    if (cmd.getName().equalsIgnoreCase("scon")) {
                        Controller.staff.put(p, false);
                        sender.sendMessage("");
                        p.sendMessage("§a§lSTAFF CHAT ativado");

                    } else {
                        Controller.staff.put(p, true);
                        sender.sendMessage("");
                        p.sendMessage("§c§lSTAFF CHAT desativado");
                    }
                }
            }
        }else{
            sender.sendMessage(Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.noPermCmd").replaceAll("&", "§").replaceAll("%cmd%", "sc"));
        }

        return false;
    }
}
