package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

class TopMenu implements ActionListener {
    
    private JMenuBar bar;
    private JMenuItem open,exit;
    private JMenuItem tutorial,about,issue;
    private MainFrame parent;
    
    TopMenu(MainFrame parent) {
        this.parent=parent;
        bar=new JMenuBar();
        bar.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        createMenu1();
        createMenu2();
    }
    
    private void createMenu1() {
        JMenu m1=new JMenu("File");
        open=new JMenuItem("Open File...");
        open.addActionListener(this);
        exit=new JMenuItem("Exit");
        exit.addActionListener(this);
        m1.add(open);
        m1.add(exit);
        bar.add(m1);
    }
    
    private void createMenu2() {
        JMenu m2=new JMenu("Help");
        tutorial=new JMenuItem("Tutorial");
        tutorial.addActionListener(this);
        about=new JMenuItem("About");
        about.addActionListener(this);
        issue=new JMenuItem("Report issue");
        issue.addActionListener(this);
        m2.add(tutorial);
        m2.add(about);
        m2.add(issue);
        bar.add(m2);
    }
    
    public JMenuBar getMenu() {
        return bar;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src=e.getSource();
        if(src==open) {
            parent.getBody().getFileBtn().doClick(69);
        } else if(src==exit) {
            parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
        } else if(src==tutorial) {
            showtutorial();
        } else if(src==about) {
            showInfo();
        } else {
            reportIssue();
        }
    }
    
    private void showtutorial() {
        String s="To compress a file, first select a file.\n"
                +"Then select the output directory (where the output will be saved)      \n"
                +"Click on the \"Compress\" button to begin compression\n"
                +"The File will be saved  with a .huff extension added\n\n"
                +"Follow similar steps for decompression too\n\n";
        JOptionPane.showMessageDialog(null,s,"Tutorial",JOptionPane.PLAIN_MESSAGE,new ImageIcon("qmark.png"));
    }
    
    private void showInfo() {
        String s="A software to compress and decompress files        \n"
                +"using huffman coding developed with Java.\n\n"
                +"                   Version: 1.00 (2021)\n"
                +"                     (c) Farhan Tanvir\n";
        JOptionPane.showMessageDialog(null,s,"About",JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void reportIssue() {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI("https://github.com/fnvir/Huffman-Compressor/issues"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Unknown Error","Error!",JOptionPane.ERROR_MESSAGE);
        }
    }

}
