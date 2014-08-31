/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package s.w.os;

import javax.swing.JFrame; //create the frame of the OS
import javax.swing.JButton; //make buttons
import javax.swing.JLabel; //make labels, such as Time and Date
import javax.swing.JPanel; //Panels to do things
import javax.swing.JMenu; //Menu for things such as File, and Help
import javax.swing.JSeparator; //Separator to make things pretty
import javax.swing.JOptionPane; //Use this for Yes, No confirmations

/**
 *
 * @author Michael
 */
public class Controller {
    
    //Command Interface
    public interface Command
    {
        void execute();
    }
    
    //constructor
    public Controller ()
    {
    }
    
    //Run the OS
    public void runOS()
    {
        //create the frame
        createFrame();
    }
    
    //Create the frame for the OS
    private void createFrame()
    {
        JFrame OSFrame = new OSFrame(); //Create the framework for the OS
        OSFrame.setTitle("S&W OS"); //set the title
        OSFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        OSFrame.setVisible(true); //make it visible
    }
    
}
