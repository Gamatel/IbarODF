package ibarodf.gui.toolbar;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import ibarodf.core.IbarOdfCore;
import ibarodf.gui.TreeStructurePanel;
import ibarodf.gui.toolbar.exception.CurrentDirectoryIsAFile;

import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;

public class OpenDirectoryButton extends IconButtonWithLabel {
    private final static String BUTTON_LABEL = "Ouvrir un dossier "; 
    private final static String DIALOG_FRAME_MESSAGE = " Entrer le chemin absolu du dossier "; 
    private String directoryToOpenPath; 
    private TreeStructurePanel treeToPerformActionOn;
    public final static ImageIcon IMAGE_BUTTON = new ImageIcon("src/main/resources/icons/new_folder.png");

    public OpenDirectoryButton(Dimension dimension, TreeStructurePanel treeToPerformActionOn){
        super( BUTTON_LABEL,  IMAGE_BUTTON , dimension);
        this.treeToPerformActionOn = treeToPerformActionOn;
    }

    public void mouseClicked(MouseEvent e){
        super.mouseClicked(e);
        directoryToOpenPath = JOptionPane.showInputDialog(getParent(), DIALOG_FRAME_MESSAGE);
        if(directoryToOpenPath!=null){
            refreshTreePanel();
        }
    }



    public void refreshTreePanel(){
        try{
            Path newRootPath = IbarOdfCore.stringToPath(directoryToOpenPath);
            if(newRootPath.toFile().isDirectory()){
                treeToPerformActionOn.refresh(newRootPath.toString());
            }else{
                throw new CurrentDirectoryIsAFile(newRootPath);
            }
        }catch(CurrentDirectoryIsAFile e){
            JOptionPane.showMessageDialog(getParent(), e.getMessage() ,"Wrong Button!",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            File wrongFile= new File(directoryToOpenPath);
            if(wrongFile.exists()){
                JOptionPane.showMessageDialog(getParent(), "Accès réfuser à "+wrongFile.getName() ,"ERROR",JOptionPane.ERROR_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(getParent(), "Le fichier "+ directoryToOpenPath +" n'existe pas" ,"ERROR",JOptionPane.ERROR_MESSAGE);
            }
        }
    }





    
}
