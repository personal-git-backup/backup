package br.com.gmfonseca.mine.autominer.entity;

import br.com.gmfonseca.mine.autominer.main.Main;
import br.com.gmfonseca.mine.autominer.util.Direction;
import br.com.gmfonseca.mine.autominer.util.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Miner implements InventoryHolder {

    // Attributes
    private int id = -1;
    private Player owner;
    private Inventory backpack;
    private Material tool;

    private Location location;
    private Block target;
    private Direction direction;
    private ArmorStand stand;

    // Controllers
    private boolean mining = false;
    private boolean placed = false;
    private int currentTask = -1;
    private boolean up = false;
    private boolean down = false;
    private int delay = 200;

    // Constructors
    public Miner(Player owner, Location location, int inventorySize, List<ItemStack> items, Material tool) {
        this.owner = owner;
        this.location = location;
        this.tool = tool;

        this.backpack = Bukkit.createInventory(this, inventorySize);

        if(items != null) {
            for (int i = 0; i < items.size(); i++) {
                backpack.setItem(i, items.get(i));
            }
        }

        spawn();

        owner.sendMessage("target location: " + target.getX() + " " + target.getY() + " " + target.getZ()); //TODO: customize later, debug line
        owner.sendMessage("target type: " + target.getType()); //TODO: customize later, debug line
    }

    public boolean spawn(Location location){
        if(isPlaced() || location == null) return false;

        this.location=location;
        spawn();

        return true;
    }

    /**
     *
     */
    private void spawn(){
        direction = Util.getDirection(location);

        ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);
        stand.setVisible(true);
        stand.setArms(true);
        stand.setBasePlate(false);
        stand.setSmall(true);
        stand.setHelmet(Util.getCustomHead("Miner"));
        stand.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        stand.setItemInHand(new ItemStack(tool));
        stand.setGravity(false);
        stand.setCustomName("§6§lMiner");
        this.stand = stand;
        stand.setCustomNameVisible(true);

        buildTarget();

        placed=true;
    }

    /**
     *
     */
    private void buildTarget(){
        Location targetLocation = location.clone();
        targetLocation.setX(targetLocation.getX() + direction.getX());
        targetLocation.setZ(targetLocation.getZ() + direction.getZ());

        target = targetLocation.getBlock();
    }

    /**
     *
     */
    public void removeMiner(){
        remove();
    }

    private synchronized void remove(){
        stand.remove();
        target = null;
        location = null;

        stop();

        placed = false;
    }

    /**
     *
     */
    public boolean startMining(){
        boolean started = start();

        if(started) owner.sendMessage(ChatColor.GREEN + "[Miner #" + id + "] Starting mining.");

        return started;
    }

    private synchronized boolean start(){
        if(!placed || mining) return false;

        if(Util.isInventoryFull(backpack, null)){
            owner.sendMessage(ChatColor.RED + "[Miner #" + getId() + "] Can't start mining, backpack is full.");

            return false;
        }

        if(target.getType() != Material.AIR) {
            mining = true;

            mineTarget();
        }else{
            owner.sendMessage(ChatColor.RED + "[Miner #" + getId() + "] Can't mine AIR Block.");
        }

        return mining;
    }

    /**
     *
     */
    public boolean stopMining(){
        boolean stopped = stop();

        if(stopped) owner.sendMessage(ChatColor.RED + "[Miner #" + getId() + "] Stopping mining.");

        return stopped;
    }

    private synchronized boolean stop(){
        if(currentTask != -1) {
            Bukkit.getScheduler().cancelTask(currentTask);
            currentTask = -1;
        }

        if(!mining) return false;

        mining = false;
        up = false;
        down = false;

        return true;
    }

    private synchronized void mineTarget(){
        if(currentTask!=-1) {
            Bukkit.getScheduler().cancelTask(currentTask);
            currentTask=-1;
        }

        Miner miner = this;
        currentTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.MAIN, new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    miner.owner.sendMessage("minerando " + (miner.target.getType() != Material.AIR)); //TODO: remove later, debug line

                    if (miner.isPlaced() && miner.target.getType() != Material.AIR) {
                        miner.breakTarget();
                        miner.owner.sendMessage("minerei"); //TODO: remove later, debug line
                    } else {
                        miner.owner.sendMessage(ChatColor.RED + "[Miner #" + miner.getId() + "] No blocks to mine, stopping now.");
                        miner.stop();
                    }

                }catch (Exception err){
                    Bukkit.getConsoleSender().sendMessage("§c|thread  Miner:mineTarget() -> " + err.getMessage());
                }
            }
        }, 0, miner.delay); // 200 period = each 10 seconds
    }

    private void breakTarget(){
        try {
            Material targetType = target.getType();
            ItemStack item = new ItemStack(targetType, 1, (short) 0, target.getData());

            //If is a MobSpawner
            if(targetType == Material.MOB_SPAWNER){
                CreatureSpawner spawner = (CreatureSpawner) target.getState();
                item = new ItemStack(targetType, 1);
                ItemMeta meta = item.getItemMeta();
                meta.setLore(Arrays.asList(ChatColor.ITALIC + spawner.getCreatureTypeName()));
                item.setItemMeta(meta);
            }

            insertItemBackpack(item);
            target.getWorld().playEffect(target.getLocation(), Effect.STEP_SOUND, targetType);
            target.setType(Material.AIR);
        }catch (Exception err){
            Bukkit.getConsoleSender().sendMessage("§c|method  Miner:breakTarget() -> " + err.getMessage());
        }
    }

    // BACKPACK CONTROLLERS
    public void openBackpack(Player p){
        p.openInventory(backpack);
    }

    public void insertItemBackpack(ItemStack item){
        try {
            Material[] filter = {item.getType()};

            backpack.addItem(item);

            if (Util.isInventoryFull(backpack, filter)) {
                owner.sendMessage(ChatColor.RED + "[Miner #" + getId() + "] Backpack full, stopping now.");
                stop();
            }

        }catch (Exception err){
            Bukkit.getConsoleSender().sendMessage("§c|method  Miner:insertItemBackpack() -> " + err.getMessage());
        }
    }

    /**
     *
     */
    public Material getTool() {
        return tool;
    }

    /**
     *
     */
    public Player getOwner() {
        return owner;
    }

    /**
     *
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     *
     */
    public Location getLocation(){
        return location;
    }
    /**
     *
     */
    public Inventory getInventory() {
        return backpack;
    }

    public int getDelaySeconds(){
        return delay/20;
    }

    /**
     *
     */
    public ArmorStand getStand() {
        return stand;
    }

    public boolean isMining() {
        return mining;
    }

    public boolean isPlaced() {
        return placed;
    }

    public Map<Material, Integer> backpackToMap(){
        Map<Material, Integer> back = new HashMap<>();

        for (ItemStack item : backpack.getContents()) {
            if(item != null) {
                if (back.containsKey(item.getType())) {
                    int current = back.get(item.getType());
                    back.put(item.getType(), current + item.getAmount());
                } else {
                    back.put(item.getType(), item.getAmount());
                }
            }
        }

        return back;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Block getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Miner miner = (Miner) o;

        if(this.location == null || miner.location == null) {
            return id==miner.id
                   && owner.getUniqueId().equals(miner.owner.getUniqueId());
        }

        return (Objects.equals(location.getX(), miner.location.getX())
                && Objects.equals(location.getY(), miner.location.getY())
                && Objects.equals(location.getZ(), miner.location.getZ())
        );
    }

    @Override
    public int hashCode() {
        if(location == null) return Objects.hash(id, owner.getUniqueId());

        return Objects.hash(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public String toString() {
        return "Miner{" +
                "id=" + id +
                ", owner=" + owner +
                ", backpack=" + Util.arrayToSet(backpack.getContents()) +
                '}';
    }
}
