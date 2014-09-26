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
            //if all PCB's are finished, quit
            if (list.readyQueue.size() <= 0 && list.runningQueue.size() <= 0)
            {
                break;
            }
            
            if (list.runningQueue.size() == 0) //if it is not running anything currently, add a PCB
            {
                //insert the first PCB in the ready queue
                list.runningQueue.insertPCB(list.readyQueue.getHeadPCB());
            }
            
            list.runningQueue.timeCycle(); //run the PCB for one time cycle
        }
        list.runningQueue.outputEnd();
        
        return list;
    }
}
