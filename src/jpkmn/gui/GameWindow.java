package jpkmn.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import jpkmn.*;
import jpkmn.battle.Battle;
import jpkmn.battle.BattleEndException;
import jpkmn.pokemon.Pokemon;

public class GameWindow extends JFrame {
  public Player player;

  private JPanel root;
  private JPanel main;
  private BattleView bwin;
  private Console console;

  private static final long serialVersionUID = 1L;

  public GameWindow(Player p) {
    try {
      player = p;
      root = new JPanel();
      main = new JPanel();

      if (Driver.console) {
        console = new Console(player);
        JPanel blah = new JPanel();
        blah.setLayout(new FlowLayout());
        add(blah);
        blah.add(root);
        blah.add(console);
      }
      else {
        add(root);
      }

      construct();

      showMain();
      setVisible(true);

      Tools.createMessageWindow();
      setLocationRelativeTo(null);
    } catch (Exception e) {
      e.printStackTrace();
      dispose();
    }
  }

  private void construct() {
    Tools.game = this;
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setIconImage(Tools.findImage("main-icon"));
    setResizable(false);
    root.setLayout(new FlowLayout());
    main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
    main.add(new BattleButton());
    main.add(new SaveButton());
    main.add(new QuitButton());
  }

  public void showMain() {
    setTitle("Main Menu");
    this.setSize(Driver.console ? 350 : 200, 150);
    root.removeAll();
    root.add(main);
  }

  public void dispose() {
    Tools.messages.dispose();
    super.dispose();
  }

  public void showBattle(Battle b) {
    setTitle("Battle!");
    setSize(620, Driver.console ? 210 : 190);
    root.removeAll();

    if (bwin == null)
      bwin = new BattleView(this, b);
    else
      bwin.load(b);

    root.add(bwin);
  }

  private class BattleButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 1L;

    public BattleButton() {
      super("Fight something");
      addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

      int num = Integer.parseInt(Tools.askForInput("Number", "Which Number?",
          "\\d{1,3}"));
      int lvl = Integer.parseInt(Tools.askForInput("Level", "What level?",
          "\\d{1,3}"));
      Pokemon e = new Pokemon(num, lvl);

      try {
        Battle b = new Battle(player, e);
        showBattle(b);
      } catch (BattleEndException bee) {
        Driver.log(BattleView.class, "Battle ended code=" + bee.getExitStatus()
            + ". " + bee.getExitDescription());
      }
    }
  }

  private class SaveButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 1L;

    public SaveButton() {
      super("Save game");
      addActionListener(this);
    }

    public void actionPerformed(ActionEvent arg0) {
      JFileChooser fc = new JFileChooser(Driver.prefs.get("save_location", ""));
      fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fc.addChoosableFileFilter(new FileNameExtensionFilter("JPokemon Files", "jpkmn"));
      
      if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        File f = fc.getSelectedFile();

        try {
          // Append .jpkmn if it isn't already
          if (!f.getName().endsWith(".jpkmn")) {
            f = new File(f.getAbsolutePath()+".jpkmn");
          }

          // Store the default save location
          Driver.prefs.put("save_location", f.getAbsolutePath());
          Driver.prefs.flush();

          // Load game
          PrintWriter pw = new PrintWriter(f);

          player.toFile(pw);

          pw.close();

        } catch (Exception e) {
          e.printStackTrace();
          jpkmn.gui.Tools.notify((Image) null, "ERROR", "General Error");
        }
        
      }
    }
  }

  private class QuitButton extends JButton implements ActionListener {
    private static final long serialVersionUID = 1L;

    public QuitButton() {
      super("Quit Game");
      addActionListener(this);
    }

    public void actionPerformed(ActionEvent arg0) {
      dispose();
    }
  }

}
