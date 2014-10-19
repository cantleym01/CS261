package s.w.os;

/**
 * FILE FORMAT FOR PCB: Process Name, Class, Priority, Memory, Time Remaining, Time of Arrival, Percentage of CPU
 * 
 * NOTE: THE PARSER TAKES PCBs WITH UNIQUE PROCESS NAMES, TIME REMAINING, AND TIME OF ARRIVAL
 * IT WILL DO FUNKY STUFF WITH PCBs WITH THE SAME DATA IN ANY OF THESE 3 FIELDS
 * THE ONLY SCHEDULER TO BE AFFECTED BY SAME TIME REMAININGS WILL BE THE SJF
 * AND ON THE OTHER END, SAME TIME OF ARRIVAL WILL AFFECT ALL SCHEDULERS EXCEPT SJF
 * SAME PROCESS NAMES WILL AFFECT ALL SCHEDULERS
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PCBParser 
{
    private File PCBFile; //file for the PCBs
    private int nextTOA; //the next Time of Arrival for incomplete knowledge queues
    private  String currentLine = null; //string for the current line being read from the file
    private BufferedReader reader; //reader to help file input
    public Boolean quit = false; //if the user chooses to stop in the input
    public Boolean doneWFile = false; //bool to tell when the file is done reading
    
    //hashtables to hold info
    public Hashtable<Integer, String> processNameTime = new Hashtable<Integer, String>(); //process names accessed by time remaining
    public Hashtable<Integer, String> processNameTOA = new Hashtable<Integer, String>(); //process names accessed by time of arrival
    
    //data unique to process names
    public Hashtable<String, Integer> timeOfArrival = new Hashtable<String, Integer>(); //time of arrival accessed by process name
    public Hashtable<String, Character> Classes = new Hashtable<String, Character>(); //class accessed by processname
    public Hashtable<String, Integer> CPU = new Hashtable<String, Integer>(); //CPU usage accessed by process name
    public Hashtable<String, Integer> priority = new Hashtable<String, Integer>(); //priority accessed by process name
    public Hashtable<String, Integer> timeRemaining = new Hashtable<String, Integer>(); //time remaining accessed by process name
    public Hashtable<String, Integer> memory = new Hashtable<String, Integer>(); //memory accessed by process name
    
    //linkedLists to use quicksort on time remaining for SJF
    public LinkedList timeSort = new LinkedList();//The data in string form
    
    //read a file with this input for full knowledge schedulers
    public void readWholeFile()
    {
        if (quit) //stop everything if user decides to quit
        {
            return;
        }
        
        while (!doneWFile)
        {
            readNextLine();  
        }
    }
    
    public int getNextTOA() //return next Time of Arrival for the scheduler
    {
        return nextTOA;
    }
    
    public void readNextLine() //read the next line and parse the data
    {   
        try
        {   
            if ((currentLine = reader.readLine()) != null) //read until end of file and parse stuff
            {
                String[] PCBPart = currentLine.split(" "); //split by spaces

                //hash tables
                processNameTime.put(strToInt(PCBPart[4]), PCBPart[0]); //process names accessed by time remaining
                processNameTOA.put(strToInt(PCBPart[5]), PCBPart[0]); //process names accessed by time of arrival
                timeOfArrival.put(PCBPart[0], strToInt(PCBPart[5])); //time of arrival accessed by process name
                Classes.put(PCBPart[0], PCBPart[1].charAt(0)); //class accessed by processname
                CPU.put(PCBPart[0], strToInt(PCBPart[6])); //CPU usage accessed by process name
                priority.put(PCBPart[0], strToInt(PCBPart[2])); //priority accessed by process name
                timeRemaining.put(PCBPart[0], strToInt(PCBPart[4])); //time remaining accessed by process name
                memory.put(PCBPart[0], strToInt(PCBPart[3]));
                
                //insert the data to linked lists
                timeSort.add(strToInt(PCBPart[4]));
                
                //sort the priority and time remaining to find best versions
                quickSort(timeSort, 0, timeSort.size() - 1);
                
                //set next TOA (time of arrival)
                nextTOA = strToInt(PCBPart[5]);
            }
            
            //close the file and be done with everything if we are done reading the file
            if (currentLine == null)
            {
                doneWFile = true;
                reader.close(); //close the reader
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error in Reading the File.");
        }
    }
    
    public void getFileName()
    {
        String fileName; //name of the file
        
        //make a frame to contain the OptionPane
        JFrame inputFrame = new JFrame();
        inputFrame.setSize(500, 100); //width, height
       
        //keep trying to get the filename
        while (true) 
        {
            //get the name
            fileName = (String)JOptionPane.showInputDialog(inputFrame, 
               "Enter a file name. \n"
            + "or type \"Quit\" to stop input");
            
            //if the user wants to quit, let them
            if ("Quit".equals(fileName) || "quit".equals(fileName))
            {
                quit = true;
                return;
            }
            
            PCBFile = new File(fileName); //access the file
            
            //if the input is good, break and continue
            if(PCBFile.exists() && !PCBFile.isDirectory())
            {
                break;
            }
        }
        
        //create the file Buffered reader
        try
        {
        reader = new BufferedReader(new FileReader(PCBFile));
        }
        catch(Exception ex)
        {
            System.out.println("Error in creating Buffered Reader");
        }
    }

    private void quickSort(LinkedList list, int lowVal, int hiVal) //quicksort a list
    {   
        if (list.size() <= 1) //cannot sort the array
        {
            return;
        }
        int middle = lowVal + (hiVal - lowVal) / 2; //the median
        int midEle = (int)list.get(middle); //what the middle element is
        
        //reference these to not change initial values
        int top = lowVal, bottom = hiVal;
        
        while (top <= bottom)
        {
            while((int)list.get(top) < midEle)
            {
                top++;
            }
            
            while((int)list.get(bottom) > midEle)
            {
                bottom--;
            }
            
            if (top <= bottom)
            {
                int temp = (int)list.get(top);
                list.set(top, list.get(bottom));
                list.set(bottom, temp);
                top++;
                bottom--;
            }
        }

        //do recursive stuff
        if (lowVal < bottom)
        {
            quickSort(list, lowVal, bottom);
        }
        if (top < hiVal)
        {
            quickSort(list, top, hiVal);
        }
    }
    
    private int strToInt(String strToChange)
    {   
        int newInt = Integer.parseInt(strToChange);

        return newInt;
    }
}
