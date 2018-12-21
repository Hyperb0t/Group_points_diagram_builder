package ru.kpfu.itis.hyperbot.diagrams;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DiagramWindow extends JFrame {
    private JPanel panel;
    public DiagramWindow(BufferedImage image) {
        super("Diagram");
        this.setBounds(0,0, image.getWidth(), image.getHeight());
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setBackground(Color.GRAY);
        this.getContentPane().add(panel);
        while(true) {
            Graphics g = panel.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
        }
    }

}
