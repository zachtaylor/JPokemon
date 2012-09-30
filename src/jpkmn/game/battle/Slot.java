package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpkmn.exceptions.CancelException;
import jpkmn.game.item.Item;
import jpkmn.game.pokemon.Condition.Issue;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.move.MoveStyle;
import jpkmn.game.pokemon.storage.Party;

public class Slot {
  public Slot(int id, SlotType t, Party p) {
    _id = id;
    _type = t;
    _party = p;
    _bide = false;

    _field = new Field(this);
    _rivals = new HashMap<Pokemon, List<Pokemon>>();
  }

  public int id() {
    return _id;
  }

  public SlotType type() {
    return _type;
  }

  public Pokemon leader() {
    return _party.get(0);
  }

  public Party party() {
    return _party;
  }

  public Slot target() {
    return _target;
  }

  public boolean chooseMove() {
    if (leader().condition.contains(Issue.WAIT))
      return true;

    try {
      _index = _party.owner().screen.getMoveIndex("attack", leader());

      if (_index != -1)
        return true;
    } catch (CancelException c) {
    }

    _party.owner().screen.refresh();
    return false;
  }

  public Move getMove() {
    return leader().moves.get(_index);
  }

  public boolean chooseItem() {
    try {
      _item = _party.owner().screen.getItemChoice("item");

      if (_item != null)
        return true;
    } catch (CancelException c) {
    }

    _party.owner().screen.refresh();
    return false;
  }

  public Item getItem() {
    return _item;
  }

  public boolean chooseSwapPosition() {
    try {
      _index = _party.owner().screen.getPartyIndex("swap");

      return true;
    } catch (CancelException c) {
    }

    _party.owner().screen.refresh();
    return false;
  }

  public boolean chooseAttackTarget(List<Slot> enemySlots) {
    try {
      _target = _party.owner().screen.getTargetSlot(enemySlots);
      return true;
    } catch (CancelException c) {
    }

    _party.owner().screen.refresh();
    return false;
  }

  public boolean chooseItemTarget(List<Slot> enemySlots) {
    try {
      if (_item.target() == Target.SELF) {
        _target = this;
        _index = _party.owner().screen.getPartyIndex("item");
        return true;
      }
      else {
        _target = _party.owner().screen.getTargetSlot(enemySlots);
        _index = -1;
        return true;
      }
    } catch (CancelException c) {
    }

    _party.owner().screen.refresh();
    return false;
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
      if (!move.enabled())
        turn.nullify("Move is not enabled!");
      else
        move.pp(move.pp() - 1);

      // 3 Measure accuracy
      if (!move.hits(_target._party.get(0))) {

        // Move # 60 (Hi Jump Kick) and Move # 69 (Jump Kick) hurt on miss
        if (move.number() == 60 || move.number() == 69) {
          int damage = Battle.computeDamage(leader, move, _target.leader());
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

        if (move.style().attackAfterDelay())
          turn.nullify("Resting this turn");
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
    if (_bide)
      _bidedamage += damage;

    leader().takeDamage(damage);

    _field.rollDownDuration();
  }

  public int getXPAwarded() {
    double factor = _type.getXPFactor();
    factor *= (Math.random() * .5 + 2);
    return (int) (factor * leader().level());
  }

  public void rival(Pokemon p) {
    if (_rivals.get(leader()) == null)
      _rivals.put(leader(), new ArrayList<Pokemon>());

    List<Pokemon> rivals = _rivals.get(leader());

    if (!rivals.contains(p))
      rivals.add(p);
  }

  public void rival(Slot s) {
    if (s.id() == id())
      return;

    Pokemon dead = s.leader();
    int xp = s.getXPAwarded(), count = 0;
    List<String> message = new ArrayList<String>();
    List<Pokemon> rivals, earners = new ArrayList<Pokemon>();

    message.add(dead.name() + " fained!");

    for (Pokemon cur : _party) {
      rivals = _rivals.get(cur);

      // If cur holding xp share, add to earners

      if (rivals == null)
        continue;
      if (rivals.contains(dead)) {
        count++;
        earners.add(cur);
        rivals.remove(dead);
      }
      if (rivals.isEmpty())
        _rivals.remove(cur);
    }

    xp = (xp / count) > 0 ? (xp / count) : 1;
    for (Pokemon cur : earners) {
      message.add(cur.name() + " received " + xp + " experience!");
      cur.xp(xp);
    }

    _party.owner().screen.notify(message.toArray(new String[message.size()]));
  }

  // Slot
  private int _id;
  private Field _field;
  private Slot _target;
  private Party _party;
  private SlotType _type;
  private Map<Pokemon, List<Pokemon>> _rivals;

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
