package s.w.os;

import javax.swing.JMenuItem;

//SJF will run the PCB's according to time remaining, and will ignore Time of Arrival
public class SJFScheduler extends JMenuItem implements CommandPCB
{
    @Override
    public PCBList execute(PCBList list)
    {
        PCBParser parser = new PCBParser(); //create the parser
        parser.getFileName();//get the file name
        
        if (parser.quit) //if the user said to quit, stop everything in the command
        {
            return list;
        }
        
        parser.readWholeFile(); //get all of the data for the SJF scheduler
        
        //SJF will put create PCBs by their time remaining
        for (int i = 0; i < parser.timeSort.size(); i++)
        {
            int actualClass = 0; //variable that used in making the PCB
            
            //stuff to add to the PCB being created
            int timeRemaining = (int)parser.timeSort.get(i);
            String processName = parser.processNameTime.get(timeRemaining);
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
            int CPU = parser.CPU.get(processName);
            
            //make the PCB and shove it onto the ready queue
            list.SetupPCB(processName, actualClass, priority, timeRemaining, memory, timeOfArrival, CPU);
        }
        
        //run the scheduler
        while (true)
        {  
            //if all PCB's are finished, and there is no more input from the file quit
            if (list.readyQueue.size() <= 0 && list.runningQueue.size() <= 0 && parser.doneWFile)
            {
                break;
            }
            
            //while there is available memory, keep inserting (as long as there is something to insert)
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
            
            list.runningQueue.timeCycle(); //run the running queue for one time cycle
        }
        
        list.runningQueue.outputEnd();
        list.runningQueue.resetMemory();
        
        return list;
    }
}
