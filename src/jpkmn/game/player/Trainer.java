package jpkmn.game.player;

import java.util.List;

import jpkmn.game.base.AIInfo;
import jpkmn.game.base.AIParty;
import jpkmn.game.pokemon.Pokemon;

public class Trainer extends AbstractPlayer {
  public Trainer(int trainerNumber) {
    _id = trainerNumber;

    List<Pokemon> pokemon = AIParty.getPartyForTrainer(_id);
    AIInfo info = AIInfo.getInfoForTrainer(_id);

    for (Pokemon p : pokemon) {
      party.add(p);
    }

    _name = info.getName();
    _cash = info.getCash();
  }
}