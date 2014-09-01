/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package s.w.os;

import javax.swing.JFrame; //create the frame of the OS
import javax.swing.JLabel; //make labels, such as Time and Date
import javax.swing.JPanel; //Panels to do things
import javax.swing.JMenu; //Menu for things such as File
import javax.swing.JMenuBar; //The bar to hold all of the Menu items
import javax.swing.JMenuItem; //Sub items, like an exit command in the File Menu
import java.awt.BorderLayout; //Use this to add layout for the OS
import java.awt.Color;

//stuff to update time and date as the OS runs
import javax.swing.Timer;
import java.util.Date;  

//Event Items
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//imports to keep track of the date and time
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        
        JPanel OSToolBarPanel = new JPanel();
        
        //Now do things to get OS labels (version, date, and time)
        String[] OSDataStr = new String [3];//The data in string form
        JLabel[] OSDataLabels = new JLabel[3]; //The data's cooresponding labels
        
        OSDataStr [0] = "Ver 0.0.1"; //the version of the OS
                
        Calendar cal1 = Calendar.getInstance(); //create calendar obj
        cal1.getTime(); //get the time data (includes date as well
    	SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/YYYY"); //get format
    	OSDataStr[1] = "Date: " + sdf1.format(cal1.getTime()); //put the date into the data
        
        Calendar cal2 = Calendar.getInstance(); //create calendar obj
        cal2.getTime(); //get the time data (includes date as well
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a"); //get format
    	OSDataStr[2] = sdf2.format(cal2.getTime()); //put the time into the data
        
        for (int i = 0; i < OSDataStr.length; i++)
        {
            OSDataLabels[i] = new JLabel(OSDataStr[i]); //name the labels
        }
        
        //create the display
        Initialize(OSFrame, OSMenu, fileApp, OSToolBarPanel, OSDataLabels);
        
        //update the date and time as the OS runs
        updateDateAndTime(sdf1, sdf2, OSDataLabels);
    }
    
    private void Initialize(JFrame frame, JMenuBar menu, JMenu file, JPanel toolPanel,
                            JLabel[] ToolData)
    {
        createFrame(frame); //create the frame of the OS
        createMenu(menu, file); //create the menu for the OS
        addLayout(frame, menu, toolPanel, ToolData);
    }
    
    private void updateDateAndTime(final SimpleDateFormat sdf1, final SimpleDateFormat sdf2,
                                final JLabel OSDataLabels[])
    {
        //Update time & date
        ActionListener timeListener = new ActionListener() //listener for time
        {  
            //once 1 second has passed update, but the display is in H:MM
            public void actionPerformed(ActionEvent timer)  
            {  
                Date date = new Date();  
                String time1 = sdf1.format(date);  //update date
                OSDataLabels[1].setText("Date: " +  time1);  //update date display
                
                String time2 = sdf2.format(date);  //update time 
                OSDataLabels[2].setText(time2);  //update time display
            }  
        };  
        
        Timer timer = new Timer(1000, timeListener); //update stuff ever 1 second
        timer.start(); //start the timer
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

        menu.add(file); //Add the file to the menu
    }
    
    private void addLayout(JFrame frame, JMenuBar menu, JPanel toolPanel,
                            JLabel[] ToolData)
    {
        //Add the menu to the top of the layout 
        frame.add(BorderLayout.NORTH, menu);
        
        toolPanel.setBackground(Color.magenta); //change the toolPanel's color
        
        //Add the toolBarPanel to the bottom of the layout
        frame.add(BorderLayout.SOUTH, toolPanel);
        
        //Set color for labels (It is darkgrey by default)
        for(int i = 0; i < ToolData.length; i++)
        {
            ToolData[i].setForeground(Color.white);
        }
        
        toolPanel.add(ToolData[0]); //Add the version
        toolPanel.add(ToolData[1]); //Add the date
        toolPanel.add(ToolData[2]); //Add the time
       
        frame.setVisible(true); //update the layout
    }
}
