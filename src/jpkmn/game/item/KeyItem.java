package jpkmn.game.item;

import jpkmn.game.pokemon.Pokemon;

public class KeyItem extends Item {

  public KeyItem(int data, String name, int value) {
    super(name, value);
    // TODO Auto-generated constructor stub
  }

  @Override
  public boolean effect(Pokemon p) {
    // TODO : useful stuff
    return false;
  }

}