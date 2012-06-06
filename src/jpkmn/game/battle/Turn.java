package jpkmn.game.battle;

import jpkmn.game.item.Item;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.Condition;
import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.move.MoveEffect;

public class Turn {
  public Turn(Move m, Slot target) {
    _move = m;
    _target = target;
    _mode = Mode.ATTACK;
  }

  public Turn(Item i, Slot target) {
    _mode = Mode.ITEM;
  }

  public void setAbsoluteDamage(int d) {
    _strength = d;
    _absolute = true;
  }

  public int damage() {
    return _strength;
  }

  public void nullify(String reason) {
    // TODO
  }

  public void execute() {
    _strength = Battle.computeDamage(_move, _target.getLeader());

    if (_absolute)
      _target.takeDamageAbsolute(_strength);
    else
      _target.takeDamage(this);

    applyMoveEffects();

    sendNotifications();
  }

  private void sendNotifications() {
    // TODO
  }

  private void applyMoveEffects() {
    Pokemon leader = _move.pkmn;
    Pokemon enemy = _target.getLeader();

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
    SWAP, ITEM, RUN, ATTACK;
  }

  private Move _move;
  private Mode _mode;
  private Slot _target;
  private int _strength;
  private boolean _absolute;
}
