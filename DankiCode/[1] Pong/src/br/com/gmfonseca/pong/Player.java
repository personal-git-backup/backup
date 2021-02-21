package br.com.gmfonseca.pong;

import java.awt.*;

public class Player implements Entity {

    public boolean right=false;
    public boolean left=false;
    public int x;
    public int y;
    public int width;
    public int height;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 40;
        this.height = 5;
    }

    public void tick(){
        if(right){
            if(x+width < Game.WIDTH) x++;
        } else if (left) {
            if(x >= 0) x--;
        }
    }

    public void render(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

}
