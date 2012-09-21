package jpkmn.game.player;

import java.util.List;

import jpkmn.game.base.AIParty;
import jpkmn.game.pokemon.Pokemon;

public class MockPlayer extends Trainer {
  public final OpponentType type;

  public MockPlayer() {
    type = OpponentType.WILD;
  }

  public MockPlayer(int type, String name, int cash, int trainerNumber) {
    this.type = OpponentType.valueOf(type);
    _name = name;
    _cash = cash;
    _id = trainerNumber;

    List<Pokemon> pokemon = AIParty.getParty(_id);

    for (Pokemon p : pokemon)
      party.add(p);
  }
}