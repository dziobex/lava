package org.maze;
import javax.swing.*;

public class MainWindow extends JFrame {
    JMenuBar bar;
    JMenu openItem, saveItem;
    JMenuItem openTextItem, openBinaryItem;
    JMenuItem saveTextItem, saveBinaryItem, saveImageItem;
    private JPanel mainPanel;
    private JPanel toolPanel;
    private JPanel mazePanel;
    private JButton PRZYCISKButton;

    public MainWindow() {
        setTitle("Lava - main window");
        menuBarSetup();
        setSize(800, 600);
        setVisible(true);
    }

    void menuBarSetup() {
        bar = new JMenuBar();

        openItem = new JMenu("Open From");
        openTextItem = new JMenuItem("text");
        openBinaryItem = new JMenuItem("binary");
        openItem.add(openTextItem);
        openItem.add(openBinaryItem);
        bar.add(openItem);

        saveItem = new JMenu("Save As");
        saveTextItem = new JMenuItem("text");
        saveBinaryItem = new JMenuItem("binary");
        saveImageItem = new JMenuItem("image");
        saveItem.add(saveTextItem);
        saveItem.add(saveBinaryItem);
        saveItem.add(saveImageItem);
        bar.add(saveItem);

        setJMenuBar(bar);
    }
}
