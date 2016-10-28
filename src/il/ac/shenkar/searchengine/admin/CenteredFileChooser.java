package il.ac.shenkar.searchengine.admin;

import javax.swing.*;
import java.awt.*;

class CenteredFileChooser extends JFileChooser {
    @Override
    protected JDialog createDialog(Component parent)
            throws HeadlessException {
        JDialog dialog = super.createDialog(parent);
        dialog.setLocationRelativeTo(null);
        return dialog;
    }
}