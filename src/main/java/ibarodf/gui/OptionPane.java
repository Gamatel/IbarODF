package ibarodf.gui;

import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * this class generate dialog message
 */
public class OptionPane {
    private static final String ERROR_TITLE = "Erreur";

    /**
     * This class launch alert message dialog
     * @param parent parent component
     * @param msg dialog message
     */
    public static void alertError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }
}
