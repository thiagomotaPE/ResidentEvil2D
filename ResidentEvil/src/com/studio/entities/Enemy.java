package com.studio.entities;

import com.studio.main.Game;
import com.studio.world.Camera;
import com.studio.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity{
    private double speed = 0.6;
    private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;
    private int maskx = 8, masky = 8, maskw = 10, maskh = 10;
    private BufferedImage[] sprites;
    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {

        super(x, y, width, height, sprite);
        sprites = new BufferedImage[4];
        sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
        sprites[1] = Game.spritesheet.getSprite(112 + 16, 16, 16, 16);
        sprites[2] = Game.spritesheet.getSprite(112 + 32, 16, 16, 16);
        sprites[3] = Game.spritesheet.getSprite(112 + 32, 32, 16, 16);



    }

    public void tick() {
        if(isColiddingWithPlayer() == false) {
            if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
                    && !isColidding((int) (x + speed), this.getY()))
                x += speed;
            if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
                    && !isColidding((int) (x - speed), this.getY()))
                x -= speed;

            if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed))
                    && !isColidding(this.getX(), (int) (y + speed)))
                y += speed;
            if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
                    && !isColidding(this.getX(), (int) (y - speed)))
                y -= speed;
        }else {
            //estamos colidindo
            if(Game.rand.nextInt(100) < 10) {
                Game.player.life-= Game.rand.nextInt(3);
                Game.player.isDamaged = true;
                if(Game.player.life <= 0 ) {
                    //Game over
                    System.exit(1);
                }
                System.out.println("Vida: " + Game.player.life);
            }
        }

            frames++;
            if(frames == maxFrames) {
                frames = 0;
                index++;
                if(index > maxIndex) {
                    index = 0;
                }
            }
    }

    public boolean isColiddingWithPlayer() {
        Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
        Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

        return enemyCurrent.intersects(player);
    }
    public boolean isColidding(int xnext, int ynext) {
        Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
        for(int i = 0; i < Game.enemies.size(); i++){
            Enemy e = Game.enemies.get(i);
            if(e == this)
                continue;
            Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
            if(enemyCurrent.intersects(targetEnemy)) {
                return true;
            }
        }
        return false;
    }

    public void render(Graphics g) {
        g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
//        super.render(g);
//        g.setColor(Color.BLUE);
//        g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);

    }
}

