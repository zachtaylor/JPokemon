package org.jpokemon.action;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Player;

public class HealAction extends Action {
  @Override
  public void execute(Player player) {
    for (Pokemon pokemon : player.party()) {
      pokemon.healDamage(pokemon.maxHealth());
    }
  }
}