package jpkmn.pokemon;

import java.io.PrintWriter;
import java.util.Scanner;

import jpkmn.Driver;
import jpkmn.gui.Splash;
import jpkmn.gui.Tools;

public class Party {

  public Party() {
    pkmn = new Pokemon[Driver.PARTYSIZE];
    amount = 0;
  }

  public Pokemon get(int i) {
    return pkmn[i];
  }
  
  /**
   * Gets the current leader of the party
   * 
   * @return the leader
   */
  public Pokemon getLeader() {
    return pkmn[0];
  }

  /**
   * Gets the number of pokemon currently in the party
   * 
   * @return the amount of pokemon
   */
  public int size() {
    return amount;
  }

  /**
   * Swaps pokemon in this party.
   * 
   * @return True if a swap occurred
   */
  public boolean swap() {
    int lead = Tools.selectFromParty("Select a new leader", this);

    if (lead > 0 && lead < amount) {
      Pokemon swap = pkmn[0];
      pkmn[0] = pkmn[lead];
      pkmn[lead] = swap;
      return true;
    }

    return false;
  }

  /**
   * Repetitively calls swap until the leader is awake. May fail if number of
   * awake pokemon is 0.
   * 
   * @return True if the leader is awake
   */
  public boolean forceAwakeLeader() {
    if (countAwake() == 0) return false;

    while (!getLeader().isAwake()) {
      swap();
    }
    return true;
  }

  /**
   * Tries to add p. Reports success
   * 
   * @param p Pokemon to be added
   * @return true if it is added
   */
  public boolean add(Pokemon p) {
    if (amount == Driver.PARTYSIZE || contains(p)) return false;

    pkmn[amount++] = p;
    return true;

  }

  /**
   * Counts the number of awake Pokemon in the party
   * 
   * @return Number of awake Pokemon
   */
  public int countAwake() {
    int answer = 0;

    for (Pokemon p : pkmn) {
      if (p != null && p.isAwake()) answer++;
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
    for (int i = 0; i < amount; i++) {
      if (pkmn[i].equals(p)) return true;
    }
    return false;
  }

  /**
   * Removes Pokemon p from this party
   * 
   * @param p Pokemon to remove
   * @return True if p was removed
   */
  public boolean remove(Pokemon p) {
    int index = indexOf(p);
    if (index < 0) return false;

    for (int i = index; i < amount - 1; i++) {
      pkmn[i] = pkmn[i + 1];
    }
    pkmn[--amount] = null;

    return true;
  }

  public String getNameList() {
    String response = "[";

    for (int i = 0; i < 6; ++i) {
      if (pkmn[i] != null) response += pkmn[i].name();
      if (i != 5) response += ", ";
    }

    return (response + "]");
  }

  /**
   * Constructs a party from a file
   * 
   * @param s Scanner to read from
   */
  public void readFile(Scanner s) {
    for (int i = 0; i < Driver.PARTYSIZE && s.hasNext(); i++) {
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

  private int indexOf(Pokemon p) {
    for (int i = 0; i < amount; i++) {
      if (pkmn[i].equals(p)) return i;
    }
    return -1;
  }

  private int amount;
  private Pokemon[] pkmn;
}
