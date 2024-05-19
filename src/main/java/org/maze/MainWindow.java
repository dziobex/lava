package org.maze;
import javax.swing.*;
import java.awt.*;
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

    boolean setStart, setEnd;

    public MainWindow() {
        this.setStart = this.setEnd = false;

        setTitle("Lava - LAbirynth in jaVA");

        setIconImage(new ImageIcon("./assets/favicon.png").getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setSize(800, 600);
        setContentPane(mainPanel);

        menuBarSetup();
        toolPanelSetup();
        updateMaze();

        setResizable(false);
        setVisible(true);

        setStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStart = !setStart;
                if ( setEnd && setStart ) {
                    setEnd = false;
                    setEndButton.setBackground(Color.WHITE);
                }
                setStartButton.setBackground(setStart ? Color.GRAY : Color.WHITE);
            }
        });

        setEndButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEnd = !setEnd;
                if ( setStart && setEnd ) {
                    setStart = false;
                    setStartButton.setBackground(Color.WHITE);
                }
                setEndButton.setBackground(setEnd ? Color.GRAY : Color.WHITE);
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
        bar.setFont(solveButton.getFont());

        openItem = new JMenu("Open From");
        openItem.setFont(solveButton.getFont());

        openTextItem = new JMenuItem("text");
        openTextItem.setFont(solveButton.getFont());

        openBinaryItem = new JMenuItem("binary");
        openBinaryItem.setFont(solveButton.getFont());

        openItem.add(openTextItem);
        openItem.add(openBinaryItem);
        bar.add(openItem);

        saveItem = new JMenu("Save As");
        saveItem.setFont(solveButton.getFont());

        saveTextItem = new JMenuItem("text");
        saveTextItem.setFont(solveButton.getFont());

        saveBinaryItem = new JMenuItem("binary");
        saveBinaryItem.setFont(solveButton.getFont());

        saveImageItem = new JMenuItem("image");
        saveImageItem.setFont(solveButton.getFont());

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

    void toolPanelSetup() {
        solveButton.setIcon(new ImageIcon("./assets/solvebtn.png"));
        setStartButton.setIcon(new ImageIcon("./assets/startbtn.png"));
        setEndButton.setIcon(new ImageIcon("./assets/endbtn.png"));
        removeButton.setIcon(new ImageIcon("./assets/removebtn.png"));
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( mazePanel == null ) return;
                if ( JOptionPane.showConfirmDialog(
                        null,
                        "The maze will be removed. Are you sure?",
                        "Bye bye?",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ) {
                    mazePanel = null;
                    mazeScrollPane.setViewportView(fillingLabel);
                }
            }
        });
    }
}
