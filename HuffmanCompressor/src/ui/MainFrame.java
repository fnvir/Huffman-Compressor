package ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.sun.tools.javac.Main;

import huffman.AbstractCompressor;
import huffman.Compressor;
import huffman.Decompressor;


public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private static FileSelector f;
    private Body b;
    private String inputFile,outDir;
    
    public MainFrame() {
        setSize(600,320);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setTitle("Huff Compressor");
        setIconImage(new ImageIcon("icon.png").getImage());
        setLayout(null);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(null,"Exit the program?","Confirm Exit",JOptionPane.YES_NO_OPTION);
                if(response == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
        f=new FileSelector(this);
        setJMenuBar(new TopMenu(this).getMenu());
        b=new Body(this);
        add(b.getPanel());
    }    
    
    Body getBody() {
        return b;
    }
    
    protected String[] openFileDialog(boolean dirOnly) {
        if(dirOnly) {
            f.selectDir();
            outDir=f.getDirectory();
        } else {
            f.selectFile();
            inputFile=f.getAbsolutePath();
            if(outDir==null)
                outDir=f.getDirectory();
            if(inputFile!=null) {
                boolean bl=isHuffFile(inputFile);
                b.getCompressBtn().setEnabled(!bl);
                b.getDecompressBtn().setEnabled(bl);
            }
        }
        return new String[] {f.getDirectory(),f.getAbsolutePath()};
    }
    
    public static boolean isHuffFile(String f) {
        String s[]=f.split("\\.");
        if(s.length>1)
            return s[s.length-1].equals("huff");
        return false;
    }
    
    protected void compressAction() {
        if(inputFile==null)
            b.getFileBtn().doClick();
        if(inputFile!=null) {
            AbstractCompressor algo=isHuffFile(inputFile)?new Decompressor(inputFile):new Compressor(inputFile);
            algo.setOutputDir(outDir);
            MainFrame x=this;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new NewWindow(x,algo);
                }
            });
            algo.execute();
        }
    }
    
}
