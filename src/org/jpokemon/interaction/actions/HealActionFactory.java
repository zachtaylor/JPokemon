package org.jpokemon.interaction.actions;

import org.jpokemon.interaction.Action;
import org.jpokemon.interaction.ActionFactory;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Player;

public class HealActionFactory implements ActionFactory {
  @Override
  public Action buildAction(String options) {
    return new HealAction();
  }
}

class HealAction implements Action {
  @Override
  public void execute(Player player) {
    for (Pokemon pokemon : player.party()) {
      pokemon.healDamage(pokemon.maxHealth());
    }
  }
}