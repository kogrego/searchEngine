package il.ac.shenkar.searchengine.serach.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import il.ac.shenkar.searchengine.serach.Search;
import il.ac.shenkar.searchengine.utils.Doc;
import il.ac.shenkar.searchengine.utils.Utils;

public class Form extends JFrame implements ListSelectionListener {
    private JPanel rootPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel searchPanel;
    private JLabel searchLabel;
    private JLabel appName;
    private JList searchResults;
    private JTextArea showDocument;
    private JPanel docPanel;
    private JButton printButton;
    private JButton backButton;
    private JScrollPane docScrollPanel;
    private JScrollPane resultsScrollPanel;
    private JPanel toolBar;
    private JLabel resultsLabel;
    private JTabbedPane tabbedPane;
    DefaultListModel<String> model;
    private ArrayList<String> searchTerms;
    private DefaultListModel listModel;
    private ArrayList<String> results;
    private Search search;

    @SuppressWarnings({"unchecked", "BoundFieldAssignment"})
    public Form() {
        super("search engine");
        initComponents();
        search = new Search();
        searchTerms = new ArrayList<>();
        results = new ArrayList<>();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        searchButton.addActionListener(e -> {
            searchTerms.clear();
            results.clear();
            results = search.search(searchField.getText(), searchTerms);
            searchResults.addListSelectionListener(this);
            searchResults.removeAll();
            if (results.isEmpty()) {
                noResults();
            } else {
                showResults(results);
            }
        });

        backButton.addActionListener(e -> {
            this.setContentPane(rootPanel);
            searchResults.clearSelection();
            docPanel.setVisible(false);
            if (results.isEmpty()) {
                noResults();
            } else {
                showResults(results);
            }
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

        setContentPane(rootPanel);
        setVisible(true);
    }

    private void showResults(ArrayList<String> results) {
        resultsLabel.setText("Search Results:\n");
        DefaultListModel listModel = new DefaultListModel();
        results.forEach((result)-> {
            Doc doc = Utils.getPostingMap().get(result);
            listModel.addElement(doc.getFileName());
        });
        searchResults.setModel(listModel);
        searchResults.addListSelectionListener(this);
    }

    private void noResults() {
        searchResults.setModel(new DefaultListModel());
        resultsLabel.setText("no results");
        searchResults.removeListSelectionListener(this);
    }

    private void highlight(Map<String, ArrayList<Integer>> wordData) {
        Highlighter highlighter = showDocument.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
        wordData.forEach((word, loc) -> loc.forEach((num) -> {
            try {
                highlighter.addHighlight(num, num + word.length(), painter);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }));
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            Map<String, Map<String, ArrayList<Integer>>> doc = null;
            try {
                doc = search.showDocument(searchResults.getSelectedValue().toString(), searchTerms);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            searchResults.removeListSelectionListener(this);
            if (doc != null) {
                doc.forEach((text, wordData) -> {
                    showDocument.setText(text);
                    highlight(wordData);
                });
                docScrollPanel.setBorder(BorderFactory.createEmptyBorder());
                docPanel.setVisible(true);
                this.setContentPane(docPanel);
            }
        }
    }
}
