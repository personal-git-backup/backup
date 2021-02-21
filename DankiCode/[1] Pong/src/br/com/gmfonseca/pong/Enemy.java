package br.com.gmfonseca.pong;

import java.awt.*;

public class Enemy implements Entity {

    public double x;
    public double y;
    public int width;
    public int height;

    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
        this.width = 40;
        this.height = 5;
    }

    public void tick(){
//        if()
        x += (Game.ball.x - x) * 0.8;

    }

    public void render(Graphics g){
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, width, height);
    }

}
