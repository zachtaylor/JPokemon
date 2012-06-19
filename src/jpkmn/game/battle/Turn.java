package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.Condition;
import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.move.MoveEffect;

public class Turn {
  private int a;

  public Turn(Move m, Slot user) {
    _move = m;
    _user = user;
    _mode = Mode.ATTACK;
    _messages = new ArrayList<String>();
    
    _messages.add(_user.getLeader().name() + " used " + _move.name() + "!");
  }

  public int damage() {
    return _strength;
  }

  public void setDamage(int d) {
    _strength = d;
  }

  public Slot getUserSlot() {
    return _user;
  }

  public void setAbsoluteDamage(int d) {
    _strength = d;
    _absolute = true;
  }

  public void nullify(String reason) {
    _mode = Mode.NULL;
    _messages.add(reason);
  }

  public void changeToSwap() {
    // TODO Depends on Slot's implementation of Swap
  }

  public void destroy() {
    _mode = Mode.NULL;
    _messages = null;
  }

  public void execute() {
    if (_mode == Mode.ATTACK) {
      Slot enemy = _user.getTarget();

      _strength = Battle.computeDamage(_move, enemy.getLeader());

      if (_absolute)
        enemy.takeDamageAbsolute(_strength);
      else
        enemy.takeDamage(this);

      applyMoveEffects();
    }
  }

  public List<String> getNotifications() {
    Pokemon target = _user.getTarget().getLeader();

    if (_mode == Mode.ATTACK) {
      _messages.add(target.name() + " took " + _strength + " damage!");
    }
    else if (_mode == Mode.ITEM) {
      
    }
    else if (_mode == Mode.SWAP) {
      
    }
    else if (_mode == Mode.RUN) {
      
    }

    return _messages;
  }

  private void applyMoveEffects() {
    Pokemon leader = _move.pkmn;
    Pokemon enemy = _user.getLeader();

    for (MoveEffect be : _move.getMoveEffects()) {
      // Move # 73 (Leech Seed) fix cause it targets both user and enemy
      if (be == MoveEffect.LEECH) {
        enemy.condition.addIssue(Condition.Issue.SEEDED);
        leader.condition.addIssue(Condition.Issue.SEEDUSR);
      }
      else if (be.target == Target.SELF)
        be.effect(leader);
      else
        be.effect(enemy);
    }
  }

  public static class TurnComparator implements Comparator<Turn> {
    public int compare(Turn o1, Turn o2) {
      if (o1._mode.ordinal() < o2._mode.ordinal())
        return -1;
      else if (o1._mode == o2._mode) {
        if (o1._mode == Mode.ATTACK) {
          return o2._move.pkmn.stats.spd.cur() - o1._move.pkmn.stats.spd.cur();
        }
        else {
          return 0;
        }
      }
      else
        return 1;
    }
  }

  private enum Mode {
    RUN, SWAP, ITEM, ATTACK, NULL;
  }

  private Move _move;
  private Slot _user;
  private int _strength;
  private boolean _absolute;
  private List<String> _messages;

  private Mode _mode;
}
