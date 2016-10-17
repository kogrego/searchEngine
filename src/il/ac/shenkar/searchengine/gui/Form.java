package il.ac.shenkar.searchengine.gui;

import il.ac.shenkar.searchengine.engine.Search;
import il.ac.shenkar.searchengine.storage.Indexer;
import il.ac.shenkar.searchengine.utils.Utils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private JList searchResults;
    private JTextArea showDocument;
    DefaultListModel<String> model;
    private ArrayList<String> searchTerms;


    @SuppressWarnings({"unchecked", "BoundFieldAssignment"})
    public Form() {
        super("search engine");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./input"));
        fileChooser.setDialogTitle("Search Engine");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        setContentPane(rootPanel);
        searchTerms = new ArrayList<>();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    Utils.saveMapToFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });

        searchButton.addActionListener(e -> {
            Search search = new Search();
            ArrayList<String> results = search.search(searchField.getText(), searchTerms);
            searchResults.removeAll();
            DefaultListModel listModel = new DefaultListModel();
            results.forEach(listModel::addElement);
            searchResults.setModel(listModel);
        });

        searchResults.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) {
                Search search = new Search();
                Map<String, ArrayList<Integer>> doc = null;
                try {
                    doc = search.showDocument(searchResults.getSelectedValue().toString(), searchTerms);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (doc != null) {
                    doc.forEach((text, locations) -> {
                        showDocument.setText(text);
                    });
                }
                searchTerms.clear();
                System.out.print("done");
            }
        });

        loadButton.addActionListener(e -> {
            fileChooser.showOpenDialog(loadButton);
            File[] files = fileChooser.getSelectedFiles();
            String paths = "loaded files:";
            Indexer indexer = new Indexer();
            for (File file : files) {
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



        setVisible(true);
    }

}
