package jpkmn.game.player;

import java.util.List;

import jpkmn.game.base.AIParty;
import jpkmn.game.pokemon.Pokemon;

public class Trainer extends AbstractPlayer {
  public final TrainerType type;

  public Trainer(int type, String name, int cash, int trainerNumber) {
    this.type = TrainerType.valueOf(type);
    _name = name;
    _cash = cash;
    _id = trainerNumber;

    List<Pokemon> pokemon = AIParty.getParty(_id);

    for (Pokemon p : pokemon)
      party.add(p);
  }
}