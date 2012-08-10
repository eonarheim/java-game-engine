package com.isometric.toolkit.editor;

import static org.junit.Assert.fail;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.sun.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.Image;

//Following this naming covention:
//JFC (Java Swing) variables should be suffixed by the element type.
//http://geosoft.no/development/javastyle.html
public class Editor {
	private Icon imageIcon;
	private String resourceFolder = "images/";

	public void display() {
		final JFrame f = new JFrame("Swing Editor");
		f.setSize(500, 500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// f.setLayout(new FlowLayout());
		f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
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
		// f.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		f.add(buttonPanel);

		// Create/Add combo box - load files from "images/" folder
		String[] objectList = null;
		File dir = null;
		try {
			dir = new File(ClassLoader.getSystemResource(resourceFolder)
					.toURI());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Set combo box list - do some checking on it
		objectList = dir.list();
		if (objectList == null)
			System.out.println("No resource files found"); // TODO Change these
															// to log file
		else if (objectList.length == 0)
			System.out.println("No resource files found");

		final JComboBox objectComboBox = new JComboBox(objectList);
		objectComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(f, "Item Selected: "
						+ objectComboBox.getSelectedItem().toString());
			}
		});
		f.add(objectComboBox);

		final int gridRowCount = 5;
		final int gridColCount = 8;
		// Wtf, how do I force a minimum number of grids - I need to create a
		// label for each one

		final JPanel myPanel = new JPanel(new GridLayout(gridRowCount,
				gridColCount));
		for (int i = 0; i < (gridRowCount * gridColCount); i++) {
			JLabel j = new JLabel();
			j.setBorder(new LineBorder(Color.YELLOW));
			j.setOpaque(true);
			j.setBackground(Color.BLACK);
			myPanel.add(j);
		}
		myPanel.setSize(50, 50); // currently doesn't seem to do anything
		myPanel.setOpaque(true);
		myPanel.setDoubleBuffered(false);
		myPanel.setBackground(new Color(0, 0, 0));

		// for (Component j: myPanel2.getComponents())
		// {
		// ((JLabel)j).setBorder(new LineBorder(Color.YELLOW));
		// ((JLabel)j).setOpaque(true);
		// ((JLabel)j).setBackground(Color.BLACK);
		// }
		f.add(myPanel);

		final JButton myBut = new JButton();
		myBut.setFont(new Font("arial", Font.BOLD, 10));
		myBut.setForeground(Color.YELLOW);
		myBut.setBackground(Color.GREEN);
		myBut.setContentAreaFilled(true);
		myBut.setSize(100, 50);
		myBut.setLocation(50, 25);

		myBut.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("you clicked my button ~ x:" + e.getX()
						+ " y: " + e.getY());
			}
		});

		myPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("you clicked my panel ~ x:" + e.getX()
						+ " y: " + e.getY());
				System.out.println("Panel Width: " + myPanel.getWidth()
						+ " Panel Height: " + myPanel.getHeight());
				double rowHeight = myPanel.getHeight() / gridRowCount;
				double colWidth = myPanel.getWidth() / gridColCount;
				double rowClicked = Math.floor((e.getY() / rowHeight));
				double colClicked = Math.floor((e.getX() / colWidth));
				// why do i have to make rowcounts final?
				System.out.println("Cell: " + rowClicked + "," + colClicked);
				JLabel clickedLabel = (JLabel) myPanel
						.getComponent((int) ((rowClicked * gridColCount) + colClicked));

				try {
					BufferedImage image = ImageIO.read(ClassLoader
							.getSystemResourceAsStream(resourceFolder
									+ objectComboBox.getSelectedItem()
											.toString()));
					// Should I read these in from a textbox somewhere?
					int horizontalCount = 10;
					int verticalCount = 1;

					int w = image.getWidth();
					int h = image.getHeight();
					int verticalSpacing = (int) Math.floor(w / horizontalCount);
					int horizontalSpacing = (int) Math.floor(h / verticalCount);

					List<BufferedImage> images = new ArrayList<BufferedImage>();
					for (int j = 0; j < verticalCount; j++) {
						for (int i = 0; i < horizontalCount; i++) {
							images.add(image.getSubimage((i) * verticalSpacing,
									j * horizontalSpacing, verticalSpacing,
									horizontalSpacing));
						}
					}
					BufferedImage tmp = null;
					if (image == null) {
						// logger.error("Internal image "+ref+" failed to load!");
					} else
						tmp = images.get(0);

					imageIcon = new ImageIcon(tmp);
				} catch (IOException ex) {
					// TODO handle exception...
					ex.printStackTrace();
				}

				// if imageIcon is null, it just shows nothing
				if (clickedLabel.getIcon() != imageIcon)
					clickedLabel.setIcon(imageIcon);
				else
					clickedLabel.setIcon(null);
				// clickedLabel.setBackground(Color.BLACK);
				// clickedLabel.setOpaque(true);
				// clickedLabel.setForeground(Color.GREEN);
				// clickedLabel.setText("c");
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
