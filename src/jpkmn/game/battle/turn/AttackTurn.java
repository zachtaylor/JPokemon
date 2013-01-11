package jpkmn.game.battle.turn;

import jpkmn.game.battle.Battle;
import jpkmn.game.battle.slot.Slot;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;

public class AttackTurn extends Turn {
  public AttackTurn(Slot user, Slot target, Move move) {
    super(user, target);
    _move = move;
    _executions = 0;

    addMessage(user.leader().name() + " used " + move.name() + "!");
  }

  @Override
  protected void doExecute() {
    Pokemon leader = slot().leader();

    _executions++;

    if (_executions == 1) {
      if (!leader.canAttack()) {
        addMessage(leader.condition());
        return;
      }
      else if (!_move.enabled()) {
        addMessage("Move is not enabled!");
        return;
      }
      else if (!_move.use()) {
        addMessage("It missed.");

        if (_move.hurtUserOnMiss()) {
          int d = Battle.computeDamage(leader, _move, target().leader()) / 8;
          slot().takeDamage(d);
          addMessage(leader.name() + " took " + d + " recoil damage!");
        }

        return;
      }
    }

    if (_move.style() == MoveStyle.DELAYNEXT && _executions != 1) {
      addMessage("Resting this turn");
      return;
    }
    if (_move.style() == MoveStyle.DELAYBEFORE && _executions != _move.turns()) {
      addMessage("Resting this turn");
      return;
    }
    if (_move.style() == MoveStyle.OHKO) {
      int levelDiff = leader.level() - target().leader().level();

      if (levelDiff < 0 || (levelDiff + 30.0) / 100.0 <= Math.random()) {
        addMessage("It missed.");
        return;
      }
    }
    if (_move.style() == MoveStyle.MISC) { // TODO MoveStyle.MISC execution
      addMessage("This doesn't work yet. Sorry about that!");
      return;
    }

    if (_move.doesDamage()) {
      calculateDamage();
      addMessage(target().leader().name() + " took " + _damage + " damage!");
      target().takeDamage(_damage);
    }

    _move.applyEffects(slot(), target(), _damage);
  }

  @Override
  public boolean reAdd() {
    return _executions < _move.turns();
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

  private void calculateDamage() {
    if (_move.damageIsAbsolute()) {
      _damage = _move.power();
    }
    else {
      _damage = Battle.computeDamage(slot().leader(), _move, target().leader());
      _damage *= target().damageModifier(_move);
    }

    _damage = Math.min(Math.max(_damage, 1), target().leader().health());
  }

  private Move _move;
  private int _damage, _executions;
}