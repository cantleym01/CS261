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
    private int timeQuantum = 100;//the variable for the time quantum
            //I honestly think this may be the nerdiest name for something in CS
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

            //get the total number of tickets
            for (int i = 0; i < list.readyQueue.size(); i++) //ready queue first
            {
                PCB tempPCB = (PCB)list.readyQueue.get(i);
                totalTicks += PCBTickets.get(tempPCB.processName); //add to the total tickets
            }
            for (int i = 0; i < list.runningQueue.size(); i++) //running queue second
            {
                PCB tempPCB = (PCB)list.runningQueue.get(i);
                totalTicks += PCBTickets.get(tempPCB.processName); //add to the total tickets
            }
            
            //if it is not running anything currently and has one thing to run, run a PCB
            if (list.runningQueue.size() == 0 && list.readyQueue.size() == 1)
            {
                //insert the first PCB in the ready queue
                list.runningQueue.insertPCB((PCB)list.readyQueue.pop());
            }
            
            //if a PCB has finished running, and the readyQueue has more than 1 do lottery
            if (list.runningQueue.size() == 0 && list.readyQueue.size() > 1)
            {
                int lottery = 0;//use to find the winner of the lottery
                //System.out.println("size" + list.readyQueue.size());
            //System.out.println("amt: " + amountOfTicks);
             //System.out.println("tot: " + totalTicks);

                //get the ticket draw
                if (amountOfTicks <= totalTicks) //enough for full lottery draw
                {
                    lottery = generator.nextInt(amountOfTicks) + 1; //get the ticket number
               //     System.out.println("lot: " + lottery);
                }
                else //not enough for full lottery draw
                {
                    lottery = generator.nextInt(totalTicks) + 1; //get the ticket number
                 //   System.out.println("lot: " + lottery);
                }
                int currentTicks = 0; //current omount of tickets
                //search through the readyQueue for the winner
                for(int i = 0; i < list.readyQueue.size(); i++)
                {   
                    PCB tempPCB = (PCB)list.readyQueue.get(i); //temp PCB for comparisons
                    
                    currentTicks += PCBTickets.get(tempPCB.processName); //add to current ticket counter
                    
                    if (currentTicks >= lottery) //current ticket has been found
                    {
                        //and push the next PCB in the readyQueue onto the running queue
                        list.runningQueue.insertPCB((PCB)list.readyQueue.remove(i));
                        break;
                    }
                }
            }
 
            //if the time quantum has expired, swap PCBs
            //if totalTime % time quantum == 0, that means that it has passed the time quantum
            //totalTime cannot = 0 in this comparison, as 0 % anything is 0
            if (list.runningQueue.size() > 0 && list.readyQueue.size() > 0 && 
               (list.runningQueue.totalTime % timeQuantum == 0) && list.runningQueue.totalTime != 0)
            {
                //push the running PCB onto the readyQueue
                list.readyQueue.insertPCB(list.runningQueue.removeRunningPCB());
                int lottery = 0;//use to find the winner of the lottery
                //System.out.println("size" + list.readyQueue.size());
            //System.out.println("amt: " + amountOfTicks);
             //System.out.println("tot: " + totalTicks);

                //get the ticket draw
                if (amountOfTicks <= totalTicks) //enough for full lottery draw
                {
                    lottery = generator.nextInt(amountOfTicks) + 1; //get the ticket number
               //     System.out.println("lot: " + lottery);
                }
                else //not enough for full lottery draw
                {
                    lottery = generator.nextInt(totalTicks) + 1; //get the ticket number
                 //   System.out.println("lot: " + lottery);
                }
                int currentTicks = 0; //current omount of tickets
                //search through the readyQueue for the winner
                for(int i = 0; i < list.readyQueue.size(); i++)
                {   
                    PCB tempPCB = (PCB)list.readyQueue.get(i); //temp PCB for comparisons
                    
                    currentTicks += PCBTickets.get(tempPCB.processName); //add to current ticket counter
                    
                    if (currentTicks >= lottery) //current ticket has been found
                    {
                        //and push the next PCB in the readyQueue onto the running queue
                        list.runningQueue.insertPCB((PCB)list.readyQueue.remove(i));
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
