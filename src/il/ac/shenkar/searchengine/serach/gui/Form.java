package il.ac.shenkar.searchengine.serach.gui;

import javax.naming.directory.SearchResult;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;
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

        printButton.addActionListener(e -> {
            try {
                showDocument.print();
            } catch (PrinterException e1) {
                e1.printStackTrace();
            }
        });

        searchResults.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                final int x = e.getX();
                final int y = e.getY();
                final Rectangle cellBounds = searchResults.getCellBounds(0, searchResults.getModel().getSize() - 1);
                if (cellBounds != null && cellBounds.contains(x, y)) {
                    searchResults.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    searchResults.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
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
        resultsLabel.setText(results.size() + " search result(s):");
        resultsLabel.setForeground(new Color(38,187,171));
        DefaultListModel listModel = new DefaultListModel();
        ListCellRenderer renderer = new ListItemRenderer();
        searchResults.setCellRenderer(renderer);
        results.forEach((result)-> {
            Doc doc = Utils.getDocsMap().get(result);
            listModel.addElement(doc);
        });
        searchResults.setModel(listModel);
        searchResults.addListSelectionListener(this);
    }

    private void noResults() {
        searchResults.setModel(new DefaultListModel());
        resultsLabel.setForeground(new Color(187,117,125));
        resultsLabel.setText("no results");
        searchResults.removeListSelectionListener(this);
    }

    private void highlight(ArrayList<String> words, String index) {
        Highlighter highlighter = showDocument.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
        words.forEach((word)->{
            Hits hit =  Utils.getMap().get(word);
            String tempWord = word.replaceAll("\"", "");
            if(hit != null) {
                Posting posting = hit.getPostings().get(index);
                if (posting != null) {
                    ArrayList<Integer> locations = posting.getOccurences();
                    locations.forEach((location) -> {
                        try {
                            highlighter.addHighlight(location, location + tempWord.length(), painter);
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
            Doc doc = (Doc) searchResults.getSelectedValue();
            try {
                search.showDocument(doc);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            searchResults.removeListSelectionListener(this);
            if (doc != null) {
                showDocument.setText(doc.getContent());
                highlight(searchTerms,doc.getSerial());
                docScrollPanel.setBorder(BorderFactory.createEmptyBorder());
                docPanel.setVisible(true);
                this.setContentPane(docPanel);
            }
        }
    }

    private class ListItemRenderer extends JPanel implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JTextPane renderer = new JTextPane();
            renderer.setFont(new Font("sans-serif", Font.PLAIN, 18));
            String fileName = ((Doc) value).getFileName();
            String preview = ((Doc) value).getPreview();
            appendToPane(renderer, "\n" + fileName + "\n\n", Color.blue, true);
            appendToPane(renderer, preview + "\n", Color.black, false);
            return renderer;
        }
    }

    private void appendToPane(JTextPane textPane, String msg, Color color, boolean shouldUnderline)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet attr = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        if(shouldUnderline) {
            attr = sc.addAttribute(attr, StyleConstants.Underline, true);
        }
        else attr = sc.addAttribute(attr, StyleConstants.Underline, false);

        int length = textPane.getDocument().getLength();
        textPane.setCaretPosition(length);
        textPane.setCharacterAttributes(attr, false);
        textPane.replaceSelection(msg);
    }
}
