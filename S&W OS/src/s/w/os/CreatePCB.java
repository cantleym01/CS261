package s.w.os;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class CreatePCB extends JMenuItem implements CommandPCB
{
    //create the variables for the data (they have to be outside, otherwise listeners cannot modify them)
    private String ProcessName = "";
    private String Class = "";
    private String Priority = "";
    
    @Override
    public PCBList execute(PCBList list)
    {
        int ClassID;
        int PriorityVal;
        
        //make a frame to contain the OptionPane
        JFrame inputFrame = new JFrame();
        inputFrame.setSize(500, 100); //width, height
        
        JTextArea errorMsg = new JTextArea(); //error message if a value is not good
        errorMsg.append("That value is not acceptable");
       
        //keep trying to get the name
        while (true) 
        {
            //get the name
            ProcessName = (String)JOptionPane.showInputDialog(inputFrame, 
               "Enter a PCB name.");

            //if the conditions were met, exit the loop the conditions are to be:
            // greater than 1 char in length and to be a unique name
            if((ProcessName.length() >= 1) && ("NULL".equals(list.FindPCB(ProcessName).processName)))
            {
                break;
            }
        }
        
       //keep trying to get the class
        while (true)
        {
            //get the Class
            Class = (String)JOptionPane.showInputDialog(inputFrame, 
                   "Enter a Class Type:\n"
                + "Valid ones are \"Sys\" or \"System\" \n"
                + "and even \"App\" or \"Application\"");
            
            //check for inputs, if they are not valid, ask again
            if("Application".equals(Class) || "App".equals(Class) || "application".equals(Class) || "app".equals(Class))
            {
                ClassID = 0;
                break;
            }
            if ("System".equals(Class) || "Sys".equals(Class) || "system".equals(Class) || "sys".equals(Class))
            {
                ClassID = 1;
                break;
            }
        }
        
        //keep checking for priority
        while (true)
        {
            //get the priority
            Priority = (String)JOptionPane.showInputDialog(inputFrame, 
                   "Enter a Priority Number:\n"
                + "They range from -127 to 128.");
            
            //convert string to int for comparrison
            PriorityVal = Integer.parseInt(Priority);
            
            //check for inputs, if they are not valid, ask again
            if(PriorityVal >= -127 && PriorityVal <= 128)
            {
                break;
            }
        }
            
        list.SetupPCB(ProcessName, ClassID, PriorityVal); //setup the PCB
        
        return list; //return the list with all of it's changes
    }
}
/*
a. Allocates and sets up a new PCB and inserts it into the Ready Queue
b. Need to get the following info from the user:
i. Process Name
ii. Class
iii. Priority
c. Must check the validity of the above and display an appropriate error message if one is invalid.
i. Process name must be unique
ii. Class must be valid.
iii. Priority must be valid
d. Calls the SetupPCB function to setup the PCB then calls the InsertPCB function to insert it into the ready
queue.
*/