package br.com.gmfonseca.pong;

import java.awt.*;
import java.util.Random;

public class Ball implements Entity {

    public double x;
    public double y;
    public int width;
    public int height;

    public double dx;
    public double dy;

    public double speed = 1;

    public Ball(double x, double y) {
        this.x = x;
        this.y = y;
        this.width = 3;
        this.height = 3;

        int angle = new Random().nextInt(120-45)+46;

        this.dx = Math.cos(Math.toRadians(angle));
        this.dy = Math.sin(Math.toRadians(angle));
    }

    public void tick(){
        if((x + (dx*speed) + width) >= Game.WIDTH){
            dx *= -1;
        }else if(x + (dx*speed) < 0){
            dx *= -1;
        }

        if(y >= Game.HEIGHT){
            //PONTO DO INIMIGO
            new Game();
            return;
        }else if(y <= 0){
            //PONTO DO JOGADOR
            new Game();
            return;
        }

        Rectangle bounds = new Rectangle((int)(x+(dx*speed)), (int)(y+(dy*speed)), width, height);

        Rectangle boundsPlayer = new Rectangle(Game.player.x, Game.player.y, Game.player.width, Game.player.height);
        Rectangle boundsEnemy= new Rectangle((int)Game.enemy.x, (int)Game.enemy.y, Game.player.width, Game.player.height);

        if(bounds.intersects(boundsPlayer)){
            int angle = new Random().nextInt(120-45)+46;

            this.dx = Math.cos(Math.toRadians(angle));
            this.dy = Math.sin(Math.toRadians(angle));

            dy *= -1;

        }else if( bounds.intersects(boundsEnemy)){

            int angle = new Random().nextInt(120-45)-46;

            this.dx = Math.cos(Math.toRadians(angle));
            this.dy = Math.sin(Math.toRadians(angle));

            dy *= -1;
        }

        x += dx*speed;
        y += dy*speed;
    }

    public void render(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect((int) x, (int) y, width, height);
    }

}
