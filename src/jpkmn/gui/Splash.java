package jpkmn.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import jpkmn.*;

public class Splash extends JFrame {
  private JLabel s = new JLabel();
  private ResetButton r;
  private JLayeredPane p;
  private Player player;

  public Splash(String serial) {
    player = new Player(serial);

    construct();
    setVisible(true);
  }

  public static void showFatalErrorMessage(String extra) {
    JOptionPane.showMessageDialog(null, "Fatal error: "
        + extra, "ERROR", JOptionPane.ERROR_MESSAGE);
  }

  private void construct() {
    setTitle("JPokemon (ver 0.1)");
    setIconImage(Tools.findImage("main-icon"));
    setSize(720, 457); // WIDTH, HEIGHT
    setUndecorated(true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    // Using JLayeredPane so my buttons can sit on the picture
    p = new JLayeredPane();
    ImageIcon bg;
    // Add Splash
    if (Driver.prefs.getBoolean("beat", false))
      bg = new ImageIcon(Tools.findImage("splashalt"));
    else
      bg = new ImageIcon(Tools.findImage("splash"));
    s.setIcon(bg);
    s.setBounds(10, 10, 700, 437);
    p.add(s, new Integer(-1));

    // Load Button
    LoadButton l = new LoadButton(this);
    l.setBounds(550, 100, 110, 30); // 10px border on all sides
    p.add(l, new Integer(0));

    // New Game Button
    NewButton n = new NewButton(this);
    n.setBounds(550, 60, 110, 30);
    p.add(n, new Integer(0));

    // Exit Game Button
    QuitButton q = new QuitButton(this);
    q.setBounds(550, 140, 110, 30);
    p.add(q, new Integer(0));

    // OPTIONAL: Reset Splash logon
    if (Driver.prefs.getBoolean("beat", false)) {
      r = new ResetButton();
      r.setBounds(550, 180, 110, 30);
      p.add(r, new Integer(0));
    }
    add(p);

    setLocationRelativeTo(null);
  }

  private class LoadButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 1L;
    private Splash splash;

    public LoadButton(Splash s) {
      super("Load Game");

      splash = s;
      addActionListener(this);
      setBackground(new Color(77, 206, 77));
      setBorderPainted(false);
      this.setFocusable(false);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

      // Get the file
      JFileChooser fc = new JFileChooser(Driver.prefs.get("save_location", ""));
      File f;
      FileNameExtensionFilter filter = new FileNameExtensionFilter("JPokemon Files",
          "jpkmn");
      
      fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fc.addChoosableFileFilter(filter);
      
      if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        f = fc.getSelectedFile();
        try {
          // Make sure the file is of type .jpkmn
          if (!f.getName()
              .substring(f.getName().lastIndexOf('.'), f.getName().length())
              .equalsIgnoreCase(".jpkmn")) {
            showFatalErrorMessage("not type .jpkmn");
          }
          
          // Store the default save location
          Driver.prefs.put("save_location", f.getAbsolutePath());
          Driver.prefs.flush();

          // Load game
          Scanner scan = new Scanner(f);
          player = Player.fromFile(scan);
          splash.dispose();
          new GameWindow(player);
        } catch (Exception e) {
          e.printStackTrace();
          showFatalErrorMessage("General Error");
        }
      } // End if
    }
  }

  private class NewButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 1L;
    private Splash splash;

    public NewButton(Splash s) {
      super("New Game");

      splash = s;
      addActionListener(this);
      setBackground(new Color(206, 77, 77));
      setBorderPainted(false);
      this.setFocusable(false);

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      splash.dispose();
      player.createNew();
      new GameWindow(player);
    }

  }

  private class QuitButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 1L;
    private Splash splash;

    public QuitButton(Splash s) {
      super("Quit Game");

      splash = s;
      addActionListener(this);
      setBackground(new Color(83, 83, 221));
      setBorderPainted(false);
      this.setFocusable(false);

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      splash.dispose();
    }

  }

  private class ResetButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 1L;

    public ResetButton() {
      super("Reset Logon");

      addActionListener(this);
      setBackground(new Color(70, 70, 70));
      this.setForeground(new Color(242, 242, 242));
      setBorderPainted(false);
      this.setFocusable(false);

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      try {
        Driver.prefs.clear();
      } catch (Exception e) {
        System.out.println("Remove preferences from splash fail");
      }

      s.setIcon(new ImageIcon(Tools.findImage("splash")));
      p.remove(r);
    }

  }

  private static final long serialVersionUID = 1L;
}