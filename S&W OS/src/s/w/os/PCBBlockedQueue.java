package s.w.os;
import java.util.LinkedList;

//This is extends to linked list because I can override functions as I need and
//they come with good functionality from the start

public class PCBBlockedQueue extends LinkedList
{   
    PCBBlockedQueue(){} //constructor
    
    public int numberOfPCBs()
    {
        return size(); //use LinkedList size() function
    }

    public void insertPCB(PCB PCBToBeAdded) //FIFO addition
    {
        addLast(PCBToBeAdded); //insert the PCB into the first position
    }
}
