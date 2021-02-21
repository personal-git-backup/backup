package br.com.gmfonseca.kits;

import br.com.gmfonseca.api.Kit;
import br.com.gmfonseca.main.Controller;
import br.com.gmfonseca.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

public class Flash extends Kit implements Listener, CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;

            if(p.hasPermission("gamma.kit.flash")) {


                if (!Controller.pHasKit(p)) {
                    giveItems(p);
                    Controller.player_kit.put(p, "flash");

                    p.sendMessage(Main.CONFIGS.get("messages").getConfig().getString("Personal.Command.getKit")
                            .replaceAll("&", "§")
                            .replaceAll("%kitname%", "FLASH"));
                } else {
                    p.sendMessage("§c§lERROR§c: Voce ja selecionou um kit.");
                }
            }else{
                p.sendMessage("§c§lERROR§c: Voce nao possui o kit flash");
            }

        }else{
            sender.sendMessage("§c§lERROR§c: Voce nao e um player.");
        }

        return false;
    }

    private void giveItems(Player p){
        PlayerInventory inv = p.getInventory();
        inv.clear();

        ItemStack flash = new ItemStack(Material.REDSTONE_TORCH_ON);
        ItemMeta m_flash = flash.getItemMeta();
        m_flash.setDisplayName("§eFlash");
        flash.setItemMeta(m_flash);

        inv.setItem(0, new ItemStack(Material.DIAMOND_SWORD));
        inv.setItem(1, flash);
        set_armor(p);
        fill_soup(p.getInventory(), p);
    }

    private void set_armor(Player p){
        PlayerInventory inv = p.getInventory();
        inv.setHelmet(new ItemStack(Material.LEATHER_HELMET));
        inv.setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
        inv.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
    }

    @EventHandler
    private void onUse(PlayerInteractEvent e){

        try {
            Player p = e.getPlayer();

            if(Controller.player_kit.get(p).equalsIgnoreCase("flash") && e.getItem() != null){
                if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                 && e.getItem().getType() == Material.REDSTONE_TORCH_ON){
                    e.setCancelled(true);

                    Block block = p.getTargetBlock((Set<Material>) null, 30).getRelative(e.getBlockFace());

                    Location oldLocation = p.getLocation();
                    Location newLocation = block.getLocation();

                    newLocation.setYaw(oldLocation.getYaw());
                    newLocation.setPitch(oldLocation.getPitch());

                    p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 1, 100);
                    p.teleport(newLocation);

                    p.playSound(newLocation, Sound.AMBIENCE_THUNDER, 10.0F, 10.0F);
                    p.sendMessage("§eTELEPORTANDO AHAAAHAH");

                }
            }
        } catch (Exception err) {
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Flash, onUse()");
        }

    }

    @EventHandler
    public void onDropKitItem(PlayerDropItemEvent e){
        try {
            if(Controller.player_kit.get(e.getPlayer()).equalsIgnoreCase("flash")) {

                    Item item = e.getItemDrop();

                    if(item.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("§eFlash")
                    && item.getItemStack().getType().equals(Material.REDSTONE_TORCH_ON))
                    {
                        e.setCancelled(true);
                    }
            }
        } catch (Exception err) {
            Bukkit.getConsoleSender().sendMessage(err.toString() + " | Class Flash, onDropKitItem()");
        }

    }

}
