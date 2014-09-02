/**
 * This operating system is made using Java Swing and the Command Design Pattern
 */

package s.w.os;

import java.awt.Desktop;
import java.io.File;
import java.io.FileFilter;

public class SWOS {
    public static void main(String[] args) {
        Controller control = new Controller(); //create the controller
        control.runOS(); //run the OS
        /*
        try
        {
            Desktop.getDesktop().open(new File("C:/Users/Michael/Documents/GitHub/CS261/S&W OS"));
        }
        catch (Exception ex){} 
        */
    }
}
