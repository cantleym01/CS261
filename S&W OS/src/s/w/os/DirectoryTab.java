package s.w.os;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;


public class DirectoryTab extends JMenuItem implements Command 
{
    @Override
    public void execute()
    {
        JFrame dirFrame = new JFrame(); //the frame for the directory output
        dirFrame.setSize(300, 500);
        
        JTextArea dirTxt = new JTextArea(); //this will be added to the frame
        
        File currentDir = new File("."); // current directory (if use C:\\, it will go with
                                //the C:\\ directories and files, but no deeper)

        File[] files = currentDir.listFiles(); //the array holding the list of items
        
        //For however many files in the current, dir, add stuff
        for (int i = 0; i < files.length; i++) 
        {
            if (files[i].isDirectory()) //if it is a directory
            {
                dirTxt.append("dir:"); //append directory identifier
            } 
            else //it is a file
            {
                dirTxt.append("     file:"); //append file identifier
            }
            try //Java keeps making me surround lines with this T.T
            {
                dirTxt.append(files[i].getCanonicalPath()); //append the file
            }
            catch (IOException ex) 
            {
                Logger.getLogger(DirectoryTab.class.getName()).log(Level.SEVERE, null, ex);
            }
            dirTxt.append("\n"); //new-line after a dir or file has been appended
        }
        
        dirFrame.add(dirTxt); //add the text to the frame
        dirFrame.setVisible(true); //make the panel visible
    }
}
