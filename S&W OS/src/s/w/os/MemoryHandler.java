package s.w.os;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

public class MemoryHandler 
{
    private LinkedList Memory = new LinkedList(); //the memory data structure
    private final int MemorySize = 1024;
    private int MemoryMethod = 0; //default first fit
    //0 = first fit, 1 = next fit, 2 = best fit, 3 = worst fit
    private int nextFitIndex = 0; //index save for next fit algorithm
    
    MemoryHandler()
    {
        //create the initial memory block
        MemoryBlock initial = new MemoryBlock();
        initial.MemorySize = MemorySize;
        initial.isInUse = 0;
        initial.PCBInUse = "";
        
        Memory.add(initial);
    }//constructor
    
    public void setMethod(int method)
    {
        if (method < 0 || method > 3) //error check just incase the one in running queue fails
        {
            System.out.println("Memory Management Method Is Not Valid.");
            return; //keep default first fit instead of crashing
        }
        else
        {
            MemoryMethod = method;
        }
    } //0 = first fit, 1 = next fit, 2 = best fit, 3 = worst fit
    
    public Boolean addPCB(PCB PCBinMem)
    {
        //choose the algorithm
        switch (MemoryMethod)
        {
            case 0: //first fit
                return firstFit(PCBinMem.processName, PCBinMem.memoryValue);
            case 1: //next fit
                return nextFit(PCBinMem.processName, PCBinMem.memoryValue);
            case 2: //best fit
                return bestFit(PCBinMem.processName, PCBinMem.memoryValue);
            case 3: //worst fit
                return worstFit(PCBinMem.processName, PCBinMem.memoryValue);
            default: //this shouldn't be hit... ever... seriously...
                System.out.println("Error in Inserting PCB into Memory!");
                return false;
        }
    } //returns true for it has been inserted, false if not enough room
    
    private Boolean firstFit(String PCBName, int memory)
    {
        int index = 0;
        
        while (index < Memory.size())
        {
            MemoryBlock compBlock = (MemoryBlock)Memory.get(index);
            
            if (compBlock.isInUse == 0 && compBlock.MemorySize == memory) //Like a glove!
            {
                compBlock.PCBInUse = PCBName;
                compBlock.isInUse = 1;
                Memory.set(index, compBlock); //set the new block
                return true; //it has been inserted, so return true
            }
            else if (compBlock.isInUse == 0 && compBlock.MemorySize > memory) //if the PCB can fit, put it in and break up memory
            {
                int remainderMemSize = compBlock.MemorySize - memory; //size for the remainder empty block
                compBlock.PCBInUse = PCBName;
                compBlock.MemorySize = memory;
                compBlock.isInUse = 1;
                Memory.set(index, compBlock); //set the new block
                
                //the memory block that is the remainder
                MemoryBlock splitBlock = new MemoryBlock();
                splitBlock.MemorySize = remainderMemSize;
                splitBlock.PCBInUse = "";
                splitBlock.isInUse = 0;
                insertAfter(compBlock, splitBlock);
                
                return true; //it has been inserted, so return true
            }
            
            index++;
        }
        return false; //it was not inserted, so return false
    }
    
