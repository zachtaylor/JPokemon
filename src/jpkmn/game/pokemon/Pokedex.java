package jpkmn.game.pokemon;

import java.util.Scanner;

import jpkmn.exceptions.LoadException;
import jpkmn.game.pokemon.Pokemon;

public class Pokedex {
  private boolean[] own;
  private boolean[] seen;

  public Pokedex() {
    own = new boolean[POKEMONNUMBER];
    seen = new boolean[POKEMONNUMBER];
  }

  /**
   * Update the pokedex to reflect having seen something new.
   * 
   * @param p The Pokemon
   */
  public void saw(Pokemon p) {
    saw(p.number());
  }

  /**
   * Update the Pokedex to reflect having caught something new.
   * 
   * @param p The Pokemon
   */
  public void caught(Pokemon p) {
    caught(p.number());
  }

  private void saw(int num) {
    seen[num - 1] = true;
  }

  private void caught(int num) {
    own[num - 1] = true;
  }

  /**
   * Gets the status of a Pokemon in the Pokedex
   * 
   * @param num Pokemon number
   * @return 0 if nothing, 1 if seen, 2 if owned.
   */
  public int getStatus(int num) {
    return own[num - 1] ? 2 : seen[num - 1] ? 1 : 0;
  }

  public String saveSeenToString() {
    StringBuilder list = new StringBuilder();
    list.append("[ ");

    for (int i = 0; i < POKEMONNUMBER; i++) {
      if (seen[i]) list.append(i + " ");

    }
    list.append("]");

    return list.toString();
  }

  public String saveOwnToString() {
    StringBuilder list = new StringBuilder();
    list.append("[ ");

    for (int i = 0; i < POKEMONNUMBER; i++) {
      if (own[i]) list.append(i + " ");

    }
    list.append("]");

    return list.toString();
  }

  public void readSeen(String s) throws LoadException {
    Scanner scan = new Scanner(s);

    if (!scan.next().equals("["))
      throw new LoadException("Pokedex seen read from: " + s);

    for (String cur = scan.next(); !cur.equals("]"); cur = scan.next()) {
      saw(Integer.parseInt(cur) + 1);
    }
  }

  public void readOwn(String s) throws LoadException {
    Scanner scan = new Scanner(s);

    if (!scan.next().equals("["))
      throw new LoadException("Pokedex own read from: " + s);

    for (String cur = scan.next(); !cur.equals("]"); cur = scan.next()) {
      caught(Integer.parseInt(cur) + 1);
    }
  }

  private static int POKEMONNUMBER = 151;
}
