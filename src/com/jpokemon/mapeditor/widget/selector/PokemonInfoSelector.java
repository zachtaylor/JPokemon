package com.jpokemon.mapeditor.widget.selector;

import org.jpokemon.pokemon.PokemonInfo;

public class PokemonInfoSelector extends JPokemonSelector<PokemonInfo> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    PokemonInfo pokemonInfo;
    for (int i = 1; (pokemonInfo = PokemonInfo.get(i)) != null; i++) {
      addElementToModel(pokemonInfo);
    }
  }

  private static final long serialVersionUID = 1L;
}