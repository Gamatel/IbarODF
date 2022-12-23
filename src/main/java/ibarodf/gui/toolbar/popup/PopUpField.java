package ibarodf.gui.toolbar.popup;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.event.ActionEvent;

public class PopUpField extends JFrame {
    private String enteredValue;

    public PopUpField(String reason){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        enteredValue = JOptionPane.showInputDialog(getParent(), reason);
    }

    public String getEnteredValue(){
        return enteredValue;
    }
    
}
