import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicArrowButton;

import jssc.SerialPort;
import jssc.SerialPortException;

public class Frame extends JFrame implements WindowListener{
	
	private static final long serialVersionUID = 1L;
	private final int FORWARD = 1;
	private final int BACK = 11;
	private final int FORWARD_RELEASE = 0;
	private final int LEFT = 2;
	private final int RIGHT = 22;
	private final int TURN_RELEASE = 3;
	
	private JTextArea textArea;
	private boolean connected = false;
	private SerialPort serialPort;
	private JComboBox<String> comBox;
	private String selectedCOM = "";
	private JButton up;
    private JButton down;
    private JButton right;
    private JButton left;
    private JPanel pane;
    
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    
    private final String MOVE_FORWARD = "move forward";
	private final String MOVE_BACK = "move back";
	private final String TURN_LEFT = "turn left";
	private final String TURN_RIGHT = "turn right";
	
	private final String FORWARD_RELEASED = "released forward";
	private final String BACK_RELEASED = "released back";
	private final String LEFT_RELEASED = "released left";
	private final String RIGHT_RELEASED = "released right";
	
    private Action forwardAction;
    private Action backAction;
    private Action leftAction;
    private Action rightAction;
    
    private Action forwardReleasedAction;
    private Action backReleasedAction;
    private Action leftReleasedAction;
    private Action rightReleasedAction;
    
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean rightPressed = false;
    private boolean leftPressed = false;

    
    
