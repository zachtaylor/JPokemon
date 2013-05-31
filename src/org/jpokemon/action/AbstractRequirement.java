package org.jpokemon.action;

import org.jpokemon.trainer.Player;

public abstract class AbstractRequirement implements Requirement {
  public boolean isOkay(Player p) {
    switch (RequirementType.valueOf(getType())) {
    case EVENT:
      return p.record().getEvent(Math.abs(getData())) ^ (getData() < 0); // XOR
    case POKEDEX:
      return p.pokedex().count() >= Math.abs(getData()) ^ (getData() < 0); // XOR
    }
    return false;
  }
}