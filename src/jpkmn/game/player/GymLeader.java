package jpkmn.game.player;

import java.util.List;

import jpkmn.game.base.AIInfo;
import jpkmn.game.base.AIParty;
import jpkmn.game.pokemon.Pokemon;

public class GymLeader extends AbstractPlayer {
  public GymLeader(int badgeNumber) {
    _id = badgeNumber;

    List<Pokemon> pokemon = AIParty.getPartyForLeader(_id);
    AIInfo info = AIInfo.getInfoForLeader(_id);

    for (Pokemon p : pokemon) {
      party.add(p);
    }

    _name = info.getName();
    _cash = info.getCash();
  }
}