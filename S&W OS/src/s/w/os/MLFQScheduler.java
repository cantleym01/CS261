package s.w.os;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

//MLFQ will run the PCB's on a certain time quantum, specified by the user, and will
//also run them by a loose priority fixation, such as fo 10 queues, it will run
//all PCBs with priority of 107 through 128 first, then the other queues in descending order
public class MLFQScheduler extends JMenuItem implements CommandPCB
{
    private int numOfQueues = 0; //how many queues to simulate for running the scheduler
    private int timeQuantum = 0;//the variable for the user-defined time quantum
            //I honestly think this may be the nerdiest name for something in CS
    private Boolean quit = false;
    private int queueInterval = 0; //the divider for determining the number of queues
    private Struct2Val[] queueArray; //will contain structs with low and high values for queues
    private int switchToRR = 0; //this int will tell when to switch to RR
    
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
        
        getRRSwitch(); //get the time quantum
        
        if (quit)
        {
            return list;
        }
        
        getNumOfQueues(); //get the number of queues
        
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
            
            //if we are still doing MLFQ, do that
            if (list.runningQueue.totalTime < switchToRR)
            {
                //if the time quantum has expired, empty PCBs from the running queue
                //if totalTime % time quantum == 0, that means that it has passed the time quantum
                //totalTime cannot = 0 in this comparison, as 0 % anything is 0
                if ( list.runningQueue.totalTime % timeQuantum == 0 && list.runningQueue.totalTime != 0)
                {
                    //empty the running queue of PCBs
                    for (int i = 0; i < list.runningQueue.size(); i++)
                    {
                        PCB currentPCB = (PCB)list.runningQueue.get(i); //PCB to remove

                        //insert the removed PCB into the ready queue
                        list.readyQueue.insertPCB((PCB)list.runningQueue.removeRunningPCB(currentPCB));
                    }
                }
                
                //put stuff into running if nothing is running and there is stuff to run
                if (list.readyQueue.size() > 0 && list.runningQueue.size() == 0)
                {
                    Boolean PCBfound = false; //used to exit the outer loop

                    //loop through the queue array, starting at the top
                    for(int i = queueArray.length-1; i > -1; i--)
                    {
                        //loop through the readyQueue to see if any of the processes are at the desired priority level
                        //if none are found at the desired priority level, it will go to the lower priority level
                        for(int j = 0; j < list.readyQueue.size(); j++)
                        {
                            PCB tempPCB = (PCB)list.readyQueue.get(j); //temp PCB for comparisons

                            //if the chosen PCB is in the correct range, input it
                            if (tempPCB.priority > queueArray[i].lowVal && tempPCB.priority < queueArray[i].hiVal)
                            {
                                //and push the next PCB in the readyQueue onto the running queue
                                list.runningQueue.insertPCB((PCB)list.readyQueue.remove(j));

                                PCBfound = true; //tells process to break from the outer loop
                            }
                        }

                        if (PCBfound) //break if the PCB has been switched
                        {
                            break;
                        }
                    }
                }
            }
            else //do RR
            {
                //if the time quantum has expired, empty PCBs from the running queue
                //if totalTime % time quantum == 0, that means that it has passed the time quantum
                //totalTime cannot = 0 in this comparison, as 0 % anything is 0
                if ( list.runningQueue.totalTime % timeQuantum == 0 && list.runningQueue.totalTime != 0)
                {
                    //empty the running queue of PCBs
                    for (int i = 0; i < list.runningQueue.size(); i++)
                    {
                        PCB currentPCB = (PCB)list.runningQueue.get(i); //PCB to remove

                        //insert the removed PCB into the ready queue
                        list.readyQueue.insertPCB((PCB)list.runningQueue.removeRunningPCB(currentPCB));
                    }
                }

                //put stuff into running if nothing is running and there is stuff to run
                if (list.readyQueue.size() > 0 && list.runningQueue.size() == 0)
                {
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
            }
            
            list.runningQueue.timeCycle(); //run the PCB for one time cycle
        }
        
        //do final stuff (output end results and return the list)
        list.runningQueue.outputEnd();
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
            "Enter a time quantum for Multi-Level Feedback Queue (must be greater than 0). \n"
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
    
    private void getNumOfQueues()
    {
        String queueStr; //string container for number of queues
        
        //make a frame to contain the OptionPane
        JFrame inputFrame = new JFrame();
        inputFrame.setSize(500, 100); //width, height
       
        //keep trying to get input
        while (true) 
        {
            //get the num of queues
            queueStr = (String)JOptionPane.showInputDialog(inputFrame, 
            "Enter the number of queues desired (must be greater than 0 and less than or equal to 256). \n"
            + "or type \"Quit\" to stop input");
            
            //if the user wants to quit, let them
            if ("Quit".equals(queueStr) || "quit".equals(queueStr))
            {
                quit = true;
                return;
            }
            
            try //try to convert the string to an integer
            {
                numOfQueues = Integer.parseInt(queueStr);
            }
            catch (NumberFormatException ex) //exception that can be thrown
            {
                //do nothing, except skip what was meant to happen
                //this is just here so the program doesn't scream at the user
            }
            finally //if the catch was not called, check if the input was good
            {
                if (numOfQueues > 0 && numOfQueues <= 256)
                {
                    queueArray = new Struct2Val[numOfQueues]; //create the array that will hold the queue values
                    queueInterval = 256/numOfQueues;
                    
                    //since this is the last input asked for, we can build the 
                    //queueArray's contents here and lose no time if user wants to quit
                    
                    //these are the constants present in creating the queueArray
                    int start = -127;
                    int end = 128;
                    
                    //now fill up the queueArray
                    for (int i = 0; i < numOfQueues; i++)
                    {
                        Struct2Val newValues = new Struct2Val(); //create the struct
                        
                        //there is one special case, the last queue
                        if (i == numOfQueues-1)
                        {
                            newValues.lowVal = start;
                            newValues.hiVal = end; //the hiVal is the end value
                            queueArray[i] = newValues;
                            break; //we're done now
                        }
                        
                        newValues.lowVal = start; //lowVal is the start value
                        newValues.hiVal = start + queueInterval; //hiVal is start value + the queueInterval
                        start += newValues.hiVal + 1; //the new start is 1 num above the last hiVal
                        queueArray[i] = newValues;
                    }
                    
                    break; //gtfo
                }
            }
        }
    }
    
    private void getRRSwitch()
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
            "Enter a time to switch from Multi-Level Feedback Queue to Round Robin. \n"
            + "or type \"Quit\" to stop input");
            
            //if the user wants to quit, let them
            if ("Quit".equals(timeStr) || "quit".equals(timeStr))
            {
                quit = true;
                return;
            }
            
            try //try to convert the string to an integer
            {
                switchToRR = Integer.parseInt(timeStr);
            }
            catch (NumberFormatException ex) //exception that can be thrown
            {
                //do nothing, except skip what was meant to happen
                //this is just here so the program doesn't scream at the user
            }
            finally //if the catch was not called, basically any int is good
            {
                    break;
            }
        }
    }
}
