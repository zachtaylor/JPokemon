package org.jpokemon.pokemon.storage;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import jpkmn.exceptions.LoadException;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.JPokemonConstants;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A representation of all the PokemonStorageUnits allocated to a Player. <br>
 * <br>
 * PokemonStorageBlock supports 1 unit of unique size, to be used for the party.
 * Other units have common size.
 */
public class PokemonStorageBlock implements Iterable<PokemonStorageUnit>, JPokemonConstants {
  public PokemonStorageBlock() {
    _data = new PokemonStorageUnit[PLAYER_STORAGE_UNIT_COUNT + 1];

    _data[0] = new PokemonStorageUnit(TRAINER_PARTY_SIZE);
    for (int i = 1; i <= PLAYER_STORAGE_UNIT_COUNT; i++)
      _data[i] = new PokemonStorageUnit(PLAYER_STORAGE_UNIT_SIZE);
  }

  public PokemonStorageUnit get(int box) {
    if (box < 0 || box > PLAYER_STORAGE_UNIT_COUNT)
      throw new IllegalArgumentException("Invalid box number: " + box);

    return _data[box];
  }

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();

    try {
      data.put("party", _data[0].toJSON());

      for (int i = 1; i <= PLAYER_STORAGE_UNIT_COUNT; i++)
        data.put("box" + i, _data[i].toJSON());

    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  public void load(Scanner scan) throws LoadException {
    if (!scan.hasNext() || !scan.nextLine().equals("POKEMON START"))
      throw new LoadException("Improper format");

    int box;
    String pokemon, line = null;
    Scanner lineScan;

    try {
      while (!(line = scan.nextLine()).equals("POKEMON END")) {
        lineScan = new Scanner(line);
        box = lineScan.nextInt();
        pokemon = lineScan.nextLine();

        if (!_data[box].add(Pokemon.load(pokemon))) {
          lineScan.close();
          throw new LoadException("Box " + box + " full on " + line);
        }
      }
    } catch (InputMismatchException e) {
      throw new LoadException("Improper form on line: " + line);
    } catch (NoSuchElementException e) {
      throw new LoadException("Improper file format");
    }
  }

  @Override
  public Iterator<PokemonStorageUnit> iterator() {
    return new PokemonStorageBlockIterator();
  }

  private class PokemonStorageBlockIterator implements Iterator<PokemonStorageUnit> {
    @Override
    public boolean hasNext() {
      return index <= PLAYER_STORAGE_UNIT_COUNT;
    }

    @Override
    public PokemonStorageUnit next() {
      return _data[index++];
    }

    @Override
    public void remove() {
      // No
    }

    private int index = 0;
  }

  private PokemonStorageUnit[] _data;
}