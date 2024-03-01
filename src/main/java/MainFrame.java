import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainFrame extends JFrame {
    private MainPanel framePanel;


    public MainFrame(){
        setFrame();
    }

    private void setFrame(){
        this.setName("BADtest");
        this.setLayout(null);
        this.setMinimumSize(new Dimension(800,600));
        this.setPreferredSize(new Dimension(1440,900));
        this.setMaximumSize(new Dimension(1920,1080));
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        setElements();
        actionOnResize(this.getContentPane().getWidth(), this.getContentPane().getHeight());
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                JFrame c = (JFrame) e.getSource();
                actionOnResize(c.getContentPane().getWidth(), c.getContentPane().getHeight());
            }
        });

    }

    private void setElements(){
        framePanel = new MainPanel();

        this.add(framePanel);
        framePanel.setSize(this.getContentPane().getWidth(), this.getContentPane().getHeight());
    }

    private void actionOnResize(int width, int height){
        framePanel.setSize(width, height);
        framePanel.actionOnResize(width, height);
    }


}
