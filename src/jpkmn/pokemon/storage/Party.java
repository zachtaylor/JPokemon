package jpkmn.pokemon.storage;

import java.util.Iterator;

import jpkmn.Constants;
import jpkmn.pokemon.Pokemon;

public class Party implements Iterable<Pokemon> {
  private int a; // flag to do work

  public Party() {
    amount = 0;
    pkmn = new Pokemon[Constants.PARTYSIZE];
  }

  public Pokemon get(int i) {
    if (i < 0 || i > amount) return null;

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
    int lead = 1; // TODO Ask user for position

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

    while (!getLeader().isAwake())
      swap();

    return true;
  }

  /**
   * Tries to add p. Reports success
   * 
   * @param p Pokemon to be added
   * @return true if it is added
   */
  public boolean add(Pokemon p) {
    if (p == null || amount == Constants.PARTYSIZE || contains(p))
      return false;

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

    for (int i = 0; i < amount; i++) {
      if (pkmn[i].isAwake()) answer++;
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

    for (int i = index; i < amount - 1; i++)
      pkmn[i] = pkmn[i + 1];

    pkmn[--amount] = null;

    return true;
  }

  public String getNameList() {
    String response = "[";

    for (int i = 0; i < amount; i++) {
      if (i != 0) response += ", ";
      response += pkmn[i].name();
    }

    return (response + "]");
  }

  private int indexOf(Pokemon p) {
    for (int i = 0; i < amount; i++)
      if (pkmn[i].equals(p)) return i;

    return -1;
  }

  private int amount;
  private Pokemon[] pkmn;

  @Override
  public Iterator<Pokemon> iterator() {
    return new PartyIterator(this);
  }

  private class PartyIterator implements Iterator<Pokemon> {
    public PartyIterator(Party p) {
      _party = p;
    }

    @Override
    public boolean hasNext() {
      return position < _party.amount;
    }

    public Pokemon next() {
      return _party.get(position++);
    }

    @Override
    public void remove() {
      // Not needed
    }

    private int position = 0;
    private Party _party;
  }

}
