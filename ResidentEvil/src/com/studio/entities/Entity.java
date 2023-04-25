package com.studio.entities;

import com.studio.main.Game;
import com.studio.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
    public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16);
    public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16);
    public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*16, 16, 16, 16);
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected BufferedImage sprite;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public void tick() {

    }
    public void render(Graphics g) {
        g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
    }

    public int getX() {
        return (int)x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getY() {
        return (int)y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
