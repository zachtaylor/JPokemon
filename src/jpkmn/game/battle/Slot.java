package jpkmn.game.battle;

import java.util.List;

import jpkmn.exceptions.CancelException;
import jpkmn.game.item.Item;
import jpkmn.game.pokemon.Condition.Issue;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.move.MoveStyle;
import jpkmn.game.pokemon.storage.AbstractParty;

public class Slot {
  public Slot(AbstractParty p) {
    _party = p;
    _field = new Field(this);
    _bide = false;
  }

  public Pokemon leader() {
    return getParty().get(0);
  }

  public AbstractParty getParty() {
    return _party;
  }

  public Slot getTarget() {
    return _target;
  }

  public boolean chooseMove() {
    if (leader().condition.contains(Issue.WAIT)) return true;

    try {
      _index = leader().owner().screen.getMoveIndex("attack", leader());
      return true;
    } catch (CancelException c) {
      return false;
    }
  }

  public Move getMove() {
    return leader().moves.get(_index);
  }

  public boolean chooseItem() {
    try {
      _item = leader().owner().screen.getItemChoice();
      return true;
    } catch (CancelException c) {
      return false;
    }
  }

  public Item getItem() {
    return _item;
  }

  public boolean chooseSwapPosition() {
    try {
      _index = leader().owner().screen.getPartyIndex("swap");

      return true;
    } catch (CancelException c) {
      return false;
    }
  }

  public boolean chooseAttackTarget(List<Slot> enemySlots) {
    try {
      _target = leader().owner().screen.getTargetSlot(enemySlots);
      return true;
    } catch (CancelException c) {
      return false;
    }
  }

  public boolean chooseItemTarget(List<Slot> enemySlots) {
    try {
      if (_item.target == Target.SELF) {
        _target = this;
        _index = leader().owner().screen.getPartyIndex("item");
        return true;
      }
      else {
        _target = leader().owner().screen.getTargetSlot(enemySlots);
        _index = -1;
        return true;
      }
    } catch (CancelException c) {
      return false;
    }
  }

  public Turn attack() {
    Pokemon leader = leader();
    Move move = getMove();
    Turn turn = new Turn(this, _index);

    if (_bide) {
      turn.damageAbsolute(_bidedamage);
      _bide = false;
      _bidedamage = 0;
    }

    // Don't perform any if they didn't choose this move
    if (!leader.condition.contains(Issue.WAIT)) {
      // 1 Measure if the user can attack
      if (!leader.condition.canAttack())
        turn.nullify(leader.condition.toString());

      // 2 Reduce and measure PP
      if (!move.use()) turn.nullify("There is not enough PP!");

      // 3 Measure accuracy
      if (!move.hits(_target._party.getLeader())) {

        // Move # 60 (Hi Jump Kick) and Move # 69 (Jump Kick) hurt on miss
        if (move.number() == 60 || move.number() == 69) {
          int damage = Battle.computeDamage(move, _target.leader());
          damage /= 8;
          takeDamageAbsolute(damage);
        }

        turn.nullify("It missed.");
      }
    }

    if (move.style() == MoveStyle.DELAY) {
      if (leader.condition.contains(Issue.WAIT)) {
        leader.condition.remove(Issue.WAIT); // take away 1 wait

        if (leader.condition.contains(Issue.WAIT) // still waiting?
            || move.style().attackBeforeDelay()) // or already attacked
          turn.nullify("Resting this turn.");
      }
      else {
        for (int i = 0; i < move.style().delay(); ++i)
          leader.condition.addIssue(Issue.WAIT); // add all the waits

        if (move.style().attackAfterDelay()) turn.nullify("Resting this turn");
      }
    }
    else if (move.style() == MoveStyle.MISC) { // Misc
      turn.nullify("This doesn't work yet. Sorry about that!");
    }

    return turn;
  }

  public Turn item() {
    return new Turn(this, _item, _index);
  }

  public Turn swap() {
    return new Turn(this, 0, _index);
  }

  public Turn run(Battle b) {
    return new Turn(this, b);
  }

  public void takeDamage(Turn turn) {
    _field.effect(turn);
    takeDamageAbsolute(turn.damage());
  }

  public void takeDamageAbsolute(int damage) {
    if (_bide) _bidedamage += damage;

    leader().takeDamage(damage);

    _field.rollDownDuration();
  }

  // Slot
  private Field _field;
  private Slot _target;
  private AbstractParty _party;

  // Move
  private boolean _bide;
  private int _bidedamage;

  // Item
  private Item _item;

  // Attack: Move index
  // Item: index in party for user
  // Swap: index in party
  private int _index;
}
