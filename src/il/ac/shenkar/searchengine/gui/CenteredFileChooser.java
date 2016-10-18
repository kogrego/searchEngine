package il.ac.shenkar.searchengine.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Seymore on 10/18/2016.
 */
class CenteredFileChooser extends JFileChooser {
    @Override
    protected JDialog createDialog(Component parent)
            throws HeadlessException {
        JDialog dialog = super.createDialog(parent);
        dialog.setLocationRelativeTo(null);
        return dialog;
    }
}