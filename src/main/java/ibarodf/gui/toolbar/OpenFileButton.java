package ibarodf.gui.toolbar;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ibarodf.core.IbarOdfCore;
import ibarodf.gui.TreeStructurePanel;
import ibarodf.gui.toolbar.exception.CurrentFileIsADirectory;
import ibarodf.gui.toolbar.popup.PopUpField;

public class OpenFileButton extends IconButtonWithLabel{
    private final static String BUTTON_LABEL = "Ouvrir un fichier"; 
    private final static String DIALOG_FRAME_MESSAGE = "Entrer le chemin absolue du fichier"; 
    private String fileToOpenPath;  
    private TreeStructurePanel treeToPerformActionOn;



    public final static ImageIcon BUTTON_IMAGE = new ImageIcon("src/main/resources/icons/file_open.png");

    public OpenFileButton(Dimension dimension, TreeStructurePanel treeToPerformActionOn){
        super(BUTTON_LABEL, BUTTON_IMAGE , dimension);
        this.treeToPerformActionOn = treeToPerformActionOn;
    }

    public void mouseClicked(MouseEvent e){
        super.mouseClicked(e);
        PopUpField enterFilePath = new PopUpField(DIALOG_FRAME_MESSAGE);
        fileToOpenPath = enterFilePath.getEnteredValue();
        refreshTreePanel();
    }

    public void refreshTreePanel(){
        try{
            Path newRootPath = IbarOdfCore.stringToPath(fileToOpenPath);
            if(!newRootPath.toFile().isDirectory()){
                treeToPerformActionOn.refresh(newRootPath.toString());
            }else{
                throw new CurrentFileIsADirectory(newRootPath);
            }

        }catch(CurrentFileIsADirectory e){
            JOptionPane.showMessageDialog(getParent(),e.getMessage() ,"Wrong Button!",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            System.err.println(e.getLocalizedMessage());
            JOptionPane.showMessageDialog(getParent(), fileToOpenPath +" does not exist or is inaccessible!" ,"Can't access",JOptionPane.ERROR_MESSAGE);
        }
    }
}
