package jpkmn.map;

import jpkmn.game.player.Player;

public class AreaConnection {
  public AreaConnection(int nextAreaID, Requirement r) {
    _req = r;
    _nextID = nextAreaID;
  }

  public void use(Player p) {
    if (!test(p)) return;

    p.area(AreaManager.get(_nextID));
  }

  public boolean test(Player p) {
    if (_req == null) return true;
    return _req.test(p);
  }

  public Area next() {
    return AreaManager.get(_nextID);
  }

  private int _nextID;
  private Requirement _req;
}