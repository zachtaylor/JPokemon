package org.jpokemon.interaction.actions;

import java.util.List;

import org.jpokemon.interaction.Action;
import org.jpokemon.interaction.ActionFactory;
import org.jpokemon.overworld.WildPokemon;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;

public class GrassActionFactory implements ActionFactory {
  @Override
  public Action buildAction(String options) {
    return new GrassAction(options);
  }
}

class GrassAction implements Action {
  private String mapId;

  public GrassAction(String mapId) {
    this.mapId = mapId;
  }

  @Override
  public void execute(Player player) throws ServiceException {
    // TODO - do nothing randomly

    int totalFlex = 0;
    List<WildPokemon> wildPokemon = WildPokemon.get(mapId);

    for (WildPokemon wp : wildPokemon) {
      totalFlex += wp.getFlex();
      wildPokemon.add(wp);
    }

    totalFlex = (int) (totalFlex * Math.random());

    for (WildPokemon wp : wildPokemon) {
      if (totalFlex < wp.getFlex()) {
        // Pokemon p = wp.instantiate();
        // TODO - start a battle
        return;
      }
      else {
        totalFlex -= wp.getFlex();
      }
    }
  }
}