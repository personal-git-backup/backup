package br.com.gmfonseca.entities;

import br.com.gmfonseca.main.Game;
import br.com.gmfonseca.main.Sound;
import br.com.gmfonseca.util.Camera;
import br.com.gmfonseca.util.GameState;
import br.com.gmfonseca.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    private static final int RIGHT_DIRECTION = 0;
    private static final int LEFT_DIRECTION = 1;
    private static final int UP_DIRECTION = 2;
    private static final int DOWN_DIRECTION = 3;

    public boolean up = false;
    public boolean down = false;
    public boolean right = false;
    public boolean left = false;
    public boolean k_shooting = false;
    public boolean m_shooting = false;
    public int mouseX;
    public int mouseY;
    public boolean jump = false;
    public boolean isJumping = false;
    public boolean jumpUp = false;
    public boolean jumpDown = false;
    public int jumpFrames = 0;
    public int jumpCur = 0;
    public int jumpSpeed = 2;
    private boolean moved = false;
    private boolean damaged = false;
    private boolean armed = false;
    private int direction = RIGHT_DIRECTION;
    private double speed = 1;
    private int frames = 0;
    private int maxFrames = 10;
    private int index = 0;
    private int maxIndex = 3;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage damagedPlayer;
    private int life = 100;
    private int ammo = 0;
    private int z = 0;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, 1, sprite);

        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        damagedPlayer = Game.sheet.getSprite(16, 16, 16, 16);

        for (int i = 0; i < rightPlayer.length; i++) {
            rightPlayer[i] = Game.sheet.getSprite(32 + (i * 16), 0, 16, 16);
            leftPlayer[i] = Game.sheet.getSprite(32 + (i * 16), 16, 16, 16);
        }

    }

    @Override
    public void render(Graphics g) {
        if (!damaged) {
            animate(g);
        } else {
            damaged = false;
            g.drawImage(damagedPlayer, (getX() - Camera.x), (getY() - Camera.y - z), null);
        }
    }

    private void animate(Graphics g) {
        if (direction == RIGHT_DIRECTION) {
            g.drawImage(rightPlayer[index], (getX() - Camera.x), (getY() - Camera.y - z), null);
            if (armed) {
                g.drawImage(Entity.WEAPON_RIGHT, (getX() - Camera.x) + 8, (getY() - Camera.y - z) - 3, null);
            }
        } else if (direction == LEFT_DIRECTION) {
            g.drawImage(leftPlayer[index], (getX() - Camera.x), (getY() - Camera.y), null);
            if (armed) {
                g.drawImage(Entity.WEAPON_LEFT, (getX() - Camera.x) - 8, (getY() - Camera.y - z) - 3, null);
            }
        }
    }

    @Override
    public void tick() {
        if (jump && !isJumping) {
            jump = false;
            isJumping = true;
            jumpUp = true;
            jumpDown = false;

        }

        if (isJumping) {
            if (jumpUp) {
                jumpCur += jumpSpeed;
            } else if (jumpDown) {
                jumpCur -= jumpSpeed;
                if (jumpCur <= 0) {
                    isJumping = false;
                    jumpUp = false;
                    jumpDown = false;
                }
            }
            z = jumpCur;

            if (jumpFrames <= jumpCur) {
                jumpUp = false;
                jumpDown = true;
            }
        }

        moved = false;


        move();

        if (moved) {
            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex) {
                    index = 0;
                }
            }
        }

        checkCollisionLifePack();
        checkCollisionAmmo();
        checkCollisionWeapon();

        if (k_shooting) {
            shoot();
        }
        if (m_shooting) {
            m_shoot();
        }

        updateCamera();
    }

    public void updateCamera() {
        Camera.x = Camera.clamp((getX() - (Game.WIDTH / 2)), 0, World.WIDTH * 16 - Game.WIDTH);
        Camera.y = Camera.clamp((getY() - (Game.HEIGHT / 2)), 0, World.HEIGHT * 16 - Game.HEIGHT);
    }

    private void move() {
        if (right && World.isFree((int) (this.getX() + speed), getY())) {
            increaseX(speed);
            moved = true;
            direction = RIGHT_DIRECTION;
        } else if (left && World.isFree((int) (this.getX() - speed), getY())) {
            decreaseX(speed);
            moved = true;
            direction = LEFT_DIRECTION;
        }

        if (up && World.isFree((this.getX()), (int) (getY() - speed))) {
            decreaseY(speed);
            moved = true;
        } else if (down && World.isFree((this.getX()), (int) (getY() + speed))) {
            increaseY(speed);
            moved = true;
        }
    }

    private void shoot() {
        k_shooting = false;
        if (hasAmmo()) {
            decreaseAmmo(1);

            int dx,
                    px,
                    py = 7;


            if (direction == RIGHT_DIRECTION) {
                px = 16;
                dx = 1;
            } else {
                px = -4;
                dx = -1;
            }

            Shoot shoot = new Shoot(getX() + px, getY() + py, 3, 3, null, dx, 0);
            Game.shoots.add(shoot);
        }
    }

    private void m_shoot() {
        m_shooting = false;

        if (hasAmmo()) {
            decreaseAmmo(1);

            double angle;
            int px,
                    py = 8;

            if (direction == RIGHT_DIRECTION) {
                px = 18;
                angle = Math.atan2(mouseY - (getY() + py - Camera.y), mouseX - (getX() + px - Camera.x));
            } else {
                px = -8;
                angle = Math.atan2(mouseY - (getY() + py - Camera.y), mouseX - (getX() - px - Camera.x));
            }

            double dx = Math.cos(angle),
                    dy = Math.sin(angle);

            Shoot shoot = new Shoot(getX() + px, getY() + py, 3, 3, null, dx, dy);
            Game.shoots.add(shoot);
        }
    }

    public void checkCollisionLifePack() {

        for (int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);

            if (atual instanceof LifePack) {
                if (Entity.isColliding(this, atual)) {
                    increaseLife(10);
                    synchronized (Game.entities) {
                        Game.entities.remove(atual);
                    }
                    i--;
                }
            }
        }

    }

    public void checkCollisionAmmo() {

        for (int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);

            if (atual instanceof Bullet) {
                if (Entity.isColliding(this, atual)) {
                    increaseAmmo(10 * Game.CURRENT_LEVEL);
                    Game.entities.remove(atual);
                    i--;
                }
            }
        }

    }

    public void checkCollisionWeapon() {

        for (int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);

            if (atual instanceof Weapon) {
                if (Entity.isColliding(this, atual)) {
                    armed = true;
                    Game.entities.remove(atual);
                    i--;
                }
            }
        }

    }

    public void increaseLife(int x) {
        if (life < 100) {

            if (life + x <= 100)
                life += x;
            else
                life = 100;
        }
    }

    public void decreaseLife(int x) {
        damaged = true;
        Sound.hurtEffect.play();
        if (life - x <= 0) {
            life = 0;
            Game.gameState = GameState.GAME_OVER;
        } else {
            life -= x;
        }
    }

    public void increaseAmmo(int x) {
        ammo += x;
    }

    public void decreaseAmmo(int x) {
        if (ammo > 0) {
            if (ammo - x >= 0)
                ammo -= x;
            else ammo = 0;
        }
    }

    public double getLife() {
        return life;
    }

    public int getAmmo() {
        return ammo;
    }

    public boolean isArmed() {
        return armed;
    }

    public boolean hasAmmo() {
        return ammo > 0;
    }
}
