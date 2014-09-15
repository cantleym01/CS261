package s.w.os;

import javax.swing.JFrame; //Create the frame
import javax.swing.JLabel;  //Make Labels
import javax.swing.JMenu; //Menu such as file
import javax.swing.JMenuBar; //Menu bar to hold menus
import javax.swing.JMenuItem; //Submenus for menus such as file
import javax.swing.JPanel; //Panels to make better design
import javax.swing.Timer; //Timer to update date and time as the OS runs
import java.awt.BorderLayout; //create the frame of the OS
import java.awt.Color; //make labels, such as Time and Date
import java.awt.event.ActionEvent; //Panels to do things
import java.awt.event.ActionListener; //Menu for things such as File
import java.text.SimpleDateFormat; //The bar to hold all of the Menu items
import java.util.Calendar; //Sub items, like an exit command in the File Menu
import java.util.Date; //Use this to add layout for the OS
import javax.swing.ImageIcon; //This allows the use of images

public class Controller 
{    
    private JFrame OSFrame = new OSFrame(); //create the frame object
    
    private JMenuBar OSMenu = new JMenuBar(); //create the menuBar object
    
    private JPanel OSToolBarPanel = new JPanel(); //The panel for the bottom of the OS
    
    private String OSVer = "Ver 0.0.1"; //The current version, change as deemed needed
    
    //Create the "File" App for the menu bar, it holds commands such as:
    //Open Directory, History, Help, and Exit (maybe also Aliasing in the future)
    private JMenu file = new JMenu("File");
    
    //Create the "PCBCommands" App for the menu bar, it holds commands such as:
    //CreatePCB, DeletePCB, Block, Unblock, Suspend, Resume, etc.
    private JMenu PCBCommands = new JMenu("PCBCommands");
    
    private PCBList pcbList = new PCBList(); //create the list of PCBs (includes queues)
    
    //constructor
    public Controller ()
    {
    }
    
    //Run the OS
    public void runOS()
    {   
        //Do things to get OS labels (version, date, and time)
        String[] OSDataStr = new String [3];//The data in string form
        JLabel[] OSDataLabels = new JLabel[3]; //The data's cooresponding labels
        
        OSDataStr [0] = OSVer; //the version of the OS
                
        Calendar cal1 = Calendar.getInstance(); //create calendar obj
        cal1.getTime(); //get the time data (includes date as well)
    	SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/YYYY"); //get format (Month/Day/Year
    	OSDataStr[1] = "Date: " + sdf1.format(cal1.getTime()); //put the date into the data
        
        Calendar cal2 = Calendar.getInstance(); //create calendar obj
        cal2.getTime(); //get the time data (includes date as well)
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a"); //get format (Hour:Minute AM/PM)
    	OSDataStr[2] = "Time: " + sdf2.format(cal2.getTime()); //put the time into the data
        
        //Name each label: 0 = version, 1 = date, 2 = time
        for (int i = 0; i < OSDataStr.length; i++)
        {
            OSDataLabels[i] = new JLabel(OSDataStr[i]); //name the labels
        }
        
        //create the display
        Initialize(OSDataLabels);
        
        //update the date and time as the OS runs
        updateDateAndTime(sdf1, sdf2, OSDataLabels);
        
        //apply the background image to the OS
        applyBkg(); 
        
        OSFrame.setVisible(true); //Update display
    }
    
