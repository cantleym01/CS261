package s.w.os;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class ResumePCB extends JMenuItem implements CommandPCB
{
    //create the variables for the data (they have to be outside, otherwise listeners cannot modify them)
    private String ProcessName = "";
    
    @Override
    public PCBList execute(PCBList list)
    {
        //make a frame to contain the OptionPane
        JFrame inputFrame = new JFrame();
        inputFrame.setSize(500, 100); //width, height
        
        JTextArea errorMsg = new JTextArea(); //error message
        String message1 = "That PCB is still suspended somehow.";
        String message2 = "That PCB is un-suspended.";
       
        //keep trying to get the name
        while (true) 
        {
            //get the name
            ProcessName = (String)JOptionPane.showInputDialog(inputFrame, 
               "Enter a PCB name to resume. \n"
            + "and type \"Quit\" to stop input");

            //if the conditions were met, exit the loop the conditions are to be:
            //greater than 1 char in length and to be a unique name
            //but first check to quit
            if ("Quit".equals(ProcessName) || "quit".equals(ProcessName))
            {
                return list;
            }
            if(ProcessName.equals(list.FindPCB(ProcessName).processName))
            {
                break;
            }
        }
        
        list.FindPCB(ProcessName).suspendedState = false; //un-suspend the PCB
        
        //see if the PCB has been suspended
        if (list.FindPCB(ProcessName).suspendedState == false) //it has been un-suspended
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