package com.isometric.toolkit.editor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import com.sun.awt.*;
import javax.swing.*;

//Following this naming covention:
//JFC (Java Swing) variables should be suffixed by the element type.
//http://geosoft.no/development/javastyle.html
public class Editor {
	public void display() {
		final JFrame f = new JFrame("Swing Editor");
		f.setSize(300, 300);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new FlowLayout());
		f.setVisible(true);
		JLabel jl = new JLabel("Level Editor");
		f.add(jl);

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
		f.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		String[] objectList = { "Grass", "PlayerDude" };
		final JComboBox objectComboBox = new JComboBox(objectList);
		objectComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(f, "Item Selected: "
						+ objectComboBox.getSelectedItem().toString());
			}
		});
		Container cont = f.getContentPane();
		cont.setLayout(new FlowLayout());
		cont.add(objectComboBox);

		JPanel myPanel = new JPanel();
		myPanel.setSize(150, 150);
		myPanel.setOpaque(true);
		myPanel.setDoubleBuffered(false);
		myPanel.setBackground(new Color(30, 190, 250));
		// myPanel.setForeground(Color.CYAN);
		// myPanel.setLayout(new BorderLayout()); //This is what f's it up. I
		// think it wraps so close around btn

		f.getContentPane().add(myPanel, BorderLayout.SOUTH);

		// JButton btn1 = new JButton("Add, but no validation");
		// JButton btn2 = new JButton("Add and validate");
		// JPanel p = new JPanel(new GridLayout(1,2));
		// myPanel.add(btn1); myPanel.add(btn2);

		// JFrame f = new JFrame();
		// f.getContentPane().add(visiblePanel);
		f.getContentPane().add(myPanel, BorderLayout.SOUTH);

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
		myPanel.add(myBut);

		myBut.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("you clicked my button");
			}
		});

		myPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("you clicked my panel");
			}
		});

		f.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("you clicked my window");
			}
		});

	}

	//Test code:
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