    private void Initialize(JLabel[] ToolData)
    {
        createFileMenu(); //create the file menu for the OS
        createPCBMenu(); //create the PCB menu for the OS
        addLayout(ToolData);
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
                OSDataLabels[2].setText("Time: " + time2);  //update time display
            }  
        };  
        
        Timer timer = new Timer(1000, timeListener); //update stuff ever 1 second
        timer.start(); //start the timer
    }
    
    //Create the file menu for the OS
    private void createFileMenu()
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

        OSMenu.add(file); //Add the file to the menu
    }
    
    //Create the PCBCommands menu for the OS
    private void createPCBMenu()
    {   
        //This listener does not have a seperate file for command design pattern,
        //because the PCBList needs to be modified with the PCB commands.
        ActionListener listener = new
            //override the ActionListner's actionPerformed for this button
            ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    CommandPCB command = (CommandPCB)e.getSource();
                    pcbList = command.execute(pcbList); //get all changes
                }
            };
        
        //Create the Create PCB button for the PCBCommand menu
        /*JMenuItem createPCB = new CreatePCB();
        createPCB.setLabel("Create PCB"); //set tab name
        createPCB.addActionListener(listener); //add a listener to the tab
        
        //Create the Delete PCB button for the PCBCommand menu
        JMenuItem deletePCB = new DeletePCB();
        deletePCB.setLabel("Delete PCB"); //set tab name
        deletePCB.addActionListener(listener); //add a listener to the tab
               
        //Create the Block PCB button for the PCBCommand menu
        JMenuItem blockPCB = new BlockPCB();
        blockPCB.setLabel("Block PCB"); //set tab name
        blockPCB.addActionListener(listener); //add a listener to the tab
        
        //Create the un-Block PCB button for the PCBCommand menu
        JMenuItem unBlockPCB = new UnBlockPCB();
        unBlockPCB.setLabel("Un-Block PCB"); //set tab name
        unBlockPCB.addActionListener(listener); //add a listener to the tab     */
        
        //Create the suspend PCB button for the PCBCommand menu
        JMenuItem suspendPCB = new SuspendPCB();
        suspendPCB.setLabel("Suspend PCB"); //set tab name
        suspendPCB.addActionListener(listener); //add a listener to the tab
        
        //Create the resume PCB button for the PCBCommand menu
        JMenuItem resumePCB = new ResumePCB();
        resumePCB.setLabel("Resume PCB"); //set tab name
        resumePCB.addActionListener(listener); //add a listener to the tab
        
        //Create the set priority PCB button for the PCBCommand menu
        JMenuItem setPriorPCB = new SetPriorityPCB();
        setPriorPCB.setLabel("Set PCB Priority"); //set tab name
        setPriorPCB.addActionListener(listener); //add a listener to the tab
        
        //Create the show PCB button for the PCBCommand menu
        JMenuItem showPCB = new ShowPCB();
        showPCB.setLabel("Show PCB"); //set tab name
        showPCB.addActionListener(listener); //add a listener to the tab
        
        //Create the show all PCB button for the PCBCommand menu
        JMenuItem showAllPCB = new ShowAllPCB(0); //show all
        showAllPCB.setLabel("Show All PCB"); //set tab name
        showAllPCB.addActionListener(listener); //add a listener to the tab
        
        //Create the show all PCB button for the PCBCommand menu
        JMenuItem showReadyPCB = new ShowAllPCB(1); //show all
        showReadyPCB.setLabel("Show Ready PCB"); //set tab name
        showReadyPCB.addActionListener(listener); //add a listener to the tab
      
        //Create the show all PCB button for the PCBCommand menu
        JMenuItem showBlockedPCB = new ShowAllPCB(2); //show all
        showBlockedPCB.setLabel("Show Blocked PCB"); //set tab name
        showBlockedPCB.addActionListener(listener); //add a listener to the tab
        
        /*PCBCommands.add(createPCB); //Add the directory tab to the file Menu
                PCBCommands.addSeparator(); //Add a separator to make things pretty
        PCBCommands.add(deletePCB); //Add the directory tab to the file Menu
                PCBCommands.addSeparator(); //Add a separator to make things pretty
        PCBCommands.add(blockPCB); //Add the directory tab to the file Menu
                PCBCommands.addSeparator(); //Add a separator to make things pretty
        PCBCommands.add(unBlockPCB); //Add the directory tab to the file Menu
                PCBCommands.addSeparator(); //Add a separator to make things pretty */
        PCBCommands.add(suspendPCB); //Add the directory tab to the file Menu
                PCBCommands.addSeparator(); //Add a separator to make things pretty
        PCBCommands.add(resumePCB); //Add the directory tab to the file Menu
               PCBCommands.addSeparator(); //Add a separator to make things pretty
        PCBCommands.add(setPriorPCB); //Add the directory tab to the file Menu
                PCBCommands.addSeparator(); //Add a separator to make things pretty
        PCBCommands.add(showPCB); //Add the directory tab to the file Menu
                PCBCommands.addSeparator(); //Add a separator to make things pretty
        PCBCommands.add(showAllPCB); //Add the directory tab to the file Menu
                PCBCommands.addSeparator(); //Add a separator to make things pretty
        PCBCommands.add(showReadyPCB); //Add the directory tab to the file Menu
                PCBCommands.addSeparator(); //Add a separator to make things pretty
        PCBCommands.add(showBlockedPCB); //Add the directory tab to the file Menu

        OSMenu.add(PCBCommands); //Add the file to the menu
    }
    
    private void addLayout(JLabel[] ToolData)
    {
        //Add the menu to the top of the layout 
        OSFrame.add(BorderLayout.NORTH, OSMenu);
        
        OSToolBarPanel.setBackground(Color.BLACK); //change the toolPanel's color
        
        //Add the toolBarPanel to the bottom of the layout
        OSFrame.add(BorderLayout.SOUTH, OSToolBarPanel);
        
        //Set color for labels (It is darkgrey by default)
        for(int i = 0; i < ToolData.length; i++)
        {
            ToolData[i].setForeground(Color.white);
        }
        
        OSToolBarPanel.add(ToolData[0]); //Add the version
        OSToolBarPanel.add(ToolData[1]); //Add the date
        OSToolBarPanel.add(ToolData[2]); //Add the time
    }
    
    //apply the background image to the OS
    private void applyBkg()
    {           
        //Add a panel to the center
        //I'm adding panels to divide everything, and make it all nicely separate
        JPanel OpenSpace = new JPanel();
        OpenSpace.setBackground(Color.BLACK);
        OSFrame.add(BorderLayout.CENTER, OpenSpace);//Add the panel to the center
                                        //(the center takes up all unused space)

        //Load an image as the OSbackground and apply it to the Panel at the center
        ImageIcon OSBkg = new ImageIcon("OSBkg.jpg");
        JLabel Bkg = new JLabel();  
        Bkg.setIcon(OSBkg);  
        OpenSpace.add(Bkg); 
    }
}