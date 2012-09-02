package com.isometric.toolkit.editor;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_BLEND;
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
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.ImageIcon;

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

  private int clickX = 0;
  private int clickY = 0;

  private int upX = 0;
  private int upY = 0;

  float oldX = 0;
  float oldY = 0;

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

  public void drawGrid ()
  {
    glPushMatrix();
    glDisable(GL_TEXTURE_2D);

    int height = 48;
    int width = 44;

    w.getCamera().applyTransform();
    Motion shift = w.getCamera().getShift();
    glBegin(GL_LINES);
    glColor4f(1.f, 1.f, 1.0f, 1f);

    // I apologize for what follows
    // Begin shame
    float leftRight =
      (float) Math.floor((w.getCamera().getX() - shift.getDx()) / width) + 1;
    float upDown =
      (float) Math.floor((w.getCamera().getY() - shift.getDy()) / height) + 1;

    // Draw horizontal lines
    for (float i = 0 + upDown * height; i < canvas.getHeight() + upDown
                                            * height + 1; i += height) {

      glVertex2f(0f - shift.getDx() + w.getCamera().getX(), i);
      glVertex2f(canvas.getWidth() + w.getCamera().getX(), i);
    }

    // Draw vertical lines
    for (float i = 0 + leftRight * width; i < canvas.getWidth() + leftRight
                                              * width + 1; i += width) {

      glVertex2f(i, 0f - shift.getDy() + w.getCamera().getY());
      glVertex2f(i, canvas.getHeight() + w.getCamera().getY());
    }
    // End shame

    glEnd();

    glEnable(GL_TEXTURE_2D);

    glPopMatrix();
  }

  public void drawSelection ()
  {

  }

  public void drawCursor ()
  {
    int height = 48;
    int width = 44;

    glPushMatrix();
    glDisable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);

    w.getCamera().applyTransform();
    Camera c = w.getCamera();
    Motion shift = c.getShift();
    // I apologize for what follows
    // Begin shame
    float leftRight =
      (float) Math.floor((w.getCamera().getX() - shift.getDx()) / width) + 1;
    float upDown =
      (float) Math.floor((w.getCamera().getY() - shift.getDy()) / height) + 1;

    float left =
      (float) Math.floor((Mouse.getX() + c.getX() - shift.getDx())
                         / c.getScale() / width)
              * width;// *(1/canvas.getWidth())*leftRight;
    float top =
      (float) Math
              .floor(((canvas.getHeight() - Mouse.getY()) + c.getY() - shift
                      .getDy()) / c.getScale() / height)
              * height;// *(1/canvas.getHeight())*upDown;
    // System.out.println("X:"+Mouse.getX());
    // System.out.println("Y:"+(canvas.getHeight()-Mouse.getY()));

    glBegin(GL_QUADS);
    glColor4f(0.f, 0.f, 1.0f, .2f);
    glVertex2f(left, top);
    glVertex2f(left, top + height);
    glVertex2f(left + width, top + height);
    glVertex2f(left + width, top);

    glEnd();

    glColor4f(1.f, 1.f, 1.0f, 1f);
    glDisable(GL_BLEND);
    glEnable(GL_TEXTURE_2D);
    glPopMatrix();

  }
  public void load(){
    jTree1.setModel(new TreeModel() {

      @Override
      public Object getRoot ()
      {
        // TODO Auto-generated method stub
        return w;
      }

      @Override
      public Object getChild (Object parent, int index)
      {
        
        if(parent instanceof String){
          String p = (String) parent;
          if(p.compareToIgnoreCase("Levels")==0){
            return w.getLevels().get(index);
          }else if(p.compareToIgnoreCase("Actors")==0){
            return w.getActors().get(index);
          }else if(p.compareToIgnoreCase("Sprite Sheets")==0){
            return w.getSpriteSheets().keySet().toArray()[index];
          }else if(w.getSpriteSheets().keySet().contains(p)){
            return w.getSpriteSheets().get(p).getAnimations().keySet().toArray()[index];
          }else if(p.compareToIgnoreCase("Sounds")==0){
            return w.getSoundManager().getSounds().keySet().toArray()[index];
          }
        }
        
        // TODO Auto-generated method stub
        if(parent instanceof World && index==0){
          return "Levels";
        }else if(parent instanceof World && index==1){
          return "Actors";
        }else if(parent instanceof World && index==2){
          return "Sprite Sheets";
        }else if(parent instanceof World && index==3){
          return "Sounds";
        }else{
          return "not implemented";
        }
        
        
        
      }

      @Override
      public int getChildCount (Object parent)
      {
        if(parent instanceof String){
          String p = (String) parent;
          if(p.compareToIgnoreCase("Levels")==0){
            return w.getLevels().size();
          }else if(p.compareToIgnoreCase("Actors")==0){
            return w.getActors().size();
          }else if(p.compareToIgnoreCase("Sprite Sheets")==0){
            return w.getSpriteSheets().keySet().size();
          }else if(w.getSpriteSheets().keySet().contains(p)){
              return w.getSpriteSheet(p).getAnimations().keySet().size();
          }else if(p.compareToIgnoreCase("Sounds")==0){
            return w.getSoundManager().getSounds().keySet().size();
          }
        }
        
        
        if(parent instanceof World){
          return 4;
        }else{
          return 1;          
        }
      }

      @Override
      public boolean isLeaf (Object node)
      {
        if(node instanceof String){
          String p = (String) node;
          if(p.compareToIgnoreCase("Levels")==0){
            return false;
          }else if(p.compareToIgnoreCase("Actors")==0){
            return false;
          }else if(p.compareToIgnoreCase("Sprite Sheets")==0){
            return false;
          }else if(w.getSpriteSheets().keySet().contains(p)){
            return false;
          }else if(p.compareToIgnoreCase("Sounds")==0){
            return false;
          }
        }
        
        
        if(node instanceof World){
          return false;
        }else{
          return true;      
        }
        
      }

      @Override
      public void valueForPathChanged (TreePath path, Object newValue)
      {
        // TODO Auto-generated method stub

      }

      @Override
      public int getIndexOfChild (Object parent, Object child)
      {
        // TODO Auto-generated method stub
        return 0;
      }

      @Override
      public void addTreeModelListener (TreeModelListener l)
      {
        // TODO Auto-generated method stub

      }

      @Override
      public void removeTreeModelListener (TreeModelListener l)
      {
        // TODO Auto-generated method stub

      }

    });
  }
  
  public void run (String[] args)
  {
    try {
      Display.setParent(canvas);
      // System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL",
      // "true");
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
      load();
      jList1.setModel(new javax.swing.DefaultListModel<ImageIcon>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public int getSize ()
        {
          return w == null? 0: w.getSpriteSheet("TestPlayer").getNumImages();
        }

        public ImageIcon getElementAt (int i)
        {
          new ImageIcon() {

          };

          return w == null? null: new ImageIcon(w.getSpriteSheet("TestPlayer")
                  .getImage(i).getBufferedImage()) {

            @Override
            public void paintIcon (Component c, Graphics g, int x, int y)
            {
              g.drawImage(getImage(), x + c.getWidth() / 4, y,
                          c.getHeight() / 2, c.getHeight() / 2, c);
            }

          };

        }
      });
      jList1.setFixedCellHeight(100);
      jList1.repaint(10);
      Camera c = w.getCamera();

      Window.setDebug(false);
      jTextArea1.setText(WorldBuilder.serializeWorld(w));
      edit.setFont(new java.awt.Font("Consolas", 1, 14));
      edit.setEditorKit(new XMLEditor());
      edit.setText(WorldBuilder.serializeWorld(w));
      while (!Display.isCloseRequested()
             && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !closeRequested) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        // a.draw(50, 50);
        // w.update();
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        w.draw();
        drawGrid();
        drawSelection();
        drawCursor();

        // Capture click events
        if (Mouse.isButtonDown(0)) {

          clickX = Mouse.getX();
          clickY = Mouse.getY();

          System.out.println("Old Click! X:" + oldX + " Y:" + oldY);
          System.out.println("Click! X:" + clickX + " Y:" + clickY);

          c.setX(c.getX() + oldX - clickX);
          c.setY(c.getY() - oldY + clickY);

          oldX = clickX;
          oldY = clickY;
        }
        if (!Mouse.isButtonDown(0)) {
          oldX = Mouse.getX();
          oldY = Mouse.getY();

        }

        while (Keyboard.next()) {
          if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
            c.setX(c.getX() - 10);
            // c.applyTransform();
          }
          if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
            c.setY(c.getY() - 10);
            // c.applyTransform();
          }
          if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
            c.setX(c.getX() + 10);
            // c.applyTransform();
          }
          if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
            c.setY(c.getY() + 10);
            // c.applyTransform();
          }

          if (Keyboard.getEventKey() == Keyboard.KEY_ADD) {
            c.setScale(c.getScale() + .1f);
          }

          if (Keyboard.getEventKey() == Keyboard.KEY_SUBTRACT) {
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
    private
    void initComponents ()
  {

    jToolBar1 = new javax.swing.JToolBar();
    menuBar1 = new java.awt.MenuBar();
    menu1 = new java.awt.Menu();
    menu2 = new java.awt.Menu();
    jPanel1 = new javax.swing.JPanel();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    jTree1 = new javax.swing.JTree();
    jList1 = new javax.swing.JList<String>();
    canvas = new java.awt.Canvas();
    jScrollPane1 = new javax.swing.JScrollPane();
    jScrollPane2 = new javax.swing.JScrollPane();
    jScrollPane3 = new javax.swing.JScrollPane();
    jTextArea1 = new javax.swing.JTextArea();
    edit = new javax.swing.JEditorPane();
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

    javax.swing.GroupLayout jPanel1Layout =
      new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);

    jList1.setModel(new javax.swing.ListModel<ImageIcon>() {

      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      public int getSize ()
      {
        return w == null? 0: w.getSpriteSheet("BasicTileSet").getNumImages();
      }

      public ImageIcon getElementAt (int i)
      {
        return w == null? null: new ImageIcon(w.getSpriteSheet("BasicTileSet")
                .getImage(i).getBufferedImage());
      }

      @Override
      public void addListDataListener (ListDataListener l)
      {
        // TODO Auto-generated method stub

      }

      @Override
      public void removeListDataListener (ListDataListener l)
      {
        // TODO Auto-generated method stub

      }
    });
    jScrollPane2.setViewportView(jList1);

    jScrollPane2.setViewportView(jList1);

    jPanel1.setLayout(jPanel1Layout);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout
            .setHorizontalGroup(jPanel1Layout
                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout
                                      .createSequentialGroup()
                                      .addContainerGap()
                                      .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane2,
                                                                      javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                      273,
                                                                      Short.MAX_VALUE))
                                      .addContainerGap()));
    jPanel1Layout
            .setVerticalGroup(jPanel1Layout
                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                              jPanel1Layout
                                      .createSequentialGroup()
                                      .addContainerGap()
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                      .addComponent(jScrollPane2,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    500,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addContainerGap()));

    jTabbedPane1.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
    jTabbedPane1.setMinimumSize(new java.awt.Dimension(600, 500));

    canvas.setBackground(new java.awt.Color(0, 0, 0));
    jTabbedPane1.addTab("[Design View]", canvas);
    jTextArea1.setFont(new java.awt.Font("Consolas", 1, 14));
    jTextArea1.setColumns(20);
    jTextArea1.setRows(5);
    jScrollPane1.setViewportView(edit); // was jTextArea1

    jTabbedPane1.addTab("[Source View]", jScrollPane1);

    jTabbedPane1.addChangeListener(new ChangeListener() {

      @Override
      public void stateChanged (ChangeEvent evt)
      {
        JTabbedPane pane = (JTabbedPane) evt.getSource();

        // Get current tab
        int sel = pane.getSelectedIndex();
        System.out.println("State changed event: " + sel);
        if (sel == 0) {
          System.out.println(jTextArea1.getText());
          w = WorldBuilder.parseWorld(jTextArea1.getText());
        }

      }
    });

    
    jScrollPane3.setViewportView(jTree1);

    javax.swing.GroupLayout jPanel2Layout =
      new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout
            .setHorizontalGroup(jPanel2Layout
                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout
                                      .createSequentialGroup()
                                      .addContainerGap()
                                      .addComponent(jScrollPane3,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    195, Short.MAX_VALUE)
                                      .addContainerGap()));
    jPanel2Layout.setVerticalGroup(jPanel2Layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
                              .addComponent(jScrollPane3).addContainerGap()));

    jMenu1.setText("File");

    jMenuItem10.setText("Import...");
    jMenu1.add(jMenuItem10);

    jMenuItem13.setText("Export...");
    jMenu1.add(jMenuItem13);

    jMenuItem1.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_O,
                          java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem1.setText("Open World");
    jMenu1.add(jMenuItem1);

    jMenuItem2.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_S,
                          java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem2.setText("Save World");
    jMenu1.add(jMenuItem2);

    jMenuItem3.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_S,
                          java.awt.event.InputEvent.ALT_MASK
                                  | java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem3.setText("Save World As...");
    jMenuItem3.setToolTipText("");
    jMenu1.add(jMenuItem3);

    jMenuItem8.setText("Quit");
    jMenu1.add(jMenuItem8);

    jMenuBar1.add(jMenu1);

    jMenu2.setText("Edit");

    jMenuItem4.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_X,
                          java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem4.setText("Cut");
    jMenuItem4.setToolTipText("");
    jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed (java.awt.event.ActionEvent evt)
      {
        jMenuItem4ActionPerformed(evt);
      }
    });
    jMenu2.add(jMenuItem4);

    jMenuItem5.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_C,
                          java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem5.setText("Copy");
    jMenu2.add(jMenuItem5);

    jMenuItem9.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_V,
                          java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem9.setText("Paste");
    jMenu2.add(jMenuItem9);

    jMenuBar1.add(jMenu2);

    jMenu5.setText("Build");

    jMenuItem6.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
    jMenuItem6.setText("Check for Errors");
    jMenuItem6.setToolTipText("");
    jMenu5.add(jMenuItem6);

    jMenuItem11.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
    jMenuItem11.setText("Run ");
    jMenu5.add(jMenuItem11);

    jMenuItem12.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_F5,
                          java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem12.setText("Package for Redistrbution");
    jMenu5.add(jMenuItem12);

    jMenuBar1.add(jMenu5);

    jMenu6.setText("Help");

    jMenuItem14.setText("Documentation");
    jMenu6.add(jMenuItem14);

    jMenuItem7.setText("About");
    jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed (java.awt.event.ActionEvent evt)
      {
        jMenuItem7ActionPerformed(evt);
      }
    });
    jMenu6.add(jMenuItem7);

    jMenuBar1.add(jMenu6);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout =
      new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                              .addContainerGap()
                              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jProgressBar1,
                                                              javax.swing.GroupLayout.DEFAULT_SIZE,
                                                              javax.swing.GroupLayout.DEFAULT_SIZE,
                                                              Short.MAX_VALUE)
                                                .addGroup(layout.createSequentialGroup()
                                                                  .addComponent(jPanel2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                  .addComponent(jTabbedPane1,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                  .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                              .addContainerGap()));
    layout.setVerticalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                      layout.createSequentialGroup()
                              .addContainerGap()
                              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jTabbedPane1,
                                                              javax.swing.GroupLayout.DEFAULT_SIZE,
                                                              javax.swing.GroupLayout.DEFAULT_SIZE,
                                                              Short.MAX_VALUE)
                                                .addComponent(jPanel1,
                                                              javax.swing.GroupLayout.DEFAULT_SIZE,
                                                              javax.swing.GroupLayout.DEFAULT_SIZE,
                                                              Short.MAX_VALUE)
                                                .addComponent(jPanel2,
                                                              javax.swing.GroupLayout.DEFAULT_SIZE,
                                                              javax.swing.GroupLayout.DEFAULT_SIZE,
                                                              Short.MAX_VALUE))
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                              .addComponent(jProgressBar1,
                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                            javax.swing.GroupLayout.PREFERRED_SIZE)));

    jTabbedPane1.getAccessibleContext().setAccessibleName("[Design View]");

    pack();
  }// </editor-fold>

  private void jMenuItem4ActionPerformed (java.awt.event.ActionEvent evt)
  {
    // TODO add your handling code here:
  }

  private void jMenuItem7ActionPerformed (java.awt.event.ActionEvent evt)
  {
    // TODO add your handling code here:
  }

  /**
   * @param args
   *          the command line arguments
   */
  /*
   * public static void main(String args[]) {
   * 
   * try {
   * for (javax.swing.UIManager.LookAndFeelInfo info :
   * javax.swing.UIManager.getInstalledLookAndFeels()) {
   * if ("Nimbus".equals(info.getName())) {
   * javax.swing.UIManager.setLookAndFeel(info.getClassName());
   * break;
   * }
   * }
   * } catch (ClassNotFoundException ex) {
   * java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.
   * logging.Level.SEVERE, null, ex);
   * } catch (InstantiationException ex) {
   * java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.
   * logging.Level.SEVERE, null, ex);
   * } catch (IllegalAccessException ex) {
   * java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.
   * logging.Level.SEVERE, null, ex);
   * } catch (javax.swing.UnsupportedLookAndFeelException ex) {
   * java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.
   * logging.Level.SEVERE, null, ex);
   * }
   * //</editor-fold>
   * 
   * 
   * java.awt.EventQueue.invokeLater(new Runnable() {
   * public void run() {
   * new Editor().setVisible(true);
   * }
   * });
   * }
   */
  // Variables declaration - do not modify
  private java.awt.Canvas canvas;
  private javax.swing.JList jList1;
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

  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JTextArea jTextArea1;
  private javax.swing.JEditorPane edit;
  private javax.swing.JToolBar jToolBar1;

  private javax.swing.JTree jTree1;
  private java.awt.Menu menu1;
  private java.awt.Menu menu2;
  private java.awt.MenuBar menuBar1;
  // End of variables declaration

}
