package s.w.os;

//Event Items
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MenuTabListener implements ActionListener 
{
    public void actionPerformed(ActionEvent e)
    {
      Command command = (Command)e.getSource();
      command.execute();
    }
}
