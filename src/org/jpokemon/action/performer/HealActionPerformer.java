package org.jpokemon.action.performer;

import org.jpokemon.manager.LoadException;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Player;

public class HealActionPerformer implements ActionPerformer {
  @Override
  public void execute(Player player) throws LoadException {
    for (Pokemon pokemon : player.party()) {
      pokemon.healDamage(pokemon.maxHealth());
    }
  }
}