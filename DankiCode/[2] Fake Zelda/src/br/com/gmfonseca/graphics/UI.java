package br.com.gmfonseca.graphics;

import br.com.gmfonseca.main.Game;

import java.awt.*;

public class UI {

    public void render(Graphics g){
        int x=10, y=8;
        int height=10;
        int width = 70;
        g.setColor(Color.gray);
        g.fillRect(x, y, width, height);
        if(Game.player.getLife() > 20){
            g.setColor(Color.green);
        }else{
            g.setColor(Color.red);
        }
        g.fillRect(x, y, (int)(Game.player.getLife()/100*width), height);
        g.setColor(Color.white);
        g.setFont(new Font("AvantGarde", Font.PLAIN, height));
        g.drawString("" + (int)Game.player.getLife(), (width+x)/2,y*2);
    }

}
