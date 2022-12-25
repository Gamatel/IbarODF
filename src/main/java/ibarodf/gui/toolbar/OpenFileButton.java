package ibarodf.gui.toolbar;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import ibarodf.ResourceLoader;
import ibarodf.core.IbarOdfCore;
import ibarodf.gui.OptionPane;
import ibarodf.gui.TreeStructurePanel;
import ibarodf.gui.toolbar.exception.CurrentFileIsADirectory;

/**
 * This class represent the Button that choice a file to be display in the TreeeStructurePanel and TablePanel
 */
public class OpenFileButton extends IconButtonWithLabel{
    private final static String BUTTON_LABEL = "Ouvrir un fichier"; 
    private final static String DIALOG_FRAME_MESSAGE = "Entrer le chemin absolu du fichier"; 
    private String fileToOpenPath;  
    private final TreeStructurePanel treeToPerformActionOn;

    public static ImageIcon BUTTON_IMAGE;

    static {
        try {
            BUTTON_IMAGE = ResourceLoader.loadImgFromResource("icons/file_open.png");
        } catch (IOException e) {
            OptionPane.alertError(null, ResourceLoader.MSG_ERROR);
        }
    }

    public OpenFileButton(Dimension dimension, TreeStructurePanel treeToPerformActionOn){
        super(BUTTON_LABEL, BUTTON_IMAGE , dimension);
        this.treeToPerformActionOn = treeToPerformActionOn;
    }

    public void mouseClicked(MouseEvent e){
        super.mouseClicked(e);
        fileToOpenPath = JOptionPane.showInputDialog(getParent(), DIALOG_FRAME_MESSAGE);
        if(fileToOpenPath !=null){
            refreshTreePanel();
        }
    }

    /**
     * This method refresh the ThreePanel after a new selection
     */
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
            File wrongFile= new File(fileToOpenPath);
            if(wrongFile.exists()){
                JOptionPane.showMessageDialog(getParent(), "Accès réfuser à "+wrongFile.getName() ,"ERROR",JOptionPane.ERROR_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(getParent(), "Le fichier "+ fileToOpenPath +" n'existe pas" ,"ERROR",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
