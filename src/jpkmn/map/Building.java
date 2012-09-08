package jpkmn.map;

import jpkmn.game.player.Player;
import jpkmn.game.pokemon.Pokemon;

public enum Building {
  CENTER, MART, HOME, ELITE4;

  public void effect(Player player) {
    if (this == CENTER) {
      for (Pokemon p : player.party) {
        if (p != null) p.healDamage(p.stats.hp.max());
      }
    }
  }

  public Building valueOf(int b) {
    return Building.values()[b];
  }
}