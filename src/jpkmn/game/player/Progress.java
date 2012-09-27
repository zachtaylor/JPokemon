package jpkmn.game.player;

import jpkmn.Constants;

/**
 * A representation of the progress a Player has made
 */
public class Progress {
  public void event(int id, boolean b) {
    _events[id - 1] = b;
  }

  public boolean event(int id) {
    return _events[id - 1];
  }

  private boolean[] _events = new boolean[Constants.EVENTNUMBER];
}