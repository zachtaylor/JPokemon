package jpkmn.map;

import jpkmn.game.base.ConnectionInfo;

import org.jpokemon.trainer.Player;

/**
 * Representation of one Area connecting to another
 */
public class AreaConnection {
  public AreaConnection(ConnectionInfo info) {
    _nextID = info.getNext();

    if (info.getRequirement() > -1)
      _req = new Requirement(info.getRequirement(), info.getRequirement_data());
  }

  /**
   * Tests the Player against any requirements for this AreaConnection, then
   * moves the Player to the new Area
   * 
   * @param p Player to move
   */
  public void use(Player p) {
    if (!test(p))
      return;

    p.area(_nextID);
  }

  /**
   * Reports whether a Player can use the AreaConnection
   * 
   * @param p Player to test
   * @return Whether the player can use the AreaConnection
   */
  public boolean test(Player p) {
    if (_req == null)
      return true;
    return _req.test(p);
  }

  /**
   * Gets the next area
   * 
   * @return The area that this connection would transfer the Player to
   */
  public Area next() {
    return AreaRegistry.get(_nextID);
  }

  private int _nextID;
  private Requirement _req;
}