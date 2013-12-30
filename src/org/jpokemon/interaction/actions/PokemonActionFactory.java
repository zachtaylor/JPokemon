package org.jpokemon.interaction.actions;

import org.jpokemon.interaction.Action;
import org.jpokemon.interaction.ActionFactory;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;

public class PokemonActionFactory implements ActionFactory {
  @Override
  public Action buildAction(String options) {
    // TODO Auto-generated method stub
    return null;
  }
}

class PokemonAction implements Action {
  @Override
  public void execute(Player player) {
    Pokemon pokemon = null;
    String[] parameters = getData().split(" ");

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