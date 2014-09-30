package s.w.os;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PCBParser 
{
    private File PCBFile; //file for the PCBs
    private int nextTOA; //the next Time of Arrival for incomplete knowledge queues
    private int seq = 0; //sequential counter
    public Boolean quit = false; //if the user chooses to stop in the input
    public Boolean doneWFile = false; //bool to tell when the file is done reading
    private  String currentLine = null; //string for the current line being read from the file
    private BufferedReader reader; //reader to help file input
    
    //hashtables to hold info
    Hashtable<Integer, String> processNamePriority = new Hashtable<Integer, String>(); //process names accessed by priority
    Hashtable<Integer, String> processNameTime = new Hashtable<Integer, String>(); //process names accessed by time remaining
    Hashtable<Integer, String> processNameSeq = new Hashtable<Integer, String>(); //process names accessed sequentially
    Hashtable<Integer, String> processNameTOA = new Hashtable<Integer, String>(); //process names accessed by time of arrival
    
    //data unique to process names
    Hashtable<String, Integer> timeOfArrival = new Hashtable<String, Integer>(); //time of arrival accessed by process name
    Hashtable<String, Character> Classes = new Hashtable<String, Character>(); //class accessed by processname
    Hashtable<String, Integer> CPU = new Hashtable<String, Integer>(); //CPU usage accessed by process name
    Hashtable<String, Integer> priority = new Hashtable<String, Integer>(); //priority accessed by process name
    Hashtable<String, Integer> timeRemaining = new Hashtable<String, Integer>(); //time remaining accessed by process name
    Hashtable<String, Integer> memory = new Hashtable<String, Integer>(); //memory accessed by process name
    
    //linkedLists to use quicksort on priority and time remaining for some schedulers
    LinkedList prioritySort = new LinkedList();//The data in string form
    LinkedList timeSort = new LinkedList();//The data in string form
    
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
                processNamePriority.put(strToInt(PCBPart[2]), PCBPart[0]); //process names accessed by priority
                processNameTime.put(strToInt(PCBPart[4]), PCBPart[0]); //process names accessed by time remaining
                processNameSeq.put(seq, PCBPart[0]); //process names accessed sequentially
                processNameTOA.put(strToInt(PCBPart[5]), PCBPart[0]); //process names accessed by time of arrival
                timeOfArrival.put(PCBPart[0], strToInt(PCBPart[5])); //time of arrival accessed by process name
                Classes.put(PCBPart[0], PCBPart[1].charAt(0)); //class accessed by processname
                CPU.put(PCBPart[0], strToInt(PCBPart[6])); //CPU usage accessed by process name
                priority.put(PCBPart[0], strToInt(PCBPart[2])); //priority accessed by process name
                timeRemaining.put(PCBPart[0], strToInt(PCBPart[4])); //time remaining accessed by process name
                memory.put(PCBPart[0], strToInt(PCBPart[3]));
                
                //insert the data to linked lists
                prioritySort.add(strToInt(PCBPart[2]));
                timeSort.add(strToInt(PCBPart[4]));
                
                //sort the priority and time remaining to find best versions
                quickSort(prioritySort, 0, prioritySort.size() - 1);
                quickSort(timeSort, 0, timeSort.size() - 1);
                
                //set next TOA (time of arrival)
                nextTOA = strToInt(PCBPart[5]);
                
                seq++; //increment the sequential counter
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

    private void quickSort(LinkedList list, int lowVal, int hiVal) //quicksort an array
    {   
        if (list.size() <= 1) //cannot sort the array
        {
            return;
        }
        int middle = lowVal + (hiVal - lowVal) / 2; //the median
        int midEle = (int)list.get(middle); //what the middle element is
        
        //reference these to not change initial values
        int i = lowVal, j = hiVal;
        
        while (i <= j)
        {
            while((int)list.get(i) < midEle)
            {
                i++;
            }
            
            while((int)list.get(j) > midEle)
            {
                j--;
            }
            
            if (i <= j)
            {
                int temp = (int)list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
                i++;
                j--;
            }
        }

        //do recursive stuff
        if (lowVal < j)
        {
            quickSort(list, lowVal, j);
        }
        if (i < hiVal)
        {
            quickSort(list, i, hiVal);
        }
    }
    
    private int strToInt(String strToChange)
    {   
        int newInt = Integer.parseInt(strToChange);

        return newInt;
    }
}
