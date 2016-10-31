package il.ac.shenkar.searchengine.admin.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import il.ac.shenkar.searchengine.admin.Indexer;
import il.ac.shenkar.searchengine.utils.Doc;
import il.ac.shenkar.searchengine.utils.Utils;

public class Admin extends JFrame{
    private JPanel rootPanel;
    private JButton loadButton;
    private JLabel title;
    private JButton showButton;
    private JPanel actionPanel;
    private JPanel wrapperPanel;
    private JTextArea resultLabel;
    private JButton hideButton;
    private CenteredFileChooser fileChooser;

    public Admin() {
        super("search engine - admin");
        initComponents();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
                        Doc doc = new Doc(file.getName());
                        File storedFile = Utils.storeFile(file, doc);
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

        hideButton.addActionListener(e -> {
            fileChooser.showOpenDialog(loadButton);
            File[] files = fileChooser.getSelectedFiles();
            Map<String, Doc> posting = Utils.getPostingMap();
            posting.forEach((key, doc)->{
                for(File file: files){
                    if(Objects.equals(file.getName(), doc.getFileName())){
                        doc.hide();
                        resultLabel.setText(file.getName() + " is now hidden");
                    }
                }
            });
            try {
                Utils.savePostingToFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        showButton.addActionListener(e -> {
            fileChooser.showOpenDialog(loadButton);
            File[] files = fileChooser.getSelectedFiles();
            Map<String, Doc> posting = Utils.getPostingMap();
            posting.forEach((key, doc)->{
                for(File file: files){
                    if(Objects.equals(file.getName(), doc.getFileName())){
                        doc.show();
                        resultLabel.setText(file.getName() + " is now visible");
                    }
                }
            });
            try {
                Utils.savePostingToFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void initComponents() {
        this.setSize(1024, 720);
        this.setLocationRelativeTo(null);

        loadButton.setBorderPainted(false);
        showButton.setBorderPainted(false);
        hideButton.setBorderPainted(false);

        fileChooser = new CenteredFileChooser();
        fileChooser.setCurrentDirectory(new File("./input"));
        fileChooser.setDialogTitle("Search Engine - Admin");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        setContentPane(rootPanel);
        setVisible(true);
    }

}
