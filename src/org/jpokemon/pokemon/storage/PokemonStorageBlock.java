package org.jpokemon.pokemon.storage;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import jpkmn.exceptions.LoadException;
import jpkmn.game.player.PokemonTrainer;
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
  public PokemonStorageBlock(PokemonTrainer trainer) {
    _trainer = trainer;

    _data = new PokemonStorageUnit[BOXNUMBER + 1];

    _data[0] = new PokemonStorageUnit(PARTYSIZE, _trainer);
    for (int i = 1; i <= BOXNUMBER; i++)
      _data[i] = new PokemonStorageUnit(BOXSIZE, _trainer);
  }

  public PokemonStorageUnit get(int box) {
    if (box < 0 || box > BOXNUMBER)
      throw new IllegalArgumentException("Invalid box number: " + box);

    return _data[box];
  }

  public JSONObject toJSONObject() {
    JSONObject data = new JSONObject();

    try {
      data.put("party", _data[0].toJSONArray());

      for (int i = 1; i <= BOXNUMBER; i++)
        data.put("box" + i, _data[i].toJSONArray());

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
      return index <= BOXNUMBER;
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

  private PokemonTrainer _trainer;
  private PokemonStorageUnit[] _data;
}