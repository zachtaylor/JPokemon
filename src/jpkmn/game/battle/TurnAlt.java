package jpkmn.game.battle;

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
  }

  public int damage() {
    return _strength;
  }

  public Slot getUserSlot() {
    return _user;
  }

  public void setAbsoluteDamage(int d) {
    _strength = d;
    _absolute = true;
  }

  public void nullify(String reason) {
    // TODO
    _mode = Mode.NULL;
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

  private List<String> getNotifications() {
    // TODO Return notifications.
    // Generate them from execute and nullify
    return null;
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
<<<<<<< HEAD
    RUN, SWAP, ITEM, ATTACK;
=======
    SWAP, ITEM, ATTACK, NULL;
>>>>>>> 16d7dd412f87ae2be13a1e813aed1d349c22105c
  }

  private Move _move;
  private Slot _user;
  private int _strength;
  private boolean _absolute;

  private Mode _mode;
}
