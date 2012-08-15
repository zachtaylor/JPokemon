package jpkmn.exe.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import jpkmn.exe.gui.battle.BattleView;
import jpkmn.exe.gui.world.WorldView;
import jpkmn.img.ImageFinder;

public class GameWindow extends JFrame {
  public GameWindow(int playerID) {
    _playerID = playerID;
    _inbox = new MessageView();
    _battle = new BattleView();
    _main = new WorldView(this);

    setResizable(false);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setIconImage(ImageFinder.find("main-icon"));

    setVisible(true);
  }

  public int playerID() {
    return _playerID;
  }

  public MessageView inbox() {
    return _inbox;
  }

  public void showMain(int areaID) {
    _battle.disable();
    remove(_battle);

    _main.enable();
    add(_main);

    size(600, 300);
    refresh();
  }

  public void showBattle(int battleID, int slotID) {
    _main.disable();
    remove(_main);

    _battle.setup(battleID, slotID);
    add(_battle);

    size(625, 200);
    refresh();
  }

  public void refresh() {
    _main.refresh();
    _battle.refresh();

    setVisible(false);
    setVisible(true);
  }

  private void size(int x, int y) {
    setSize(new Dimension(x, y));
  }

  private int _playerID;
  private WorldView _main;
  private BattleView _battle;
  private MessageView _inbox;
  private static final long serialVersionUID = 1L;
}