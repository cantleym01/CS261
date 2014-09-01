package s.w.os;
import javax.swing.JFrame; //create the frame of the OS
import javax.swing.JMenuItem; //Sub items, like an exit command in the File Menu
import javax.swing.JOptionPane; //Use this for Yes, No confirmations

public class ExitTab extends JMenuItem implements Command
{
    @Override
    public void execute()
    {
        //Exit logic
        JFrame frame = new JFrame(); //frame for the query of closing
        
        //get whether or not to close the OS
        int query = JOptionPane.showConfirmDialog(frame, "Are you sure?");

        //if result == 0, that means "Yes" was chosen
        if (query == 0)
        {
            System.exit(0);
        }
    }
}
