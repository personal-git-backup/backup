package br.com.gmfonseca.pong;

import java.awt.Graphics;

public interface Entity {

    void tick();

    void render(Graphics g);

}
