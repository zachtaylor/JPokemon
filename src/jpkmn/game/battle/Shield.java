package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.pokemon.Type;
import jpkmn.game.pokemon.move.MoveStyle;

public class Shield {
  private int a;

  public Shield(MoveStyle style, int duration) {
    _style = style;
    _duration = duration;
    _exceptions = new ArrayList<String>();
  }

  public void addException(String... s) {
    for (String exception : s)
      _exceptions.add(exception);
  }

  public int reduceDamage(int d, Type t) {
    // TODO
    return d;
  }

  public boolean reduceDuration() {
    return _duration-- > 0;
  }

  private MoveStyle _style;
  private int _duration;
  private List<String> _exceptions;
}
