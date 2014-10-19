/**
 * This operating system is made using Java Swing and the Command Design Pattern
 */

package s.w.os;

import java.util.LinkedList;

public class SWOS {
    public static void main(String[] args) 
    {
        Controller SW = new Controller(); //create the controller
        SW.runOS(); //run the OS
    }
}
