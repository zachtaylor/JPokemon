package org.jpokemon.pokemon;

import org.jpokemon.JPokemonConstants;

public class EffortValue implements JPokemonConstants {
  private int pokemon, stat, amount;

  //@preformat
  public int getPokemon() { return pokemon; } public void setPokemon(int val) { pokemon = val; }
  public int getStat() { return stat; } public void setStat(int val) { stat = val; }
  public int getAmount() { return amount; } public void setAmount(int val) { amount = val; }
  //@format
}