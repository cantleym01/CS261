package s.w.os;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//This is a linked list because I can override functions as I need and
//they come with good functionality from the start

public class PCBRunningQueue extends LinkedList
{   
    private JFrame frame = new JFrame(); //frame for output
    private JPanel panel = new JPanel(); //panel component
    private JScrollPane pane = new JScrollPane(panel); //make the panel scrollable
    public int totalTime = 0;
    private LinkedList turnAroundTimes = new LinkedList();
    private Boolean firstTime = true; //this tells whether we should as for the memory management algorithm
    
    private MemoryHandler Memory = new MemoryHandler();
    
    PCBRunningQueue()
    {
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS)); //give the panel the right layout
        frame.setBounds(0, 0, 400, 200); //set initial bounds of the frame
        frame.add(pane); //add the scrollable pane to the frame
    }
    
    public Boolean insertPCB(PCB PCBToRun) //add a pcb to the running queue
    {
        //ask for the memory management method if it is the first time
        if (firstTime)
        {
            Memory.setMethod(getMemMethod());
            firstTime = false;
        }
        
        if (Memory.addPCB(PCBToRun)) //try to insert, if it returns true, it was good
        {
            addLast(PCBToRun); //insert the PCB into the last open position
            Memory.writeMemory();
        }
        else //try to make more memory available
        {
            //try coalescing first
            Memory.Coalescing();
            if (Memory.addPCB(PCBToRun))
            {
                addLast(PCBToRun);
                
                //output to memory file
                try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Memory.txt", true)))) 
                {
                    out.println("\nAfter Coalescing:\n");
                    out.close();
                }
                catch (Exception e){}
                
                Memory.writeMemory();
            }
            else //use compaction
            {
                Memory.Compaction();
                Memory.Coalescing();
                if (Memory.addPCB(PCBToRun))
                {
                    addLast(PCBToRun);
                    
                    //output to memory file
                    try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Memory.txt", true)))) 
                    {
                        out.println("\nAfter Compaction:\n");
                        out.close();
                    }
                    catch (Exception e){}
                    
                    Memory.writeMemory();
                }
                else //it won't fit, so wait until memory is freed up
                {
                    return false;
                }
            }  
        }
    
        //new status
        JLabel newStatus = new JLabel(PCBToRun.processName + " has entered the running queue.");
        panel.add(newStatus);
        frame.setVisible(true); //make is all visible
        
        return true;
    }
    
    public PCB removeRunningPCB(PCB PCBToRemove)
    {
        PCB tempPCB = (PCB)get(indexOf(PCBToRemove)); //get the PCB running
        
        //new status
        JLabel newStatus = new JLabel(tempPCB.processName + " has exited the running queue.");
        panel.add(newStatus);
        frame.setVisible(true); //make is all visible
        
        //remove the PCB
        remove(tempPCB);
        Memory.removePCB(tempPCB);
        
        //print to Memory
        Memory.writeMemory();
        
        //System.out.println(tempPCB.processName);
        return tempPCB; //return the PCB that was running and clear the running queue at the same time
    }
    
    public void timeCycle()
    {
        int index = 0;
        
        //increment time
        totalTime++;
        
        //decrement the timer on each pcb in the running queue
        while (index < size())
        {
            PCB tempPCB = (PCB) get(index); //temp PCB to manipulate
            tempPCB.PCBTimer--; //it has run for 1 second
            set(index, tempPCB); //replace old PCB with new PCB
            
            //System.out.println(tempPCB.processName + " -> " + tempPCB.PCBTimer);

            //check if the PCB has finished, if so, notify user and remove it from the running queue
            if (tempPCB.PCBTimer <= 0)
            {
                //remove the PCB from memory
                Memory.removePCB(tempPCB);
                
                //print to memory
                Memory.writeMemory();
                
                //new status
                JLabel newStatus = new JLabel(tempPCB.processName + " has finished running.");
                panel.add(newStatus);
                frame.setVisible(true); //make is all visible
                
                int turnAroundTime = totalTime - tempPCB.timeOfArrival;
                turnAroundTimes.push(turnAroundTime);
                remove(index); //remove the PCB that just finished
                index--; //have to take a step backwards when this happens
            }
            
            index++;
        }
    }
    
    public void outputEnd()
    {
        //new status'
        JLabel newStatus = new JLabel("Total Time: " + totalTime);
        panel.add(newStatus); 
        
        //get avg turnAroundTime
        int avgSum = 0; //part of taking the average
        for (int i = 0; i < turnAroundTimes.size(); i++)
        {
            avgSum += (int)turnAroundTimes.get(i);
        }
        int avg = avgSum/turnAroundTimes.size(); //get the avg
        JLabel newStatus2 = new JLabel("Average Turn-around Time: " + avg);
        panel.add(newStatus2); 
        
        //clean up global variables for if user wants to run another scheduler
        frame = new JFrame(); //frame for output
        panel = new JPanel(); //panel component
        pane = new JScrollPane(panel); //make the panel scrollable
        totalTime = 0;
        turnAroundTimes = new LinkedList();
        
        //also have to re-do constructor work
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS)); //give the panel the right layout
        frame.setBounds(0, 0, 400, 200); //set initial bounds of the frame
        frame.add(pane); //add the scrollable pane to the frame
    }
    
    //this allows the user to choose a different memory management method next time w/o restarting
    public void resetMemory()
    {
        firstTime = true;
    }
    
    private int getMemMethod()
    {
        String method; //container for str of time
        int returnMethod = 0; //default first fit
        
        //make a frame to contain the OptionPane
        JFrame inputFrame = new JFrame();
        inputFrame.setSize(500, 100); //width, height
       
        //keep trying to get input
        while (true) 
        {
            //get the time
            method = (String)JOptionPane.showInputDialog(inputFrame, 
            "Enter a Memory Management Method to use.\n" + 
            "0 = FirstFit, 1 = NextFit, 2 = BestFit, 3 = WorstFit\n");
            
            try //try to convert the string to an integer
            {
                returnMethod = Integer.parseInt(method);
            }
            catch (NumberFormatException ex) //exception that can be thrown
            {
                //do nothing, except skip what was meant to happen
                //this is just here so the program doesn't scream at the user
            }
            finally //if the catch was not called, check if the input was good
            {
                if (returnMethod >= 0 && returnMethod <= 3)
                {
                    break;
                }
            }
        }
        
        return returnMethod;
    }
}
