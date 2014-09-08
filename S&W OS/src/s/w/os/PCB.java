package s.w.os;

public class PCB
{
    String processName; //the process name for the PCB
    int processClass; //0 = an application, 1 = System
    int[] priorityAry = new int[256]; //an array of all valid numbers
    int priority; //priority for this particular PCB
    Boolean suspendedState; //true means it is suspended, false means its not
    int memoryValue; //how much memory does the process take
    
    public PCB()
    {
        int priorNum = -127;//starting num
        
        for (int i = 0; i < 256; i++) //fill priority with -127 -> +128
        {
            priorityAry[i] = priorNum;
            priorNum++;
        }
    } //constructor
}
