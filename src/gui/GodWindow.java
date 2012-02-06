package gui;

import item.Bag;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import jpkmn.Driver;
import jpkmn.Player;
import pokemon.Pokemon;

public class GodWindow extends JFrame {
  private static final long serialVersionUID = 1L;
// TODO
  // Heal All button, All Items, Level Leader X
  private Player player;

  public GodWindow(Player p) {
    player = p;

    construct();
    setVisible(true);
  }

  private void construct() {
    setTitle("");
    setIconImage(Tools.getImage("main-icon"));
    setSize(150, 110);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new FlowLayout());
    add(new HealButton());
    add(new ItemButton());
    add(new LevelButton());
    add(new WinButton());
  }

  private class HealButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 1L;

    public HealButton() {
      super("Heal");
      addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      Driver.logGodEvent("Heal All Pokemon");
      for (Pokemon p : player.party.pkmn) {
        if (p != null) p.healDamage(10000);
      }
    }

  }

  private class ItemButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 1L;

    public ItemButton() {
      super("Item");
      addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      Driver.logGodEvent("Populate Bag");
      Bag bag = player.bag;
      for (int i = 0; i < 4; ++i) {
        bag.potion(i).add(10);
        bag.ball(i).add(10);
      }
      bag.xstat("attack").add(10);
      bag.xstat("sattack").add(10);
      bag.xstat("defense").add(10);
      bag.xstat("sdefense").add(10);
      bag.xstat("speed").add(10);
      bag.stone("fire").add(10);
      bag.stone("water").add(10);
      bag.stone("thunder").add(10);
      bag.stone("moon").add(10);
      bag.stone("leaf").add(10);
      bag.cash += 1000;
    }

  }

  private class LevelButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 1L;

    public LevelButton() {
      super("Level");
      addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        int x = Integer.parseInt(JOptionPane.showInputDialog(null,
            "How many levels to advance : ", "God Ability : Level Leader",
            JOptionPane.QUESTION_MESSAGE));
        Driver.logGodEvent(player.party.leader().name + " advanced " + x
            + " levels.");
        while (x > 0) {
          player.party.leader().gainExperience(player.party.leader().xpNeeded());
          --x;
        }
      } catch (NumberFormatException f) {
        // Do nothing
      }
    }

  }

  private class WinButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 1L;

    public WinButton() {
      super("Win");
      addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      boolean old = Driver.prefs.getBoolean("beat", false);
      Driver.prefs.putBoolean("beat", !old);
      try {
        Driver.prefs.flush();
      } catch (Exception e) {
      }
      Driver.logGodEvent("Registry Win toggled. Now : " + (!old));
    }

  }
}
