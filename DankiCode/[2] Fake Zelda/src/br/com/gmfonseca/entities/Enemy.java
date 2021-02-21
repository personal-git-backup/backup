package br.com.gmfonseca.entities;

import br.com.gmfonseca.main.Game;
import br.com.gmfonseca.util.Camera;
import br.com.gmfonseca.world.AStar;
import br.com.gmfonseca.world.Vector2i;
import br.com.gmfonseca.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private final int MAX_FRAMES = 10;
    private double speed = 1;
    private int frames = 0;
    private int index = 0;
    private int maxIndex = 3;
    private BufferedImage[] rightEnemy;
    private BufferedImage[] leftEnemy;
    private BufferedImage damagedEnemy;

    private int life = 10;
    private boolean damaged = false;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, 2, sprite);
        rightEnemy = new BufferedImage[4];
        leftEnemy = new BufferedImage[4];

        damagedEnemy = Game.sheet.getSprite(16, 32, 16, 16);

        for (int i = 0; i < rightEnemy.length; i++) {
            rightEnemy[i] = Game.sheet.getSprite(32 + (i * 16), 32, 16, 16);
            leftEnemy[i] = Game.sheet.getSprite(32 + (i * 16), 48, 16, 16);
        }
    }

    @Override
    public void tick() {
        if (computeDistance(getX(), getY(), Game.player.getX(), Game.player.getY()) < 100 && Game.rand.nextInt(100) > 30) {
            // collision system
            if (!isCollidingWithPlayer()) {
                if (path == null || path.isEmpty()) {
                    Vector2i start = new Vector2i((getX() / 16), (getY() / 16));
                    Vector2i end = new Vector2i((Game.player.getX() / 16), (Game.player.getY() / 16));
                    path = AStar.findPath(Game.world, start, end);
                }
                if (getX() < Game.player.getX() && World.isFree((int) (getX() + speed), getY()) && !isColliding((int) (getX() + speed), getY())) {
//                    increaseX(speed);
                } else if (getX() > Game.player.getX() && World.isFree((int) (getX() - speed), getY()) && !isColliding((int) (getX() - speed), getY())) {
//                    decreaseX(speed);
                }

                if (getY() < Game.player.getY() && World.isFree(getX(), (int) (getY() + speed)) && !isColliding(getX(), (int) (getY() + speed))) {
//                    increaseY(speed);
                } else if (getY() > Game.player.getY() && World.isFree(getX(), (int) (getY() - speed)) && !isColliding(getX(), (int) (getY() - speed))) {
//                    decreaseY(speed);
                }
            } else { //Colide com player
                if (Game.rand.nextInt(100) < 10) {
                    Game.player.decreaseLife(1);
                }
            }
        }

        if (Game.rand.nextInt(100) < 75) {
            followPath(path);

        }

        checkDamaged();

        frames++;
        if (frames == MAX_FRAMES) {
            frames = 0;
            index++;
            if (index > maxIndex) index = 0;
        }
    }

    public void checkDamaged() {
        for (int i = 0; i < Game.shoots.size(); i++) {

            if (isColliding(this, Game.shoots.get(i))) {
                decreaseLife(1);
                Game.shoots.remove(i);
                i--;
            }
        }
    }

    public boolean isCollidingWithPlayer() {
        Rectangle currentEnemy = new Rectangle(getX(), getY(), World.TILE_SIZE, World.TILE_SIZE);
        Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), World.TILE_SIZE, World.TILE_SIZE);

        return currentEnemy.intersects(player);
    }

    public boolean isColliding(int nextX, int nextY) {
        Rectangle currentEnemy = new Rectangle(nextX, nextY, World.TILE_SIZE, World.TILE_SIZE);

        for (Enemy e : Game.enemies) {
            if (e == this) continue;
            Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);

            if (currentEnemy.intersects(targetEnemy)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void render(Graphics g) {
        int x = getX() - Camera.x;
        int y = getY() - Camera.y;

        if (damaged) {
            damaged = false;
            g.drawImage(damagedEnemy, x, y, null);
        } else
            g.drawImage(rightEnemy[index], x, y, null);
    }

    public void decreaseLife(int x) {
        damaged = true;

        if (life - x <= 0) {
            life = 0;
            kill();
        } else {
            life -= x;
        }

    }

    public void kill() {
        if (life <= 0) {
            Game.entities.remove(this);
            Game.enemies.remove(this);
        }
    }
}