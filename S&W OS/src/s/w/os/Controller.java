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
import javax.swing.JMenu; //Menu for things such as File
import javax.swing.JMenuBar; //The bar to hold all of the Menu items
import javax.swing.JMenuItem; //Sub items, like an exit command in the File Menu
import javax.swing.JSeparator; //Separator to make things pretty
import javax.swing.JOptionPane; //Use this for Yes, No confirmations
import java.awt.BorderLayout; //Use this to add layout for the OS

//Event Items
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Controller {
    
    //constructor
    public Controller ()
    {
    }
    
    //Run the OS
    public void runOS()
    {
        JFrame OSFrame = new OSFrame(); //create the frame object
        
        JMenuBar OSMenu = new JMenuBar(); //create the menuBar object
        
        //Create the "File" App for the menu bar, it holds commands such as:
        //Open Directory, History, Help, and Exit
        JMenu fileApp = new JMenu("File");
        
        Initialize(OSFrame, OSMenu, fileApp); //create the display
    }
    
    private void Initialize(JFrame frame, JMenuBar menu, JMenu file)
    {
        createFrame(frame); //create the frame of the OS
        createMenu(menu, file); //create the menu for the OS
        addLayout(frame, menu);
    }
    
    //Create the frame of the OS
    private void createFrame(JFrame frame)
    {
        frame.setTitle("S&W OS"); //set the title
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); //make it visible
    }
    
    //Create the menu for the OS
    private void createMenu(JMenuBar menu, JMenu file)
    {   
        ActionListener listener = new MenuTabListener(); //listener for the tabs
        
        //Create the Directory button for the file menu
        JMenuItem dir = new DirectoryTab();
        dir.setLabel("Directory"); //set tab name
        dir.addActionListener(listener); //add a listener to the tab
       
        //Create the History button for the file menu
        JMenuItem his = new HistoryTab();
        his.setLabel("History"); //set tab name
        his.addActionListener(listener); //add a listener to the tab
        
        //Create the Help button for the file menu
        JMenuItem help = new HelpTab();
        help.setLabel("Help"); //set tab name
        help.addActionListener(listener); //add a listener to the tab
        
        //Create the Exit button for the file menu
        JMenuItem exit = new ExitTab();
        exit.setLabel("Exit"); //set tab name
        exit.addActionListener(listener); //add a listener to the tab
        
        file.add(dir); //Add the directory tab to the file Menu
                file.addSeparator(); //Add a separator to make things pretty
        file.add(his); //Add the history tab to the file Menu
                file.addSeparator(); //Add a separator to make things pretty
        file.add(help); //Add the history tab to the file Menu
                file.addSeparator(); //Add a separator to make things pretty
        file.add(exit); //Add the history tab to the file Menu

        menu.add(file);
    }
    
    private void addLayout(JFrame frame, JMenuBar menu)
    {
        frame.add(BorderLayout.NORTH, menu);  
    }
    
}
