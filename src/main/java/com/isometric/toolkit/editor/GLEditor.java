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

import org.apache.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.cameras.Camera;
import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.Image;
import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.Window;
import com.isometric.toolkit.engine.World;
import com.isometric.toolkit.parser.WorldBuilder;


/***
 * Main editor for the toolkit.
 * 
 * @author Jeff and Erik
 *
 */
public class GLEditor extends JFrame
{
  // Get Logger
  Logger log = LoggerFactory.getLogger();

  // UI Variable Declarations

  private java.awt.Canvas canvas;
  private javax.swing.JList jList1;
  private javax.swing.JMenu editorMenu;
  private javax.swing.JMenu editMenu;
  private javax.swing.JMenu buildMenu;
  private javax.swing.JMenu helpMenuItem;
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JMenuItem openWorldMenuItem;
  private javax.swing.JMenuItem importMenuItem;
  private javax.swing.JMenuItem runMenuItem;
  private javax.swing.JMenuItem packageMenuItem;
  private javax.swing.JMenuItem exportMenuItem;
  private javax.swing.JMenuItem documentationMenuItem;
  private javax.swing.JMenuItem saveWorldMenuItem;
  private javax.swing.JMenuItem saveAsWorldMenuItem;
  private javax.swing.JMenuItem cutMenuItem;
  private javax.swing.JMenuItem copyMenuItem;
  private javax.swing.JMenuItem checkErrorsMenuItem;
  private javax.swing.JMenuItem aboutMenuItem;
  private javax.swing.JMenuItem quitMenuItem;
  private javax.swing.JMenuItem pasteMenuItem;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JProgressBar jProgressBar1;
  private javax.swing.JTabbedPane editorTabs;

  private javax.swing.JTree projectTree;

  // Xml Editor Controls
  private RSyntaxTextArea xmlEditorTextArea;
  private RTextScrollPane xmlEditorScrollPane;

  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JTextArea jTextArea1;
  private javax.swing.JToolBar jToolBar1;

 

  // End UI Variable Declarations

  // Logic Variable Declarations
  volatile boolean closeRequested;
  private World w;

  private int clickX = 0;
  private int clickY = 0;

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

    
    // Initializes all JSwing UI elements
    log.info("Initializing UI elements");
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

  // TODO: Refactor this code into a custom control
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

  // Todo Refactor this code into a custom control
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

