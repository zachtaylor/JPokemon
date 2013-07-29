package org.jpokemon.action.requirement;

import org.jpokemon.trainer.Player;

public abstract class Requirement {
  public boolean isOkay(Player p) {
    switch (RequirementType.valueOf(getType())) {
    case EVENT:
      return p.record().getEvent(Math.abs(getData())) ^ (getData() < 0); // XOR
    case POKEDEX:
      return p.pokedex().count() >= Math.abs(getData()) ^ (getData() < 0); // XOR
    }
    return false;
  }

  public abstract int getType();

  public abstract void setType(int t);

  public abstract void commitTypeChange(int newType);

  public abstract int getData();

  public abstract void setData(int d);

  public abstract void commitDataChange(int newData);
}