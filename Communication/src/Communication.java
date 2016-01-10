import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;

import jssc.SerialPortException;

public class Communication{

	
	private static void createAndShowGUI() {
		 
        //Create and set up the window.
        Frame frame = new Frame();
        //frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);        
        
        //Display the window.
        frame.setFocusable(true);
        frame.setSize(new Dimension(600,600));
        frame.setLayout(new GridLayout());
        frame.setLocation(500, 250);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
	
	}
	
	public static void main(String[] args)
	{

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        
       
    }

}