  public void load ()
  {
    projectTree.setModel(new TreeModel() {

      @Override
      public Object getRoot ()
      {
        // TODO Auto-generated method stub
        return w;
      }

      @Override
      public Object getChild (Object parent, int index)
      {

        if (parent instanceof String) {
          String p = (String) parent;
          if (p.compareToIgnoreCase("Levels") == 0) {
            return w.getLevels().get(index);
          }
          else if (p.compareToIgnoreCase("Actors") == 0) {
            return w.getActors().get(index);
          }
          else if (p.compareToIgnoreCase("Sprite Sheets") == 0) {
            return w.getSpriteSheets().keySet().toArray()[index];
          }
          else if (w.getSpriteSheets().keySet().contains(p)) {
            return w.getSpriteSheets().get(p).getAnimations().keySet()
                    .toArray()[index];
          }
          else if (p.compareToIgnoreCase("Sounds") == 0) {
            return w.getSoundManager().getSounds().keySet().toArray()[index];
          }
        }

        // TODO Auto-generated method stub
        if (parent instanceof World && index == 0) {
          return "Levels";
        }
        else if (parent instanceof World && index == 1) {
          return "Actors";
        }
        else if (parent instanceof World && index == 2) {
          return "Sprite Sheets";
        }
        else if (parent instanceof World && index == 3) {
          return "Sounds";
        }
        else {
          return "not implemented";
        }

      }

      @Override
      public int getChildCount (Object parent)
      {
        if (parent instanceof String) {
          String p = (String) parent;
          if (p.compareToIgnoreCase("Levels") == 0) {
            return w.getLevels().size();
          }
          else if (p.compareToIgnoreCase("Actors") == 0) {
            return w.getActors().size();
          }
          else if (p.compareToIgnoreCase("Sprite Sheets") == 0) {
            return w.getSpriteSheets().keySet().size();
          }
          else if (w.getSpriteSheets().keySet().contains(p)) {
            return w.getSpriteSheet(p).getAnimations().keySet().size();
          }
          else if (p.compareToIgnoreCase("Sounds") == 0) {
            return w.getSoundManager().getSounds().keySet().size();
          }
        }

        if (parent instanceof World) {
          return 4;
        }
        else {
          return 1;
        }
      }

      @Override
      public boolean isLeaf (Object node)
      {
        if (node instanceof String) {
          String p = (String) node;
          if (p.compareToIgnoreCase("Levels") == 0) {
            return false;
          }
          else if (p.compareToIgnoreCase("Actors") == 0) {
            return false;
          }
          else if (p.compareToIgnoreCase("Sprite Sheets") == 0) {
            return false;
          }
          else if (w.getSpriteSheets().keySet().contains(p)) {
            return false;
          }
          else if (p.compareToIgnoreCase("Sounds") == 0) {
            return false;
          }
        }

        if (node instanceof World) {
          return false;
        }
        else {
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

      // Setup Display and OpenGL properties
      Display.setParent(canvas);
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

      // TODO: This code is in the wrong spot.
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
                  .getImage(i).loadBufferedImageFromFile()) {

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

      // Setup the XML editor for the world file

      xmlEditorTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
      xmlEditorTextArea.setText(WorldBuilder.writeWorld(w));
      xmlEditorTextArea.setCodeFoldingEnabled(true);
      xmlEditorTextArea.setAntiAliasingEnabled(true);

      xmlEditorScrollPane.setFoldIndicatorEnabled(true);

      // Handle World Events
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

          if (Keyboard.getEventKey() == Keyboard.KEY_S) {
            WorldBuilder.writeWorldToLogger(w);
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

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private
    void initComponents ()
  {

    jToolBar1 = new javax.swing.JToolBar();
    jPanel1 = new javax.swing.JPanel();
    editorTabs = new javax.swing.JTabbedPane();
    projectTree = new javax.swing.JTree();
    jList1 = new javax.swing.JList<String>();
    canvas = new java.awt.Canvas();
    jScrollPane2 = new javax.swing.JScrollPane();
    jScrollPane3 = new javax.swing.JScrollPane();
    jTextArea1 = new javax.swing.JTextArea();

    // Xml Editor Control init
    xmlEditorTextArea = new RSyntaxTextArea(20, 60);
    xmlEditorScrollPane = new RTextScrollPane(xmlEditorTextArea);

    jPanel2 = new javax.swing.JPanel();
    jProgressBar1 = new javax.swing.JProgressBar();
    menuBar = new javax.swing.JMenuBar();
    editorMenu = new javax.swing.JMenu();
    importMenuItem = new javax.swing.JMenuItem();
    exportMenuItem = new javax.swing.JMenuItem();
    openWorldMenuItem = new javax.swing.JMenuItem();

    saveWorldMenuItem = new javax.swing.JMenuItem();
    saveAsWorldMenuItem = new javax.swing.JMenuItem();
    quitMenuItem = new javax.swing.JMenuItem();
    editMenu = new javax.swing.JMenu();
    cutMenuItem = new javax.swing.JMenuItem();
    copyMenuItem = new javax.swing.JMenuItem();
    pasteMenuItem = new javax.swing.JMenuItem();
    buildMenu = new javax.swing.JMenu();
    checkErrorsMenuItem = new javax.swing.JMenuItem();
    runMenuItem = new javax.swing.JMenuItem();
    packageMenuItem = new javax.swing.JMenuItem();
    helpMenuItem = new javax.swing.JMenu();
    documentationMenuItem = new javax.swing.JMenuItem();
    aboutMenuItem = new javax.swing.JMenuItem();

    jToolBar1.setRollover(true);

    

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
      private static final long serialVersionUID = 1L;

      public int getSize ()
      {
        return w == null? 0: w.getSpriteSheet("BasicTileSet").getNumImages();
      }

      public ImageIcon getElementAt (int i)
      {
        return w == null? null: new ImageIcon(w.getSpriteSheet("BasicTileSet")
                .getImage(i).loadBufferedImageFromFile());
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
    
    


    // Set the default canvas color to black 
    canvas.setBackground(new java.awt.Color(0, 0, 0));
    
    // Initialize Editor Tabs
    editorTabs.setFont(new java.awt.Font("Consolas", 1, 14)); 
    editorTabs.setMinimumSize(new java.awt.Dimension(600, 500));
    editorTabs.addTab("[Design View]", canvas);
    editorTabs.addTab("[Source View]", xmlEditorScrollPane);
    
    // Parse back to world file
    editorTabs.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged (ChangeEvent evt)
      {
        JTabbedPane pane = (JTabbedPane) evt.getSource();

        // Get current tab
        int sel = pane.getSelectedIndex();
        log.info("Tab Selections Changed: " + sel);
        
        if (sel == 0) {
          try{
            log.info("Reading world from text area...");
            w = WorldBuilder.parseWorld(xmlEditorTextArea.getText());
          }catch(Exception e){
            log.error("Could not parse world");
          }
        } else if (sel == 1){
         
        }

      }
    });

    jScrollPane3.setViewportView(projectTree);

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

    editorMenu.setText("File");

    importMenuItem.setText("Import...");
    editorMenu.add(importMenuItem);

    exportMenuItem.setText("Export...");
    editorMenu.add(exportMenuItem);

    openWorldMenuItem.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_O,
                          java.awt.event.InputEvent.CTRL_MASK));
    openWorldMenuItem.setText("Open World");
    editorMenu.add(openWorldMenuItem);

    saveWorldMenuItem.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_S,
                          java.awt.event.InputEvent.CTRL_MASK));
    saveWorldMenuItem.setText("Save World");
    editorMenu.add(saveWorldMenuItem);

    saveAsWorldMenuItem.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_S,
                          java.awt.event.InputEvent.ALT_MASK
                                  | java.awt.event.InputEvent.CTRL_MASK));
    saveAsWorldMenuItem.setText("Save World As...");
    saveAsWorldMenuItem.setToolTipText("");
    editorMenu.add(saveAsWorldMenuItem);

