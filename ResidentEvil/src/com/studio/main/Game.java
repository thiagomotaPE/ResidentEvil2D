package com.studio.main;

import com.studio.entities.BulletShoot;
import com.studio.entities.Enemy;
import com.studio.entities.Entity;
import com.studio.entities.Player;
import com.studio.graficos.Spritesheet;
import com.studio.graficos.UI;
import com.studio.world.Camera;
import com.studio.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements  Runnable, KeyListener, MouseListener {

    private Thread thread;
    private boolean isRunning = true;
    public static JFrame frame;
    private int CUR_LEVEL = 1, MAX_LEVEL = 2;
    private BufferedImage image;

    public static final int WIDTH = 240; //240
    public static final int HEIGHT = 160; //160
    public static final int SCALE = 3;

    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<BulletShoot> bullets;
    public static Spritesheet spritesheet;
    public static World world;
    public static Player player;
    public static Random rand;
    public UI ui;

    public Game() {
        rand = new Random();
        addKeyListener(this);
        addMouseListener(this);
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
        ui = new UI();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        bullets = new ArrayList<BulletShoot>();
        spritesheet = new Spritesheet("/spritesheet.png");
        player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
        entities.add(player);
        world = new World("/level1.png");
    }

    public void initFrame() {
        frame = new JFrame("RESIDENT EVIL 2D");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    public void tick() {
        //logica do game
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.tick();
        }
        for(int i = 0; i < bullets.size(); i++) {
            bullets.get(i).tick();
        }
        if(enemies.size() == 0) {
            //proximo level
            CUR_LEVEL++;
            if(CUR_LEVEL > MAX_LEVEL) {
                CUR_LEVEL = 1;
            }
            String newWorld = "level" + CUR_LEVEL + ".png";
            World.restartGame(newWorld);
        }
    }
    public void render() {
        //graficos do game
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        world.render(g);

        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.render(g);
        }
        for(int i = 0; i < bullets.size(); i++) {
            bullets.get(i).render(g);
        }
        ui.render(g);

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
        g.setFont(new Font("arial", Font.BOLD, 17));
        g.setColor(Color.WHITE);
        g.drawString("Munição: " + player.ammo, 610, 40);
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();

        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1) {
                tick();
                render();
                frames++;
                delta--;
            }
            if(System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }
        stop();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT ||e.getKeyCode() == KeyEvent.VK_D) {
            player.right = true;
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT ||e.getKeyCode() == KeyEvent.VK_A) {
            player.left = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_UP ||e.getKeyCode() == KeyEvent.VK_W) {
            player.up = true;
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN ||e.getKeyCode() == KeyEvent.VK_S) {
            player.down = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.shoot = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT ||e.getKeyCode() == KeyEvent.VK_D) {
            player.right = false;
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT ||e.getKeyCode() == KeyEvent.VK_A) {
            player.left = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_UP ||e.getKeyCode() == KeyEvent.VK_W) {
            player.up = false;
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN ||e.getKeyCode() == KeyEvent.VK_S) {
            player.down = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.mouseShoot = true;
        player.mx = e.getX() / 3;
        player.my = e.getY() / 3;
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