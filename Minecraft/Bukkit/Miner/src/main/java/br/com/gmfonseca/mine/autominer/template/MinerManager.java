package br.com.gmfonseca.mine.autominer.template;

import br.com.gmfonseca.mine.autominer.util.Direction;
import br.com.gmfonseca.mine.autominer.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MinerManager implements Listener,CommandExecutor {
    public static HashMap<Player, Miner> miner_interact = new HashMap<Player, Miner>();

    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        String cmd = command.getName();
        if(cmd.equalsIgnoreCase("getminer")){
            if(sender instanceof Player){
                Player p = (Player) sender;
                Location p_location = p.getLocation();

                Location miner_location = p.getTargetBlock((HashSet<Byte>)null, 3).getLocation();
                miner_location.setX(miner_location.getX()+0.5);
                miner_location.setY(miner_location.getY()+1);
                miner_location.setZ(miner_location.getZ()+0.5);
                miner_location = miner_location.setDirection(p_location.getDirection());

                Location target_location = miner_location.clone();
                Direction dir = Util.getDirection(target_location);

                target_location.setX(target_location.getX() + dir.getX());
                target_location.setZ(target_location.getZ() + dir.getZ());

                Block target = p.getWorld().getBlockAt(target_location);

                Miner miner = new Miner(p, target, miner_location, new ArrayList<ItemStack>());

                /*if(!Controller.player_miners.containsKey(p)){
                    Controller.player_miners.put(p, new ArrayList<>());
                }

                Controller.player_miners.get(p).add(miner);
                Controller.ownerMiners.add(miner);*/

            }
        }

        return false;
    }

    @EventHandler
    private synchronized void onInteract(PlayerArmorStandManipulateEvent e){
        ArmorStand stand = e.getRightClicked();
        if (ChatColor.stripColor(stand.getCustomName()).equalsIgnoreCase("miner")){
            e.setCancelled(true);
            Player p = e.getPlayer();

            Miner miner = getMiner(p, stand);
            if (miner != null){

                if(miner.getOwner() == p || p.hasPermission("gamma.staff.miner")) {
                    if (p.isSneaking()) {
                        miner.generate_miner_inventory(p);
                        miner_interact.put(p, miner);
                    }
                }else{
                    p.sendMessage("§c§lOps!§c Voce nao tem permissao para acessar o §6Miner§c de §6" + miner.getOwner().getName() + ".");
                }

            } else {
                p.sendMessage("§c§lOps!§c Voce nao tem permissao para acessar este §6Miner§c. Contate um Administrador!");
            }
        }
    }

    @EventHandler
    private synchronized void onHit(EntityDamageByEntityEvent e){
        try {
            if (e.getEntity() instanceof ArmorStand) {
                ArmorStand stand = (ArmorStand) e.getEntity();
                if (ChatColor.stripColor(stand.getCustomName()).equalsIgnoreCase("miner")) {
                    if (e.getDamager() instanceof Player) {
                        Player p = (Player) e.getDamager();
                        if (p.hasPermission("gamma.staff.miner") && p.getItemInHand() != null && p.getItemInHand().getItemMeta() != null){
                            if(ChatColor.stripColor(p.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("MinerRemover")) {

                                removeMiner(p, getMiner(p, stand));
                                return;
                            }
                        }
                    }
                    e.setCancelled(true);
                }
            }
        }catch (Exception err){

        }
    }

    @EventHandler
    private void onBreakBlock(BlockBreakEvent e){
//        e.getBlock().b
    }

    @EventHandler
    private synchronized void onInventoryInteract(InventoryClickEvent e){
        Inventory inv = e.getInventory();

        if (ChatColor.stripColor(inv.getName()).equalsIgnoreCase("Miner")){
            ItemStack item = e.getCurrentItem();
            Player p = (Player) e.getWhoClicked();
            if ((item != null) && (item.getItemMeta() != null)){

                Miner miner = miner_interact.get(p);

                if (miner != null) {

                    if(miner.getOwner().equals(p) || p.hasPermission("gamma.staff.miner")) {

                        if (item.getType().equals(Material.INK_SACK)) {

                            e.setCancelled(true);
                            ItemMeta meta = item.getItemMeta();

                            if (miner.isMining()) {
                                item.setData(new MaterialData(10));
                                meta.setDisplayName("§aStart");
                                miner.stop_mining();

                            } else {
                                item.setData(new MaterialData(1));
                                meta.setDisplayName("§cStop");
                                miner.start_mining();

                            }

                            item.setItemMeta(meta);
                            inv.setItem(0, item);
                            p.closeInventory();

                        } else if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Backpack")) {
                            miner_interact.remove(p);

                            e.setCancelled(true);
                            p.closeInventory();

                            miner.generate_miner_backpack(p);
                        } else if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Remove")) {
                            e.setCancelled(true);
                            removeMiner(p, miner);
                        }
                    }else{
                        p.sendMessage("§c§lOps!§c Voce nao tem permissao para acessar o §6Miner§c de §6" + miner.getOwner().getName() + ".");
                    }
                } else {
                    p.sendMessage("§c§lOps!§c Voce nao tem permissao para acessar este §6Miner§c. Contate um Administrador!");
                }
            }
        }
    }

    private synchronized Miner getMiner(Player p, ArmorStand stand){
        Miner miner = null;
        /*
        for (Miner m : Controller.ownerMiners) {
            if (m.getMiner().equals(stand)){
                miner = m;
                break;
            }
        }*/
        return miner;
    }

    private synchronized void removeMiner(Player p, Miner miner){
        //Controller.player_miners.get(p).remove(miner);
        //Controller.ownerMiners.remove(miner);
        miner.getLocation().getWorld().playSound(miner.getLocation(), Sound.VILLAGER_DEATH, 1, 1);
        miner.getMiner().remove();

        /*
        if(!Controller.not_placed_miners.containsKey(p)){
            Controller.not_placed_miners.put(p, 0);
        }
        int new_value = Controller.not_placed_miners.get(p)+1;
        Controller.not_placed_miners.put(p, new_value);*/
    }

    private void openMinersListInventory(Player p){

    }

}
