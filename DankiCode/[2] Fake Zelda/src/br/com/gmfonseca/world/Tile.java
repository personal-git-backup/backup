package br.com.gmfonseca.world;

import br.com.gmfonseca.main.Game;
import br.com.gmfonseca.util.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Tile {

    public static BufferedImage TILE_FLOOR = Game.sheet.getSprite(0, 0, 16, 16);
    public static BufferedImage TILE_WALL = Game.sheet.getSprite(16, 0, 16, 16);
    public static BufferedImage TILE_TREE = Game.sheet.getSprite(0, 16, 16, 16);

    private BufferedImage sprite;
    private int x;
    private int y;

    public Tile(int x, int y, BufferedImage sprite) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g){
        g.drawImage(sprite, (x - Camera.x), (y - Camera.y), null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
