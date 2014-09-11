package s.w.os;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ShowAllPCB extends JMenuItem implements CommandPCB
{
    //create the variables for the data (they have to be outside, otherwise listeners cannot modify them)
    private String ProcessName = "";
    int toShow;
    
    ShowAllPCB(int whatToShow)
    {
        toShow = whatToShow; //this is used for: 0 = ready, 1 = blocked, 2 = all
    }
    
    @Override
    public PCBList execute(PCBList list)
    {
        //make a frame to contain the OptionPane
        JFrame inputFrame = new JFrame();
        inputFrame.setSize(200, 300); //width, height
        
        //add a scrollbar to output
        JPanel display = new JPanel();
        JScrollPane keepOnScrollinScrollinScrollin = new JScrollPane(display);
        
        JTextArea PCBshow = new JTextArea(); //text area to show the pcb stuff
        //initialize some stuff that will always be there
        PCBshow.append("Output in this order: \n Name, Memory, Priority, Suspended State, and then Class:\n");
        
        //variables for data
        int allocatedMem = 0;
        int priorityVal = 0;
        Boolean susState = false;
        int Class = 0;
        String classStr = "";
        
        switch(toShow)
        {
            case 0: //all queues
                //check if there is any in both
                if (list.readyQueue.isEmpty() && list.blockedQueue.isEmpty())
                {
                    return list;
                }
            case 1: //ready queue
                //first check if the ready queue is empty, and if it is, don't do anything
                if (list.readyQueue.isEmpty() && toShow == 1)
                {
                    return list;
                }
                else
                {
                    //append the display for the blocked queue
                    PCBshow.append("     Ready Queue:\n");
                    for (int i = 0; i < list.readyQueue.numberOfPCBs(); i++) //for the entire ready queue
                    {
                        PCB currentPCB; //a pcb holder to get the info
                        currentPCB = (PCB) list.readyQueue.get(i);
                        
                        PCBshow.append(currentPCB.processName + "; ");//set the name

                        allocatedMem = currentPCB.memoryValue; //get the memory
                        PCBshow.append(allocatedMem + "; ");//set the memory

                        priorityVal = currentPCB.priority; //get the priority
                        PCBshow.append(priorityVal + "; ");//set the priority

                        susState = currentPCB.suspendedState; //get the susState
                        PCBshow.append(susState + "; ");//set the susState

                        //get the class
                        Class = currentPCB.processClass; 
                        if (Class == 0) //application
                        {
                            classStr = "Application";
                        }
                        else //system
                        {
                            classStr = "System";
                        }
                        PCBshow.append(classStr + ".\n");//set the class
                    }
                }
                if (toShow != 0) //if we're not doing all, break, else, we do blocked queue as well
                {
                    break;
                }
            case 2: //blocked queue
                //first check if the blocked queue is empty, and if it is, don't do anything
                if (list.blockedQueue.isEmpty() && toShow == 2)
                {
                    return list;
                }
                else
                {
                    //append the display for the blocked queue
                    PCBshow.append("     Blocked Queue:\n");
                    for (int i = 0; i < list.blockedQueue.numberOfPCBs(); i++) //for the entire ready queue
                    {
                        PCB currentPCB; //a pcb holder to get the info
                        currentPCB = (PCB) list.blockedQueue.get(i);
                        
                        PCBshow.append(currentPCB.processName + "; ");//set the name

                        allocatedMem = currentPCB.memoryValue; //get the memory
                        PCBshow.append(allocatedMem + "; ");//set the memory

                        priorityVal = currentPCB.priority; //get the priority
                        PCBshow.append(priorityVal + "; ");//set the priority

                        susState = currentPCB.suspendedState; //get the susState
                        PCBshow.append(susState + "; ");//set the susState

                        //get the class
                        Class = currentPCB.processClass; 
                        if (Class == 0) //application
                        {
                            classStr = "Application";
                        }
                        else //system
                        {
                            classStr = "System";
                        }
                        PCBshow.append(classStr + ".\n");//set the class
                    }
                }
                break;
        }
            
        //add the PCBshow text to the display
        display.add(PCBshow); //herpDerp
        inputFrame.add(BorderLayout.WEST, keepOnScrollinScrollinScrollin);
        
        inputFrame.setVisible(true); //so user can see it
        
        return list; //return the list with all of it's changes
    }
}