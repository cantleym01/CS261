package s.w.os;
import javax.swing.JMenuItem; //Sub items, like an exit command in the File Menu

public class HistoryTab extends JMenuItem implements Command
{
    @Override
    public void execute()
    {
        //History logic
        System.out.print("Hello");
    }
}
