package com.studio.entities;

import com.studio.graficos.Spritesheet;
import com.studio.main.Game;
import com.studio.world.Camera;
import com.studio.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity{
    public boolean right, up, left, down;
    public int right_dir = 0, left_dir = 1;
    public int dir = right_dir;
    public double speed = 1.2;
    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage playerDamage;
    private boolean hasGun = false;
    public int ammo = 5;
    public boolean isDamaged = false;
    private int damageFrames = 0;
    public boolean shoot, mouseShoot = false;
    public  double life = 100, maxLife = 100;
    public int mx, my;
    public Player(int x, int y, int width, int height, BufferedImage sprite) {

        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
        for( int i = 0; i < 4; i++){
            rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);

        }
        for( int i = 0; i < 4; i++){
            leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);

        }
    }

    public void tick() {
        moved = false;
        if(right && World.isFree((int)(x + speed), this.getY())) {
            moved = true;
            dir = right_dir;
            x += speed;
        }else if(left && World.isFree((int)(x - speed), this.getY())) {
            moved = true;
            dir = left_dir;
            x -= speed;
        }
        if(up  && World.isFree(this.getX(), (int)(y - speed))) {
            moved = true;
            y -= speed;
        }else if(down && World.isFree(this.getX(), (int)(y + speed))) {
            moved = true;
            y += speed;
        }

        if(moved) {
            frames++;
            if(frames == maxFrames) {
                frames = 0;
                index++;
                if(index > maxIndex) {
                    index = 0;
                }
            }
        }
        this.checkCollisionGun();
        this.checkCollisionAmmo();
        this.checkCollisionLife();

        if(isDamaged) {
            this.damageFrames++;
            if(this.damageFrames == 8) {
                this.damageFrames = 0;
                isDamaged = false;
            }
        }

        if(shoot) {
            shoot = false;
            if(hasGun && ammo > 0) {
                //criar bala e atirar
                ammo--;

                int dx = 0;
                int px = 0;
                int py = 7;
                if(dir == right_dir) {
                    px = 20;
                    dx = 1;
                }else {
                    px = -8;
                    dx = -1;
                }
                BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
                Game.bullets.add(bullet);
            }
        }

        if(mouseShoot) {
            mouseShoot = false;
            if(hasGun && ammo > 0) {
                //criar bala e atirar
                ammo--;


                int px = 0;
                int py = 7;
                double angle = 0;
                if(dir == right_dir) {
                    px = 20;
                    angle = Math.toDegrees(Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x)));
                }else {
                    px = -8;
                    angle = Math.toDegrees(Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x)));
                }
                double dx = Math.cos(angle);
                double dy = Math.sin(angle);

                BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
                Game.bullets.add(bullet);
            }
        }

        if(life <= 0) {
            //Gameover
        }
        Camera.x =Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH*16 - Game.WIDTH);
        Camera.y =Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT*16 - Game.HEIGHT);
    }

    public void checkCollisionGun() {
        for(int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);
            if(ammo < 12) {
                if(atual instanceof Weapon) {
                    if(Entity.isColidding(this, atual)) {
                        hasGun = true;
                        //System.out.println(hasGun);
                        Game.entities.remove(atual);
                    }
                }
            }
        }
    }
    public void checkCollisionAmmo() {
        for(int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);
            if(ammo < 20) {
                if(atual instanceof Bullet) {
                    if(Entity.isColidding(this, atual)) {
                        ammo += 5;
                        //System.out.println(ammo);
                        Game.entities.remove(atual);
                    }
                }
            }
        }
    }
    public void checkCollisionLife() {
        for(int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);
            if(life < 100) {
                if(atual instanceof Life) {
                    if(Entity.isColidding(this, atual)) {
                        life += 10;
                        Game.entities.remove(atual);
                        if(life > 100)
                            life = 100;
                    }
                }
            }

        }
    }
    public void render(Graphics g) {
        if(!isDamaged) {
            if(dir == right_dir) {
                g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if(hasGun) {
                    //desenhar arma para a direita
                    g.drawImage(Entity.GUN_RIGHT, this.getX() + 8 - Camera.x, this.getY() - Camera.y, null);
                }
            }else if(dir == left_dir) {
                g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if(hasGun) {
                    //desenhar arma para a esquerda
                    g.drawImage(Entity.GUN_LEFT, this.getX() - 8 - Camera.x, this.getY() - Camera.y, null);
                }
            }
        }else {
            g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
    }
}
