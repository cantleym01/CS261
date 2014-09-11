package s.w.os;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class UnBlockPCB extends JMenuItem implements CommandPCB
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
        String message1 = "That PCB has not been made ready somehow.";
        String message2 = "That PCB has been made ready.";
       
        //keep trying to get the name
        while (true) 
        {
            //get the name
            ProcessName = (String)JOptionPane.showInputDialog(inputFrame, 
               "Enter a PCB name to un-block. \n"
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
        
        PCB tempPCB = list.FindPCB(ProcessName); //create a temp PCB to hold info
        list.removePCB(list.FindPCB(ProcessName)); //remove last PCB
        list.insertPCBToQueue(0, tempPCB); //insert the tempPCB into the ready queue
        
        //remove has been called, so as long as the PCB is found, it should be good
        if (ProcessName.equals(list.FindPCB(ProcessName).processName)) //it has been made ready
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