package s.w.os;

import java.awt.FlowLayout; //FlowLayout to make text prettier
import javax.swing.JFrame; //create the frame of the help command

public class OSHelpFrame extends JFrame
{
    private static final int FrameW = 950; //frame width
    private static final int FrameH = 200; //frame height
    
    public OSHelpFrame()
    {   
        setSize(FrameW, FrameH); //set the size of the help panel
        setLayout(new FlowLayout(FlowLayout.LEFT));
    } 
}
