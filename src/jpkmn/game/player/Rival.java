package jpkmn.game.player;

import java.util.List;

import jpkmn.game.base.AIInfo;
import jpkmn.game.base.AIParty;
import jpkmn.game.pokemon.Pokemon;

public class Rival extends AbstractPlayer {
  public Rival(int rivalNumber) {
    _id = rivalNumber;

    List<Pokemon> pokemon = AIParty.getPartyForRival(_id);
    AIInfo info = AIInfo.getInfoForRival(_id);

    for (Pokemon p : pokemon) {
      party.add(p);
    }

    _name = info.getName();
    _cash = info.getCash();
  }
}