	public Frame(){
		
		setFocusable(true);
		pane = new JPanel();
		pane.setLayout(new BorderLayout(3,1));
		pane.setFocusable(true);
		
		forwardAction = new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void,Integer> worker = new SwingWorker<Void,Integer>(){
					@Override
					protected Void doInBackground() throws Exception {
						if (!upPressed) { 
							goForward();
							upPressed = true;
						}
						return null;
					}
					
				};
				worker.execute();
			}
			
		};
		backAction = new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void,Integer> worker = new SwingWorker<Void,Integer>(){
					@Override
					protected Void doInBackground() throws Exception {
						if (!downPressed) {	
							goBack();
							downPressed = true;
						}
						return null;
					}
					
				};
				worker.execute();
			}
			
		};
		rightAction = new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void,Integer> worker = new SwingWorker<Void,Integer>(){
					@Override
					protected Void doInBackground() throws Exception {
						if (!rightPressed) {
							turnRight();
							rightPressed = true;
						}
						return null;
					}
					
				};
			worker.execute();
			}
			
		};
		leftAction = new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void,Integer> worker = new SwingWorker<Void,Integer>(){
					@Override
					protected Void doInBackground() throws Exception {
						if (!leftPressed) {
							turnLeft();
							leftPressed = true;
						}
						return null;
					}
					
				};
			worker.execute();
			}	
		};
		
		forwardReleasedAction = new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void,Integer> worker = new SwingWorker<Void,Integer>(){
					
					@Override
					protected Void doInBackground() throws Exception {
						forwardReleased();
						upPressed = false;
						return null;
					}
					
				};
			worker.execute();
			}
			
		};
		backReleasedAction = new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void,Integer> worker = new SwingWorker<Void,Integer>(){
					@Override
					protected Void doInBackground() throws Exception {
						backReleased();
						downPressed = false;
						return null;
					}
					
				};
			worker.execute();
			}
			
		};
		rightReleasedAction = new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void,Integer> worker = new SwingWorker<Void,Integer>(){
					@Override
					protected Void doInBackground() throws Exception {
						rightReleased();
						rightPressed = false;
						return null;
					}
					
				};
			worker.execute();
			}
			
		};
		leftReleasedAction = new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void,Integer> worker = new SwingWorker<Void,Integer>(){
					@Override
					protected Void doInBackground() throws Exception {
						leftReleased();
						leftPressed = false;
						return null;
					}
					
				};
			worker.execute();
			}
		};
		pane.getInputMap(IFW).put(KeyStroke.getKeyStroke("UP"), MOVE_FORWARD);
		pane.getInputMap(IFW).put(KeyStroke.getKeyStroke("DOWN"), MOVE_BACK);
		pane.getInputMap(IFW).put(KeyStroke.getKeyStroke("RIGHT"), TURN_RIGHT);
		pane.getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"), TURN_LEFT);
		
		pane.getInputMap(IFW).put(KeyStroke.getKeyStroke("released UP"), FORWARD_RELEASED);
		pane.getInputMap(IFW).put(KeyStroke.getKeyStroke("released DOWN"), BACK_RELEASED);
		pane.getInputMap(IFW).put(KeyStroke.getKeyStroke("released RIGHT"), RIGHT_RELEASED);
		pane.getInputMap(IFW).put(KeyStroke.getKeyStroke("released LEFT"), LEFT_RELEASED);
		
		pane.getActionMap().put(MOVE_FORWARD,forwardAction);
		pane.getActionMap().put(MOVE_BACK,backAction);
		pane.getActionMap().put(TURN_RIGHT,rightAction);
		pane.getActionMap().put(TURN_LEFT,leftAction);
		
		pane.getActionMap().put(FORWARD_RELEASED,forwardReleasedAction);
		pane.getActionMap().put(BACK_RELEASED,backReleasedAction);
		pane.getActionMap().put(RIGHT_RELEASED,rightReleasedAction);
		pane.getActionMap().put(LEFT_RELEASED,leftReleasedAction);
		
		
		
		
		
		textArea = new JTextArea(5, 20);
		textArea.setEditable(false);
		textArea.setFocusable(false);
		JScrollPane scrollPane = new JScrollPane(textArea); 
        scrollPane.setPreferredSize(new Dimension(150,100));
        
        JPanel buttonPane = new JPanel();
    	String[] comList = {"COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9" };
        comBox = new JComboBox<String>(comList);
        comBox.setSelectedItem("COM4");
        comBox.setFocusable(false);
        buttonPane.add(comBox);
        comBox.setEditable(true);
        comBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedCOM = (String)comBox.getSelectedItem();
			}
        });
        
        // CONNECT BUTTON
        JButton b1 = new JButton("Connect");
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        b1.setHorizontalTextPosition(AbstractButton.CENTER); 
        b1.setMnemonic(KeyEvent.VK_C);
        b1.setActionCommand("disable");
        b1.setFocusable(false);
        
        b1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(!connected){
	        		textArea.append("Connecting to the device on port "+ selectedCOM + "....\n");
	            	try {
	            	    serialPort = new SerialPort(selectedCOM);
	          			textArea.append("Port opened: " + serialPort.openPort() + "\n");
	          			textArea.append("Params set: " + serialPort.setParams(9600, 8, 1, 0) + "\n");
	          			
	          			textArea.append("Connected succesfully!\n");
		            	connected = true;
	
	            	} catch (SerialPortException e1) {
	            		//e1.printStackTrace();
	            		textArea.append("Error: "+e1.getExceptionType()+"\n");	
	            	}
	            	
        		} else {
        			textArea.append("You can't do that, you are already connected!\n");
        		}
        	}
        });
        
        // DISCONNECT BUTTON
        JButton b2 = new JButton("Disonnect");
        b2.setVerticalTextPosition(AbstractButton.CENTER);
        b2.setHorizontalTextPosition(AbstractButton.CENTER);
        b2.setMnemonic(KeyEvent.VK_D);
        b2.setActionCommand("disable");
        b2.setFocusable(false);
        
        b2.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
              disconnect();
          }
        });
        
        
        
        JPanel arrowsPane = new JPanel();
        arrowsPane.setLayout(new GridLayout(3,3));
        arrowsPane.setPreferredSize(new Dimension(150,150));
        up = new BasicArrowButton(BasicArrowButton.NORTH);
        down = new BasicArrowButton(BasicArrowButton.SOUTH);
        right = new BasicArrowButton(BasicArrowButton.EAST);
        left = new BasicArrowButton(BasicArrowButton.WEST);
        
        
        arrowsPane.add(Box.createRigidArea(new Dimension(5,0)));
        arrowsPane.add(up);
        arrowsPane.add(Box.createRigidArea(new Dimension(5,0)));
        
        arrowsPane.add(left);
        arrowsPane.add(Box.createRigidArea(new Dimension(5,0)));
        arrowsPane.add(right);
        
        arrowsPane.add(Box.createRigidArea(new Dimension(5,0)));
        arrowsPane.add(down);
        arrowsPane.add(Box.createRigidArea(new Dimension(5,0)));
        
        buttonPane.add(b1);
        buttonPane.add(b2);
        
        scrollPane.setFocusable(false);
        buttonPane.setFocusable(false);
        
        pane.add(arrowsPane, BorderLayout.PAGE_START);
        pane.add(scrollPane, BorderLayout.PAGE_END);
        pane.add(buttonPane, BorderLayout.CENTER);
        
        add(pane);
        
        
	}
	
	private void goForward(){
		up.getModel().setPressed(true);
		if(connected){
        	try {
        		serialPort.writeInt(FORWARD);
        		System.out.println(serialPort.readBytes(1));
			} catch (SerialPortException e1) {
				textArea.append("Error: "+e1.getExceptionType()+"\n");
				//e1.printStackTrace();
				
			}
        }
    	else {
    		textArea.append("You are not connected.\n");
    	}
	}
	private void goBack(){
		down.getModel().setPressed(true);
		if(connected){
        	try {
        		serialPort.writeInt(BACK);
        		System.out.println(serialPort.readBytes(1));
			} catch (SerialPortException e1) {
				//e1.printStackTrace();
				textArea.append("Error: "+e1.getExceptionType()+"\n");
			}
        }
    	else {
    		textArea.append("You are not connected.\n");
    	}
	}
	
	private void turnLeft(){
		left.getModel().setPressed(true);
		if(connected){
        	try {
        		serialPort.writeInt(LEFT);
        		System.out.println(serialPort.readBytes(1));
			} catch (SerialPortException e1) {
				//e1.printStackTrace();
				textArea.append("Error: "+e1.getExceptionType()+"\n");
			}
        }
    	else {
    		textArea.append("You are not connected.\n");
    	}
	}
	
	private void turnRight(){
		right.getModel().setPressed(true);
		if(connected){
        	try {
        		serialPort.writeInt(RIGHT);
        		System.out.println(serialPort.readBytes(1));
			} catch (SerialPortException e1) {
				//e1.printStackTrace();
				textArea.append("Error: "+e1.getExceptionType()+"\n");
			}
        }
    	else {
    		textArea.append("You are not connected.\n");
    	}	
	}
	
	private void forwardReleased(){
		up.getModel().setPressed(false);
		if (connected) {
			try {
	    		serialPort.writeInt(FORWARD_RELEASE);
	    		System.out.println(serialPort.readBytes(1));
			} catch (SerialPortException e1) {
				//e1.printStackTrace();
				textArea.append("Error: "+e1.getExceptionType()+"\n");
			}
		}
	}
	
	private void backReleased(){
		down.getModel().setPressed(false);
		if (connected) {
			try {
	    		serialPort.writeInt(FORWARD_RELEASE);
	    		System.out.println(serialPort.readBytes(1));
			} catch (SerialPortException e1) {
				//e1.printStackTrace();
				textArea.append("Error: "+e1.getExceptionType()+"\n");
			}
		}

	}
	
	private void rightReleased(){
		right.getModel().setPressed(false);
		if (connected) {
			try {
	    		serialPort.writeInt(TURN_RELEASE);
	    		System.out.println(serialPort.readBytes(1));
			} catch (SerialPortException e1) {
				//e1.printStackTrace();
				textArea.append("Error: "+e1.getExceptionType()+"\n");
			}
		}
	}
	
	private void leftReleased() {
		left.getModel().setPressed(false);
		
		if (connected) {
			try {
	    		serialPort.writeInt(TURN_RELEASE);
	    		System.out.println(serialPort.readBytes(1));
			} catch (SerialPortException e1) {
				//e1.printStackTrace();
				textArea.append("Error: "+e1.getExceptionType()+"\n");
			}
		}
	}
	
	private void disconnect(){
		if(connected){
      	  textArea.append("Disconnectiong from the device....\n");
            try {
				textArea.append("Port closed: " + serialPort.closePort() + "\n");
			} catch (SerialPortException e1) {
					//e1.printStackTrace();
					textArea.append("Error: "+e1.getExceptionType()+"\n");
			}
           textArea.append("Disconnected succefully!\n");
           System.out.println("disconnected");
           connected = false;
        } else {
      	  textArea.append("You can't do that, you are already disconnected!\n");
        }
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		disconnect();
		System.exit(0);
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}