package jpkmn.map;

import jpkmn.game.base.AIInfo;
import jpkmn.game.player.MockPlayer;
import jpkmn.game.player.OpponentType;
import jpkmn.game.player.Player;

public class TrainerProto {
  public TrainerProto(AIInfo info) {
    _id = info.getNumber();
    _name = info.getName();
    _cash = info.getCash();
    _type = OpponentType.valueOf(info.getType());

    if (info.getRequirement() > -1)
      _req = new Requirement(info.getRequirement(), info.getReqData());
  }

  public int id() {
    return _id;
  }

  public String name() {
    return _name;
  }

  public int cash() {
    return _cash;
  }

  public OpponentType type() {
    return _type;
  }

  public boolean test(Player p) {
    if (_req != null) return _req.test(p);
    return true;
  }

  public MockPlayer make() {
    return new MockPlayer(_type, _name, _cash, _id);
  }

  private String _name;
  private int _id, _cash;
  private Requirement _req;
  private OpponentType _type;
}