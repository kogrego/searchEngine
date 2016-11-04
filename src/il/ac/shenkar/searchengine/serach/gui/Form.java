package il.ac.shenkar.searchengine.serach.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;

import il.ac.shenkar.searchengine.serach.Search;
import il.ac.shenkar.searchengine.utils.Doc;
import il.ac.shenkar.searchengine.utils.Hits;
import il.ac.shenkar.searchengine.utils.Posting;
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
    private DefaultListModel<String> model;
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

        printButton.addActionListener(e-> {
            try {
                showDocument.print();
            } catch (PrinterException e1) {
                e1.printStackTrace();
            }
        });

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
            Doc doc = Utils.getDocsMap().get(result);
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

    private void highlight(ArrayList<String> words, String index) {
        Highlighter highlighter = showDocument.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
        words.forEach((word)->{
            Hits hit =  Utils.getMap().get(word);
            if(hit != null) {
                Posting posting = hit.getPostings().get(index);
                if (posting != null) {
                    ArrayList<Integer> locations = posting.getOccurences();
                    locations.forEach((location) -> {
                        try {
                            highlighter.addHighlight(location, location + word.length(), painter);
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            AbstractMap.Entry<String, String> KVPair = null;
            try {
                KVPair = search.showDocument(searchResults.getSelectedValue().toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            searchResults.removeListSelectionListener(this);
            if (KVPair != null) {
                showDocument.setText(KVPair.getValue());
                highlight(searchTerms, KVPair.getKey());
                docScrollPanel.setBorder(BorderFactory.createEmptyBorder());
                docPanel.setVisible(true);
                this.setContentPane(docPanel);
            }
        }
    }
}
