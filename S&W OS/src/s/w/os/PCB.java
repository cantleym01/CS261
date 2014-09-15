package s.w.os;

public class PCB
{
    String processName; //the process name for the PCB
    int processClass; //0 = an application, 1 = System
    int priority; //priority for this particular PCB
    Boolean suspendedState; //true means it is suspended, false means its not
    int memoryValue; //how much memory does the process take
}
