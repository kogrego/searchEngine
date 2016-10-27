package il.ac.shenkar.searchengine.gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import il.ac.shenkar.searchengine.engine.Search;
import il.ac.shenkar.searchengine.storage.Indexer;
import il.ac.shenkar.searchengine.utils.Utils;

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
    private JPanel toolBar;
    private JTabbedPane tabbedPane;
    DefaultListModel<String> model;
    private ArrayList<String> searchTerms;
    private DefaultListModel listModel;
    private CenteredFileChooser fileChooser;

    @SuppressWarnings({"unchecked", "BoundFieldAssignment"})
    public Form() {
        super("search engine");
        initComponents();
        Search search = new Search();
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
            ArrayList<String> results = search.search(searchField.getText(), searchTerms);
            showResults(results);
        });

        searchResults.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) {
                Map<String, Map<String,ArrayList<Integer>>> doc = null;
                try {
                    doc = search.showDocument(searchResults.getSelectedValue().toString(), searchTerms);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (doc != null) {
                    doc.forEach((text, wordData) -> {
                        showDocument.setText(text);
                        highlight(wordData);
                    });
                    docScrollPanel.setBorder(BorderFactory.createEmptyBorder());
                    docPanel.setVisible(true);
                    this.setContentPane(docPanel);
                }
                searchTerms.clear();
            }
        });

        backButton.addActionListener(e -> {
            this.setContentPane(rootPanel);
            docPanel.setVisible(false);
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
                    Utils.saveMapToFile();
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

    }

    private void initComponents() {
        this.setSize(1024, 720);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);

        resultsScrollPanel.setBorder(BorderFactory.createEmptyBorder());

        fileChooser = new CenteredFileChooser();
        fileChooser.setCurrentDirectory(new File("./input"));
        fileChooser.setDialogTitle("Search Engine");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        setContentPane(rootPanel);
        setVisible(true);
    }

    private void showResults(ArrayList<String> results) {
        searchResults.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        results.forEach(listModel::addElement);
        searchResults.setModel(listModel);
    }

    private void highlight(Map<String,ArrayList<Integer>> wordData) {
        Highlighter highlighter = showDocument.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
        wordData.forEach((word, loc) -> loc.forEach((num)->{
            try {
                highlighter.addHighlight(num, num + word.length(), painter );
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }));
    }

}
