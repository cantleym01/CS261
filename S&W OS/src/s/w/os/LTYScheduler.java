package s.w.os;

import java.util.Hashtable;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

//Lottery will run the PCB's on a certain time quantum, 50, and will
//determine what process to run for that quantum from a "lottery"
public class LTYScheduler extends JMenuItem implements CommandPCB
{
    private int timeQuantum = 150;//the variable for the time quantum
            //it has a time quantum as I don't want to do 1 time cycle per lottery winner
    private Boolean quit = false;
    private int amountOfTicks = 0;
    private int totalTicks = 0;
    
    @Override
    public PCBList execute(PCBList list)
    {
        PCBParser parser = new PCBParser(); //create the parser
        parser.getFileName();//get the file name
        Random generator = new Random(); //use this to make a draw for the lottery
        
        //this is used to reference a PCB to it's ticket amount
        Hashtable<String, Integer> PCBTickets = new Hashtable<String, Integer>();
        
        if (parser.quit) //if the user said to quit, stop everything in the command
        {
            return list;
        }
        
        getTickets(); //get the time quantum
        
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
                
                //create the tickets for the PCB
                PCBTickets.put(processName, (CPU*amountOfTicks)/100);
                    
                //make the PCB and shove it onto the ready queue
                list.SetupPCB(processName, actualClass, priority, timeRemaining, memory, timeOfArrival, CPU);
                    
                parser.readNextLine(); //get next line of data for the PCB
            }
            
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
            
            //get the total number of tickets
            for (int i = 0; i < list.readyQueue.size(); i++) //ready queue first
            {
                PCB tempPCB = (PCB)list.readyQueue.get(i);
                totalTicks += PCBTickets.get(tempPCB.processName); //add to the total tickets
            }
            
            //if a PCB has finished running, and the readyQueue has 1 or more do lottery
            if (list.runningQueue.size() == 0 && list.readyQueue.size() > 0)
            {
                //keep doing lottery while the memory is not full
                while (true)
                {
                    int lottery = 0;//use to find the winner of the lottery
                    Boolean quiter = false; //tell it to stahp the lottery drawings

                    //get the ticket draw
                    if (amountOfTicks <= totalTicks) //enough for full lottery draw
                    {
                        lottery = generator.nextInt(amountOfTicks) + 1; //get the ticket number
                    }
                    else //not enough for full lottery draw
                    {
                        lottery = generator.nextInt(totalTicks) + 1; //get the ticket number
                    }
                    
                    int currentTicks = 0; //current omount of tickets
                    //search through the readyQueue for the winner
                    for(int i = 0; i < list.readyQueue.size(); i++)
                    {   
                        PCB tempPCB = (PCB)list.readyQueue.get(i); //temp PCB for comparisons

                        currentTicks += PCBTickets.get(tempPCB.processName); //add to current ticket counter

                        if (currentTicks >= lottery) //current ticket has been found
                        {
                            PCB tryPCB = tempPCB;
                            
                            //and push the next PCB in the readyQueue onto the running queue
                            if(list.runningQueue.insertPCB(tryPCB))
                            {
                                list.readyQueue.remove(i);
                            }
                            else
                            {
                                quiter = true;
                            }
                            break;
                        }
                    }
                    
                    //break if the memory is filled or the ready queue is empty
                    if (quiter || list.readyQueue.size() == 0)
                    {
                        break;
                    }
                }
            }

            list.runningQueue.timeCycle(); //run the PCB for one time cycle
            totalTicks = 0; //reset the total ticket counter
        }
        
        //do final stuff (output end results and return the list)
        list.runningQueue.outputEnd();
        return list;
    }
    
    private void getTickets()
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
            "Enter the amount of tickets for the Lottery (must be greater than 0). \n"
            + "or type \"Quit\" to stop input");
            
            //if the user wants to quit, let them
            if ("Quit".equals(timeStr) || "quit".equals(timeStr))
            {
                quit = true;
                return;
            }
            
            try //try to convert the string to an integer
            {
                amountOfTicks = Integer.parseInt(timeStr);
            }
            catch (NumberFormatException ex) //exception that can be thrown
            {
                //do nothing, except skip what was meant to happen
                //this is just here so the program doesn't scream at the user
            }
            finally //if the catch was not called, check if the input was good
            {
                if (amountOfTicks > 0)
                {
                    break;
                }
            }
        }
    }
}
