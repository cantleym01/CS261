package s.w.os;

import static javax.lang.model.type.TypeKind.NULL;

public class PCBList 
{
    PCBBlockedQueue blockedQueue; //the blockedQueue of PCB's
    PCBReadyQueue readyQueue; //the readyQueue of PCB's
    
    public PCB AllocatePCB()
    {
        PCB newPCBForOS = new PCB(); //create a new PCB
        
        newPCBForOS.memoryValue = 1; //set a value for allowed memory (1 right now)
        
        return newPCBForOS; //return the obj reference
    }
    
    public void FreePCB(PCB PCBToUnallocateMemory)
    {
        PCBToUnallocateMemory.memoryValue = 0; //memory = 0
    }
    
    public void SetupPCB(String Name, int Priority, int Class)
    {
        //see if it is already in the queues
        if (FindPCB(Name) == NULL) //if it is not, create a new one
        {
            PCB newPCB = AllocatePCB(); //allocate the new PCB
            
            //check for valid priorities
            Boolean validPriority = false; //use to see if the priority is valid
            for (int i = 0; i < 256; i++)
            {
                if (newPCB.priorityAry[i] == Priority) //is the priority in accepted lenghts?
                {
                    validPriority = true;
                }
            }
            
            if (validPriority == true) //if it is accepted, continue
            {
                newPCB.priority = Priority;
            }
            else
            {
                //error message
                System.out.println("Not a valid priority.");
                return;
            }
            
            if (Class == 0 || Class == 1)//if the Class is valid, assign it
            {
                newPCB.processClass = Class;
            }
            else
            {
                //error message
                System.out.println("Class for process is not valid.");
                return;
            }
            
            //everything has been good if this point is reached.
            //now just push it to the ready queue and set it to not suspended
            newPCB.suspendedState = false; //it is not suspended
            readyQueue.insertPCB(newPCB);
        }
        else
        {
            //error message
            System.out.println("PCB already exists.");
            return;
        }
    }
        
    public Object FindPCB(String PCBNameToFind) //Returns an obj, since it can return PCB or NULL
    {
        //since we are not using the obj reference, we need to search the queues
        
        //first, search the readyQueue, by first checking if it has atleast 1 PCB
        if (readyQueue.isEmpty() != true)
        {
            for (int i = 0; i < readyQueue.numberOfPCBs(); i++)
            {
                //first we create a comparePCB to see if it's name is the one we're looking for
                PCB comparePCB;
                comparePCB = (PCB) readyQueue.get(i); //cast the obj to a PCB
                
                if (comparePCB.processName == PCBNameToFind)
                {
                    return comparePCB; //the PCB was found, so return it
                }
            }
        }
        if (blockedQueue.numberOfPCBs() >= 1)
        {
            for (int i = 0; i < blockedQueue.numberOfPCBs(); i++)
            {
                //first we create a comparePCB to see if it's name is the one we're looking for
                PCB comparePCB;
                comparePCB = (PCB) blockedQueue.get(i); //cast the obj to a PCB
                
                if (comparePCB.processName == PCBNameToFind)
                {
                    return comparePCB; //the PCB was found, so return it
                }
            }
        }
        return NULL; //default return if nothing is returned before now
    }
    
    //insert the PCB onto the appropiate queue, 0 = Ready, 1 = blocked
    public void insertPCBToQueue(int ReadyOrBlocked, PCB PCBToInsert)
    {
        if (ReadyOrBlocked == 0) //insert to readyQueue
        {
            readyQueue.insertPCB(PCBToInsert);
        }
        else if (ReadyOrBlocked == 1) //insert to blockedQueue
        {
            blockedQueue.insertPCB(PCBToInsert);
        } 
        else //something is wrong here
        {
            System.out.println("No valid queue to insert.");
        }
    }

    public void removePCB(PCB PCBToRemove)
    {
        if (FindPCB(PCBToRemove.processName) != NULL)
        {
            PCB PCBToRemoveVer2;
            PCBToRemoveVer2 = (PCB) FindPCB(PCBToRemove.processName); //get the PCB
            
            readyQueue.remove(PCBToRemoveVer2);//remove if in readyQueue
            blockedQueue.remove(PCBToRemoveVer2);//remove if in blockedQueue
            
            //I did some experiements, and Java WON'T scream at you for trying
            //to remove something that is not there, so there is no worry about
            //checking both of the Queues at once
        }
        else
        {
            System.out.println("PCB does not exist.");
        }
    }
}
