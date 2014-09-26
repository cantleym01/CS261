package s.w.os;

import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JFrame;

//File reading and exception handling
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class HelpTab extends JMenuItem implements Command
{
    @Override
    public void execute()
    {
        //Help logic
        JFrame helpFrame = new OSHelpFrame(); //The frame for the Help menu
        
        JTextArea helpTxt = new JTextArea(); //Java swing text extension
        
        //some error checking since this is filereading and not sure-fire
        try
        {
            //I tried help.txt, as it is in the folder with other files, but it is overlooked still
            File helpFile = new File("help.txt");
            FileReader helpRead = new FileReader(helpFile);
            
            BufferedReader reader = new BufferedReader(helpRead);
            
            String currentLine = null;
            
            while ((currentLine = reader.readLine()) != null) //read until end of file
            {
                helpTxt.append(currentLine + "\n"); //append the new line to the text area
            }
            reader.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        helpFrame.add(helpTxt);
        helpFrame.setVisible(true); //make the panel visible
    }
        
}
