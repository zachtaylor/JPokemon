package jpkmn.game.battle.turn;

import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;

import jpkmn.game.battle.Battle;
import jpkmn.game.battle.Slot;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.Condition.Issue;

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

  public void execute() {
    if (needSwap()) {
      executeForcedSwap();
      return;
    }

    Slot targetSlot = _user.battle().get(_user.target());
    Pokemon leader = _user.leader();

    if (!leader.hasIssue(Issue.WAIT)) {
      // 1 Measure if the user can attack
      if (!leader.condition.canAttack())
        nullify(leader.condition.toString());

      // 2 Reduce and measure PP
      else if (!_move.enabled())
        nullify("Move is not enabled!");

      // 3 Measure accuracy
      else if (!_move.use()) {
        if (_move.hurtUserOnMiss()) {
          int d = Battle.computeDamage(leader, _move, targetSlot.leader()) / 8;
          _user.takeDamage(d);
        }

        nullify("It missed.");
        return;
      }
    }

    if (_move.style() == MoveStyle.DELAYNEXT) {
      if (leader.removeIssue(Issue.WAIT))
        nullify("Resting this turn.");
      else
        leader.addIssue(Issue.WAIT);
    }
    else if (_move.style() == MoveStyle.DELAYBEFORE) {
      if (!leader.removeIssue(Issue.WAIT)) {
        nullify("Resting this turn.");
        leader.addIssue(Issue.WAIT);
      }
    }
    else if (_move.style() == MoveStyle.OHKO) {
      int levelDiff = leader.level() - targetSlot.leader().level();

      if (levelDiff < 0)
        nullify("Cannot perform OHKO on " + targetSlot.leader().name());
      else if ((levelDiff + 30.0) / 100.0 <= Math.random())
        nullify("It missed.");
    }
    else if (_move.style() == MoveStyle.MISC) { // Misc
      nullify("This doesn't work yet. Sorry about that!");
    }

    if (!_absolute) {
      _damage = Battle.computeDamage(leader, _move, targetSlot.leader());
      _damage *= targetSlot.damageModifier(_move);
    }

    _messages.add(targetSlot.leader().name() + " took " + _damage + " damage!");
    targetSlot.takeDamage(_damage);
    applyMoveEffects();
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

  private void applyMoveEffects() {
    Pokemon leader = _user.leader();
    Pokemon enemy = _user.battle().get(_user.target()).leader();

    _move.applyEffects(leader, enemy);
  }

  private void nullify(String reason) {
    _messages.add(reason);
  }

  private Move _move;
  private int _damage;
  private boolean _absolute;
}