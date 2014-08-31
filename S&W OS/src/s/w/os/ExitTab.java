package s.w.os;
import javax.swing.JMenuItem; //Sub items, like an exit command in the File Menu
import javax.swing.JOptionPane; //Use this for Yes, No confirmations

public class ExitTab extends JMenuItem implements Command
{
    @Override
    public void execute()
    {
        //Exit logic
    }
}
