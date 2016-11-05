package il.ac.shenkar.searchengine.serach.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.*;
import java.util.ArrayList;

import il.ac.shenkar.searchengine.serach.Search;
import il.ac.shenkar.searchengine.utils.Doc;
import il.ac.shenkar.searchengine.utils.Hits;
import il.ac.shenkar.searchengine.utils.Posting;
import il.ac.shenkar.searchengine.utils.Utils;

public class SearchForm extends JFrame implements ListSelectionListener {
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
    public SearchForm() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super("search engine");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(new Color(37, 156, 140));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(new Color(38,187,171));
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

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(37, 156, 140));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(new Color(38,187,171));
            }
        });


        printButton.addActionListener(e -> {
            try {
                showDocument.print();
            } catch (PrinterException e1) {
                e1.printStackTrace();
            }
        });

        printButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                printButton.setBackground(new Color(37, 156, 140));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                printButton.setBackground(new Color(38,187,171));
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

        createMenuBar();
        resultsScrollPanel.setBorder(BorderFactory.createEmptyBorder());
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        setContentPane(rootPanel);
        setVisible(true);
    }

    private void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_F);

        JMenuItem helpItem = new JMenuItem("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpItem.setMnemonic(KeyEvent.VK_E);
        aboutItem.setMnemonic(KeyEvent.VK_E);

        helpItem.setToolTipText("Hoe to use Search Engine");
        helpItem.addActionListener((ActionEvent event) -> {
            JFrame helpFrame = new JFrame("Help");
            helpFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            helpFrame.setSize(640, 480);
            helpFrame.setBackground(Color.white);
            helpFrame.setLocationRelativeTo(null);
            helpFrame.setVisible(true);
        });

        aboutItem.addActionListener((ActionEvent event) -> {
            JFrame aboutFrame = new JFrame("About");
            JTextArea textArea = new JTextArea("Created by Dassi Rosen and Gregory K\nReleased November 2016");
            textArea.setEditable(false);
            textArea.setMargin(new Insets(30, 30, 30, 30));
            aboutFrame.add(textArea, BorderLayout.CENTER);
            aboutFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            aboutFrame.setSize(480, 320);
            aboutFrame.setBackground(Color.white);
            aboutFrame.setLocationRelativeTo(null);
            aboutFrame.setVisible(true);
        });

        menu.add(aboutItem);
        menu.add(helpItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);
    }

    private void showResults(ArrayList<String> results) {
        resultsLabel.setText(results.size() + " search result(s):");
        resultsLabel.setForeground(new Color(38, 187, 171));
        DefaultListModel listModel = new DefaultListModel();
        ListCellRenderer renderer = new ListItemRenderer();
        searchResults.setCellRenderer(renderer);
        results.forEach((result) -> {
            Doc doc = Utils.getDocsMap().get(result);
            listModel.addElement(doc);
        });
        searchResults.setModel(listModel);
        searchResults.addListSelectionListener(this);
    }

    private void noResults() {
        searchResults.setModel(new DefaultListModel());
        resultsLabel.setForeground(new Color(187, 117, 125));
        resultsLabel.setText("no results");
        searchResults.removeListSelectionListener(this);
    }

    private void highlight(ArrayList<String> words, String index) {
        Highlighter highlighter = showDocument.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
        words.forEach((word) -> {
            Hits hit = Utils.getIndexMap().get(word);
            String tempWord = word.replaceAll("\"", "");
            if (hit != null) {
                Posting posting = hit.getPostings().get(index);
                if (posting != null) {
                    ArrayList<Integer> locations = posting.getHits();
                    locations.forEach((location) -> {
                        try {
                            highlighter.addHighlight(location, location + tempWord.length() + 1, painter);
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
                backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                printButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                highlight(searchTerms, doc.getSerial());
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
            appendToPane(renderer, "\n" + fileName + "\n", Color.blue, true);
            appendToPane(renderer, preview + "\n", Color.black, false);
            return renderer;
        }
    }

    private void appendToPane(JTextPane textPane, String msg, Color color, boolean shouldUnderline) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet attr = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        if (shouldUnderline) {
            attr = sc.addAttribute(attr, StyleConstants.Underline, true);
        } else attr = sc.addAttribute(attr, StyleConstants.Underline, false);

        int length = textPane.getDocument().getLength();
        textPane.setCaretPosition(length);
        textPane.setCharacterAttributes(attr, false);
        textPane.replaceSelection(msg);
    }
}
