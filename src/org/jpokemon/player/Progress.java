package org.jpokemon.player;

import jpkmn.exceptions.LoadException;

import org.jpokemon.JPokemonConstants;

/**
 * A representation of the progress a Player has made
 */
public class Progress implements JPokemonConstants {
  public Progress() {
    _events = new boolean[EVENTNUMBER];
  }

  /**
   * Sets the specified event id
   * 
   * @param id Event number to record
   */
  public void put(int id) {
    if (id < 1 || id >= EVENTNUMBER)
      throw new IllegalArgumentException("Out of bounds event: " + id);
    if (_events[id - 1])
      throw new IllegalArgumentException("Duplicate put for event: " + id);

    _events[id - 1] = true;
  }

  /**
   * Gets the status of an event
   * 
   * @param id Event number to look up
   * @return True if the event has been completed
   */
  public boolean get(int id) {
    if (id < 1 || id >= EVENTNUMBER)
      throw new IllegalArgumentException("Out of bounds event: " + id);

    return _events[id - 1];
  }

  public String save() {
    StringBuilder data = new StringBuilder();

    data.append("PROGRESS: ");

    for (int i = 0; i < EVENTNUMBER; i++) {
      if (_events[i]) {
        data.append(i);
        data.append(" ");
      }
    }

    data.append("\n");

    return data.toString();
  }

  public void load(String s) throws LoadException {
    try {
      if (!s.startsWith("PROGRESS: "))
        throw new Exception();

      s = s.substring("PROGRESS: ".length(), s.length() - 1);

      for (String event : s.split(" "))
        put(Integer.parseInt(event) + 1);

    } catch (Exception e) {
      e.printStackTrace();
      throw new LoadException("Progress could not load: " + s);
    }
  }

  private boolean[] _events;
}