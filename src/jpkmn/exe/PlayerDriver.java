package jpkmn.exe;

import jpkmn.game.Player;

public class PlayerDriver implements Runnable {
  public PlayerDriver(Player p) {
    _player = p;

    new Thread(this);
  }

  public Player player() {
    return _player;
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
  }

  private Player _player;
}
