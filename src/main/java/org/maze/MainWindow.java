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
    private JScrollPane mazeScrollPane;

    public MainWindow() {
        setTitle("Lava - main window");
        menuBarSetup();

        int[][] mazeData = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1},
                {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1},
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800, 600);

        Maze maze = new Maze(mazeData, 20, 20);
        MazePanel mazePanel = new MazePanel(maze);

        mazeScrollPane = new JScrollPane(mazePanel);
        mazeScrollPane.setPreferredSize(mazePanel.getPreferredSize());
        getContentPane().add(mazeScrollPane);

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

                    MazeLoader loader = new MazeLoader();
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

                    // show it
                    MazePanel mazePanel = new MazePanel(loader.GetMaze());
                    mazeScrollPane = new JScrollPane(mazePanel);
                    mazeScrollPane.setPreferredSize(mazePanel.getPreferredSize());
                    getContentPane().add(mazeScrollPane);

                    setVisible(true);

                }
            }
        });

        setJMenuBar(bar);
    }
}