    quitMenuItem.setText("Quit");
    editorMenu.add(quitMenuItem);

    menuBar.add(editorMenu);

    editMenu.setText("Edit");

    cutMenuItem.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_X,
                          java.awt.event.InputEvent.CTRL_MASK));
    cutMenuItem.setText("Cut");
    cutMenuItem.setToolTipText("");
    cutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed (java.awt.event.ActionEvent evt)
      {
        jMenuItem4ActionPerformed(evt);
      }
    });
    editMenu.add(cutMenuItem);

    copyMenuItem.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_C,
                          java.awt.event.InputEvent.CTRL_MASK));
    copyMenuItem.setText("Copy");
    editMenu.add(copyMenuItem);

    pasteMenuItem.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_V,
                          java.awt.event.InputEvent.CTRL_MASK));
    pasteMenuItem.setText("Paste");
    editMenu.add(pasteMenuItem);

    menuBar.add(editMenu);

    buildMenu.setText("Build");

    checkErrorsMenuItem.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
    checkErrorsMenuItem.setText("Check for Errors");
    checkErrorsMenuItem.setToolTipText("");
    buildMenu.add(checkErrorsMenuItem);

    runMenuItem.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
    runMenuItem.setText("Run ");
    buildMenu.add(runMenuItem);

    packageMenuItem.setAccelerator(javax.swing.KeyStroke
            .getKeyStroke(java.awt.event.KeyEvent.VK_F5,
                          java.awt.event.InputEvent.CTRL_MASK));
    packageMenuItem.setText("Package for Redistrbution");
    buildMenu.add(packageMenuItem);

    menuBar.add(buildMenu);

    helpMenuItem.setText("Help");

    documentationMenuItem.setText("Documentation");
    helpMenuItem.add(documentationMenuItem);

    aboutMenuItem.setText("About");
    aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed (java.awt.event.ActionEvent evt)
      {
        jMenuItem7ActionPerformed(evt);
      }
    });
    helpMenuItem.add(aboutMenuItem);

    menuBar.add(helpMenuItem);

    setJMenuBar(menuBar);

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
                                                                  .addComponent(editorTabs,
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
                                                .addComponent(editorTabs,
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

    editorTabs.getAccessibleContext().setAccessibleName("[Design View]");

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

}
