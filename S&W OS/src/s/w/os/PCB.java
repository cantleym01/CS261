package s.w.os;

public class PCB
{
    String processName; //the process name for the PCB
    int processClass; //0 = an application, 1 = System
    int priority; //priority for this particular PCB
    Boolean suspendedState; //true means it is suspended, false means its not
    int memoryValue; //how much memory does the process take
    int timeRemaining; //how much time left until completion
    int timeOfArrival; //what is it's time of arrival to the system
    int CPU; //what is it's CPU usage percentage?
    int PCBTimer; //this is used to calculate turn-around time
}
