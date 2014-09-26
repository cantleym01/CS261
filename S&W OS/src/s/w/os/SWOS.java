/**
 * This operating system is made using Java Swing and the Command Design Pattern
 */

package s.w.os;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

public class SWOS {
    public static void main(String[] args) 
    {
        Controller SW = new Controller(); //create the controller
        SW.runOS(); //run the OS
    }
}
