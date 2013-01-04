package org.jpokemon.trainer;

import jpkmn.exceptions.LoadException;

import org.jpokemon.JPokemonConstants;
import org.json.JSONArray;
import org.json.JSONException;

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
    if (id < 1 || id > EVENTNUMBER)
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
    if (id < 1 || id > EVENTNUMBER)
      throw new IllegalArgumentException("Out of bounds event: " + id);

    return _events[id - 1];
  }

  public JSONArray toJSON() {
    JSONArray data = new JSONArray();

    for (int i = 0; i < EVENTNUMBER; i++)
      if (_events[i])
        data.put(i);

    return data;
  }

  public void loadJSON(JSONArray json) throws LoadException {
    try {
      for (int i = 0; i < json.length(); i++)
        put(json.getInt(i) + 1);
    } catch (Exception e) {
      if (e instanceof JSONException)
        e.printStackTrace();

      throw new LoadException("Progress could not load: " + json);
    }
  }

  private boolean[] _events;
}