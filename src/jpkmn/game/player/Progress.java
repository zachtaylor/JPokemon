package jpkmn.game.player;

import java.util.Scanner;

import jpkmn.Constants;
import jpkmn.exceptions.LoadException;

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

  public String save() {
    StringBuilder data = new StringBuilder();

    data.append("PROGRESS: ");

    for (int i = 0; i < Constants.EVENTNUMBER; i++) {
      if (!_events[i])
        continue;

      data.append(i);
      data.append(" ");
    }

    data.append("\n");

    return data.toString();
  }

  public void load(String s) throws LoadException {
    try {
      Scanner scan = new Scanner(s);

      if (!scan.next().equals("PROGRESS:"))
        throw new Exception();

      while (scan.hasNext())
        event(Integer.parseInt(scan.next()) + 1, true);

    } catch (Exception e) {
      throw new LoadException("Progress could not load: " + s);
    }
  }

  private boolean[] _events = new boolean[Constants.EVENTNUMBER];
}