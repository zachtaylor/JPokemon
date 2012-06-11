package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.move.MoveStyle;

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
    Move move = t.getUserSlot().getMove();

    if (_exceptions.contains(move.name())) {
      // That move is an exception to this shield
      t.setDamage(t.damage() * 2);
    }
    else if (_type == Field.Effect.INVULNERABLE) {
      t.nullify("It didn't work");
    }
    else if (_type == Field.Effect.PHYSSHIELD
        && move.style() == MoveStyle.PHYSICAL) {
      // TODO
    }
    else if (_type == Field.Effect.SPECSHIELD
        && move.style() == MoveStyle.SPECIAL) {
      // TODO
    }
  }

  public boolean reduceDuration() {
    return _duration-- > 0;
  }

  private int _duration;
  private double _reduction;
  private Field.Effect _type;
  private List<String> _exceptions;
}
