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
    case EVENT:
      return p.events().get(Math.abs(_data)) ^ (_data < 0); // XOR
    }
    return false;
  }

  public String denialReason() {
    switch (_type) {
    case EVENT:
      return "You are not ready yet";
    }

    return "foo bar?";
  }

  private int _data;
  private RequirementType _type;
}