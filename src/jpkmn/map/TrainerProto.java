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
    // TODO : check if the player has fought this trainer before
    return true;
  }

  public MockPlayer make() {
    return new MockPlayer(_type, _name, _cash, _id);
  }

  private String _name;
  private int _id, _cash;
  private OpponentType _type;
}