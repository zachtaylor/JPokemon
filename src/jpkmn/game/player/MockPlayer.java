package jpkmn.game.player;

import jpkmn.exceptions.LoadException;
import jpkmn.game.base.AIInfo;
import jpkmn.game.base.AIParty;
import jpkmn.game.pokemon.Pokemon;

public class MockPlayer extends Trainer {
  public MockPlayer() {
    _type = OpponentType.WILD;
  }

  public MockPlayer(int ai_number) throws LoadException {
    AIInfo info = AIInfo.get(ai_number);

    _id = ai_number;
    _name = info.getName();
    _cash = info.getCash();
    _type = OpponentType.valueOf(info.getType());

    for (AIParty entry : AIParty.get(_id))
      party.add(Pokemon.load(entry.getEntry()));
  }

  public OpponentType type() {
    return _type;
  }

  private OpponentType _type;
}