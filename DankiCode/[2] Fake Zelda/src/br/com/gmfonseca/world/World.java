package br.com.gmfonseca.world;

import br.com.gmfonseca.entities.*;
import br.com.gmfonseca.main.Game;
import br.com.gmfonseca.util.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class World {

    public static final int TILE_SIZE = 16;
    public static Tile[] tiles;
    public static int WIDTH;
    public static int HEIGHT;

    public World(String path) {
        try {
            System.out.println(getClass());
            System.out.println(getClass().getResource(path).getPath());
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            tiles = new Tile[pixels.length];

            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();

            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

            int pixel, index;

            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    index = x + (y * WIDTH);

                    pixel = pixels[index];

                    if (pixel == 0xFF007F0E) {
                        tiles[index] = new TreeTile(x * 16, y * 16, Tile.TILE_TREE);
                    } else {
                        tiles[index] = new FloorTile(x * 16, y * 16, Tile.TILE_FLOOR);
                    }

                    if (pixel == 0xFF0026FF) {
                        //PLAYER
                        Game.player.setX(x * 16);
                        Game.player.setY(y * 16);
                        Game.player.setMask(4, 0, 8, 16);
                    } else if (pixel == 0xFFFF6A00) {
                        //ENEMIES
                        Enemy enemy = new Enemy(x * 16, y * 16, 16, 16, Entity.ENEMY);
                        enemy.setMask(4, 3, 8, 11);
                        Game.entities.add(enemy);
                        Game.enemies.add(enemy);
                    } else if (pixel == 0xFFFF0000) {
                        //LIFEPACK
                        LifePack lifepack = new LifePack(x * 16, y * 16, 16, 16, Entity.LIFEPACK);
                        lifepack.setMask(4, 9, 9, 7);
                        Game.entities.add(lifepack);
                    } else if (pixel == 0xFF404040) {
                        //BULLET
                        Bullet bullet = new Bullet(x * 16, y * 16, 16, 16, Entity.BULLET);
                        Game.entities.add(bullet);
                    } else if (pixel == 0xFF7F3300) {
                        //GUN
                        Weapon weapon = new Weapon(x * 16, y * 16, 16, 16, Entity.WEAPON_RIGHT);
                        Game.entities.add(weapon);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFree(int x, int y, int tile_size) {
        boolean occuped = false;
        int[] positions = new int[2 * 4];
        positions[0] = x / tile_size;
        positions[1] = y / tile_size;

        positions[2] = (x + tile_size - 1) / tile_size;
        positions[3] = y / tile_size;

        positions[4] = x / tile_size;
        positions[5] = (y + tile_size - 1) / tile_size;

        positions[6] = (x + tile_size - 1) / tile_size;
        positions[7] = (y + tile_size - 1) / tile_size;

        for (int i = 0; i < positions.length; i += 2) {
            occuped = occuped || (tiles[positions[i] + (positions[i + 1] * World.WIDTH)]) instanceof TreeTile;
        }

        return !occuped;
    }

    public static boolean isFree(int x, int y) {
        return isFree(x, y, TILE_SIZE);
    }

    public static void renderMiniMap() {
        List<Enemy> enemies = Game.enemies;

        for (int i = 0; i < Game.miniMapPixels.length; i++) {
            Game.miniMapPixels[i] = 0;
        }

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int pos = x + (y * WIDTH);
                if (tiles[pos] instanceof TreeTile) {
                    Game.miniMapPixels[pos] = 0x00ff00;
                }
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            Game.miniMapPixels[(enemy.getY() / 16) + ((enemy.getY() / 16) * WIDTH)] = 0xff0000;
        }

        int xPlayer = Game.player.getX() / 16;
        int yPlayer = Game.player.getY() / 16;

        Game.miniMapPixels[xPlayer + (yPlayer * WIDTH)] = 0x0000ff;
    }

    public static boolean isColliding(Tile tile, Entity entity) {
        Rectangle tileMask = new Rectangle(tile.getX(), tile.getY(), TILE_SIZE, TILE_SIZE);
        Rectangle entityMask = new Rectangle(entity.getX() + entity.getMaskX(), entity.getY() + entity.getMaskY(), entity.getMaskWidth(), entity.getMaskHeight());

        return tileMask.intersects(entityMask);
    }

    public void render(Graphics g) {
        int xstart = Camera.x >> 4;
        int ystart = Camera.y >> 4;

        int xfinal = xstart + (Game.WIDTH >> 4);
        int yfinal = ystart + (Game.WIDTH >> 4);

        for (int x = xstart; x <= xfinal; x++) {
            for (int y = ystart; y <= yfinal; y++) {
                if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
                    continue;
                }
                Tile tile = tiles[x + (y * WIDTH)];
                tile.render(g);
            }
        }
    }
}
