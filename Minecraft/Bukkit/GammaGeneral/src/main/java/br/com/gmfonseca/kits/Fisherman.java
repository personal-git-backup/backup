package br.com.gmfonseca.kits;

import br.com.gmfonseca.api.Kit;
import br.com.gmfonseca.main.Controller;
import br.com.gmfonseca.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Fisherman extends Kit implements Listener, CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;

            if(p.hasPermission("gamma.kit.fisherman")) {
                if(!Controller.pHasKit(p)){
                    giveItems(p);
                    Controller.player_kit.put(p, "fisherman");

                    p.sendMessage(Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.getKit")
                            .replaceAll("&", "§")
                            .replaceAll("%kitname%", "FISHERMAN"));
                }else{
                    p.sendMessage("§c§lERROR§c: Voce ja selecionou um kit.");
                }
            }else{
                p.sendMessage("§c§lERROR§c: Voce nao possui o kit fisherman");
            }
        }else{
            sender.sendMessage("§c§lERROR§c: Voce nao e um player.");
        }

        return false;
    }

    private void giveItems(Player p){
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack fisherman = new ItemStack(Material.FISHING_ROD);
        ItemMeta m_fisherman = fisherman.getItemMeta();
        m_fisherman.setDisplayName("§eFisherman");
        fisherman.setItemMeta(m_fisherman);

        inv.setItem(0, new ItemStack(Material.IRON_SWORD));
        inv.setItem(1, fisherman);
        set_armor(p);
        fill_soup(p.getInventory(), p);
    }

    private void set_armor(Player p){
        PlayerInventory inv = p.getInventory();
        inv.setHelmet(new ItemStack(Material.IRON_HELMET));
        inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        inv.setBoots(new ItemStack(Material.GOLD_BOOTS));
    }

    @EventHandler
    private void onFish(PlayerFishEvent e){
        try {
            Player p = e.getPlayer();
            if (Controller.player_kit.get(p).equalsIgnoreCase("fisherman") && p.getItemInHand().getType() == Material.FISHING_ROD) {
                Entity caught = e.getCaught();

                if (caught instanceof Player) {
                    caught.teleport(e.getPlayer().getLocation());
                }
            }
        }catch (Exception err){
            Bukkit.getLogger().warning(err.toString() + " | Class Fisherman, onFish()");
        }

    }

    @EventHandler
    public void onDropKitItem(PlayerDropItemEvent e){
        try {
            if (Controller.player_kit.get(e.getPlayer()).equalsIgnoreCase("fisherman")) {

                Item item = e.getItemDrop();

                if (item.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("§eFisherman")
                        && item.getItemStack().getType().equals(Material.FISHING_ROD)) {
                    e.setCancelled(true);
                }
            }

        } catch (Exception err) {
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Fisherman, onDropKitItem()");
        }

    }

}
