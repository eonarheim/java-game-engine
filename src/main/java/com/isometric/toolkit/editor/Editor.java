package com.isometric.toolkit.editor;

import java.awt.*;

import javax.swing.BorderFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import com.sun.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

//Following this naming covention:
//JFC (Java Swing) variables should be suffixed by the element type.
//http://geosoft.no/development/javastyle.html
public class Editor {
	public void display() {
		final JFrame f = new JFrame("Swing Editor");
		f.setSize(500, 500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//f.setLayout(new FlowLayout());
		 f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
		f.setVisible(true);
		JLabel jl = new JLabel("Level Editor");
		f.add(jl);
		
		//f.setPreferredSize(new Dimension(5,5));
		
		// Create Exit button
		JButton exitButton = new JButton("Exit");
		// Add event handler for button
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(f, "You've clicked Exit");
			}
		});
		// Add button to a panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(exitButton);
		//f.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		f.add(buttonPanel);
		
		String[] objectList = { "Grass", "PlayerDude" };
		final JComboBox objectComboBox = new JComboBox(objectList);
		objectComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(f, "Item Selected: "
						+ objectComboBox.getSelectedItem().toString());
			}
		});
		//ComboBox
		//Container cont = f.getContentPane();
		//cont.setLayout(new FlowLayout());
		//cont.add(objectComboBox);
		f.add(objectComboBox);

//		JPanel myPanel = new JPanel(new GridLayout(0, 2));
//		myPanel.add(new JLabel("JLabel 1"));
//		myPanel.add(new JButton("JButton 2"));
//		myPanel.add(new JCheckBox("JCheckBox 3"));
//		myPanel.add(new JTextField("Long-Named JTextField 4"));
//		myPanel.add(new JButton("JButton 5"));
//
//		//myPanel.setSize(300, 150);
//		myPanel.setOpaque(true);
//		myPanel.setDoubleBuffered(false);
//		myPanel.setBackground(new Color(30, 190, 250));
//		// myPanel.setForeground(Color.CYAN);
//		// myPanel.setLayout(new BorderLayout()); //This is what f's it up. I
//		// think it wraps so close around btn
//
//		f.add(myPanel);
		
		
		//Wtf, how do I force a minimum number of grids
		//Border border = new BorderFactory.createEmptyBorder();// EmptyBorder(1, 1, 1, 1);
		JPanel myPanel2 = new JPanel(new GridLayout(5, 8));
		//myPanel2.setBorder(new EmptyBorder(1, 1, 1, 1));
		for (int i = 0; i < 40; i++)
		{
			myPanel2.add(new JLabel());
		}

		myPanel2.setSize(50, 50); //currently doesn't seem to do anything
		myPanel2.setOpaque(true);
		myPanel2.setDoubleBuffered(false);
		myPanel2.setBackground(new Color(0, 0, 0));
		// myPanel.setForeground(Color.CYAN);
		// myPanel.setLayout(new BorderLayout()); //This is what f's it up. I
		// think it wraps so close around btn
		for (Component j: myPanel2.getComponents())
		{
			((JLabel)j).setBorder(new LineBorder(Color.YELLOW));
		}
		
		f.add(myPanel2);

		// JButton btn1 = new JButton("Add, but no validation");
		// JButton btn2 = new JButton("Add and validate");
		// JPanel p = new JPanel(new GridLayout(1,2));
		// myPanel.add(btn1); myPanel.add(btn2);

		// JFrame f = new JFrame();
		// f.getContentPane().add(visiblePanel);
		// f.getContentPane().add(myPanel, BorderLayout.SOUTH);

		// GraphicsEnvironment env =
		// GraphicsEnvironment.getLocalGraphicsEnvironment();
		// GraphicsDevice[] devices = env.getScreenDevices();
		//
		// GraphicsConfiguration translucencyCapableGC = null;
		//
		// // first see if we can find a translucency-capable graphics device
		// for (int i = 0; i < devices.length && translucencyCapableGC == null;
		// i++)
		// {
		// GraphicsConfiguration[] configs = devices[i].getConfigurations();
		//
		// for (int j = 0; j < configs.length && translucencyCapableGC == null;
		// j++)
		// {
		// //translucency requires java version Java SE 6u10
		// if (AWTUtilities.isTranslucencyCapable(configs[j]))
		// {
		// translucencyCapableGC = configs[j];
		// }
		// }
		// }

		// try
		// {
		// //set window transparent
		// Class<?> awtUtilitiesClass =
		// Class.forName("com.sun.awt.AWTUtilities");
		// Method mSetWindowOpacity =
		// awtUtilitiesClass.getMethod("setWindowOpacity", Window.class,
		// float.class);
		// mSetWindowOpacity.invoke(null, f, Float.valueOf(1.0f));
		// //comment this next line out to see it working as an opaque window
		// AWTUtilities.setWindowOpaque(f, false);
		// }
		// catch (Exception ex)
		// {
		// ex.printStackTrace();
		// }

		final JButton myBut = new JButton();
		myBut.setFont(new Font("arial", Font.BOLD, 10));
		myBut.setForeground(Color.YELLOW);
		// myBut.setContentAreaFilled(false);
		myBut.setBackground(Color.GREEN);
		myBut.setContentAreaFilled(true);
		myBut.setSize(100, 50);
		myBut.setLocation(50, 25);
		//myPanel.add(myBut);

		myBut.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("you clicked my button ~ x:" + e.getX()
						+ " y: " + e.getY());
			}
		});

		myPanel2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("you clicked my panel ~ x:" + e.getX()
						+ " y: " + e.getY());
			}
		});

		f.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("you clicked my window ~ x:" + e.getX()
						+ " y: " + e.getY());
			}
		});

	}

	// Test code:
	JPanel visiblePanel = new JPanel(new FlowLayout()); // is the default, but
														// showing it set

	public void buildGUI() {
		visiblePanel.setBorder(BorderFactory
				.createTitledBorder("Visible Panel"));
		JButton btn1 = new JButton("Add, but no validation");
		JButton btn2 = new JButton("Add and validate");
		JPanel p = new JPanel(new GridLayout(1, 2));
		p.add(btn1);
		p.add(btn2);

		JFrame f = new JFrame();
		f.getContentPane().add(visiblePanel);
		f.getContentPane().add(p, BorderLayout.SOUTH);
		f.setSize(400, 300);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				visiblePanel.add(new JButton("Hello"));
			}
		});
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				visiblePanel.add(new JButton("World"));
				visiblePanel.revalidate();
				visiblePanel.repaint();
			}
		});
	}

	public static void start() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Editor obj = new Editor();
				// obj.buildGUI();
				obj.display();
			}
		});
	}

}
