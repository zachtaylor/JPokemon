package org.jpokemon.map;

import org.jpokemon.trainer.Player;

public class Requirement {
  public Requirement(int type, int data) {
    _type = RequirementType.valueOf(type);
    _data = data;
  }

  public RequirementType type() {
    return _type;
  }

  public void type(RequirementType t) {
    _type = t;
  }

  public boolean isOkay(Player p) {
    switch (_type) {
    case POKEDEX_COUNT:
      return p.pokedex().count() >= _data;
    case POKEMON_MOVE:
      return false; // TODO check party for move
    case EVENT:
      return false; // TODO check player for event history
    default:
      break;
    }
    return false;
  }

  private int _data;
  private RequirementType _type;
}