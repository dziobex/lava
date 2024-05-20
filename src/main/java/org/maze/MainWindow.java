package org.maze;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow extends JFrame {
    private JMenuBar bar;
    private JMenu openItem, saveItem;
    private JMenuItem openTextItem, openBinaryItem, saveTextItem, saveBinaryItem, saveImageItem;
    private JPanel mainPanel;
    private MazePanel mazePanel;
    private JScrollPane mazeScrollPane;
    private JButton solveButton, setStartButton, setEndButton, removeButton;
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
        setResizable(false);
        setVisible(true);

        menuBarSetup();
        toolPanelSetup();
        updateMaze();

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
        mazePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // get the location on the screen
                // and transform it into the location on the jpanel
                Point location = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(location, mazePanel);

                Point xypos = new Point(location.x / mazePanel.cellSize, location.y / mazePanel.cellSize);

                //System.out.println(String.format("%s: x is %d and y is %d", location, xypos.x, xypos.y));

                if ( setStart && xypos.x < mazePanel.getMaze().getWidth() && xypos.y < mazePanel.getMaze().getHeight()
                        && (mazePanel.getMaze().getEndLocation() == null ||
                        (mazePanel.getMaze().getEndLocation().x != xypos.x
                        || mazePanel.getMaze().getEndLocation().y != xypos.y))) {

                    System.out.println("New start");
                    mazePanel.setStartLocation(xypos);
                }
                else if ( setEnd && xypos.x < mazePanel.getMaze().getWidth() && xypos.y < mazePanel.getMaze().getHeight()
                        && (mazePanel.getMaze().getStartLocation() == null ||
                        (mazePanel.getMaze().getStartLocation().x != xypos.x
                                || mazePanel.getMaze().getStartLocation().y != xypos.y))) {

                    System.out.println("New end");
                    mazePanel.setEndLocation(xypos);
                }
            }
        });
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
                            System.out.println(String.format("Labirynt (%s) został załadowany", selectedFile.getName()));
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
