package com.studio.entities;

import com.studio.main.Game;
import com.studio.world.World;

import java.awt.image.BufferedImage;

public class Enemy extends Entity{
    private double speed = 0.4;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void tick() {
        if((int)x < Game.player.getX() && World.isFree((int)(x + speed), this.getY()))
            x += speed;
        if((int)x > Game.player.getX() && World.isFree((int)(x - speed), this.getY()))
            x -= speed;

        if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y + speed)))
            y += speed;
        if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y - speed)))
            y -= speed;

    }
}