    private Boolean nextFit(String PCBName, int memory)
    {
        
        //unique case of only 1 memory block (size 1024, so the block will always fit unless the input file is wrong)
        //otherwise algorithm will work
        if (Memory.size() == 1)
        {
            MemoryBlock compBlock = (MemoryBlock)Memory.get(0);
            
            if (compBlock.isInUse == 0 && compBlock.MemorySize == memory) //Like a glove!
            {
                compBlock.PCBInUse = PCBName;
                compBlock.isInUse = 1;
                nextFitIndex = 0; //new location saved
                Memory.set(0, compBlock); //set the new block
                return true; //it has been inserted, so return true
            }
            else if (compBlock.isInUse == 0 && compBlock.MemorySize > memory) //if the PCB can fit, put it in and break up memory
            {   
                int remainderMemSize = compBlock.MemorySize - memory; //size for the remainder empty block
                compBlock.PCBInUse = PCBName;
                compBlock.MemorySize = memory;
                compBlock.isInUse = 1;
                Memory.set(0, compBlock); //set the new block
                
                //the memory block that is the remainder
                MemoryBlock splitBlock = new MemoryBlock();
                splitBlock.MemorySize = remainderMemSize;
                splitBlock.PCBInUse = "";
                splitBlock.isInUse = 0;
                insertAfter(compBlock, splitBlock);
                
                nextFitIndex = 0; //new location saved
                return true; //it has been inserted, so return true
            }
        }
        else //it was not the unique case so do regular algorithm
        {
            int index = nextFitIndex + 1; //starts just after where it left off

            //check if the new index starting point is out of bounds
            if (index >= Memory.size())
            {
                index = 0; //if so, set it back to the start
            }

            while (index != nextFitIndex)
            {
                MemoryBlock compBlock = (MemoryBlock)Memory.get(index);

                if (compBlock.isInUse == 0 && compBlock.MemorySize == memory) //Like a glove!
                {
                    compBlock.PCBInUse = PCBName;
                    compBlock.isInUse = 1;
                    nextFitIndex = index; //new location saved
                    Memory.set(index, compBlock); //set the new block
                    return true; //it has been inserted, so return true
                }
                else if (compBlock.isInUse == 0 && compBlock.MemorySize > memory) //if the PCB can fit, put it in and break up memory
                {   
                    int remainderMemSize = compBlock.MemorySize - memory; //size for the remainder empty block
                    compBlock.PCBInUse = PCBName;
                    compBlock.MemorySize = memory;
                    compBlock.isInUse = 1;
                    Memory.set(index, compBlock); //set the new block

                    //the memory block that is the remainder
                    MemoryBlock splitBlock = new MemoryBlock();
                    splitBlock.MemorySize = remainderMemSize;
                    splitBlock.PCBInUse = "";
                    splitBlock.isInUse = 0;
                    insertAfter(compBlock, splitBlock);

                    nextFitIndex = index; //new location saved
                    return true; //it has been inserted, so return true
                }

                //if we hit the end, we are not finished until we hit our last saved location
                if (index == Memory.size() - 1)
                {
                    index = 0; //goes back to the beginning
                }
                else //just keep going
                {
                    index++;
                }
            }
        }
        return false; //it was not inserted, so return false
    }
        
    private Boolean bestFit(String PCBName, int memory)
    {
        int index = 0; //start at the beginning
        int bestIndex = index; //this will tell where the best location is
        int SpaceSave = 10000; //the amount of space left over after splitting the block
        //very large number to start with so it will always fit in the first fittable block
        
        while(index < Memory.size())
        {
            MemoryBlock compBlock = (MemoryBlock)Memory.get(index);
            
            //if the block will save more memory here than the other one, it is the new bestFit
            if (compBlock.isInUse == 0 && compBlock.MemorySize >= memory &&
               ((compBlock.MemorySize - memory) < SpaceSave))
            {
                bestIndex = index;
                SpaceSave = compBlock.MemorySize - memory; //new space saved
                System.out.println(SpaceSave);
            }
            
            index++;
        }
        
        if (SpaceSave != 10000) //the number changed, so the pcb was able to fit somewhere
        {
            MemoryBlock blockToSplit = (MemoryBlock)Memory.get(bestIndex);
            
            if (blockToSplit.MemorySize == memory) //Like a glove!
            {
                blockToSplit.PCBInUse = PCBName;
                blockToSplit.isInUse = 1;
                Memory.set(bestIndex, blockToSplit); //set the new block
            }
            else //have to split the block
            {
                int remainderMemSize = SpaceSave; //size for the remainder empty block
                blockToSplit.PCBInUse = PCBName;
                blockToSplit.MemorySize = memory;
                Memory.set(bestIndex, blockToSplit); //set the new block

                //the memory block that is the remainder
                MemoryBlock splitBlock = new MemoryBlock();
                splitBlock.MemorySize = remainderMemSize;
                splitBlock.PCBInUse = "";
                splitBlock.isInUse = 0;
                insertAfter(blockToSplit, splitBlock);
            }
            
            return true; //it has been inserted, so return true
        }
        else //it did not fit anywhere
        {
            return false; //it was not inserted, so return false
        }
    }
            
