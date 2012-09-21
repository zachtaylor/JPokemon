package jpkmn.map;

import jpkmn.game.player.Player;

/**
 * Representation of one Area connecting to another
 * 
 * @author zach
 */
public class AreaConnection {
  public AreaConnection(int nextAreaID, Requirement r) {
    _req = r;
    _nextID = nextAreaID;
  }

  /**
   * Tests the Player against any requirements for this AreaConnection, then
   * moves the Player to the new Area
   * 
   * @param p Player to move
   */
  public void use(Player p) {
    if (!test(p)) return;

    p.area(_nextID);
  }

  /**
   * Reports whether a Player can use this AreaConnection
   * 
   * @param p Player to test
   * @return Whether the player can use this AreaConnection
   */
  public boolean test(Player p) {
    if (_req == null) return true;
    return _req.test(p);
  }

  /**
   * @return
   */
  public Area next() {
    return AreaRegistry.get(_nextID);
  }

  private int _nextID;
  private Requirement _req;
}