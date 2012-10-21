package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jpkmn.exceptions.CancelException;
import jpkmn.game.item.Item;
import jpkmn.game.item.ItemType;
import jpkmn.game.player.Player;
import jpkmn.game.pokemon.Condition;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.move.MoveEffect;

public class Turn {
  public Turn(Slot user, Battle b) {
    _int1 = 100;
    _battle = b;
    _user = user;
    _mode = Mode.RUN;

    for (Slot s : _battle) {
      if (_user.leader().level() < s.leader().level())
        _int1 -= 10 * (s.leader().level() - _user.leader().level());
      else
        _int1 += 7 * (_user.leader().level() - s.leader().level());
    }

    _messages = new ArrayList<String>();
    _messages.add(_user.party().owner().name() + " tried to run!");
  }

  public Turn(Slot user, int moveIndex) {
    _user = user;
    _int1 = moveIndex;
    _mode = Mode.ATTACK;
    _messages = new ArrayList<String>();

    _messages.add(_user.leader().name() + " used "
        + _user.leader().moves.get(_int1).name() + "!");
  }

  public Turn(Slot user, int swap1, int swap2) {
    _user = user;
    _int1 = swap1;
    _int2 = swap2;
    _mode = Mode.SWAP;

    _messages = new ArrayList<String>();
    _messages.add("Come back, " + user.party().get(_int1) + "!");
  }

  public Turn(Slot user, Item item, int index) {
    _user = user;
    _item = item;
    _int1 = index;
    _mode = Mode.ITEM;

    _messages = new ArrayList<String>();
    _messages.add(_user.party().owner().name() + " used " + item.name());
  }

  public int damage() {
    return _int2;
  }

  public void damage(int d) {
    _int2 = d;
  }

  public Slot getUserSlot() {
    return _user;
  }

  public void damageAbsolute(int d) {
    _int2 = d;
    _absolute = true;
  }

  public void nullify(String reason) {
    _mode = Mode.NULL;
    _messages.add(reason);
  }

  public void changeToSwap() {
    _int1 = _int2 = 0;
    _mode = Mode.SWAP;

    _messages.clear();
    _messages.add(_user.leader().name() + " has fainted!");

    while (!_user.party().get(_int2).condition.awake()) {
      try {
        _int2 = _user.party().owner().screen.getPartyIndex("swap");
      } catch (CancelException c) {
        _int2 = 0;
      }
    }
  }

  public void destroy() {
    _mode = Mode.NULL;
    _messages = null;
  }

  public void execute() {
    if (_mode == Mode.ATTACK) {
      Slot enemy = _user.target();
      Move move = _user.leader().moves.get(_int1);
      _int2 = Battle.computeDamage(_user.leader(), move, enemy.leader());

      if (_absolute)
        enemy.takeDamageAbsolute(_int2);
      else
        enemy.takeDamage(this);

      applyMoveEffects();
    }
    else if (_mode == Mode.SWAP) {
      _user.party().swap(_int1, _int2);
    }
    else if (_mode == Mode.ITEM) {
      Pokemon target;

      if (_item.target() == Target.SELF) {
        target = _user.party().get(_int1);

        if (_item.type() == ItemType.MACHINE)
          _messages.add("Machines aren't allowed in battle!");
        else if (_item.type() == ItemType.STONE)
          _messages.add("Stones aren't allowed in battle!");
        else
          _item.effect(target);
      }
      else {
        target = _user.target().leader();

        if (_item.type() == ItemType.BALL) {
          if (_user.target().type() != SlotType.WILD) {
            _messages.add("Cannot use a ball against " + target.name() + "!");
          }
          else if (_item.effect(target)) {
            if (!_user.party().add(target))
              ((Player) (_user.party().owner())).box.add(target);
            _user.target().party().remove(target);

            _messages.add(target.name() + " was caught!");
          }
          else
            _messages.add(target.name() + "broke free!");
        }
      }
    }
    else if (_mode == Mode.RUN) {
      if ((_int1 / 250.0) > Math.random()) {
        _battle.remove(_user.id());
        _battle = null; // careful
        _messages.add("Got away successfully!");
      }
      else
        _messages.add("Didn't get away!");
    }
  }

  public String[] getNotifications() {
    Pokemon p;

    if (_mode == Mode.ATTACK) {
      p = _user.target().leader();
      _messages.add(p.name() + " took " + _int2 + " damage!");
    }
    else if (_mode == Mode.ITEM) {

    }
    else if (_mode == Mode.SWAP) {
      p = _user.leader();
      _messages.add(p.owner().name() + " sent out " + p.name());
    }

    return _messages.toArray(new String[_messages.size()]);
  }

  public static class TurnComparator implements Comparator<Turn> {
    public int compare(Turn o1, Turn o2) {
      if (o1._mode.ordinal() < o2._mode.ordinal())
        return -1;
      else if (o1._mode == o2._mode) {
        if (o1._mode == Mode.ATTACK) {
          Pokemon p1 = o1._user.leader(), p2 = o2._user.leader();

          return p2.speed().cur() - p1.speed().cur();
        }
        else {
          return 0;
        }
      }
      else
        return 1;
    }
  }

  private void applyMoveEffects() {
    Pokemon leader = _user.leader(), enemy = _user.target().leader();
    Move move = leader.moves.get(_int1);

    for (MoveEffect me : move.moveEffects()) {
      // Move # 73 (Leech Seed) fix cause it targets both user and enemy
      if (me.type() == MoveEffect.Type.LEECH) {
        enemy.addIssue(Condition.Issue.SEEDVIC);
        leader.addIssue(Condition.Issue.SEEDUSR);
      }
      else if (me.target() == Target.SELF)
        me.effect(leader);
      else
        me.effect(enemy);
    }
  }

  private enum Mode {
    RUN, SWAP, ITEM, ATTACK, NULL;
  }

  private Mode _mode;
  private Item _item;
  private Slot _user;
  private Battle _battle;
  private boolean _absolute;
  private List<String> _messages;

  // Attack: Index of move, power of move
  // Item: index in party for user, not used
  // Swap: index in party 1, index in party 2
  // Run: Odds of being able to run away, not used
  private int _int1, _int2;
}