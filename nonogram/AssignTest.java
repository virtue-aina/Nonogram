package nonogram;



import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.Scanner;

import static org.junit.Assert.*;
/**
 * The test class AssignTest.
 *
 * @author  (Virtue Ayokanmi Aina)
 * @version (a version number or a date)
 */
public class AssignTest
{
   /**
//     * Default constructor for test class AssignTest
//     */
    public AssignTest()
   {
}

   /**
//     * Sets up the test fixture.
//     *
//     * Called before every test case method.
//     */
    @Before
    public void setUp()
   {
   }

   /**
//     * Tears down the test fixture.
//     *
//     * Called after every test case method.
//     */
    @After
   public void tearDown()
   {
  }

@Test (expected = IllegalArgumentException.class)
   public void testAssignWithInvalidRow()
   {
       new Assign(-1, 1, Nonogram.FULL);
   }
  @Test (expected = NonogramException.class)
  public void testAssignWithNullScanner() {
      new Assign(null);
}
public void testConstructor() {
        Assign a = new Assign(2, 3, Nonogram.FULL);
        assertEquals(2, a.getRow());
        assertEquals(3, a.getCol());
        assertEquals(Nonogram.FULL, a.getState());
    }
@Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidCol() {
        Assign a = new Assign(2, -3, Nonogram.FULL);
    }
    
@Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidState() {
        Assign a = new Assign(2, 3, -1);
    }
    

    
       @Test
    public void testToStringForFile() {
        Assign a = new Assign(2, 3, Nonogram.FULL);
        assertEquals("2 3 1", a.toStringForFile());
    }
    
}

