package org.jpokemon.interaction.requirements;

import org.jpokemon.interaction.Requirement;
import org.jpokemon.interaction.RequirementFactory;
import org.jpokemon.trainer.Player;

public class PokedexRequirementFactory implements RequirementFactory {
  @Override
  public Requirement buildRequirement(String options) {
    return new PokedexRequirement(Integer.parseInt(options));
  }
}

class PokedexRequirement implements Requirement {
  private int pokedexCount;

  public PokedexRequirement(int pokedexCount) {
    this.pokedexCount = pokedexCount;
  }

  @Override
  public boolean isSatisfied(Player player) {
    return player.pokedex().count() >= Math.abs(pokedexCount) ^ (pokedexCount < 0); // XOR
  }
}