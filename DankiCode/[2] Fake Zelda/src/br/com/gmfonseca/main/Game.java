package br.com.gmfonseca.main;

import br.com.gmfonseca.entities.Enemy;
import br.com.gmfonseca.entities.Entity;
import br.com.gmfonseca.entities.Player;
import br.com.gmfonseca.entities.Shoot;
import br.com.gmfonseca.graphics.Spritesheet;
import br.com.gmfonseca.graphics.UI;
import br.com.gmfonseca.util.Camera;
import br.com.gmfonseca.util.GameState;
import br.com.gmfonseca.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ARRUMAR GAME OVER
 */
public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

    public static final int WIDTH = 240;
    public static final int HEIGHT = 160;
    public static final int SCALE = 3;
    public static JFrame frame;
    public static World world;
    public static Spritesheet sheet;
    public static Menu menu;
    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<Shoot> shoots;
    public static Player player;
    public static GameState gameState = GameState.MENU;
    public static Random rand;
    public static int CURRENT_LEVEL = 1;
    public static BufferedImage miniMapImage;
    public static int[] miniMapPixels;
    public final int MAX_LEVEL = 2;
    private final BufferedImage image;
    public UI ui;
    private Thread thread;
    private boolean isRunning;
    private int framesGameOver;
    private boolean showMessageGameOver;

    public Game() {
        Sound.musicBackground.loop();
        rand = new Random();
        addKeyListener(this);
        addMouseListener(this);
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
        ui = new UI();
        menu = new Menu();

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        restart("/level1.png");
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    public static void restartGame() {
        CURRENT_LEVEL = 0;
        gameState = GameState.NORMAL;
        restart("/level1.png");
    }

    public static void restartLevel() {
        restart("/level" + CURRENT_LEVEL + ".png");
    }

    public static void restart(String world) {
        if (Game.entities != null) Game.entities.clear();
        Game.entities = new ArrayList<>();

        if (Game.enemies != null) Game.enemies.clear();
        Game.enemies = new ArrayList<>();

        if (Game.shoots != null) Game.shoots.clear();
        Game.shoots = new ArrayList<>();

        Game.sheet = new Spritesheet("/spritesheet.png");

        Game.player = new Player(0, 0, 16, 16, sheet.getSprite(32, 0, 16, 16));
        Game.entities.add(player);

        Game.world = new World(world);
        Game.miniMapImage = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Game.miniMapPixels = ((DataBufferInt) miniMapImage.getRaster().getDataBuffer()).getData();
    }

    public void initFrame() {
        frame = new JFrame("Game #1");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tick() {

        if (gameState == GameState.NORMAL) {

            if (enemies.isEmpty()) {
                CURRENT_LEVEL++;

                if (CURRENT_LEVEL > MAX_LEVEL) {
                    CURRENT_LEVEL = MAX_LEVEL;
                }

                restart("/level" + CURRENT_LEVEL + ".png");
            }

            for (int i = 0; i < entities.size(); i++) {
                entities.get(i).tick();
            }

            for (int i = 0; i < shoots.size(); i++) {
                shoots.get(i).tick();
            }

        } else if (gameState == GameState.GAME_OVER) {
            this.framesGameOver++;
            if (this.framesGameOver == 30) {
                this.framesGameOver = 0;

                showMessageGameOver = !showMessageGameOver;
            }
        } else if (gameState == GameState.MENU) {
            player.updateCamera();
            menu.tick();
        }

    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = image.getGraphics();
//        g.setColor(new Color(0, 0 ,0));
        g.setColor(new Color(255, 222, 173));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        /*
         * Renderização do jogo
         */
        world.render(g);

        /*
         * Renderização de entidades
         */

        entities.sort(Entity.entitiesSorter);

        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).render(g);
        }

        for (int i = 0; i < shoots.size(); i++) {
            shoots.get(i).render(g);
        }

        ui.render(g);
        /*          */
        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g.setFont(new Font("arial", Font.BOLD, 17));
        g.drawString("Munição: " + player.getAmmo(), 620, 16);

        if (gameState == GameState.GAME_OVER) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
            g.setFont(new Font("arial", Font.BOLD, 36));
            g.setColor(Color.white);
            g.drawString("Game Over", (WIDTH * SCALE) / 2 - 100, (HEIGHT * SCALE) / 2 - 20);
            g.setFont(new Font("arial", Font.BOLD, 28));
            if (showMessageGameOver)
                g.drawString("> Pressione ENTER para reiniciar <", (WIDTH * SCALE) / 2 - 260, (HEIGHT * SCALE) / 2 + 20);
        } else if (gameState == GameState.MENU) {
            menu.render(g);
        }

        World.renderMiniMap();
        g.drawImage(miniMapImage, 615, 25, World.WIDTH * 5, World.HEIGHT * 5, null);
        bs.show();
    }

    @Override
    public void run() {
        requestFocus();

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        long NANO_SECOND = 1000000000;
        double ns = NANO_SECOND / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                frames++;
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }

        stop();
    }

    // KEYBOARD LISTENERS
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gameState == GameState.NORMAL) {
            if (key == KeyEvent.VK_SPACE) {
                player.jump = true;
            }

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                player.right = true;
            } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                player.left = true;

            }

            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                player.up = true;
            } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                player.down = true;
            }

            if (key == KeyEvent.VK_X && player.isArmed()) {
                player.k_shooting = true;
            }
            if (key == KeyEvent.VK_ESCAPE) {
                gameState = GameState.MENU;
            }
        } else if (gameState == GameState.GAME_OVER) {
            if (key == KeyEvent.VK_ENTER)
                restartGame();
        } else if (gameState == GameState.MENU) {
            if (key == KeyEvent.VK_UP) {
                menu.up = true;
            } else if (key == KeyEvent.VK_DOWN) {
                menu.down = true;
            } else if (key == KeyEvent.VK_ENTER) {
                menu.enter = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (gameState == GameState.NORMAL) {
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                player.right = false;
            } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                player.left = false;
            }

            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                player.up = false;
            } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                player.down = false;
            }
        }
    }

// MOUSE LISTENERS

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && player.isArmed()) {
            player.mouseX = (e.getX() / 3) + Camera.x;
            player.mouseY = (e.getY() / 3) + Camera.y;
            player.m_shooting = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
