package org.jpokemon.battle.turn;

import org.jpokemon.battle.Battle;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.pokemon.ConditionEffect;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;

public class AttackTurn extends Turn {
  public AttackTurn(Battle b, Slot user, Slot target, Move move) {
    super(b, user, target);
    _move = move;
    _executions = 0;
  }

  @Override
  public void execute() {
    Pokemon leader = slot().leader();

    _executions++;

    if (_executions == 1) {
      for (ConditionEffect conditionEffect : leader.getConditionEffects()) {
        if (conditionEffect.blocksAttack()) {
          battle().log(leader.name() + conditionEffect.getPersistanceMessage());
          return;
        }
      }

      if (!_move.enabled()) {
        battle().log(leader.name() + " cannot use " + _move.name() + ". It's not enabled");
        return;
      }
      else if (!_move.use()) {
        battle().log(leader.name() + " tried to use " + _move.name() + ", but it missed");

        if (_move.hurtUserOnMiss()) {
          int d = Battle.computeDamage(leader, _move, target().leader()) / 8;
          slot().takeDamage(d);
          battle().log(leader.name() + " took " + d + " recoil damage");
        }

        return;
      }
    }

    battle().log(slot().trainer().getName() + "'s " + slot().leader().name() + " used " + _move.name());

    if (_move.style() == MoveStyle.DELAYNEXT && _executions != 1) {
      battle().log(slot().trainer().getName() + "'s " + slot().leader().name() + " is resting this turn");
      return;
    }
    if (_move.style() == MoveStyle.DELAYBEFORE && _executions != _move.turns()) {
      battle().log(slot().trainer().getName() + "'s " + slot().leader().name() + " is resting this turn");
      return;
    }
    if (_move.style() == MoveStyle.OHKO) {
      int levelDiff = leader.level() - target().leader().level();

      if (levelDiff < 0 || (levelDiff + 30.0) / 100.0 <= Math.random()) {
        battle().log(leader.name() + " tried to use " + _move.name() + ", but it missed");
        return;
      }
    }
    if (_move.style() == MoveStyle.MISC) { // TODO MoveStyle.MISC execution
      battle().log(_move.name() + " doesn't work yet. Sorry about that!");
      return;
    }

    if (_move.doesDamage()) {
      calculateDamage();
      battle().log(target().leader().name() + " took " + _damage + " damage");
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