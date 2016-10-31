package il.ac.shenkar.searchengine.admin.gui;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import il.ac.shenkar.searchengine.admin.Indexer;
import il.ac.shenkar.searchengine.utils.Doc;
import il.ac.shenkar.searchengine.utils.Utils;

public class Admin extends JFrame{
    private JPanel rootPanel;
    private JButton loadButton;
    private JLabel title;
    private JButton deleteButton;
    private JPanel actionPanel;
    private JPanel wrapperPanel;
    private JTextArea resultLabel;
    private CenteredFileChooser fileChooser;

    public Admin() {
        super("search engine - admin");
        initComponents();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    Utils.saveMapToFile();
                    Utils.savePostingToFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });

        loadButton.addActionListener(e -> {
            fileChooser.showOpenDialog(loadButton);
            File[] files = fileChooser.getSelectedFiles();
            String msg = "";
            Indexer indexer = new Indexer();
            for (File file : files) {
                if(Utils.getStorageFileNames().contains(file.getName())) {
                    msg += file.getName() + " already exists\n";
                }
                else {
                    try {
                        File storedFile = Utils.storeFile(file);
                        Doc doc = new Doc(storedFile.getName());
                        indexer.index(storedFile, doc);
                        Utils.saveMapToFile();
                        Utils.savePostingToFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    String fileName = file.getName();
                    msg += fileName + "  was loaded successfully\n";
                }
            }
            resultLabel.setText(msg);
        });
    }

    private void initComponents() {
        this.setSize(1024, 720);
        this.setLocationRelativeTo(null);

        loadButton.setBorderPainted(false);
        deleteButton.setBorderPainted(false);

        fileChooser = new CenteredFileChooser();
        fileChooser.setCurrentDirectory(new File("./input"));
        fileChooser.setDialogTitle("Search Engine - Admin");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        setContentPane(rootPanel);
        setVisible(true);
    }

}
