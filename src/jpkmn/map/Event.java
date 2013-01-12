package jpkmn.map;

import jpkmn.exceptions.LoadException;
import jpkmn.game.base.AIInfo;
import jpkmn.game.base.EventInfo;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.item.ItemInfo;

import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.Trainer;

public class Event {
  private enum Type {
    BATTLE, ITEM;

    public static Type valueOf(int t) {
      return Type.values()[t];
    }
  }

  public Event(EventInfo info) {
    _id = info.getNumber();
    _int1 = info.getData1();
    _int2 = info.getData2();
    _type = Event.Type.valueOf(info.getType());

    if (info.getRequirement() > -1)
      _requirement = new Requirement(info.getRequirement(), info.getRequirement_data());
  }

  public int id() {
    return _id;
  }

  public String description() {
    switch (_type) {
    case BATTLE:
      return "Challenge " + AIInfo.get(_int1).getName();
    case ITEM:
      ItemInfo info = ItemInfo.getInfo(_int1);
      if (_int2 < 0)
        return "Give away " + (-_int2) + " " + info.getName();
      else
        return "Receive " + _int2 + " " + info.getName();
    }
    return null;
  }

  public boolean test(Player p) {
    if (p.events().get(_id))
      return false;

    switch (_type) {
    case ITEM:
      if (_int2 < 0 && p.item(_int1).amount() < -_int2)
        return false;
    break;
    default:
    break;
    }

    if (_requirement == null)
      return true;

    return _requirement.test(p);
  }

  public void trigger(Player p) throws LoadException {
    if (!test(p))
      return;

    Trainer mock;

    switch (_type) {
    case BATTLE:
      mock = new Trainer(_int1);

      BattleRegistry.start(p, mock);
      p.setState("battle");
    break;
    case ITEM:
      p.item(_int1).add(_int2);
      p.events().put(_id);
    break;
    }
  }

  private Event.Type _type;
  private int _id, _int1, _int2;
  private Requirement _requirement;
}