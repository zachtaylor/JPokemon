package jpkmn.exe.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jpkmn.exe.gui.battle.BattleView;

public class GameWindow extends JFrame {
  public GameWindow() {
    _main = new JPanel();
    _battle = new BattleView();

    _main.add(new JLabel("Main Menu!"));
  }

  public void showMain() {
    _battle.disable();
    remove(_battle);

    add(_main);

    setMinimumSize(new Dimension(200, 100));
    refresh();
  }

  public void showBattle(int battleID, int slotID) {
    remove(_main);

    _battle.setup(battleID, slotID);
    add(_battle);

    setMinimumSize(new Dimension(625, 200));
    refresh();
  }

  public void refresh() {
    _battle.refresh();

    setVisible(false);
    setVisible(true);
  }

  private JPanel _main;
  private BattleView _battle;
  private static final long serialVersionUID = 1L;
}