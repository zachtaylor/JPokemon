package jpkmn;

import java.io.PrintWriter;
import java.util.Scanner;

import jpkmn.pokemon.Pokemon;


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

  public void toFile(PrintWriter p) {
    p.print("[ ");
    for (int i = 0; i < POKEMONNUMBER; i++) {
      if (seen[i]) p.print(i + " ");

    }
    p.print("]\n[ ");
    for (int i = 0; i < POKEMONNUMBER; i++) {
      if (own[i]) p.print(i + " ");

    }
    p.println("]");
  }

  public void readFile(Scanner s) {
    for (String cur = s.next(); !cur.equals("]"); cur = s.next()) {
      saw(Integer.parseInt(cur) + 1);
    }
    s.next();
    for (String cur = s.next(); !cur.equals("]"); cur = s.next()) {
      caught(Integer.parseInt(cur) + 1);
    }
  }

  private static int POKEMONNUMBER = 151;
}
