package s.w.os;

public class MemoryBlock
{
    public int MemorySize = 0; //the size of the memory block
    public int isInUse = 0; //is this memory block in use currently? (0 = false, 1 = true)
    public String PCBInUse; //the name of the PCB in use
}
