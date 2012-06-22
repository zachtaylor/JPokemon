package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jpkmn.game.Player;
import jpkmn.game.item.Ball;
import jpkmn.game.item.Item;
import jpkmn.game.pokemon.Condition;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.move.MoveEffect;

public class Turn {
  private int a;

  public Turn(Slot user) {
    _user = user;
    _mode = Mode.RUN;

    _messages = new ArrayList<String>();
  }

  public Turn(Move m, Slot user) {
    _move = m;
    _user = user;
    _mode = Mode.ATTACK;
    _messages = new ArrayList<String>();

    _messages.add(_user.getLeader().name() + " used " + _move.name() + "!");
  }

  public Turn(int index, Slot user) {
    _user = user;
    _integer = index;
    _mode = Mode.SWAP;
    _messages = new ArrayList<String>();

    _messages.add("Come back, " + user.getLeader().name() + "!");
  }

  public Turn(Item i, int index, Slot user) {
    _item = i;
    _user = user;
    _mode = Mode.ITEM;
    _messages = new ArrayList<String>();

  }

  public int damage() {
    return _integer;
  }

  public void setDamage(int d) {
    _integer = d;
  }

  public Slot getUserSlot() {
    return _user;
  }

  public void setAbsoluteDamage(int d) {
    _integer = d;
    _absolute = true;
  }

  public void nullify(String reason) {
    _mode = Mode.NULL;
    _messages.add(reason);
  }

  public void changeToSwap() {
    _integer = 0;
    _mode = Mode.SWAP;

    while (!_user.getParty().get(_integer).condition.getAwake()) {
      // TODO Ask the user for position
    }
  }

  public void destroy() {
    _mode = Mode.NULL;
    _messages = null;
  }

  public void execute() {
    if (_mode == Mode.ATTACK) {
      Slot enemy = _user.getTarget();

      _integer = Battle.computeDamage(_move, enemy.getLeader());

      if (_absolute)
        enemy.takeDamageAbsolute(_integer);
      else
        enemy.takeDamage(this);

      applyMoveEffects();
    }
    else if (_mode == Mode.SWAP) {
      _user.getParty().swap(0, _integer);
    }
    else if (_mode == Mode.ITEM) {
      Pokemon target;
      
      if (_item.target == Target.SELF) {
        target = _user.getParty().get(_integer);
      }
      else {
        target = _user.getTarget().getLeader();
      }
      
      if (_item.effect(target)) {
        if (_item instanceof Ball) {
          if (!_user.getParty().add(target))
            ((Player) (_user.getLeader().getOwner())).box.add(target);
          _user.getTarget().getParty().remove(target);
        }
        else {

        }
      }
    }
    else if (_mode == Mode.RUN) {

    }
  }

  public String[] getNotifications() {
    Pokemon target = _user.getTarget().getLeader();

    if (_mode == Mode.ATTACK) {
      _messages.add(target.name() + " took " + _integer + " damage!");
    }
    else if (_mode == Mode.ITEM) {

    }
    else if (_mode == Mode.SWAP) {

    }
    else if (_mode == Mode.RUN) {

    }

    return (String[]) _messages.toArray();
  }

  private void applyMoveEffects() {
    Pokemon leader = _move.pkmn;
    Pokemon enemy = _user.getLeader();

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

  public static class TurnComparator implements Comparator<Turn> {
    public int compare(Turn o1, Turn o2) {
      if (o1._mode.ordinal() < o2._mode.ordinal())
        return -1;
      else if (o1._mode == o2._mode) {
        if (o1._mode == Mode.ATTACK) {
          return o2._move.pkmn.stats.spd.cur() - o1._move.pkmn.stats.spd.cur();
        }
        else {
          return 0;
        }
      }
      else
        return 1;
    }
  }

  private enum Mode {
    RUN, SWAP, ITEM, ATTACK, NULL;
  }

  private Mode _mode;
  private Move _move;
  private Item _item;
  private Slot _user;
  private boolean _absolute;
  private List<String> _messages;

  // Used as: swap index, item on index in user party, calculated move power
  private int _integer;
}
