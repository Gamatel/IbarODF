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

public class OpenFileButton extends IconButtonWithLabel{
    private final static String BUTTON_LABEL = "Ouvrir un fichier";
    private final static String DIALOG_FRAME_MESSAGE = "Entrer le chemin du fichier";
    private String fileToOpenPath;
    private final TreeStructurePanel treeToPerformActionOn;

    public static ImageIcon BUTTON_IMAGE = new ImageIcon("src/main/resources/icons/file_open.png");

    static {
        try {
            BUTTON_IMAGE = ResourceLoader.loadImgFromResource("icons/file_open.png");
        } catch (IOException ignored) {
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

    public void refreshTreePanel(){
        StringBuilder filePath = new StringBuilder(fileToOpenPath);
        String currentSeparator = IbarOdfCore.getCurrentSystemSeparator();
        try{
            if(fileToOpenPath.startsWith("..")){
                fileToOpenPath = treeToPerformActionOn.rootName(currentSeparator)+ currentSeparator + filePath.toString();
            }else if(fileToOpenPath.startsWith(".")){
                filePath.deleteCharAt(0);
                fileToOpenPath = treeToPerformActionOn.rootName(currentSeparator)+ currentSeparator + filePath.toString();
            }
            Path newRootPath = IbarOdfCore.stringToPath(fileToOpenPath);
            if(!newRootPath.toFile().isDirectory()){
                treeToPerformActionOn.refresh(newRootPath.toString(), false);
            }else{
                throw new CurrentFileIsADirectory(newRootPath);
            }
        }catch(CurrentFileIsADirectory e){
            JOptionPane.showMessageDialog(getParent(),e.getMessage() ,"Wrong Button!",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(filePath);
            File wrongFile= new File(fileToOpenPath);
            if(wrongFile.exists()){
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(getParent(), "Accès réfuser à "+wrongFile.getName() ,"ERROR",JOptionPane.ERROR_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(getParent(), "Le fichier "+ fileToOpenPath +" n'existe pas" ,"ERROR",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
