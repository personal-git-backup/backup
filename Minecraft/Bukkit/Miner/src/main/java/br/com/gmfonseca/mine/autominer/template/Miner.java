package br.com.gmfonseca.mine.autominer.template;

import br.com.gmfonseca.mine.autominer.util.Util;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Miner {

    private Player owner;
    private Block target;
    private List<ItemStack> items;
    private Inventory backpack;
    private ArmorStand miner;
    private Location location;
    private int animation;
    private int down_animation;
    private int up_animation;
    private boolean mining;
    private ArrayList<Integer> animacoes_ativas;

    public Miner(Player owner, Block target, Location location, ArrayList<ItemStack> items){
        this.owner = owner;
        this.target = target;
        this.location = location;
        this.items = items;
        mining = false;

        this.backpack = Bukkit.createInventory(owner, 54);
        for(ItemStack item : items){
            backpack.addItem(item);
        }

        spawn();
    }

    public void spawn(){
        ArmorStand miner = location.getWorld().spawn(location, ArmorStand.class);
        miner.setVisible(true);
        miner.setSmall(true);
        miner.setArms(true);
        miner.setItemInHand(new ItemStack(Material.IRON_PICKAXE));
        miner.setHelmet(Util.getCustomHead("Miner"));
        miner.setBoots(new ItemStack(Material.CHEST));
        miner.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        miner.setBasePlate(false);
        miner.setCustomNameVisible(true);
        miner.setCustomName("§6§lMiner");
        miner.setRightArmPose(miner.getRightArmPose().setX(-2f)); //deixar mão para cima

        this.miner = miner;
    }

    private void setUp_animation(int animation){
        up_animation = animation;
    }
    private void setDown_animation(int animation){
        down_animation = animation;
    }

    public boolean start_mining(){
        boolean started=false;

        if(miner != null) {
            miner.setRightArmPose(miner.getRightArmPose().setX(-2f)); //deixar mão para cima

            //MinerAnimation down_animation = new MinerAnimation(miner, 0.1f);
            //MinerAnimation up_animation = new MinerAnimation(miner, -0.1f);

  /*        animation = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.MAIN, new BukkitRunnable() {
                long delay=0;

                public void run() {
                    int down = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.MAIN, down_animation, delay, 0);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getScheduler().cancelTask(down);

                            target.breakNaturally();
                            backpack.addItem(new ItemStack(target.getType()));
                            target.getWorld().playSound(target.getLocation(), Sound.DIG_STONE, 1.0F, 1.0F);

                        }
                    }.runTaskLater(Main.MAIN, delay+20);

                    delay+=20;

                    int up = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.MAIN, up_animation, delay, 0);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getScheduler().cancelTask(up);

                        }
                    }.runTaskLater(Main.MAIN, delay+20);

                }

            }, 0, 65);*/

            mining=true;
            started=true;
        }

        return started;
    }
    public boolean stop_mining(){
        boolean stopped=false;

        if(mining && animation != 0){
            mining=false;
            Bukkit.getScheduler().cancelTask(animation);
            stopped=true;
        }

        return stopped;
    }

    public void generate_miner_inventory(Player p){
        miner_inventory(p);
    }
    private void miner_inventory(Player p){
        Inventory miner_inv = Bukkit.createInventory(p, 9, "§6§lMiner");

        byte data = (byte) (mining ? 1 : 10);
        String name = (mining ? "§cStop" : "§aStart");
        ItemStack mining_control = new ItemStack(Material.INK_SACK, 1, (short) 0, data);
        ItemMeta meta = mining_control.getItemMeta();
        meta.setDisplayName(name);
        mining_control.setItemMeta(meta);
        miner_inv.setItem(0, mining_control);

        ItemStack setblock = new ItemStack(Material.CHEST);
        ItemMeta setblock_meta = mining_control.getItemMeta();
        setblock_meta.setDisplayName("§eBackpack");
        setblock.setItemMeta(setblock_meta);
        miner_inv.setItem(6, setblock);

        ItemStack remove = new ItemStack(Material.BED);
        ItemMeta remove_meta = mining_control.getItemMeta();
        remove_meta.setDisplayName("§cRemove");
        remove.setItemMeta(remove_meta);
        miner_inv.setItem(8, remove);

        p.openInventory(miner_inv);
    }

    public void generate_miner_backpack(Player p){
        miner_backpack(p);
    }
    private void miner_backpack(Player p){
        if(backpack != null) {
            p.openInventory(backpack);
        }else{
            p.sendMessage("Miner Inventory is §cNULL");
        }
    }

    public List<ItemStack> backpackAsList(){
        items = new ArrayList<ItemStack>();

        for(ItemStack item : backpack.getContents()){
            if(item != null) {
                items.add(item);
            }
        }

        return items;
    }

    public Block getTarget() {
        return target;
    }
    public List<ItemStack> getItems() {
        return items;
    }
    public Location getLocation() {
        return location;
    }
    public ArmorStand getMiner() {
        return miner;
    }
    public Inventory getBackpack() {
        return backpack;
    }
    public Player getOwner() {
        return owner;
    }

    public boolean isMining() {
        return mining;
    }
}
