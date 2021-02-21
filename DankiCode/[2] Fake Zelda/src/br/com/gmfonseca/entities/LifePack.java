package br.com.gmfonseca.entities;

import java.awt.image.BufferedImage;

public class LifePack extends Entity {

    public LifePack(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, 3, sprite);
    }

    @Override
    public void tick() {

    }


}
