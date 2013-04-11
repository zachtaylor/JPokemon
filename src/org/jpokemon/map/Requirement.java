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

  public boolean isOkay(Player p) {
    switch (_type) {
    case POKEDEX_COUNT:
      return p.pokedex().count() >= _data;
    case POKEMON_MOVE:
      return false; // TODO check party for move
    case EVENT:
      return p.events().get(Math.abs(_data)) ^ (_data < 0); // XOR
    default:
      break;
    }
    return false;
  }

  public String denialReason() {
    switch (_type) {
    case POKEDEX_COUNT:
      return "You have not seen enough pokemon!";
    case POKEMON_MOVE:
      return "Something is in the way";
    case EVENT:
      return "You are not ready yet";
    }

    return "foo bar?";
  }

  private int _data;
  private RequirementType _type;
}