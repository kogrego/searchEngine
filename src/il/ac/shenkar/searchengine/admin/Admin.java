package il.ac.shenkar.searchengine.admin;
import il.ac.shenkar.searchengine.utils.Utils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class Admin extends JFrame{
    private JPanel rootPanel;
    private JButton loadButton;
    private JLabel title;
    private JButton deleteButton;
    private JPanel actionPanel;
    private JPanel wrapperPanel;
    private JLabel selectedFilesLabel;
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
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
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
                    Utils.saveMapToFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                String path = file.getPath();
                paths += path + " ";
            }
            selectedFilesLabel.setText(paths);
        });
    }

    private void initComponents() {
        this.setSize(1024, 720);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
