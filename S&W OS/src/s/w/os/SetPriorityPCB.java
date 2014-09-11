package s.w.os;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class SetPriorityPCB extends JMenuItem implements CommandPCB
{
    //create the variables for the data (they have to be outside, otherwise listeners cannot modify them)
    private String ProcessName = "";
    private String Priority = "";
    
    @Override
    public PCBList execute(PCBList list)
    {
        //initialize this to a number that is not valid
        int PriorityVal = 200;
        
        //make a frame to contain the OptionPane
        JFrame inputFrame = new JFrame();
        inputFrame.setSize(500, 100); //width, height
        
        JTextArea errorMsg = new JTextArea(); //error message
        String message1 = "That PCB priority has not been set somehow.";
        String message2 = "That PCB priority has been set.";
       
        //keep trying to get the name
        while (true) 
        {
            //get the name
            ProcessName = (String)JOptionPane.showInputDialog(inputFrame, 
               "Enter a PCB name to set it's priority. \n"
            + "and type \"Quit\" to stop input");

            //if the conditions were met, exit the loop the conditions are to be:
            //greater than 1 char in length and to be a unique name
            //but first check to quit
            if ("Quit".equals(ProcessName) || "quit".equals(ProcessName))
            {
                return list;
            }
            if((ProcessName.length() >= 1) && (!"NULL".equals(list.FindPCB(ProcessName).processName)))
            {
                break;
            }
        }
        
        //keep checking for priority
        while (true)
        {
            //get the priority
            Priority = (String)JOptionPane.showInputDialog(inputFrame, 
                   "Enter a Priority Number:\n"
                + "They range from -127 to 128."
                + "and type \"Quit\" to stop input");
            
            //Quit if that was the choice
            if ("Quit".equals(Priority) || "quit".equals(Priority))
            {
                return list;
            }
            
            //convert string to int for comparrison
            PriorityVal = Integer.parseInt(Priority);
            
            //check for inputs, if they are not valid, ask again
            if(PriorityVal >= -127 && PriorityVal <= 128)
            {
                break;
            }
        }
        
        list.FindPCB(ProcessName).priority = PriorityVal; //setup the PCB
        
        //see if the PCB has been suspended
        if (list.FindPCB(ProcessName).priority == PriorityVal) //it has been assigned it's priority
        {
            errorMsg.append(message2);
            inputFrame.add(errorMsg);
        }
        else //it failed somehow
        {
            errorMsg.append(message1);
            inputFrame.add(errorMsg);
        }
        
        inputFrame.setVisible(true); //so user can see it
        
        return list; //return the list with all of it's changes
    }
}