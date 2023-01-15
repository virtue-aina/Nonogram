package nonogram;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Write a description of class PanelCell here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PanelCell extends JButton {
    /**
     * Constructor, that also adds mouse  adapters
     *
     * @param p the parent NonogramPanel
     * @param r the row in the puzzle
     * @param c the column in the puzzle
     */
    public PanelCell(NonogramPanel p, int r, int c) {
        if (p == null)
            throw new NonogramException("cannot have null panel");
        if ((r < 0) || (r > Nonogram.MIN_SIZE))
            throw new NonogramException("invalid row (" + r + ")");
        if ((c < 0) || (c > Nonogram.MIN_SIZE))
            throw new NonogramException("invalid col (" + c + ")");

        panel = p;
        row = r;
        col = c;
        addMouseListener(new MouseAdapter() {
            

            public void mouseClicked(MouseEvent e) {
                Color x = getBackground();
                if (x == Color.GRAY) {
                    panel.makeMove(row, col, Nonogram.FULL);
                    setBackground(Color.BLACK);
                } else if (x == Color.BLACK) {
                    panel.makeMove(row, col, Nonogram.EMPTY);
                    setBackground(Color.WHITE);
                } else if (x == Color.WHITE) {
                    panel.makeMove(row, col, Nonogram.UNKNOWN);
                    setBackground(Color.GRAY);
                }
            }


        });
        setBackground(Color.GRAY);
        setHorizontalAlignment(CENTER);
        setMargin(new Insets(5, 5, 5, 5));
        setForeground(Color.black);
        setFont(font);
        setPreferredSize(new Dimension(30, 30));
    }


    /**
     * Clears the text in the cell
     */
    void clear() {
        setBackground(Color.GRAY);
    }


    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */


    Font font = new Font("Dialog", Font.BOLD, 20);

    NonogramPanel panel;
    int row;
    int col;

  final Color EMPTY = Color.WHITE;
  final Color FULL = Color.BLACK;
  final Color UNKNOWN = Color.GRAY;


}
