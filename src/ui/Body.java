package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Body {
    
    private JPanel mainPanel;
    private MainFrame parent;
    private JButton fileBtn,dirBtn;
    private JButton cmpBtn,dcmpBtn;
    private boolean dirSelected;
    
    public Body(MainFrame x) {
        parent=x;
        mainPanel=new JPanel();
        mainPanel.setBounds(0,0,600,320);
        mainPanel.setLayout(null);
        mainPanel.add(labelPanel());
        mainPanel.add(selectBtnPanel());
        addStartBtns();
        setBtnActions();
    }
    
    protected JPanel getPanel() {
        return mainPanel;
    }
    
    private JPanel labelPanel() {
        JPanel infoPanel=new JPanel();
        infoPanel.setLayout(null);
//        infoPanel.setBackground(Color.white);
        infoPanel.setBounds(0,0,135,120);
        JLabel input=new JLabel("Selected file:");
        input.setBounds(15,30,120,30);
        input.setFont(new Font("Arial",Font.BOLD,13));
        infoPanel.add(input);
        JLabel output=new JLabel("Output directory:");
        output.setBounds(15,80,120,30);
        output.setFont(new Font("Arial",Font.BOLD,13));
        infoPanel.add(output);
        return infoPanel;
    }
    
    private JPanel selectBtnPanel() {
        JPanel filePanel=new JPanel();
        filePanel.setLayout(null);
        filePanel.setBounds(135,0,450,120);
        fileBtn=createSelectBtn("Click here to select a file",0,30,430,30);
        dirBtn=createSelectBtn("Click here to choose directory",0,80,430,30);
        filePanel.add(fileBtn);
        filePanel.add(dirBtn);
        return filePanel;
    }
    
    private JButton createSelectBtn(String s,int x,int y,int w,int h) {
        JButton lbl=new JButton(s);
        lbl.setToolTipText(s);
        lbl.setFocusable(false);
        lbl.setContentAreaFilled(false);
        lbl.setBackground(Color.white);
        lbl.setForeground(Color.black);
        lbl.setOpaque(true);
        lbl.setBounds(x,y,w,h);
        lbl.setFont(new Font(null,Font.PLAIN,13));
        lbl.setBorder(BorderFactory.createLineBorder(Color.black,1));
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbl.addMouseListener(new MouseAdapter() {  
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                String s=parent.openFileDialog(true);
//                if(s!=null) {
//                    lbl.setText(s);
//                    lbl.setToolTipText(s);
//                    dirSelected=true;
//                }
//            }
            @Override
            public void mouseEntered(MouseEvent e) {
                lbl.setBackground(Color.decode("#c9def5"));
                lbl.setBorder(BorderFactory.createRaisedBevelBorder());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lbl.setBackground(Color.white);
                lbl.setBorder(BorderFactory.createLineBorder(Color.black,1));
            }
        });
        return lbl;
    }
    
    private void addStartBtns() {
        cmpBtn=createStartBtn("Compress",70,160,180,40);
        dcmpBtn=createStartBtn("Decompress",330,160,180,40);
        mainPanel.add(cmpBtn);
        mainPanel.add(dcmpBtn);
    }
    
    private JButton createStartBtn(String s,int x,int y,int w,int h) {
        JButton bt=new JButton(s);
        bt.setToolTipText("Begin "+s+"ion");
        bt.setBounds(x,y,w,h);
        bt.setFocusable(false);
        bt.addActionListener(e->parent.compressAction());
        bt.setEnabled(false);
        return bt;
    }
    
    
    private void setBtnActions() {
        fileBtn.addActionListener(e->{
            String s[]=parent.openFileDialog(false),dir=s[0],file=s[1];
            if(file!=null) {
                fileBtn.setText(file);
                fileBtn.setToolTipText(file);
                if(!dirSelected) {
                    dirBtn.setText(dir);
                    dirBtn.setToolTipText(dir);
                }
            }
        });
        dirBtn.addActionListener(e->{
            String s=parent.openFileDialog(true)[0];
            if(s!=null) {
                dirBtn.setText(s);
                dirBtn.setToolTipText(s);
                dirSelected=true;
            }
        });
    }

    protected JButton getFileBtn() {
        return fileBtn;
    }
    
    protected JButton getDirBtn() {
        return dirBtn;
    }
    
    protected JButton getCompressBtn() {
        return cmpBtn;
    }
    
    protected JButton getDecompressBtn() {
        return dcmpBtn;
    }
    
}
