package il.ac.shenkar.searchengine.admin.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import il.ac.shenkar.searchengine.admin.Indexer;
import il.ac.shenkar.searchengine.utils.Doc;
import il.ac.shenkar.searchengine.utils.Utils;

public class AdminForm extends JFrame{
    private JPanel rootPanel;
    private JButton loadButton;
    private JLabel title;
    private JButton showButton;
    private JPanel actionPanel;
    private JPanel wrapperPanel;
    private JTextArea resultLabel;
    private JButton hideButton;
    private CenteredFileChooser fileChooser;

    public AdminForm() {
        super("search engine - admin");
        initComponents();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        loadButton.addActionListener(e -> {
            fileChooser.setCurrentDirectory(new File("./input"));
            fileChooser.setDialogTitle("Search Engine - load");
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
                        Utils.saveDocsToFile();
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
            fileChooser.setCurrentDirectory(new File("./storage"));
            fileChooser.setDialogTitle("Search Engine - hide");
            fileChooser.showOpenDialog(hideButton);
            File[] files = fileChooser.getSelectedFiles();
            Map<String, Doc> posting = Utils.getDocsMap();
            final String[] text = {""};
            posting.forEach((key, doc)->{
                for(File file: files){
                    if(Objects.equals(file.getName(), doc.getFileName())){
                        doc.hide();
                        text[0] += file.getName() + " is now hidden\n";
                    }
                }
            });
            resultLabel.setText(text[0]);
            try {
                Utils.saveDocsToFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        showButton.addActionListener(e -> {
            fileChooser.setCurrentDirectory(new File("./storage"));
            fileChooser.setDialogTitle("Search Engine - show");
            fileChooser.showOpenDialog(showButton);
            File[] files = fileChooser.getSelectedFiles();
            Map<String, Doc> posting = Utils.getDocsMap();
            final String[] text = {""};
            posting.forEach((key, doc)->{
                for(File file: files){
                    if(Objects.equals(file.getName(), doc.getFileName())){
                        doc.show();
                        text[0] += file.getName() + " is now visible\n";
                    }
                }
            });
            resultLabel.setText(text[0]);
            try {
                Utils.saveDocsToFile();
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

        loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hideButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        fileChooser = new CenteredFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        resultLabel.setEnabled(false);

        setContentPane(rootPanel);
        setVisible(true);
    }

}
