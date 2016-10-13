package il.ac.shenkar.searchengine.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Seymore on 10/13/2016.
 */
public class Form extends JFrame {
    private JButton loadFileSButton;
    private JPanel rootPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel searchLabel;

    public Form() {
        super("search engine");

        setContentPane(rootPanel);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadFileSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // process file
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchLabel.setText("results for: " + searchField.getText());
                // search
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchLabel.setText("results for: " + searchField.getText());
                }
            }
        });

        setVisible(true);
    }
}
