package pokemon;

import gui.Splash;
import gui.Tools;

import java.io.PrintWriter;
import java.util.Scanner;
import jpkmn.Driver;

public class Party {
  public Pokemon[] pkmn;
  private int amount;

  public Party() {
    pkmn = new Pokemon[6];
    amount = 0;
  }

  public Party(Pokemon... p) {
    if (p.length > 6) {
      throw new IllegalArgumentException(
          "Parties cannot be greater than 6 in length");
    }
    pkmn = new Pokemon[6];
    amount = p.length;

    for (int i = 0; i < amount; i++) {
      pkmn[i] = p[i];
    }
  }

  /**
   * Gets the current leader of the party
   * 
   * @return the leader
   */
  public Pokemon leader() {
    return pkmn[0];
  }

  /**
   * Swaps pokemon in this party. Forces leader to be awake.
   * 
   * @return True if a swap occurred
   */
  public boolean doSwap() {
    Driver.log(Party.class, "First call to doSwap. Leader = " + pkmn[0].name);
    return doSwap(pkmn[0]);
  }

  private boolean doSwap(Pokemon origLeader) {
    int lead = Tools.selectFromParty(this);

    if (lead <= 0 && pkmn[0].awake)
      return false;

    if (lead > 0) {
      Pokemon swap = pkmn[0];
      pkmn[0] = pkmn[lead];
      pkmn[lead] = swap;
      Driver.log(Party.class, "doSwap new leader = " + pkmn[0].name);
    }

    if (pkmn[0].awake)
      return true;
    else {
      Tools
          .notify(pkmn[0], "UNCONSCIOUS", "Leader is unconscious. Pick again.");
      Driver.log(Party.class,
          "doSwap calling recursively. " + pkmn[0] == null ? "" : "");
      return doSwap(origLeader);
    }
  }

  /**
   * Tries to add p. Returns if the party is full or p is already in this Party
   * 
   * @param p Pokemon to be added
   * @return true if it is added
   */
  public boolean add(Pokemon p) {
    if (amount == 6 || contains(p))
      return false;
    else {
      pkmn[amount++] = p;
      return true;
    }
  }

  /**
   * Counts the number of awake Pokemon in the party
   * 
   * @return Number of awake Pokemon
   */
  public int countAwake() {
    int answer = 0;

    for (Pokemon p : pkmn) {
      if (p != null && p.awake)
        answer++;
    }

    return answer;
  }

  /**
   * Decides if the Pokemon is already in party
   * 
   * @param p Pokemon to check for
   * @return True if this party contains p
   */
  public boolean contains(Pokemon p) {
    return indexOf(p) >= 0;
  }

  /**
   * Removes Pokemon p from this party
   * 
   * @param p Pokemon to remove
   * @return True if p was removed
   */
  public boolean remove(Pokemon p) {
    int i = indexOf(p);

    if (i < 0)
      return false;

    pkmn[i] = null;
    return true;
  }

  /**
   * Returns the index of a Pokemon in the internal array
   * 
   * @param p Pokemon to get the index of
   * @return position of p in the internal array
   */
  public int indexOf(Pokemon p) {
    for (int i = 0; i < amount; i++) {
      if (pkmn[i].equals(p))
        return i;
    }
    return -1;
  }

  /**
   * Calls resetTempStats on each pokemon in the party
   */
  public void resetTempStats() {
    for (int i = 0; i < amount; i++) {
      if (pkmn[i] != null)
        pkmn[i].resetTempStats();
    }
  }

  public String getNameList() {
    String response = "[";

    for (int i = 0; i < 6; ++i) {
      if (pkmn[i] != null)
        response += pkmn[i].name;
      if (i != 5) response += ", ";
    }

    return (response + "]");
  }

  /**
   * Constructs a party from a file
   * 
   * @param s Scanner to read from
   */
  public void fromFile(Scanner s) {
    for (int i = 0; i < 6 && s.hasNext(); i++) {
      String token = s.next();
      if (token.equals("|")) {
        pkmn[i] = Pokemon.fromFile(s);
        ++amount;
      }
      else {
        if (!token.equals("||"))
          Splash.showFatalErrorMessage("Error reading party");
      }
    }
  }

  /**
   * Prints all of the pokemon in this party in 6 lines
   * 
   * @param p File to print to
   */
  public void toFile(PrintWriter p) {
    for (int i = 0; i < 6; i++) {
      if (pkmn[i] == null)
        p.println("||");
      else
        pkmn[i].toFile(p);
    }
  }
}
