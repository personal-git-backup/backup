package br.com.gmfonseca.mine.autominer.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Util {

    public static ItemStack getCustomHead(String skull_owner){

        ItemStack custom_head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) custom_head.getItemMeta();
        skull.setOwner(skull_owner);
        custom_head.setItemMeta(skull);

        return custom_head;
    }

    public static Direction getDirection(Location loc){

        Direction direction;

        double rotation = (loc.getYaw() - 90.0F) % 360.0F;

        if (rotation < 0.0D) {
            rotation += 360.0D;
        }

        if(rotation >= 22.5 && rotation < 67.5){
            direction = Direction.NORTH_WEST;
        }else if(rotation >= 67.5 && rotation < 112.5){
            direction = Direction.NORTH;
        }else if(rotation >= 112.5 && rotation < 157.5){
            direction = Direction.NORTH_EAST;
        }else if(rotation >= 157.5 && rotation < 202.5){
            direction = Direction.EAST;
        }else if(rotation >= 202.5 && rotation < 247.5){
            direction = Direction.SOUTH_EAST;
        }else if(rotation >= 247.5 && rotation < 292.5){
            direction = Direction.SOUTH;
        }else if(rotation >= 292.5 && rotation < 337.5){
            direction = Direction.SOUTH_WEST;
        }else {
            direction = Direction.WEST;
        }

        return direction;
    }

    public static boolean isInventoryFull(Inventory inventory, Material[] materials){
        boolean fully = true;

        try {
            if (inventory != null && inventory.getContents() != null) {
                List<ItemStack> items = Arrays.asList(inventory.getContents());
                Set<Material> filter = arrayToSet(materials);

                if (!items.contains(null)) {
                    for (ItemStack item : items) {
                        if (item == null || (filter.contains(item.getType()) && item.getAmount() < item.getMaxStackSize())) {
                            fully = false;
                            break;
                        }
                    }
                } else {
                    fully = false;
                }
            }
        }catch (Exception err){
            Bukkit.getConsoleSender().sendMessage("§c|method  Util:isInventoryFull() -> " + err.getMessage());
        }

        return fully;
    }

    public static boolean isInventoryEmpty(Inventory inventory){
        boolean empty = true;

        try {
            if (inventory != null && inventory.getContents() != null) {
                Set<ItemStack> items = arrayToSet(inventory.getContents());

                for (ItemStack item : items) {
                    if(item != null){
                        empty = false;
                        break;
                    }
                }
            }
        }catch (Exception err){
            Bukkit.getConsoleSender().sendMessage("§c|method  Util:isInventoryFull() -> " + err.getMessage());
        }

        return empty;
    }

    public static <T> Set<T> arrayToSet(T[] array){
        if(array == null) return new HashSet<>();

        return new HashSet<>(Arrays.asList(array));

    }

}
