package jpkmn.game.player;

import java.util.List;

import jpkmn.game.base.AIParty;
import jpkmn.game.pokemon.Pokemon;

public class MockPlayer extends Trainer {
  public MockPlayer() {
    _type = OpponentType.WILD;
  }

  public MockPlayer(OpponentType type, String name, int cash, int trainerNumber) {
    _type = type;
    _name = name;
    _cash = cash;
    _id = trainerNumber;

    List<Pokemon> pokemon = AIParty.getParty(_id);

    for (Pokemon p : pokemon)
      party.add(p);
  }

  public OpponentType type() {
    return _type;
  }

  private OpponentType _type;
}