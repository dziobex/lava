package org.maze;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
    public static MazeLoader loader = null;

    public MainWindow() {
        setTitle("Lava - main window");
        menuBarSetup();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800, 600);

        System.out.println(loader);

        updateMaze();

        setVisible(true);
    }

    void updateMaze() {
        System.out.println("A");
        if ( loader == null || loader.GetMaze() == null )
            return;
        System.out.println("B");

        if ( mazeScrollPane != null)
            this.remove(mazeScrollPane);

        mazePanel = new MazePanel(loader.GetMaze());
        mazeScrollPane = new JScrollPane(mazePanel);
        mazeScrollPane.setPreferredSize(mazePanel.getPreferredSize());

        this.add(mazeScrollPane);
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
