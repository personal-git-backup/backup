package br.com.gmfonseca.kits;

import br.com.gmfonseca.api.Kit;
import br.com.gmfonseca.main.Controller;
import br.com.gmfonseca.main.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Pvp extends Kit implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;

            if(p.hasPermission("gamma.kit.pvp")){
                if(!Controller.pHasKit(p)){
                    giveItems(p);
                    Controller.player_kit.put(p, "pvp");

                    p.sendMessage(Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.getKit")
                                    .replaceAll("&", "§")
                                    .replaceAll("%kitname%", "PVP"));
                }else{
                    p.sendMessage("§c§lERROR§c: Voce ja selecionou um kit.");
                }
            }else{
                p.sendMessage("§c§lERROR§c: Voce nao possui o kit kangaroo");
            }

        }else{
            sender.sendMessage("§c§lERROR§c: Voce nao e um player.");
        }

        return false;
    }

    private void giveItems(Player p){
        PlayerInventory inv = p.getInventory();
        inv.clear();

        inv.setItem(0, new ItemStack(Material.DIAMOND_SWORD));
        set_armor(p);
        fill_soup(p.getInventory(), p);
    }

    private void set_armor(Player p){
        PlayerInventory inv = p.getInventory();
        inv.setHelmet(new ItemStack(Material.IRON_HELMET));
        inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        inv.setBoots(new ItemStack(Material.IRON_BOOTS));
    }
}
