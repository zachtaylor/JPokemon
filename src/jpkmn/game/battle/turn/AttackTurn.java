package jpkmn.game.battle.turn;

import jpkmn.game.battle.Battle;
import jpkmn.game.battle.slot.Slot;
import jpkmn.game.pokemon.ConditionEffect;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;

public class AttackTurn extends Turn {
  public AttackTurn(Slot user, Move move) {
    super(user);
    _move = move;
    _absolute = false;

    addMessage(user.leader().name() + " used " + move.name() + "!");
  }

  public void absoluteDamage(int d) {
    _damage = d;
    _absolute = true;
  }

  protected void doExecute() {
    Slot targetSlot = slot().target();
    Pokemon leader = slot().leader();

    if (!leader.hasIssue(ConditionEffect.WAIT)) {
      if (!leader.condition.canAttack()) {
        addMessage(leader.condition.toString());
        return;
      }
      else if (!_move.enabled()) {
        addMessage("Move is not enabled!");
        return;
      }
      else if (!_move.use()) {
        addMessage("It missed.");

        if (_move.hurtUserOnMiss()) {
          int d = Battle.computeDamage(leader, _move, targetSlot.leader()) / 8;
          slot().takeDamage(d);
          addMessage(leader.name() + " took " + d + " recoil damage!");
        }

        return;
      }
    }

    if (_move.style() == MoveStyle.DELAYNEXT) {
      if (leader.removeIssue(ConditionEffect.WAIT)) {
        addMessage("Resting this turn");
        return;
      }
      else
        leader.addIssue(ConditionEffect.WAIT);
    }
    else if (_move.style() == MoveStyle.DELAYBEFORE) {
      if (!leader.removeIssue(ConditionEffect.WAIT)) {
        leader.addIssue(ConditionEffect.WAIT);
        addMessage("Resting this turn");
        return;
      }
    }
    else if (_move.style() == MoveStyle.OHKO) {
      int levelDiff = leader.level() - targetSlot.leader().level();

      if (levelDiff < 0 || (levelDiff + 30.0) / 100.0 <= Math.random()) {
        addMessage("It missed.");
        return;
      }
    }
    else if (_move.style() == MoveStyle.MISC) { // Misc
      addMessage("This doesn't work yet. Sorry about that!");
      return;
    }

    if (!_absolute) {
      _damage = Battle.computeDamage(leader, _move, targetSlot.leader());
      _damage *= targetSlot.damageModifier(_move);
    }

    if (_move.style() != MoveStyle.STATUS) {
      addMessage(targetSlot.leader().name() + " took " + _damage + " damage!");
      targetSlot.takeDamage(_damage);
    }

    _move.applyEffects(slot(), slot().target(), _damage);
  }

  @Override
  public int compareTo(Turn t) {
    if (t._needSwap) {
      if (_needSwap)
        return 0;

      return 1;
    }

    if (t instanceof AttackTurn)
      return t.slot().leader().speed() - slot().leader().speed();

    return 1;
  }

  private Move _move;
  private int _damage;
  private boolean _absolute;
}