    private Boolean worstFit(String PCBName, int memory)
    {
        int index = 0; //start at the beginning
        int worstIndex = index; //this will tell where the worst location is
        int SpaceSave = 10000; //the amount of space left over after splitting the block
        //very large number to start with so it will always fit in the first fittable block
        
        while(index < Memory.size())
        {
            MemoryBlock compBlock = (MemoryBlock)Memory.get(index);
            
            //if the block will save less memory here than the other one, it is the new worstFit
            if (compBlock.isInUse == 0 && compBlock.MemorySize >= memory &&
               ((compBlock.MemorySize - memory) > SpaceSave))
            {
                worstIndex = index;
                SpaceSave = compBlock.MemorySize - memory; //new space saved
            }
            
            index++;
        }
        
        if (SpaceSave != 10000) //the number changed, so the pcb was able to fit somewhere
        {
            MemoryBlock blockToSplit = (MemoryBlock)Memory.get(worstIndex);
            
            if (blockToSplit.MemorySize == memory) //Like a glove!
            {
                blockToSplit.PCBInUse = PCBName;
                blockToSplit.isInUse = 1;
                Memory.set(worstIndex, blockToSplit); //set the new block
            }
            else //have to split the block
            {
                int remainderMemSize = SpaceSave; //size for the remainder empty block
                blockToSplit.PCBInUse = PCBName;
                blockToSplit.MemorySize = memory;
                Memory.set(worstIndex, blockToSplit); //set the new block

                //the memory block that is the remainder
                MemoryBlock splitBlock = new MemoryBlock();
                splitBlock.MemorySize = remainderMemSize;
                splitBlock.PCBInUse = "";
                splitBlock.isInUse = 0;
                insertAfter(blockToSplit, splitBlock);
            }
            
            return true; //it has been inserted, so return true
        }
        
        return false; //it was not inserted, so return false
    }
    
    public void removePCB(PCB PCBtoRemove)
    {
        for (int i = 0; i < Memory.size(); i++)
        {
            MemoryBlock compBlock = (MemoryBlock)Memory.get(i);
            
            if (compBlock.PCBInUse.equals(PCBtoRemove.processName))
            {
                //set it to empty
                compBlock.PCBInUse = "";
                compBlock.isInUse = 0;
                Memory.set(i, compBlock);
            }
        }
    } //remove the PCB running, but that is all (the PCB should always be there)
    
    public void Coalescing()
    {   
        if (Memory.size() <= 1) //Cannot Coalesce empty-ish memory
        {
            return;
        }
        
        int index = 0; //the current location in coalescing the memory
        
        while (true) //while we have not gone through the entire Memory Structure
        {
            MemoryBlock compareBlock = (MemoryBlock)Memory.get(index); //block to compare with
            
            while (compareBlock.isInUse != 0)
            {
                index++; //increase index
                
                if (index >= Memory.size())
                {   
                    return; //done as we can no longer coalesce
                }
                
                compareBlock = (MemoryBlock)Memory.get(index); //new compare block
            }
            
            index++; //increase index for next sequential block
                
            if (index >= Memory.size())
            {
                return; //we are done since we are at end of the thing 
                        //(just a check so we don't get out of bounds)
            }
            
            MemoryBlock currentBlock = (MemoryBlock)Memory.get(index); //block we are checking
            
            while (currentBlock.isInUse == 0) //while we can coalesce, do eeeeeeeeeeeet
            {
                compareBlock.MemorySize += currentBlock.MemorySize; //combine memory
                Memory.remove(currentBlock);//delete old memory
                
                if (index >= Memory.size())
                {   
                    return; //we are done since we are at end of the memory 
                        //(just a check so we don't get out of bounds)
                }
                
                currentBlock = (MemoryBlock)Memory.get(index); //next block
            }
            
            index = Memory.indexOf(compareBlock) + 1;//index is now where the compare block was + 1
        }
        
    } //Coalesce the memory (combine empty sequential blocks)
    
    public void Compaction()
    {   
        int index = 0; //start at beginning
        
        while(index < Memory.size())
        {
            MemoryBlock compBlock = (MemoryBlock)Memory.get(index);
            
            //if the compBlock is not running, put it at the back of the list
            if (compBlock.isInUse == 0)
            {
                Memory.addLast(compBlock); //TO THE BACK WITH YE!
                Memory.remove(index); //remove it from it's last location
            }
            
            index++;
        } 
    } //Compact the memory (not running memory sinks to the bottom of the list)
    
    public void writeMemory()
    {
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Memory.txt", true)))) 
        {
            //print the memory visual
            if (Memory.size() > 0)
            {
                for (int i = 0; i < Memory.size(); i++)
                {
                    MemoryBlock temp = (MemoryBlock)Memory.get(i);
                    out.print(temp.PCBInUse + " ");
                    out.println(temp.MemorySize);
                }
            out.println(" ---------- ");
            }
            out.close();
        }
        catch (Exception e){}
        
    } //write the current visual representation of the memory to a file
    
    private void insertAfter(MemoryBlock prevMem ,MemoryBlock newMem) 
    {
        //if it inserts node after the last node in the list, just add it to the end
        if (Memory.indexOf(prevMem) == Memory.size() - 1)
        {
            Memory.add(newMem);
        }
        else //do less fast insertion
        {
            Memory.add(Memory.indexOf(prevMem) + 1, newMem);
        }
    } //insert a memoryblock after a specified one
}
