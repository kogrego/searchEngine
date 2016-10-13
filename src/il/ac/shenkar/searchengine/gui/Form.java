package il.ac.shenkar.searchengine.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

public class Form extends JFrame {
    private JPanel rootPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton loadButton;
    private JPanel searchPanel;
    private JLabel searchLabel;
    private JLabel appName;
    private JPanel loadPanel;
    private JLabel selectedFilesLabel;

    public Form() {
        super("search engine");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./input"));
        fileChooser.setDialogTitle("Search Engine");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        setContentPane(rootPanel);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchLabel.setText("results for: " + searchField.getText());
                // search

            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.showOpenDialog(loadButton);
                File[] files = fileChooser.getSelectedFiles();
                String paths = "loaded files:";
                for(File file : files) {
                    String path = file.getPath();
                    paths += path + " ";

                }
                selectedFilesLabel.setText(paths);
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
