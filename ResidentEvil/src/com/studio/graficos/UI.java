package com.studio.graficos;

import com.studio.entities.Player;

import java.awt.*;

public class UI {
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(8, 8, 50, 8);
        g.setColor(Color.GREEN);
        g.fillRect(8, 8, (int)((Player.life/Player.maxLife)*50), 8);
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString((int)Player.life+"/"+(int)Player.maxLife, 20, 15);
    }
}
