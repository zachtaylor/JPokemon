package org.jpokemon.pokemon.storage;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import jpkmn.Constants;
import jpkmn.exceptions.LoadException;
import jpkmn.game.player.Trainer;
import jpkmn.game.pokemon.Pokemon;

/**
 * A representation of all the PokemonStorageUnits allocated to a Player. <br>
 * <br>
 * PokemonStorageBlock supports 1 unit of unique size, to be used for the
 * party. Other units have common size.
 */
public class PokemonStorageBlock {
  public PokemonStorageBlock(Trainer trainer) {
    _trainer = trainer;

    _data = new PokemonStorageUnit[Constants.BOXNUMBER + 1];

    _data[0] = new PokemonStorageUnit(Constants.PARTYSIZE, _trainer);
    for (int i = 1; i <= Constants.BOXNUMBER; i++)
      _data[i] = new PokemonStorageUnit(Constants.BOXSIZE, _trainer);
  }

  public String save() {
    StringBuilder sb = new StringBuilder();
    sb.append("POKEMON START\n");

    for (int i = 0; i < _data.length; i++)
      for (Pokemon pokemon : _data[i])
        sb.append(i + " " + pokemon.save());

    sb.append("POKEMON END\n");
    return sb.toString();
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

  private Trainer _trainer;
  private PokemonStorageUnit[] _data;
}