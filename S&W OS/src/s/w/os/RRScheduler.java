package s.w.os;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

//RR will run the PCB's on a certain time quantum, specified by the user, and will
//go to the next process once that quantum has expired, or the running process finishes
public class RRScheduler extends JMenuItem implements CommandPCB
{
    private int timeQuantum = 0;//the variable for the user-defined time quantum
            //I honestly think this may be the nerdiest name for something in CS
    private Boolean quit = false;
    
    @Override
    public PCBList execute(PCBList list)
    {
        PCBParser parser = new PCBParser(); //create the parser
        parser.getFileName();//get the file name
        
        if (parser.quit) //if the user said to quit, stop everything in the command
        {
            return list;
        }
        
        getTimeQuantum(); //get the time quantum
        
        if (quit)
        {
            return list;
        }
        
        //get first line of the file
        parser.readNextLine(); //get next line of data for the PCB
        
        //run the scheduler
        while (true)
        {
            //if all PCB's are finished, and there is no more input from the file, quit
            if (list.readyQueue.size() <= 0 && list.runningQueue.size() <= 0 && parser.doneWFile)
            {
                break;
            }
            
            //if it is time for last PCB read to enter
            if (list.runningQueue.totalTime == parser.getNextTOA())
            {
                int actualClass = 0; //variable that used in making the PCB

                //stuff to add to the PCB being created
                String processName = parser.processNameTOA.get(parser.getNextTOA());
                Character pclass = parser.Classes.get(processName);
                switch(pclass)
                {
                    case 'A':
                        actualClass = 0;
                        break;
                    case 'S':
                        actualClass = 1;
                        break;
                }

                int priority = parser.priority.get(processName);
                int memory = parser.memory.get(processName);
                int timeOfArrival = parser.timeOfArrival.get(processName);
                int timeRemaining = parser.timeRemaining.get(processName);
                int CPU = parser.CPU.get(processName); 
                    
                //make the PCB and shove it onto the ready queue
                list.SetupPCB(processName, actualClass, priority, timeRemaining, memory, timeOfArrival, CPU);
                    
                parser.readNextLine(); //get next line of data for the PCB
            }
            
            //if the time quantum has expired, swap PCBs
            //if totalTime % time quantum == 0, that means that it has passed the time quantum
            //totalTime cannot = 0 in this comparison, as 0 % anything is 0
            if (list.runningQueue.totalTime % timeQuantum == 0 && list.runningQueue.totalTime != 0)
            {
                //empty the running queue of PCBs
                for (int i = 0; i < list.runningQueue.size(); i++)
                {
                    PCB currentPCB = (PCB)list.runningQueue.get(i); //PCB to remove
                    
                    //insert the removed PCB into the ready queue
                    list.readyQueue.insertPCB((PCB)list.runningQueue.removeRunningPCB(currentPCB));
                }
                
                //insert next round of PCBs
                while (list.readyQueue.size() > 0)
                {
                    //if the PCB fits, remove it from the ready queue, otherwise exit
                    if (list.runningQueue.insertPCB((PCB)list.readyQueue.get(0)))
                    {
                        list.readyQueue.remove(0);
                    }
                    else 
                    {
                        break;
                    }
                }
            }
            
            list.runningQueue.timeCycle(); //run the PCB for one time cycle
        }
        
        //do final stuff (output end results and return the list)
        list.runningQueue.outputEnd();
        list.runningQueue.resetMemory();
        return list;
    }
    
    private void getTimeQuantum()
    {
        String timeStr; //container for str of time
        
        //make a frame to contain the OptionPane
        JFrame inputFrame = new JFrame();
        inputFrame.setSize(500, 100); //width, height
       
        //keep trying to get input
        while (true) 
        {
            //get the time
            timeStr = (String)JOptionPane.showInputDialog(inputFrame, 
            "Enter a time quantum for Round Robin (must be greater than 0). \n"
            + "or type \"Quit\" to stop input");
            
            //if the user wants to quit, let them
            if ("Quit".equals(timeStr) || "quit".equals(timeStr))
            {
                quit = true;
                return;
            }
            
            try //try to convert the string to an integer
            {
                timeQuantum = Integer.parseInt(timeStr);
            }
            catch (NumberFormatException ex) //exception that can be thrown
            {
                //do nothing, except skip what was meant to happen
                //this is just here so the program doesn't scream at the user
            }
            finally //if the catch was not called, check if the input was good
            {
                if (timeQuantum > 0)
                {
                    break;
                }
            }
        }
    }
}
