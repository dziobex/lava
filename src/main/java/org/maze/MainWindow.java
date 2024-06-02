package org.maze;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
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
    private JButton clearSolutionButton;

    public static Loader loader = null;
    public static Saver saver = null;
    boolean setStart, setEnd;

    public MainWindow() {
        this.setStart = this.setEnd = false;

        setTitle("Lava - LAbirynth in jaVA");
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

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Maze.getInstance().solveMaze();
                mazePanel.refresh();
            }
        });

        clearSolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Maze.getInstance().clearPath();
                mazePanel.refresh();
            }
        });

        niceLookSetup();
    }

    void updateMaze() {
        if ( loader == null || Maze.getInstance().IsEmpty() )
            return;
        mazePanel = new MazePanel();
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

                if ( setStart && xypos.x < Maze.getInstance().getWidth() && xypos.y < Maze.getInstance().getHeight()
                        && (Maze.getInstance().getEndLocation() == null ||
                        (Maze.getInstance().getEndLocation().x != xypos.x
                        || Maze.getInstance().getEndLocation().y != xypos.y))) {

                    System.out.println("New start");
                    mazePanel.setStartLocation(xypos);
                }
                else if ( setEnd && xypos.x < Maze.getInstance().getWidth() && xypos.y < Maze.getInstance().getHeight()
                        && (Maze.getInstance().getStartLocation() == null ||
                        (Maze.getInstance().getStartLocation().x != xypos.x
                                || Maze.getInstance().getStartLocation().y != xypos.y))) {

                    System.out.println("New end");
                    mazePanel.setEndLocation(xypos);
                }
            }
        });
    }

    void menuBarSetup() {
        bar = new JMenuBar();

        /* here is the composite pattern? */

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

        openBinaryItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                openFilePattern(LoaderFactory.LoadType.BINARY);
            }
        });

        openTextItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                openFilePattern(LoaderFactory.LoadType.TEXT);
            }
        });

        saveTextItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                makeFilePattern(SaverFactory.SaveType.TEXT);
            }
        });

        saveBinaryItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                makeFilePattern(SaverFactory.SaveType.BINARY);
            }
        });

        saveImageItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                openFilePattern(LoaderFactory.LoadType.IMAGE);
            }
        });

        setJMenuBar(bar);
    }

    void openFilePattern(LoaderFactory.LoadType type) {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory( new File("./samples") );
        fileChooser.setFileFilter( new FileNameExtensionFilter(type + " FILES",
                type == LoaderFactory.LoadType.TEXT ? "txt" : type == LoaderFactory.LoadType.BINARY ? "bin" : "PNG") );

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            loader = LoaderFactory.CreateLoader(type);
            Loader.LoadResult loadResult = loader.Load(selectedFile);

            switch (loadResult) {
                default:
                case SUCCESS:
                    System.out.println(String.format("Labirynt (%s) został załadowany", selectedFile.getName()));
                    break;
                case BAD_DIMS:
                    System.out.println("Złe wymiary!");
                    JOptionPane.showMessageDialog(null, "Złe wymiary labiryntu!", "O nie!", JOptionPane.WARNING_MESSAGE);
                    break;
                case BAD_CHARS:
                    System.out.println("Złe znaki!");
                    JOptionPane.showMessageDialog(null, "Użyto nieprawidłowych znaków!", "O nie!", JOptionPane.WARNING_MESSAGE);
                    break;
                case INVALID_STRUCT:
                    System.out.println("Niepoprawna struktura!");
                    JOptionPane.showMessageDialog(null, "Zła struktura pliku!", "O nie!", JOptionPane.WARNING_MESSAGE);
                    break;
            }

            updateMaze();
        }
    }

    void makeFilePattern(SaverFactory.SaveType type) {
        JFileChooser fileChooser = new JFileChooser();
        String extension = type == SaverFactory.SaveType.TEXT ? "txt" : type == SaverFactory.SaveType.BINARY ? "bin" : "png";

        fileChooser.setCurrentDirectory( new File("./solutions") );
        fileChooser.setFileFilter( new FileNameExtensionFilter(type + " FILES",
                extension) );

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = new File(fileChooser.getCurrentDirectory().getName() + '/' + fileChooser.getSelectedFile().getName() + "." + extension);

            saver = SaverFactory.CreateSaver(type);
            Saver.SaveResult saveResult = saver.Save(selectedFile);

            switch (saveResult) {
                default:
                case SUCCESS:
                    System.out.println(String.format("Zapisano ścieżkę do pliku (%s)", selectedFile.getName()));
                    break;
                case NO_MAZE:
                    System.out.println("Brak labiryntu!");
                    JOptionPane.showMessageDialog(null, "Nie ma załadowanego labiryntu!", "O nie!", JOptionPane.WARNING_MESSAGE);
                    break;
                case NO_SPACE:
                    System.out.println("Problemy z plikiem!");
                    JOptionPane.showMessageDialog(null, "Nie udało się stworzyć lub otworzyć pliku!", "O nie!", JOptionPane.WARNING_MESSAGE);
                    break;
            }

            updateMaze();
        }
    }

    void toolPanelSetup() {
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

    // setting icons, fonts etc (kinda useless) stuff
    void niceLookSetup() {
        Font myFont = new Font("Artifakt Element", Font.BOLD, 13);
        UIDefaults defaultUI = UIManager.getDefaults();
        defaultUI.put("Button.font", myFont);
        defaultUI.put("Label.font", myFont);
        defaultUI.put("Menu.font", myFont);
        defaultUI.put("MenuItem.font", myFont);
        defaultUI.put("TextArea.font", myFont);
        defaultUI.put("Table.font", myFont);
        defaultUI.put("List.font", myFont);

        setIconImage(new ImageIcon("./assets/favicon.png").getImage());
        solveButton.setIcon(new ImageIcon("./assets/solvebtn.png"));
        setStartButton.setIcon(new ImageIcon("./assets/startbtn.png"));
        setEndButton.setIcon(new ImageIcon("./assets/endbtn.png"));
        clearSolutionButton.setIcon(new ImageIcon("./assets/clearbtn.png"));
        removeButton.setIcon(new ImageIcon("./assets/removebtn.png"));
    }
}
