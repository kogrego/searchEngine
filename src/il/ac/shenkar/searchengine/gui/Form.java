package il.ac.shenkar.searchengine.gui;

import il.ac.shenkar.searchengine.engine.Search;
import il.ac.shenkar.searchengine.storage.Indexer;
import il.ac.shenkar.searchengine.utils.Utils;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

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
    DefaultListModel<String> model;


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

        searchButton.addActionListener(e -> {
            searchLabel.setText("results for: " + searchField.getText());
            Search search = new Search();
            try {
                search.search(searchField.getText());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });

        loadButton.addActionListener(e -> {
            fileChooser.showOpenDialog(loadButton);
            File[] files = fileChooser.getSelectedFiles();
            String paths = "loaded files:";
            Indexer indexer = new Indexer();
            for(File file : files) {
                try {
                    File storedFile = Utils.storeFile(file);
                    indexer.index(storedFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                String path = file.getPath();
                paths += path + " ";
            }
            selectedFilesLabel.setText(paths);
        });

//        hideFilesSButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                fileChooser.showOpenDialog(loadButton);
//                File[] files = fileChooser.getSelectedFiles();
//                Indexer indexer = new Indexer();
//                for(File file : files) {
//                    indexer.remove(file.getName());
//                }
//            }
//        });

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
