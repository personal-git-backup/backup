package br.com.gmfonseca.mine.autominer.miner.controller;

import br.com.gmfonseca.mine.autominer.entity.Miner;
import br.com.gmfonseca.mine.autominer.main.Main;
import br.com.gmfonseca.mine.autominer.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.*;

public class MinerManager {

    public static final Map<UUID, Byte> minersAmount = new HashMap<>();
    public static final Map<UUID, Set<Miner>> ownerMiners = new HashMap<>();
    public static int MINER_ID = Main.CONFIGS.get("miners").getConfig().getInt("MINER_ID");

    public static final Set<Miner> miners = new HashSet<>(); // TODO: transferir para um controlador de listas, caso exista

    public static Miner getMinerByArmorStand(ArmorStand stand){
        Miner miner = null;

        // try all miners
        for (Miner m : getAllMiners()) {
            if(m.getStand().equals(stand)){
                miner = m;
                break;
            }
        }

        return miner;
    }

    public static Miner getMinerByArmorStand(ArmorStand stand, Player owner){
        //TODO: build this one to get a miner from specific owner
        Miner miner = null;
        Set<Miner> miners = getPlayerMiners(owner);

        // try all miners
        for (Miner m : miners) {
            if(m.getStand().equals(stand)){
                miner = m;
                break;
            }
        }

        return miner;
    }

    /**
     * Method to increase the amount of miners for a specific player
     *
     * @param owner the player to increase amount
     * @param incrementAmount number to increase the amount of miners
     */
    public static synchronized void increaseMinerAmount(Player owner, byte incrementAmount){
        //TODO: improve logic, maybe too many unneeded comparisons

        try {
            //if doesn't exists owner's key on map
            byte currentAmount = minersAmount.getOrDefault(owner.getUniqueId(), (byte) 0);

            minersAmount.put(owner.getUniqueId(), (byte) (currentAmount + incrementAmount));
        }catch (Exception err){
            Bukkit.getConsoleSender().sendMessage("§c|method  MinerManager:increaseMinerAmount() -> " + err.getMessage());
        }
    }

    /**
     * Method to decrease the amount of miners for a specific player
     *
     * @param owner the player to decrease amount
     * @param decrementAmount number to decrease the amount of miners
     */
    public static synchronized void decreaseMinerAmount(Player owner, byte decrementAmount){
        //TODO: improve logic, maybe too many unneeded comparisons

        try {
            //if doesn't exists owner's key on map
            byte currentAmount = minersAmount.getOrDefault(owner.getUniqueId(), (byte) 0);

            byte generatedAmount = ((currentAmount - decrementAmount) <= 0) ? 0 : (byte)(currentAmount - decrementAmount);

            minersAmount.put(owner.getUniqueId(), generatedAmount);
        }catch (Exception err){
            Bukkit.getConsoleSender().sendMessage("§c|method  MinerManager:increaseMinerAmount() -> " + err.getMessage());
        }
    }

    /**
     *
     */
    public static synchronized void placeMiner(Player owner, Miner miner){
        //TODO: improve logic, maybe too many unneeded comparisons

        //if doesn't exists owner's key on map
        ownerMiners.putIfAbsent(owner.getUniqueId(), new HashSet<>());

        //if doesn't exists owner's key on map
        minersAmount.putIfAbsent(owner.getUniqueId(), (byte) 0);

        byte currentAmount = getMinersAmount(owner);
        int registeredAmount = getRegisteredMinersAmount(owner);

        if (registeredAmount < currentAmount) {

            if(miner.getId() == -1) {
                miner.setId(MINER_ID++);
            }

            ownerMiners.get(owner.getUniqueId()).add(miner);
            miners.add(miner);
        }
    }

    /**
     *
     */
    public static Location buildMinerLocation(Player player){
        Block targetedBlock = player.getTargetBlock((HashSet<Material>) null, 4);
        Location minerLocation = targetedBlock.getLocation();

        minerLocation.setX(minerLocation.getX()+0.5);
        minerLocation.setY(minerLocation.getY()+1);
        minerLocation.setZ(minerLocation.getZ()+0.5);
        minerLocation.setYaw(player.getLocation().getYaw());

        return minerLocation;
    }

    public static synchronized void removeMiner(Player author, Miner miner){
        //TODO: permission to remove a miner
        if(author.hasPermission("gamma.staff.worker") || author.equals(miner.getOwner())) {

            if(Util.isInventoryEmpty(miner.getInventory())){
                ownerMiners.getOrDefault(miner.getOwner().getUniqueId(), new HashSet<>()).remove(miner);
                miners.remove(miner);
            }

            miner.removeMiner();

        }
    }

    // INTEGER

    /**
     * Get player's amount of miners
     *
     * @param owner Player to check placed miners amount
     *
     * @return number of placed miners that is owned by the player p
     */
    public static int getPlacedMinersAmount(Player owner){
        int amount=0;

        Set<Miner> miners = getPlayerMiners(owner);

        for (Miner miner : miners) {
            if(miner.isPlaced()) amount++;
        }

        return amount;
    }

    public static int getRegisteredMinersAmount(Player owner){
        return getPlayerMiners(owner).size();
    }

    public static synchronized boolean hasUnplacedMiner(Player p){

        return getUnplacedMiner(p) != null;
    }

    public static Miner getUnplacedMiner(Player p){
        Miner unplaced = null;

        for (Miner miner : ownerMiners.getOrDefault(p.getUniqueId(), new HashSet<>())) {
            if(!miner.isPlaced()){
                unplaced = miner;
                break;
            }
        }

        return unplaced;
    }

    public static boolean canPlaceMiner(Player p){
        return hasUnplacedMiner(p);
    }

    public static boolean canCreateMiner(Player p){
        return getMinersAmount(p) > getRegisteredMinersAmount(p);
    }

    // GETTERS
    public static Set<Miner> getAllMiners(){
        return miners;
    }

    /**
     * Get a Set of miners
     *
     * @param owner Miner's owner
     *
     * @return empty HashSet as default or the Value if was defined.
     */
    public static Set<Miner> getPlayerMiners(Player owner){
        return ownerMiners.getOrDefault(owner.getUniqueId(), new HashSet<>());
    }

    /**
     *
     */
    public static Miner getMinerById(int id, Player owner){
        Miner found = null;

        for (Miner miner : getPlayerMiners(owner)) {
            if(miner.getId() == id){
                found = miner;
                break;
            }
        }

        return found;
    }

    /**
     * Get the number of miners the specific player
     *
     * @param player Target to know how many miners have.
     *
     * @return 0 as default or the Value if was defined.
     */
    public static byte getMinersAmount(Player player){
        return minersAmount.getOrDefault(player.getUniqueId(), (byte) 0);
    }
}
