package jpkmn.game.battle.turn;

import jpkmn.game.battle.Battle;
import jpkmn.game.battle.slot.Slot;
import jpkmn.game.pokemon.Condition.Issue;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;

public class AttackTurn extends AbstractTurn {
  public AttackTurn(Slot user, Move move) {
    super(user);
    _move = move;

    _absolute = false;

    _messages.add(user.leader().name() + " used " + move.name() + "!");
  }

  public void absoluteDamage(int d) {
    _damage = d;
    _absolute = true;
  }

  public String[] execute() {
    if (needSwap())
      return executeForcedSwap();

    Slot targetSlot = _user.target();
    Pokemon leader = _user.leader();

    if (!leader.hasIssue(Issue.WAIT)) {
      // 1 Measure if the user can attack
      if (!leader.condition.canAttack())
        return nullify(leader.condition.toString());

      // 2 Reduce and measure PP
      else if (!_move.enabled())
        return nullify("Move is not enabled!");

      // 3 Measure accuracy
      else if (!_move.use()) {
        if (_move.hurtUserOnMiss()) {
          int d = Battle.computeDamage(leader, _move, targetSlot.leader()) / 8;
          _user.takeDamage(d);
        }

        return nullify("It missed.");
      }
    }

    if (_move.style() == MoveStyle.DELAYNEXT) {
      if (leader.removeIssue(Issue.WAIT))
        return nullify("Resting this turn.");
      else
        leader.addIssue(Issue.WAIT);
    }
    else if (_move.style() == MoveStyle.DELAYBEFORE) {
      if (!leader.removeIssue(Issue.WAIT)) {
        leader.addIssue(Issue.WAIT);
        return nullify("Resting this turn.");
      }
    }
    else if (_move.style() == MoveStyle.OHKO) {
      int levelDiff = leader.level() - targetSlot.leader().level();

      if (levelDiff < 0)
        return nullify("Cannot perform OHKO on " + targetSlot.leader().name());
      else if ((levelDiff + 30.0) / 100.0 <= Math.random())
        return nullify("It missed.");
    }
    else if (_move.style() == MoveStyle.MISC) { // Misc
      return nullify("This doesn't work yet. Sorry about that!");
    }

    if (!_absolute) {
      _damage = Battle.computeDamage(leader, _move, targetSlot.leader());
      _damage *= targetSlot.damageModifier(_move);
    }

    if (_move.style() != MoveStyle.STATUS) {
      _messages.add(targetSlot.leader().name() + " took " + _damage
          + " damage!");
      targetSlot.takeDamage(_damage);
    }

    _move.applyEffects(_user, _user.target(), _damage);
    return getNotifications();
  }

  @Override
  public int compareTo(AbstractTurn turn) {
    if (turn.needSwap()) {
      if (needSwap())
        return 0;
      return 1;
    }

    Pokemon p1 = _user.leader(), p2 = turn._user.leader();

    if (turn instanceof AttackTurn)
      return p2.speed() - p1.speed();
    return 1;
  }

  private String[] nullify(String reason) {
    _messages.add(reason);

    return getNotifications();
  }

  private Move _move;
  private int _damage;
  private boolean _absolute;
}