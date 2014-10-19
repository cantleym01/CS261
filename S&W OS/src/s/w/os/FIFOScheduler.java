package s.w.os;

import javax.swing.JMenuItem;

//FIFO will run the PCB's as they arrive, and will never interupt one
public class FIFOScheduler extends JMenuItem implements CommandPCB
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
        
        //get first line of the file
        parser.readNextLine(); //get next line of data for the PCB
        
        //run the scheduler
        while (true)
        {  
            //if all PCB's are finished, and there is no more input from the file quit
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
            
            
            //if there are things that can run, try to run them
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
            
            list.runningQueue.timeCycle(); //run the PCB for one time cycle
        }
        
        //do final stuff (output end results and return the list)
        list.runningQueue.outputEnd();
        list.runningQueue.resetMemory();
        return list;
    }
}
