package nonogram;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;


/**
 * The NonogramPanel class acts as a bridge between the Nonogram class and the user by visually representing the puzzle and allowing the player to interact with it. 
 * There are several ActionListeners in the class for buttons that allow the user to delete, undo, save, and load the game. 
 *
 * @author (VIRTUE AYOKANMI AINA. W22016723)
 * @version (12/01/2023)
 */
public class NonogramPanel extends JPanel implements Observer {
    /**
     * Default constructor, includes the game grid of PanelCells, plus
     * clear and undo buttons, and a status area for feedback
     */
    public NonogramPanel() {

        Scanner fs = null;
        try {
            fs = new Scanner(new File(NGFILE));
        } catch (FileNotFoundException e) {
            System.out.println(NGFILE + "not found");
        }
        puzzle = new Nonogram(fs);

        puzzle.addObserver(this);


        // set up main puzzle grid
        int row, col;
        grid = new JPanel(new GridLayout(Nonogram.MIN_SIZE, Nonogram.MIN_SIZE));
        cells = new PanelCell[Nonogram.MIN_SIZE][Nonogram.MIN_SIZE];
        for (row = 0; row < Nonogram.MIN_SIZE; row++) {
            for (col = 0; col < Nonogram.MIN_SIZE; col++) {
                cells[row][col] = new PanelCell(this, row, col);
                grid.add(cells[row][col]);
            }
        }

        JPanel rowValues = new JPanel(new GridLayout(Nonogram.MIN_SIZE, 1));
        JPanel colValues = new JPanel(new GridLayout(1, Nonogram.MIN_SIZE));

        for (row = 0; row < Nonogram.MIN_SIZE; row++) {
            int[] rowInput = puzzle.getRowNums(row);
            JLabel constraintRow = new JLabel(Arrays.toString(rowInput));
            JPanel constraintPanelRow = new JPanel();
            constraintPanelRow.add(constraintRow);
            rowValues.add(constraintPanelRow);
        }
        for (col = 0; col < Nonogram.MIN_SIZE; col++) {
            int[] colInput = puzzle.getColNums(col);
            JLabel constraintCol = new JLabel(Arrays.toString(colInput));
            JPanel constraintPanel = new JPanel();
            constraintPanel.add(constraintCol);
            colValues.add(constraintPanel);
        }
        JPanel placeHolder = new JPanel(new BorderLayout());
        placeHolder.add(rowValues, BorderLayout.WEST);
        placeHolder.add(colValues, BorderLayout.NORTH);
        placeHolder.add(grid, BorderLayout.CENTER);
        setLayout(new BorderLayout());
        add(placeHolder, BorderLayout.CENTER);

        // set up buttons & status bar
        clear = new JButton("Clear");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                clear();
            }
        });

        undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                undo();
            }
        });
        save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                save();
            }
        });
        load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                load();
       }
        });
        JPanel center = new JPanel(new GridLayout(2, 2));
        center.add(clear);
        center.add(undo);
        center.add(save);
        center.add(load);
        center.setPreferredSize(new Dimension(50, 50));
        add(center, BorderLayout.NORTH);
        status = new JTextArea();
        add(new JScrollPane(status), BorderLayout.SOUTH);


        // undo stack
        puzzleMoves = new Stack<Assign>();


    }

    /**
     * Updates the PanelCells when the underlying model cells are assigned
     *
     * @param o   the observable
     * @param arg the cell that was assigned
     */

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null)
            throw new NonogramException("arg (Cell) is null");
        Cell c = (Cell) arg;

        int x = c.getState();
        if (x == Nonogram.UNKNOWN) {
            cells[c.getRow()][c.getCol()].setBackground(Color.GRAY);
        } else if (x == Nonogram.FULL) {
            cells[c.getRow()][c.getCol()].setBackground(Color.BLACK);
        } else if (x == Nonogram.EMPTY) {
            cells[c.getRow()][c.getCol()].setBackground(Color.WHITE);
        }
    }

    /**
     * Resets the game
     */
    void clear() {
        for (int row = 0; row < Nonogram.MIN_SIZE; row++)
            for (int col = 0; col < Nonogram.MIN_SIZE; col++)
                cells[row][col].clear();
        puzzle.clear();
        puzzle.addObserver(this);

    }

    /**
     * undo Puzzle
     */
    public void undo() {
        puzzleMoves.pop();
        clear();
        for (Assign a : puzzleMoves) {
            puzzle.setState(a);
            //System.out.println(a.toString());
        }
    }

    /**
     * Save puzzle
     */
    public void save() {
        try {
            PrintStream ps = new PrintStream((NNGFILE));
            for (Assign a : puzzleMoves)
                ps.println(a.toStringForFile());
            ps.close();
            //System.out.println("game saved to file");
        } catch (IOException e) {
            //System.out.println("an input output error occurred");
        }
    }

    /**
     * Load Puzzle
     */
    public void load() {
        File file = new File(NNGFILE);
        try (Scanner fs = new Scanner(file)) {
            clear();
            while (fs.hasNextInt()) {
                Assign a = new Assign(fs);
                puzzle.setState(a);
                puzzleMoves.push(a);
            }
            fs.close();
            //System.out.println("game loaded from file");
        } catch (IOException e) {
           // System.out.println("an input output error occured");
        }
    }

    /**
     * Sets the status bar to a given string
     *
     * @param ST the new status
     */
    void setStatus(String st) {
        status.setText(st);
    }

    void makeMove(int row, int col, int state) {
        if ((row < 0) || (row > Nonogram.MIN_SIZE))
            throw new NonogramException("invalid row (" + row + ")");
        if ((col < 0) || (col > Nonogram.MIN_SIZE))
            throw new NonogramException("invalid col (" + col + ")");
        Assign userMove = new Assign(row, col, state);
        puzzleMoves.push(userMove);
        puzzle.setState(userMove);
        gameisWon();
    }




    /**
     * Checks if a particular assignment (move) is valid for the underlying cell
     *
     * @param //row   the cell row
     * @param //col   the cell column
     * @param //state the assignment (EMPTY)
     * @return true if valid
     */
    void gameisWon() {
        if (puzzle.isSolved()) {

            setStatus("Game is Solved");
          
        }
        
        

    }

//    /**
//     * A trace method for debugging (active when traceOn is true)
//     *
//     * @param s the string to output
//     */
//    public static void trace(String s) {
//        if (traceOn)
//            System.out.println("trace: " + s);
//    }

    public static void main(String[] args) {
        JFrame gameFrame = new JFrame("Nonogram");
        NonogramPanel panel = new NonogramPanel();
        gameFrame.add(panel);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.pack();
        gameFrame.setSize(400, 400);
        gameFrame.setVisible(true);


    }

    private PanelCell[][] cells = null;
    private JPanel grid = null;
    private JButton clear = null;
    private JButton undo = null;
    private JButton save = null;
    private JButton load = null;
    private JTextArea status = null;
    private Nonogram puzzle = null;

    private JFrame gameFrame = null;

    private Stack<Assign> puzzleMoves = null;

    private static final String NGFILE = "nons/tiny.non";
    private static final String NNGFILE = "nons/test.non";

    private static final boolean traceOn = false;
}
