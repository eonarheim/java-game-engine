package com.isometric.toolkit.editor;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.isometric.toolkit.cameras.Camera;
import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.Image;
import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.Window;
import com.isometric.toolkit.engine.World;
import com.isometric.toolkit.parser.WorldBuilder;

public class GLEditor extends JFrame
{
  
  volatile boolean closeRequested;
  private World w;

  public static void main (String[] args) throws Exception
  {

    System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");
    System.setProperty("sun.java2d.noddraw", "true");
    System.setProperty("sun.java2d.opengl", "false");

    final int desktopWidth = Display.getDesktopDisplayMode().getWidth();
    final int desktopHeight = Display.getDesktopDisplayMode().getHeight();
    // final Preferences prefs = Preferences.userNodeForPackage(c)

    final GLEditor editor = new GLEditor();
    editor.setFocusTraversalKeysEnabled(false);
    editor.setSize(desktopWidth, desktopHeight);

    editor.setVisible(true);
    editor.run(args);
    editor.dispose();
    System.exit(0);

  }

  public GLEditor ()
  {

    initComponents();
    
    canvas.setFocusTraversalKeysEnabled(false);
    // on Windows we need to transfer focus to the Canvas
    // otherwise keyboard input does not work when using alt-tab
    if (LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_WINDOWS) {
      addWindowFocusListener(new WindowAdapter() {
        @Override
        public void windowGainedFocus (WindowEvent e)
        {
          canvas.requestFocusInWindow();
        }
      });
    }

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing (WindowEvent e)
      {
        closeRequested = true;
      }
    });
    
    
    
  }
  
  public void drawGrid(){
    glPushMatrix();
    glDisable(GL_TEXTURE_2D);
    
    int height = 32;
    int width = 32;

    w.getCamera().applyTransform();
    Motion shift = w.getCamera().getShift();
    glBegin(GL_LINES);
    glColor4f(1.f, 1.f, 1.0f,1f);
    
    float leftRight = (float) Math.floor((w.getCamera().getX()-shift.getDx())/width);
    float upDown = (float) Math.floor((w.getCamera().getY()-shift.getDy())/height);
    //System.out.println(leftRight);
    //System.out.println(upDown);
    
    
    //Draw horizontal lines
    for(float i = 0-shift.getDy()+upDown*-1*width; i<canvas.getHeight(); i+=height){
      
      glVertex2f(0f-shift.getDx()+leftRight*width, i+upDown*width);
      glVertex2f(canvas.getWidth()+w.getCamera().getX(), i+upDown*width);
    }
    
    System.out.println(0-shift.getDy()+leftRight*-1*height);
    //Draw vertical lines
    for(float i = 0-shift.getDy()+leftRight*height; i<canvas.getWidth(); i+=width){
     
      glVertex2f(i,0f-shift.getDy());
      glVertex2f(i,canvas.getHeight()+w.getCamera().getY());
    }
    
    glEnd();
    //glColor4f(1.f,1.f,1.f,1.f);
    glEnable(GL_TEXTURE_2D);
    
    //glEnable(GL_BLEND);
    //f.drawString(0, 5, "("+x+","+y+")");
    //glDisable(GL_BLEND);

    glPopMatrix();
  }

  public void run (String[] args)
  {
    try {
      Display.setParent(canvas);
      //System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
      Display.create();
      Display.setVSyncEnabled(true);

      
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      GL11.glMatrixMode(GL11.GL_PROJECTION);
      GL11.glLoadIdentity();
      GL11.glOrtho(0, canvas.getWidth(), canvas.getHeight(), 0, 1, -1);
      GL11.glMatrixMode(GL11.GL_MODELVIEW);
      GL11.glLoadIdentity();
      GL11.glViewport(0, 0, canvas.getWidth(), canvas.getHeight());
      GL11.glClearColor(0f, 0f, 0f, 1f);
      
      GL11.glScalef(4f, 4f, 4f);
      w = WorldBuilder.parseWorldFromFile("worlds/world.xml");
      Camera c = w.getCamera();
      
      Window.setDebug(false);
      jTextArea1.setText(WorldBuilder.serializeWorld(w));
      
      while (!Display.isCloseRequested()
             && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !closeRequested) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        //a.draw(50, 50);
        //w.update();
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        w.draw();
        drawGrid();
        
        // Capture click events
        if(Mouse.isButtonDown(0)){
          int x = Mouse.getX();
          int y = Mouse.getY();
          System.out.println("Click! X:"+x+" Y:"+y);
          
        }
        
        while (Keyboard.next()) {
          if(Keyboard.getEventKey() == Keyboard.KEY_LEFT){
            c.setX(c.getX()-10);
            //c.applyTransform();
          }
          if(Keyboard.getEventKey() == Keyboard.KEY_UP){
            c.setY(c.getY()-10);
            //c.applyTransform();
          }
          if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT){
            c.setX(c.getX()+10);
            //c.applyTransform();
          }
          if(Keyboard.getEventKey() == Keyboard.KEY_DOWN){
            c.setY(c.getY()+10);
            //c.applyTransform();
          }
          
          if(Keyboard.getEventKey() == Keyboard.KEY_ADD){
            c.setScale(c.getScale() + .1f);
          }
          
          if(Keyboard.getEventKey() == Keyboard.KEY_SUBTRACT){
            c.setScale(c.getScale() - .1f);
          }
          
        }
        
        Display.update();
        Display.sync(60);

      }

      Display.destroy();
      
      this.dispose();
      System.exit(0);
    }
    catch (LWJGLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
  



      /**
       * This method is called from within the constructor to initialize the form.
       * WARNING: Do NOT modify this code. The content of this method is always
       * regenerated by the Form Editor.
       */
      @SuppressWarnings("unchecked")
      // <editor-fold defaultstate="collapsed" desc="Generated Code">
      private void initComponents() {

          jToolBar1 = new javax.swing.JToolBar();
          menuBar1 = new java.awt.MenuBar();
          menu1 = new java.awt.Menu();
          menu2 = new java.awt.Menu();
          jPanel1 = new javax.swing.JPanel();
          jTabbedPane1 = new javax.swing.JTabbedPane();
          canvas = new java.awt.Canvas();
          jScrollPane1 = new javax.swing.JScrollPane();
          jTextArea1 = new javax.swing.JTextArea();
          jPanel2 = new javax.swing.JPanel();
          jProgressBar1 = new javax.swing.JProgressBar();
          jMenuBar1 = new javax.swing.JMenuBar();
          jMenu1 = new javax.swing.JMenu();
          jMenuItem10 = new javax.swing.JMenuItem();
          jMenuItem13 = new javax.swing.JMenuItem();
          jMenuItem1 = new javax.swing.JMenuItem();
          jMenuItem2 = new javax.swing.JMenuItem();
          jMenuItem3 = new javax.swing.JMenuItem();
          jMenuItem8 = new javax.swing.JMenuItem();
          jMenu2 = new javax.swing.JMenu();
          jMenuItem4 = new javax.swing.JMenuItem();
          jMenuItem5 = new javax.swing.JMenuItem();
          jMenuItem9 = new javax.swing.JMenuItem();
          jMenu5 = new javax.swing.JMenu();
          jMenuItem6 = new javax.swing.JMenuItem();
          jMenuItem11 = new javax.swing.JMenuItem();
          jMenuItem12 = new javax.swing.JMenuItem();
          jMenu6 = new javax.swing.JMenu();
          jMenuItem14 = new javax.swing.JMenuItem();
          jMenuItem7 = new javax.swing.JMenuItem();

          jToolBar1.setRollover(true);

          menuBar1.setName("");

          menu1.setLabel("File");
          menuBar1.add(menu1);

          menu2.setLabel("Edit");
          menuBar1.add(menu2);

          setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
          setTitle("The Yet To Be Name Jave 2D Game Editor");
          setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
          setMinimumSize(new java.awt.Dimension(1000, 600));

          jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
          jPanel1.setMinimumSize(new java.awt.Dimension(200, 0));

          javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
          jPanel1.setLayout(jPanel1Layout);
          jPanel1Layout.setHorizontalGroup(
              jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGap(0, 196, Short.MAX_VALUE)
          );
          jPanel1Layout.setVerticalGroup(
              jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGap(0, 0, Short.MAX_VALUE)
          );

          jTabbedPane1.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
          jTabbedPane1.setMinimumSize(new java.awt.Dimension(600, 500));

          canvas.setBackground(new java.awt.Color(0, 0, 0));
          jTabbedPane1.addTab("[Design View]", canvas);
          jTextArea1.setFont(new java.awt.Font("Consolas", 1, 14));
          jTextArea1.setColumns(20);
          jTextArea1.setRows(5);
          jScrollPane1.setViewportView(jTextArea1);

          jTabbedPane1.addTab("[Source View]", jScrollPane1);
          
          jTabbedPane1.addChangeListener(new ChangeListener() {         

            @Override
            public void stateChanged (ChangeEvent evt)
            {
              JTabbedPane pane = (JTabbedPane)evt.getSource();

              // Get current tab
              int sel = pane.getSelectedIndex();
              System.out.println("State changed event: " + sel);
              if(sel == 0){
                System.out.println(jTextArea1.getText());
                w = WorldBuilder.parseWorld(jTextArea1.getText());
              }
              
              
            }
        });

          jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

          javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
          jPanel2.setLayout(jPanel2Layout);
          jPanel2Layout.setHorizontalGroup(
              jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGap(0, 196, Short.MAX_VALUE)
          );
          jPanel2Layout.setVerticalGroup(
              jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGap(0, 0, Short.MAX_VALUE)
          );

          jMenu1.setText("File");

          jMenuItem10.setText("Import...");
          jMenu1.add(jMenuItem10);

          jMenuItem13.setText("Export...");
          jMenu1.add(jMenuItem13);

          jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
          jMenuItem1.setText("Open World");
          jMenu1.add(jMenuItem1);

          jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
          jMenuItem2.setText("Save World");
          jMenu1.add(jMenuItem2);

          jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
          jMenuItem3.setText("Save World As...");
          jMenuItem3.setToolTipText("");
          jMenu1.add(jMenuItem3);

          jMenuItem8.setText("Quit");
          jMenu1.add(jMenuItem8);

          jMenuBar1.add(jMenu1);

          jMenu2.setText("Edit");

          jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
          jMenuItem4.setText("Cut");
          jMenuItem4.setToolTipText("");
          jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  jMenuItem4ActionPerformed(evt);
              }
          });
          jMenu2.add(jMenuItem4);

          jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
          jMenuItem5.setText("Copy");
          jMenu2.add(jMenuItem5);

          jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
          jMenuItem9.setText("Paste");
          jMenu2.add(jMenuItem9);

          jMenuBar1.add(jMenu2);

          jMenu5.setText("Build");

          jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
          jMenuItem6.setText("Check for Errors");
          jMenuItem6.setToolTipText("");
          jMenu5.add(jMenuItem6);

          jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
          jMenuItem11.setText("Run ");
          jMenu5.add(jMenuItem11);

          jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, java.awt.event.InputEvent.CTRL_MASK));
          jMenuItem12.setText("Package for Redistrbution");
          jMenu5.add(jMenuItem12);

          jMenuBar1.add(jMenu5);

          jMenu6.setText("Help");

          jMenuItem14.setText("Documentation");
          jMenu6.add(jMenuItem14);

          jMenuItem7.setText("About");
          jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  jMenuItem7ActionPerformed(evt);
              }
          });
          jMenu6.add(jMenuItem7);

          jMenuBar1.add(jMenu6);

          setJMenuBar(jMenuBar1);

          javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
          getContentPane().setLayout(layout);
          layout.setHorizontalGroup(
              layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                      .addGroup(layout.createSequentialGroup()
                          .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                          .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                          .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                  .addContainerGap())
          );
          layout.setVerticalGroup(
              layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                      .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                      .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          );

          jTabbedPane1.getAccessibleContext().setAccessibleName("[Design View]");

          pack();
      }// </editor-fold>

      private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
          // TODO add your handling code here:
      }

      private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {
          // TODO add your handling code here:
      }

      /**
       * @param args the command line arguments
       */
      /*
      public static void main(String args[]) {
          
          try {
              for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                  if ("Nimbus".equals(info.getName())) {
                      javax.swing.UIManager.setLookAndFeel(info.getClassName());
                      break;
                  }
              }
          } catch (ClassNotFoundException ex) {
              java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
          } catch (InstantiationException ex) {
              java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
          } catch (IllegalAccessException ex) {
              java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
          } catch (javax.swing.UnsupportedLookAndFeelException ex) {
              java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
          }
          //</editor-fold>

        
          java.awt.EventQueue.invokeLater(new Runnable() {
              public void run() {
                  new Editor().setVisible(true);
              }
          });
      }*/
      // Variables declaration - do not modify
      private java.awt.Canvas canvas;
      private javax.swing.JMenu jMenu1;
      private javax.swing.JMenu jMenu2;
      private javax.swing.JMenu jMenu5;
      private javax.swing.JMenu jMenu6;
      private javax.swing.JMenuBar jMenuBar1;
      private javax.swing.JMenuItem jMenuItem1;
      private javax.swing.JMenuItem jMenuItem10;
      private javax.swing.JMenuItem jMenuItem11;
      private javax.swing.JMenuItem jMenuItem12;
      private javax.swing.JMenuItem jMenuItem13;
      private javax.swing.JMenuItem jMenuItem14;
      private javax.swing.JMenuItem jMenuItem2;
      private javax.swing.JMenuItem jMenuItem3;
      private javax.swing.JMenuItem jMenuItem4;
      private javax.swing.JMenuItem jMenuItem5;
      private javax.swing.JMenuItem jMenuItem6;
      private javax.swing.JMenuItem jMenuItem7;
      private javax.swing.JMenuItem jMenuItem8;
      private javax.swing.JMenuItem jMenuItem9;
      private javax.swing.JPanel jPanel1;
      private javax.swing.JPanel jPanel2;
      private javax.swing.JProgressBar jProgressBar1;
      private javax.swing.JScrollPane jScrollPane1;
      private javax.swing.JTabbedPane jTabbedPane1;
      private javax.swing.JTextArea jTextArea1;
      private javax.swing.JToolBar jToolBar1;
      private java.awt.Menu menu1;
      private java.awt.Menu menu2;
      private java.awt.MenuBar menuBar1;
      // End of variables declaration


}
