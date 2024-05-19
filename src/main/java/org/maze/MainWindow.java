package org.maze;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow extends JFrame {
    JMenuBar bar;
    JMenu openItem, saveItem;
    JMenuItem openTextItem, openBinaryItem;
    JMenuItem saveTextItem, saveBinaryItem, saveImageItem;
    private JPanel mainPanel;
    MazePanel mazePanel;
    private JScrollPane mazeScrollPane;
    private JButton solveButton;
    private JButton setStartButton;
    private JButton setEndButton;
    private JButton removeButton;
    private JLabel fillingLabel;
    public static MazeLoader loader = null;

    public MainWindow() {
        setTitle("Lava - LAbirynth in jaVA");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setSize(800, 600);
        setContentPane(mainPanel);

        menuBarSetup();
        updateMaze();

        setResizable(false);
        setVisible(true);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mazePanel = null;
                mazeScrollPane.setViewportView(fillingLabel);
            }
        });
    }

    void updateMaze() {
        if ( loader == null || loader.GetMaze() == null )
            return;
        mazePanel = new MazePanel(loader.GetMaze());
        mazeScrollPane.setViewportView(mazePanel);
        mazeScrollPane.setPreferredSize(mazePanel.getPreferredSize());
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

        openTextItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setCurrentDirectory(
                        new File("./samples")
                );
                fileChooser.setFileFilter(
                        new FileNameExtensionFilter("TEXT FILES", "txt")
                );
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    loader = new MazeLoader();
                    MazeLoader.LoadResult loadResult = loader.LoadText(selectedFile);

                    switch (loadResult) {
                        case SUCCESS:
                            System.out.println("Udało się załadować labirynt");
                            break;
                        case BAD_DIMS:
                            System.out.println("Złe wymiary!");
                            break;
                        default: return;
                    }

                    updateMaze();
                }
            }
        });

        setJMenuBar(bar);
    }


}
