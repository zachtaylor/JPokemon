package org.jpokemon.pokemon.storage;

import java.util.Scanner;

import jpkmn.Constants;
import jpkmn.exceptions.LoadException;
import jpkmn.game.player.Trainer;
import jpkmn.game.pokemon.Pokemon;

/**
 * A representation of all the PokemonStorageUnits allocated to a Player. <br>
 * <br>
 * PokemonStorageBlock supports 1 unit of unique size, to be used for the
 * party. Other units have common size. <br>
 * <br>
 * PokemonStorageBlock plays nice with party size, box number, and box size
 * different from the system's Constants.PARTYSIZE, Constants.BOXNUMBER, and
 * Constants.BOXSIZE
 */
public class PokemonStorageBlock {
  public PokemonStorageBlock(Trainer trainer) {
    _trainer = trainer;

    initData(Constants.PARTYSIZE, Constants.BOXNUMBER, Constants.BOXSIZE);
  }

  public String save() {
    StringBuilder sb = new StringBuilder();
    sb.append("POKEMON (");
    sb.append(_data[0].size());
    sb.append(",");
    sb.append(_data.length - 1);
    sb.append("x");
    sb.append(_data[1].size());
    sb.append(")\n");

    for (int i = 0; i < _data.length; i++) {
      for (Pokemon pokemon : _data[i])
        sb.append(i + " " + pokemon.save());
    }

    return sb.toString();
  }

  public void load(Scanner scan) throws LoadException {
    if (!scan.hasNext() || !scan.next().equals("POKEMON"))
      throw new LoadException("Improper format");

    String[] dimension = scan.next().split(",");

    if (dimension.length != 2)
      throw new LoadException("Improper format");

    String[] boxDimension = dimension[1].split("x");

    if (boxDimension.length != 2)
      throw new LoadException("Improper format");

    try {
      initData(Integer.parseInt(dimension[0]),
          Integer.parseInt(boxDimension[1]), Integer.parseInt(boxDimension[1]));
    } catch (NumberFormatException e) {
      throw new LoadException("Improper format");
    }

    scan.nextLine();

    try {
      while (scan.hasNextLine()) {
        int boxId = Integer.parseInt(scan.next());
        String loadData = scan.nextLine();

        if (!_data[boxId].add(Pokemon.load(loadData)))
          throw new LoadException("Box " + boxId + " full on " + loadData);
      }
    } catch (NumberFormatException e) {
      throw new LoadException("Box number specified improperly");
    }
  }

  private void initData(int partySize, int boxNumber, int boxSize) {
    _data = new PokemonStorageUnit[boxNumber + 1];

    _data[0] = new PokemonStorageUnit(partySize, _trainer);
    for (int i = 1; i <= boxNumber; i++)
      _data[i] = new PokemonStorageUnit(boxSize, _trainer);
  }

  private Trainer _trainer;
  private PokemonStorageUnit[] _data;
}