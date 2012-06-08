package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.List;

public class Shield {
  private int a;

  public Shield(Field.Effect type, int duration, double reduction) {
    _type = type;
    _duration = duration;
    _reduction = reduction;
    _exceptions = new ArrayList<String>();
  }

  public void addException(String... s) {
    for (String exception : s)
      _exceptions.add(exception);
  }

  public void reduceDamage(Turn t) {
    // TODO
  }

  public boolean reduceDuration() {
    return _duration-- > 0;
  }

  private int _duration;
  private double _reduction;
  private Field.Effect _type;
  private List<String> _exceptions;
}
