package s.w.os;
import java.util.LinkedList;

//This is a linked list because I can override functions as I need and
//they come with good functionality from the start

public class PCBReadyQueue extends LinkedList
{
    PCBReadyQueue(){} //constructor
    
    public int numberOfPCBs()
    {
        return size(); //use LinkedList size() function
    }

    public void insertPCB(PCB PCBToBeAdded) //FIFO addition
    {
        addLast(PCBToBeAdded); //insert the PCB into the last position
    }
}
