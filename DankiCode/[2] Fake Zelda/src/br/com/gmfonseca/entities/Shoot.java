package br.com.gmfonseca.entities;

import br.com.gmfonseca.main.Game;
import br.com.gmfonseca.util.Camera;
import br.com.gmfonseca.world.FloorTile;
import br.com.gmfonseca.world.Tile;
import br.com.gmfonseca.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Shoot extends Entity {

    private double dx;
    private double dy;
    private double speed = 4;

    private int lifetime=30;
    private int currentLifeTime=0;

    public Shoot(double x, double y, int width, int height, BufferedImage sprite, double dx, double dy) {
        super(x, y, width, height, 3, sprite);
        this.dx = dx;
        this.dy = dy;
        this.setMask(1, 1, 2, 2);
    }

    @Override
    public void tick() {

        increaseX(dx*speed);
        increaseY(dy*speed);

        currentLifeTime++;

        if(currentLifeTime >= lifetime || checkCollisionTile()){
            Game.shoots.remove(this);
        }
    }

    private boolean checkCollisionTile(){
        boolean colliding = false;

        for (Tile tile : World.tiles) {
            if(!(tile instanceof FloorTile) && World.isColliding(tile, this)){
                colliding = true;
                break;
            }
        }

        return colliding;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(getX()- Camera.x, getY() - Camera.y, getWidth(), getHeight());
    }
}
