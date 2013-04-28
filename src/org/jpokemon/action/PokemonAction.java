package org.jpokemon.action;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Player;

public class PokemonAction extends Action {
  public PokemonAction(String data) {
    super(data);
  }

  public void execute(Player player) {
    Pokemon pokemon = null;
    String[] parameters = data().split(" ");

    int number = Integer.parseInt(parameters[0]);

    if (number < 1) {
      for (Pokemon cur : player.party()) {
        if (cur.number() == number && pokemon == null) {
          pokemon = cur;
        }
      }

      player.party().remove(pokemon);
    }
    else {
      pokemon = new Pokemon(number, Integer.parseInt(parameters[1]));

      for (int i = 2; i < parameters.length; i++) {
        String[] parameter = parameters[i].split("=");

        if (parameter[0].equals("ot")) {
          pokemon.setTrainerName(parameter[1]);
        }
      }

      player.add(pokemon);
    }
  }
}