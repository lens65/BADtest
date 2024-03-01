import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MainPanel extends JPanel {
    private final Color panelBackground = new Color(32, 33, 36);
    private final Color textColor = new Color(255, 255, 255);
    private JButton chooseFileButton;
    private final int buttonWidth = 150;
    private final int buttonHeight = 40;
    private JTextArea textAreaResult;
    private Thread actionOnButtonThread;
    private volatile boolean fileIsScanning;

    public MainPanel() {
        this.setLayout(null);
        this.setBackground(panelBackground);
        this.setLocation(0, 0);
        setElements();
        fileIsScanning = false;
    }

    private void setElements() {
        setButton();
        setTextAreaResult();
    }

    private void setButton() {
        chooseFileButton = new JButton("Choose file...");
        chooseFileButton.setSize(buttonWidth, buttonHeight);
        chooseFileButton.setBackground(new Color(72, 72, 74));
        chooseFileButton.setFocusPainted(false);
        chooseFileButton.setBorder(null);
        chooseFileButton.setForeground(textColor);
        chooseFileButton.setFont(new Font("Serif", Font.BOLD, 17));
        chooseFileButton.setLocation(0, 0);
        chooseFileButton.addActionListener(l -> {
            if(!fileIsScanning){
                fileIsScanning = true;
                actionOnButtonThread = new Thread(() -> actionOnButton());
                actionOnButtonThread.start();
            }
        });

        this.add(chooseFileButton);
    }

    private void actionOnButton() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            File choosenFile = fileChooser.getSelectedFile().getAbsoluteFile();
            FileScan fileScan = new FileScan(choosenFile.toString());

            Thread scaningThread = new Thread(() -> {fileScan.scan();});
            scaningThread.start();

            textAreaResult.setText("Waiting for result.");
            int i = 0;
            long t = System.currentTimeMillis();
            while (fileScan.getResult() == null) {
                if(System.currentTimeMillis() >= t + 100){
                    i++;
                    t = System.currentTimeMillis();
                    if(i > 2)
                        i = 0;
                    if (i == 0)
                        textAreaResult.setText("Waiting for result.");
                    else if (i == 1)
                        textAreaResult.setText("Waiting for result..");
                    else textAreaResult.setText("Waiting for result...");
                }
            }

            try {
                scaningThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            textAreaResult.setText(fileScan.getResult());
            fileIsScanning = false;
        }
    }

    private void setTextAreaResult() {
        textAreaResult = new JTextArea("Result...");
        textAreaResult.setBackground(new Color(130, 125, 125));
        textAreaResult.setForeground(textColor);
        textAreaResult.setFont(new Font("Serif", Font.BOLD, 18));
        textAreaResult.setCaretColor(textColor);
        textAreaResult.setCursor(Cursor.getDefaultCursor());
        textAreaResult.setLineWrap(true);
        this.add(textAreaResult);
    }

    public void actionOnResize(int width, int height) {
        chooseFileButton.setLocation((width - buttonWidth) / 2, (int) (height * 0.1));
        textAreaResult.setSize((int) (width * 0.8), (int) (height * 0.7));
        textAreaResult.setLocation((int) (width * 0.1), (int) (height * 0.2));
    }
}
