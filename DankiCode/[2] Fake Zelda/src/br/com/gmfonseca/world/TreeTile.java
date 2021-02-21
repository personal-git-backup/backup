package br.com.gmfonseca.world;

import br.com.gmfonseca.util.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TreeTile  extends Tile {

    public TreeTile(int x, int y, BufferedImage sprite) {
        super(x, y, sprite);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.green);
        super.render(g);

    }
}
