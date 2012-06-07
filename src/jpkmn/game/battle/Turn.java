package jpkmn.game.battle;

import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.Condition;
import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.move.MoveEffect;

public class Turn {
  private int a; 
  
  public Turn(Move m, Slot target) {
    _move = m;
    _slot = target;
    _mode = Mode.ATTACK;
  }

  public int damage() {
    return _strength;
  }

  public void setAbsoluteDamage(int d) {
    _strength = d;
    _absolute = true;
  }

  public void nullify(String reason) {
    // TODO
  }

  public void execute() {
    if (_mode == Mode.ATTACK) {
      _strength = Battle.computeDamage(_move, _slot.getLeader());

      if (_absolute)
        _slot.takeDamageAbsolute(_strength);
      else
        _slot.takeDamage(this);

      applyMoveEffects();
    }

    sendNotifications();
  }

  private void sendNotifications() {
    // TODO
  }

  private void applyMoveEffects() {
    Pokemon leader = _move.pkmn;
    Pokemon enemy = _slot.getLeader();

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

  private enum Mode {
    SWAP, ITEM, ATTACK;
  }

  private Move _move;
  private Slot _slot;
  private int _strength;
  private boolean _absolute;
  
  private Mode _mode;
}
