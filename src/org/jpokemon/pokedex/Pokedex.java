package org.jpokemon.pokedex;

import java.util.Scanner;

import jpkmn.Constants;
import jpkmn.exceptions.LoadException;

/**
 * A gadget which helps keep track of which Pokemon have been seen or caught in
 * the world. Pokedex maintains a status on each Pokemon it tracks.
 */
public class Pokedex {
  public Pokedex() {
    _size = Constants.POKEMONNUMBER;

    _data = new PokedexStatus[_size];

    for (int i = 0; i < _size; i++)
      _data[i] = PokedexStatus.NONE;
  }

  /**
   * Update the Pokedex with having seen a new Pokemon
   * 
   * @param num Pokemon number
   */
  public void saw(int num) {
    if (num < 1 || num > _size || _data[num - 1] == PokedexStatus.OWN)
      return;

    _data[num - 1] = PokedexStatus.SAW;
  }

  /**
   * Update the Pokedex with having caught a new Pokemon.
   * 
   * @param num Pokemon number
   */
  public void own(int num) {
    if (num < 1 || num > _size)
      return;

    _data[num - 1] = PokedexStatus.OWN;
  }

  /**
   * Gets the status of a Pokemon in the Pokedex
   * 
   * @param num Pokemon number
   * @return PokedexStatus which describes the Pokemon number in question
   */
  public PokedexStatus status(int num) {
    if (num < 1 || num > _size)
      throw new IllegalArgumentException("#" + num + " out of range: " + _size);

    return _data[num - 1];
  }

  /**
   * Gets a string representation of the current data in the Pokedex, which can
   * be used to restore the Pokdex
   * 
   * @return A string representation of the data
   */
  public String save() {
    StringBuilder list = new StringBuilder();
    list.append("DEX: ");

    for (int i = 0; i < _size; i++) {
      int status = _data[i].ordinal();

      if (status > 0) {
        list.append(i);
        list.append("-");
        list.append(status);
        list.append(" ");
      }
    }

    list.append("\n");

    return list.toString();
  }

  /**
   * Loads this Pokedex with data.
   * 
   * @param s String representation of the Pokedex data
   * @throws LoadException If an error occurs with the data being loaded
   */
  public void load(String s) throws LoadException {
    Scanner scan = new Scanner(s);

    if (!scan.hasNext() || !scan.next().equals("DEX:"))
      throw new LoadException("Improper format: " + s);

    try {
      int number;
      PokedexStatus status;
      String[] parts;

      while (scan.hasNext()) {
        parts = scan.next().split("-");
        number = Integer.parseInt(parts[0]);
        status = PokedexStatus.valueOf(Integer.parseInt(parts[1]));

        _data[number] = status;
      }
    } catch (NumberFormatException e) {
      throw new LoadException("Numbers inparsable");
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new LoadException("Entry loaded above indicated size");
    }
  }

  private int _size;
  private PokedexStatus[] _data;
}