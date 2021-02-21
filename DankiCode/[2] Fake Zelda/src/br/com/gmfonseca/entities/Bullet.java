package br.com.gmfonseca.entities;

import java.awt.image.BufferedImage;

public class Bullet extends Entity {

    public Bullet(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, 3, sprite);
    }

    @Override
    public void tick() {

    }
}
