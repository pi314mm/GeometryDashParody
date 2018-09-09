package gui;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import sprites.Player;

public class MainMenu{
	private JFrame jfrm;
	
	private static MainMenu mainMenu = new MainMenu();
	
	public static MainMenu getInstance(){
		return mainMenu;
	}
	
	private JComboBox<String> comboBox;
	
	private MainMenu(){
		jfrm = new JFrame("Geometry Dash");
		jfrm.getContentPane().setLayout(new GridLayout(3,1,5,5));
		jfrm.setSize(400,300);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrm.setIconImage(new ImageIcon(Player.class.getClassLoader().getResource("Cube.png")).getImage());
		jfrm.add(new JLabel("Welcome to Geometry Dash!",SwingConstants.CENTER));
		
		JButton start = new JButton("Start");
		start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				jfrm.setVisible(false);
				MapList.displayGui(comboBox.getSelectedItem().toString());
			}
		});
		jfrm.add(start);
		
		comboBox = new JComboBox<String>(MapList.maps());
		jfrm.add(comboBox);
		display();
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				MainMenu.getInstance();
			}
		});
	}
	
	public void display(){
		jfrm.setVisible(true);
		jfrm.toFront();
		jfrm.setState(Frame.NORMAL);
	}
}
