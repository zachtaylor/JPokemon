package org.jpokemon.pokedex;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import jpkmn.exceptions.LoadException;

import org.jpokemon.JPokemonConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A gadget which helps keep track of which Pokemon have been seen or caught in
 * the world. Pokedex maintains a status on each Pokemon it tracks.
 */
public class Pokedex implements JPokemonConstants {
  /**
   * Update the Pokedex with having seen a new Pokemon
   * 
   * @param num Pokemon number
   */
  public void saw(int num) {
    if (num < 1)
      throw new IllegalArgumentException("#" + num + " out of range");
    if (_data.get(num) == PokedexStatus.OWN)
      return;

    _data.put(num, PokedexStatus.SAW);
  }

  /**
   * Update the Pokedex with having caught a new Pokemon.
   * 
   * @param num Pokemon number
   */
  public void own(int num) {
    if (num < 1)
      throw new IllegalArgumentException("#" + num + " out of range");

    _data.put(num, PokedexStatus.OWN);
  }

  /**
   * Gets the status of a Pokemon in the Pokedex
   * 
   * @param num Pokemon number
   * @return PokedexStatus which describes the Pokemon number in question
   */
  public PokedexStatus status(int num) {
    if (num < 1)
      throw new IllegalArgumentException("#" + num + " out of range");

    PokedexStatus status = _data.get(num);

    if (status == null)
      status = PokedexStatus.NONE;

    return status;
  }

  /**
   * Count the number of Pokemon with in the Pokedex
   * 
   * @return The number of Pokemon with non-NONE PokedexStatus
   */
  public int count() {
    return _data.size();
  }

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();
    JSONArray own = new JSONArray();
    JSONArray saw = new JSONArray();

    for (Map.Entry<Integer, PokedexStatus> entry : _data.entrySet()) {
      switch (entry.getValue()) {
      case SAW:
        saw.put(entry.getKey());
        break;
      case OWN:
        own.put(entry.getKey());
        break;
      default:
      }
    }

    try {
      data.put("saw", saw);
      data.put("own", own);
    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  /**
   * Loads this Pokedex with data.
   * 
   * @param s String representation of the Pokedex data
   * @throws LoadException If an error occurs with the data being loaded
   */
  public void load(String s) throws LoadException {
    if (!s.startsWith("DEX:"))
      throw new LoadException("Improper format: " + s);

    Scanner scan = new Scanner(s);
    scan.next();

    try {
      int number;
      PokedexStatus status;
      String[] parts;

      while (scan.hasNext()) {
        parts = scan.next().split("-");
        number = 1 + Integer.parseInt(parts[0]);
        status = PokedexStatus.valueOf(Integer.parseInt(parts[1]));

        _data.put(number, status);
      }
    } catch (NumberFormatException e) {
      scan.close();
      throw new LoadException("Numbers inparsable");
    } catch (ArrayIndexOutOfBoundsException e) {
      scan.close();
      throw new LoadException("Entry loaded above indicated size");
    }
    scan.close();
  }

  private Map<Integer, PokedexStatus> _data = new HashMap<Integer, PokedexStatus>();
}