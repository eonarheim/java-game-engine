package com.isometric.toolkit.editor;

import java.awt.*;
import javax.swing.*;

public class Editor
{
  public void display ()
  {
    JFrame f = new JFrame("Swing Editor");
    f.setSize(200, 200);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLayout(new FlowLayout());
    f.setVisible(true);
    JLabel jl = new JLabel("Level Editor");
    f.add(jl);
  }

  public static void start ()
  {
    SwingUtilities.invokeLater(new Runnable() {
      public void run ()
      {
        Editor obj = new Editor();
        obj.display();
      }
    });
  }

}
