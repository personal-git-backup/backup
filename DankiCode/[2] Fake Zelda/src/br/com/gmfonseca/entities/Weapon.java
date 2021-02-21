package br.com.gmfonseca.entities;

import java.awt.image.BufferedImage;

public class Weapon extends Entity {

    public Weapon(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, 3, sprite);
    }

    @Override
    public void tick() {

    }
}
