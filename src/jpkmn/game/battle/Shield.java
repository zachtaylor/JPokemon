package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;

public class Shield {
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

  public double damageModifier(Move move) {
    String name = move.name();
    MoveStyle style = move.style();
    if (_exceptions.contains(name)) {
      return 2.0;
    }
    else if (_type == Field.Effect.INVULNERABLE) {
      return 0;
    }
    else if (_type == Field.Effect.PHYSSHIELD && style == MoveStyle.PHYSICAL) {
      return _reduction;
    }
    else if (_type == Field.Effect.SPECSHIELD && style == MoveStyle.SPECIAL) {
      return _reduction;
    }
    return 1.0;
  }

  public boolean reduceDuration() {
    return _duration-- > 0;
  }

  private int _duration;
  private double _reduction;
  private Field.Effect _type;
  private List<String> _exceptions;
}
