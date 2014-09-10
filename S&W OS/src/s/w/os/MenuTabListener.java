package s.w.os;

//Event Items
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuTabListener implements ActionListener 
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
      Command command = (Command)e.getSource();
      command.execute();
    }
}
