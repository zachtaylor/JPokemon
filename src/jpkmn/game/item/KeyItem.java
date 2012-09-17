package jpkmn.game.item;

import jpkmn.game.pokemon.Pokemon;

public class KeyItem extends Item {

  public KeyItem(String name, int itemID, int value, int data) {
    super(name, itemID, value);
    // TODO Auto-generated constructor stub
  }

  @Override
  public boolean effect(Pokemon p) {
    // TODO : useful stuff
    return false;
  }
}