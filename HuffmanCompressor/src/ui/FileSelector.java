package ui;

import java.awt.FileDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileSelector {
    
    private JFrame parent;
    private FileDialog fd;
    private JFileChooser fc;
    
    private String dir,file;
    
    public FileSelector(JFrame parent) {
        this.parent=parent;
        String s=System.getProperty("user.dir");
        fd=new FileDialog(parent, "Select File", FileDialog.LOAD);
        fd.setDirectory(s);
        fd.setLocationRelativeTo(null);
        fc=new JFileChooser(s);
        fc.setDialogTitle("Select Folder");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
    }
    
    public void selectFile() {
        fd.setVisible(true);
        dir=fd.getDirectory();
        file=fd.getFile();
    }
    
    public void selectDir() {
        file=null;
        if(fc.showSaveDialog(parent)==JFileChooser.APPROVE_OPTION)
            dir=fc.getSelectedFile().getAbsolutePath()+"\\";
        else
            dir=null;
    }
    
    public String getFile() {
        return file;
    }
    
    public String getDirectory() {
        return dir;
    }
    
    public String getAbsolutePath() {
        if(file==null) {
            if(dir==null) return null;
            return dir;
        }
        return dir+file;
    }
    
}
