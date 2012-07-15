package jpkmn.map;

import jpkmn.game.Player;

public class AreaConnection {
  public AreaConnection(int nextAreaID) {
    _nextID = nextAreaID;
  }

  public void use(Player p) {
    if (!test(p)) return;

    p.area(AreaManager.get(_nextID));
  }

  public boolean test(Player p) {
    return true;
  }

  private int _nextID;
}