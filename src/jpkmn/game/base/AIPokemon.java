package jpkmn.game.base;

import org.jpokemon.JPokemonConstants;

public class AIPokemon implements JPokemonConstants {
  private int ai_number;
  private String entry;

  //@preformat
  public int getAi_number() {return ai_number;} public void setAi_number(int _n) {ai_number = _n;}
  public String getEntry() {return entry;} public void setEntry(String e) {entry = e;}
  //@format
}