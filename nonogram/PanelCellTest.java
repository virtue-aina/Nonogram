package nonogram;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class PanelCellTest.
 *
 * @author  (VIRTUE AYOKANMI AINA. W22016723)
 * @version (12/01/23)
 */
public class PanelCellTest
{
    /**
     * Default constructor for test class PanelCellTest
     */
    public PanelCellTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
    @Test(expected = NonogramException.class)
    public void testConstructor_nullPanel() {
        PanelCell cell = new PanelCell(null, 0, 0);
    }

    @Test(expected = NonogramException.class)
    public void testConstructor_invalidRow() {
        PanelCell cell = new PanelCell(new NonogramPanel(), -1, 0);
    }

    @Test(expected = NonogramException.class)
    public void testConstructor_invalidCol() {
        PanelCell cell = new PanelCell(new NonogramPanel(), 0, -1);
    }
     @Test
    public void testClear() {
        PanelCell cell = new PanelCell(new NonogramPanel(), 0, 0);
        cell.setBackground(Color.BLACK);
        cell.clear();
        assertEquals(Color.GRAY, cell.getBackground());
    }

 
}
