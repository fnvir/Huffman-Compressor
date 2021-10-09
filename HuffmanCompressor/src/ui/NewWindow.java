package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import huffman.AbstractCompressor;

class NewWindow extends JDialog {

    private static final long serialVersionUID = -7024664516039136981L;
    
    private JPanel mainPanel;
    private JProgressBar bar;
    private JLabel outSize;
    private AbstractCompressor algo;
    
    NewWindow(MainFrame parent,AbstractCompressor algo) {
        super(parent,"0% Complete",true);
        this.algo=algo;
        init();
        addLabels();
        addProgressBar();
        setupProgressBar();
        add(mainPanel);
        setVisible(true);
    }
    
    private void init() {
        setResizable(false);
        setIconImage(new ImageIcon(NewWindow.class.getResource("/icon.png")).getImage());
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setSize(350,215);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(!algo.isDone())
                    algo.getOutputFile().deleteOnExit();
                dispose();
            }
        });
        mainPanel=new JPanel(null);
        mainPanel.setSize(350,215);
    }
    
    private void addProgressBar() {
        bar=new JProgressBar(0,100);
        bar.setVisible(true);
        bar.setStringPainted(true);
        bar.setBounds(15,20,300,30);
        bar.setForeground(Color.decode("#06b025"));
        bar.setFont(new Font(null,Font.BOLD,12));
        mainPanel.add(bar);
    }
    
    private void setupProgressBar() {
        algo.addPropertyChangeListener(
            new PropertyChangeListener() {
                public  void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        int val=(int)evt.getNewValue();
                        bar.setValue(val);
                        setTitle(val+"% Complete");
                    }
                    else if(algo.isDone()) {
                        setTitle("Finished");
                        bar.setValue(100);
                        bar.setString("Done!");
                        outSize.setText(algo.getOutputFileLength()+" bytes");
                    }
                }
            }
        );
    }
    
    private void addLabels() {
        mainPanel.add(makeLabel("Selected File Size:",15,80,120,30,true));
        mainPanel.add(makeLabel(algo.getInputFileLength()+" bytes",140,80,180,30,false));
        mainPanel.add(makeLabel("Output File Size:",15,110,120,40,true));
        mainPanel.add(outSize=makeLabel("Calculating...",140,110,120,40,false));
    }
    
    private JLabel makeLabel(String text,int x,int y,int w,int h,boolean bold) {
        JLabel lbl=new JLabel(text);
        lbl.setBounds(x,y,w,h);
        lbl.setFont(new Font(null,bold?Font.BOLD:Font.PLAIN,13));
        return lbl;
    }

}
