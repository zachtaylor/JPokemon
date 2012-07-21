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

    setResizable(false);

    _main.add(new JLabel("Main Menu!"));
  }

  public void showMain() {
    _battle.disable();
    remove(_battle);

    add(_main);

    size(200, 100);
    refresh();
  }

  public void showBattle(int battleID, int slotID) {
    remove(_main);

    _battle.setup(battleID, slotID);
    add(_battle);

    size(625, 200);
    refresh();
  }

  public void refresh() {
    _battle.refresh();

    setVisible(false);
    setVisible(true);
  }

  private void size(int x, int y) {
    setSize(new Dimension(x, y));
  }

  private JPanel _main;
  private BattleView _battle;
  private static final long serialVersionUID = 1L;
}