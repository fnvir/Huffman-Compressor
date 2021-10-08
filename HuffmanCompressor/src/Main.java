import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.MainFrame;

public class Main {

    public static void main(String[] args) {
        UIManager.put("Button.focus",new java.awt.Color(0,0,0,0));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

}
