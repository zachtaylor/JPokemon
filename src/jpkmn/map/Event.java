package jpkmn.map;

import jpkmn.game.base.AIInfo;
import jpkmn.game.base.EventInfo;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.player.MockPlayer;
import jpkmn.game.player.OpponentType;
import jpkmn.game.player.Player;
import jpkmn.game.pokemon.Pokemon;

public class Event {
  private enum Type {
    BATTLE, LEGENDARY;

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
      _requirement = new Requirement(info.getRequirement(), info.getReqData());
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
    if (_requirement == null) return true;
    return _requirement.test(p);
  }

  public void trigger(Player p) {
    if (!test(p)) return;

    MockPlayer mock;

    switch (_type) {
    case BATTLE:
      AIInfo info = AIInfo.get(_int1);
      mock = new MockPlayer(OpponentType.valueOf(info.getType()),
          info.getName(), info.getCash(), info.getNumber());
      BattleRegistry.make(p, mock);
      break;
    case LEGENDARY:
      mock = new MockPlayer();
      mock.party.add(new Pokemon(_int1, _int2));
      BattleRegistry.make(p, mock);
      break;
    }
    // TODO
  }

  private Event.Type _type;
  private int _id, _int1, _int2;
  private Requirement _requirement;
}