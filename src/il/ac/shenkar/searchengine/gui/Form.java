package il.ac.shenkar.searchengine.gui;

import javax.swing.*;

public class Form extends  JFrame {

    private JButton loadFileSButton;
    private JPanel panel;

    public Form() {
        super("Search Engine");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }
}
