package s.w.os;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ShowPCB extends JMenuItem implements CommandPCB
{
    //create the variables for the data (they have to be outside, otherwise listeners cannot modify them)
    private String ProcessName = "";
    
    @Override
    public PCBList execute(PCBList list)
    {
        //make a frame to contain the OptionPane
        JFrame inputFrame = new JFrame();
        inputFrame.setSize(200, 300); //width, height
        
        //add a scrollbar to output
        JPanel display = new JPanel();
        JScrollPane keepOnScrollinScrollinScrollin = new JScrollPane(display);
        
        JTextArea errorMsg = new JTextArea(); //error message
        String error = "That PCB has not been shown somehow.";
        
        JTextArea PCBshow = new JTextArea(); //text area to show the pcb
       
        //keep trying to get the name
        while (true) 
        {
            //get the name
            ProcessName = (String)JOptionPane.showInputDialog(inputFrame, 
               "Enter a PCB name to show. \n"
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
        
        //if it somehow made it past the first error check, tell the user
        if (!ProcessName.equals(list.FindPCB(ProcessName).processName))
        {
            errorMsg.append(error);
            inputFrame.add(errorMsg);
        }
        else //display everything normally
        {
            int allocatedMem = 0;
            int priorityVal = 0;
            Boolean susState = false;
            int Class = 0;
            String classStr = "";
            
            PCBshow.append("Name: " + ProcessName + "\n");//set the name
            
            allocatedMem = list.FindPCB(ProcessName).memoryValue; //get the memory
            PCBshow.append("Memory: " + allocatedMem + "\n");//set the memory
            
            priorityVal = list.FindPCB(ProcessName).priority; //get the priority
            PCBshow.append("Priority: " + priorityVal + "\n");//set the priority
            
            susState = list.FindPCB(ProcessName).suspendedState; //get the susState
            PCBshow.append("Suspended State: " + susState + "\n");//set the susState
            
            //get the class
            Class = list.FindPCB(ProcessName).processClass; 
            if (Class == 0) //application
            {
                classStr = "Application";
            }
            else //system
            {
                classStr = "System";
            }
            PCBshow.append("Class: " + classStr + "\n");//set the class
            
            //add the PCBshow text to the display
            display.add(PCBshow); //herpDerp
            inputFrame.add(BorderLayout.WEST, keepOnScrollinScrollinScrollin);
        }
        
        inputFrame.setVisible(true); //so user can see it
        
        return list; //return the list with all of it's changes
    }
}