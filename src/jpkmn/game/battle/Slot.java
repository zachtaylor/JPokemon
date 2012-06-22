package jpkmn.game.battle;

import jpkmn.game.item.Item;
import jpkmn.game.pokemon.Condition.Issue;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.move.MoveStyle;
import jpkmn.game.pokemon.storage.AbstractParty;

public class Slot {
  private int a;

  public Slot(AbstractParty p) {
    _party = p;
    _field = new Field(this);
    _bide = false;
  }

  public Pokemon getLeader() {
    return getParty().get(0);
  }

  public AbstractParty getParty() {
    return _party;
  }

  public Slot getTarget() {
    return _target;
  }

  public boolean chooseMove() {
    Pokemon leader = getLeader();
    int position;

    // Must keep using the same move
    if (leader.condition.contains(Issue.WAIT)) return true;

    if (Math.random() > .5) { // TODO leader.isAskable()) {
      // TODO Ask for position
      position = 0;
      if (position == -1) return false;
    }
    else {
      position = (int) (Math.random() * leader.moves.amount());
    }

    _move = leader.moves.get(position);
    return true;
  }

  public Move getMove() {
    return _move;
  }

  public boolean chooseItem() {
    _item = null; // TODO Ask

    if (_item == null)
      return false;
    else
      return true;
  }

  public Item getItem() {
    return _item;
  }

  public boolean chooseSwapPosition() {
    _index = 0; // TODO Ask the user

    return _index > 0;
  }

  public boolean chooseAttackTarget() {
    // TODO

    return true;
  }

  public boolean chooseItemTarget() {
    if (_item.target == Target.ENEMY)
      // TODO Ask
      _target = null;
    else
      _target = this;
    return true;
  }

  public Turn attack() {
    Pokemon leader = getLeader();
    Turn turn = new Turn(_move, this);

    if (_bide) {
      turn.setAbsoluteDamage(_bidedamage);
      _bide = false;
      _bidedamage = 0;
    }

    // Don't perform any if they didn't choose this move
    if (!leader.condition.contains(Issue.WAIT)) {
      // 1 Measure if the user can attack
      if (!leader.condition.canAttack())
        turn.nullify(leader.condition.toString());

      // 2 Reduce and measure PP
      if (!_move.use()) turn.nullify("There is not enough PP!");

      // 3 Measure accuracy
      if (!_move.hits(_target._party.getLeader())) {

        // Move # 60 (Hi Jump Kick) and Move # 69 (Jump Kick) hurt on miss
        if (_move.number() == 60 || _move.number() == 69) {
          int damage = Battle.computeDamage(_move, _target.getLeader());
          damage /= 8;
          takeDamageAbsolute(damage);
        }

        turn.nullify("It missed.");
      }
    }

    if (_move.style() == MoveStyle.DELAY) {
      if (leader.condition.contains(Issue.WAIT)) {
        leader.condition.remove(Issue.WAIT); // take away 1 wait

        if (leader.condition.contains(Issue.WAIT) // still waiting?
            || _move.style().attackBeforeDelay()) // or already attacked
          turn.nullify("Resting this turn.");
      }
      else {
        for (int i = 0; i < _move.style().delay(); ++i)
          leader.condition.addIssue(Issue.WAIT); // add all the waits

        if (_move.style().attackAfterDelay())
          turn.nullify("Resting this turn");
      }
    }
    else if (_move.style() == MoveStyle.MISC) { // Misc
      turn.nullify("This doesn't work yet. Sorry about that!");
    }

    return turn;
  }

  public Turn item() {
    // TODO
    return null;
  }

  public Turn swap() {
    return new Turn(_index, this);
  }

  public Turn run() {
    return new Turn(this);
  }

  public void takeDamage(Turn turn) {
    _field.effect(turn);
    takeDamageAbsolute(turn.damage());
  }

  public void takeDamageAbsolute(int damage) {
    if (_bide) _bidedamage += damage;

    getLeader().takeDamage(damage);

    _field.rollDownDuration();
  }

  // Slot
  private Field _field;
  private Slot _target;
  private AbstractParty _party;

  // Move
  private Move _move;
  private boolean _bide;
  private int _bidedamage;

  // Item
  private Item _item;

  // Swap
  private int _index;
}
