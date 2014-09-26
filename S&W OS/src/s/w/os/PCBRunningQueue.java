package s.w.os;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//This is a linked list because I can override functions as I need and
//they come with good functionality from the start

//The running queue should only ever have 1 pcb on it at a time as remove is called before insert

public class PCBRunningQueue extends LinkedList
{   
    private JFrame frame = new JFrame(); //frame for output
    private JPanel panel = new JPanel(); //panel component
    private JScrollPane pane = new JScrollPane(panel); //make the panel scrollable
    private int totalTime = 0;
    private LinkedList turnAroundTimes = new LinkedList();
    
    PCBRunningQueue()
    {
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS)); //give the panel the right layout
        frame.setBounds(0, 0, 400, 200); //set initial bounds of the frame
        frame.add(pane); //add the scrollable pane to the frame
    }
    
    public void insertPCB(PCB PCBToRun) //add a pcb to the running queue
    {
        add(PCBToRun); //insert the PCB into the first position
        
        //new status
        JLabel newStatus = new JLabel(PCBToRun.processName + " has entered the running queue.");
        panel.add(newStatus);
        frame.setVisible(true); //make is all visible
    }
    
    public PCB removeRunningPCB()
    {
        PCB tempPCB = (PCB) get(0); //get the PCB running
        clear(); //clear the running queue, there should be 0 on the queue after removing a PCB
        
        //new status
        JLabel newStatus = new JLabel(tempPCB.processName + " has exited the running queue.");
        panel.add(newStatus);
        frame.setVisible(true); //make is all visible
        
        return tempPCB; //return the PCB that was running
    }
    
    public void timeCycle()
    {
        if (size() > 0)
        {
            PCB tempPCB = (PCB) get(0); //temp PCB to manipulate
            tempPCB.PCBTimer--; //it has run for 1 second
            set(0, tempPCB); //replace old PCB with new PCB
            
            totalTime++;
            System.out.println(tempPCB.timeRemaining);

            //check if the PCB has finished, if so, notify user and remove it from the running queue
            if (tempPCB.PCBTimer <= 0)
            {
                //new status
                JLabel newStatus = new JLabel(tempPCB.processName + " has finished running.");
                panel.add(newStatus);
                frame.setVisible(true); //make is all visible
                int turnAroundTime = totalTime - tempPCB.timeOfArrival;
                turnAroundTimes.push(turnAroundTime);
                clear(); //clear the running queue
            }
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
    }
}
