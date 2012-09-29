package jpkmn.map;

import jpkmn.exceptions.LoadException;
import jpkmn.game.base.AIInfo;
import jpkmn.game.base.EventInfo;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.player.MockPlayer;
import jpkmn.game.player.Player;

public class Event {
  private enum Type {
    BATTLE;

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
    }
    return null;
  }

  public boolean test(Player p) {
    if (_requirement == null)
      return true;
    else if (p.progress.event(_id))
      return false;

    return _requirement.test(p);
  }

  public void trigger(Player p) throws LoadException {
    if (!test(p))
      return;

    MockPlayer mock;

    switch (_type) {
    case BATTLE:
      mock = new MockPlayer(_int1);
      BattleRegistry.make(p, mock);
      break;
    }
    // TODO
  }

  private Event.Type _type;
  private int _id, _int1, _int2;
  private Requirement _requirement;
}