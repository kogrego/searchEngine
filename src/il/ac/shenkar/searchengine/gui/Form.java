package il.ac.shenkar.searchengine.gui;

import il.ac.shenkar.searchengine.engine.Search;
import il.ac.shenkar.searchengine.storage.Indexer;
import il.ac.shenkar.searchengine.utils.Utils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
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
    private JPanel docPanel;
    private JButton printButton;
    private JButton backButton;
    private JScrollPane docScrollPanel;
    private JScrollPane resultsScrollPanel;
    DefaultListModel<String> model;
    private ArrayList<String> searchTerms;


    @SuppressWarnings({"unchecked", "BoundFieldAssignment"})
    public Form() {
        super("search engine");
        this.setSize(1024, 720);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        docScrollPanel.setBorder(BorderFactory.createEmptyBorder());
        resultsScrollPanel.setBorder(BorderFactory.createEmptyBorder());
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
                Map<String, Map<String,ArrayList<Integer>>> doc = null;
                try {
                    doc = search.showDocument(searchResults.getSelectedValue().toString(), searchTerms);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (doc != null) {
                    doc.forEach((text, wordData) -> {
                        showDocument.setText(text);
                        Highlighter highlighter = showDocument.getHighlighter();
                        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
                        wordData.forEach((word, loc) -> loc.forEach((num)->{
                            try {
                                highlighter.addHighlight(num, num + word.length(), painter );
                            } catch (BadLocationException e1) {
                                e1.printStackTrace();
                            }
                        }));
                        docPanel.setVisible(true);
                        this.setContentPane(docPanel);
                        this.invalidate();
                        this.validate();
                    });
                }
                searchTerms.clear();
                System.out.print("done");
            }
        });

        backButton.addActionListener(e -> {
            this.setContentPane(rootPanel);
            this.invalidate();
            this.validate();
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
