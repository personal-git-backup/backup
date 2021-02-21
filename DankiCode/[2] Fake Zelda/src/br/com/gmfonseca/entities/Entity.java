package br.com.gmfonseca.entities;

import br.com.gmfonseca.main.Game;
import br.com.gmfonseca.util.Camera;
import br.com.gmfonseca.world.Node;
import br.com.gmfonseca.world.Vector2i;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public abstract class Entity {

    public static final BufferedImage ENEMY = Game.sheet.getSprite(32, 32, 16, 16);
    public static final BufferedImage LIFEPACK = Game.sheet.getSprite(6 * 16, 0, 16, 16);
    public static final BufferedImage WEAPON_RIGHT = Game.sheet.getSprite(7 * 16, 0, 16, 16);
    public static final BufferedImage WEAPON_LEFT = Game.sheet.getSprite(8 * 16, 0, 16, 16);
    public static final BufferedImage BULLET = Game.sheet.getSprite(6 * 16, 16, 16, 16);
    public static Comparator<Entity> entitiesSorter = (o1, o2) -> Integer.compare(o2.depth, o1.depth);
    protected java.util.List<Node> path;
    private int depth;
    private double x;
    private double y;
    private int width;
    private int height;
    private int maskX;
    private int maskY;
    private int maskWidth;
    private int maskHeight;
    private BufferedImage sprite;

    public Entity(double x, double y, int width, int height, int depth, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.sprite = sprite;

        maskX = 0;
        maskY = 0;
        maskWidth = width;
        maskHeight = height;
    }

    public static boolean isColliding(Entity e1, Entity e2) {
        Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.maskWidth, e1.maskHeight);
        Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.maskWidth, e2.maskHeight);

        return e1Mask.intersects(e2Mask);
    }

    public void followPath(java.util.List<Node> path) {
        if (path != null) {
            if (!path.isEmpty()) {
                int lastIndex = path.size() - 1;
                Vector2i target = path.get(lastIndex).tile;

                if (x < target.x * 16) {
                    x++;
                } else if (x > target.x * 16) {
                    x--;
                }

                if (y < target.y * 16) {
                    y++;
                } else if (y > target.y * 16) {
                    y--;
                }

                if (x == target.x * 16 && y == target.y * 16) {
                    path.remove(lastIndex);
                }
            }
        }
    }

    public void render(Graphics g) {
        g.drawImage(sprite, getX() - Camera.x, getY() - Camera.y, null);

//        g.setColor(Color.red);
//        g.fillRect(getX()+maskX-Camera.x, getY()+maskY-Camera.y, maskWidth, maskHeight);
    }

    public abstract void tick();

    public void setMask(int maskX, int maskY, int maskWidth, int maskHeight) {
        this.maskX = maskX;
        this.maskY = maskY;
        this.maskWidth = maskWidth;
        this.maskHeight = maskHeight;
    }

    protected double computeDistance(int x1, int y1, int x2, int y2) {
        return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2));
    }

    public int getX() {
        return (int) x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void increaseX(double n) {
        setX(x + n);
    }

    public void decreaseX(double n) {
        setX(x - n);
    }

    public int getY() {
        return (int) y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void increaseY(double n) {
        setY(y + n);
    }

    public void decreaseY(double n) {
        setY(y - n);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaskX() {
        return maskX;
    }

    public int getMaskY() {
        return maskY;
    }

    public int getMaskWidth() {
        return maskWidth;
    }

    public int getMaskHeight() {
        return maskHeight;
    }

    public int getDepth() {
        return depth;
    }
}
