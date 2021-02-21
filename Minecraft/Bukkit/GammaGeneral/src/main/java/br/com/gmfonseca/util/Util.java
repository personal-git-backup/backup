package br.com.gmfonseca.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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

        if ((45.0D <= rotation) && (rotation < 135.0D)) {
            direction = Direction.NORTH;
        }else if ((135.0D <= rotation) && (rotation < 225.0D)) {
            direction = Direction.EAST;
        }else if ((225.0D <= rotation) && (rotation < 315.0D)) {
            direction = Direction.SOUTH;
        }else {
            direction = Direction.WEST;
        }
        return direction;
    }




}
