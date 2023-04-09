package com.studio.main;

import java.awt.*;
public class Game extends Canvas implements  Runnable {

    private boolean isRunning = true;
    private Thread thread;

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
    }
    public void render() {
        //graficos do game
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();

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
}