package org.jpokemon.pokemon.move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jpkmn.Constants;
import org.jpokemon.exception.ConfigurationException;

public class MoveBlock implements Iterable<Move> {
  public MoveBlock(int pokemonNumber) throws ConfigurationException {
    _pokemonNumber = pokemonNumber;
    _data = new Move[Constants.MOVESAVAILABLE];

    List<MoveMap> maps = MoveMap.get(_pokemonNumber, 1);

    try {
      for (MoveMap map : maps)
        add(map.getMove_number());
    } catch (IllegalStateException e) {
      e.printStackTrace();
      throw new ConfigurationException("Excessive default moves: "
          + pokemonNumber);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      throw new ConfigurationException(e.getMessage());
    }
  }

  public void setPokemonNumber(int number) {
    _pokemonNumber = number;
  }

  public int count() {
    return _count;
  }

  public Move get(int i) {
    if (i < 0 || i >= _count)
      throw new IllegalArgumentException("Index out of bounds");

    return _data[i];
  }

  public void add(int number) {
    if (_count == Constants.MOVESAVAILABLE)
      throw new IllegalStateException("MoveBlock is full");

    add(number, _count);
  }

  public void add(int number, int position) {
    if (position < 0 || position >= Constants.MOVESAVAILABLE)
      throw new IllegalArgumentException("Position out of bounds: " + position);
    if (contains(number))
      throw new IllegalArgumentException("Duplicate move: " + number);

    _data[position] = new Move(number);
    _count++;
  }

  public void restoreAll() {
    for (Move move : this)
      move.restore();
  }

  public void removeAll() {
    _data = new Move[Constants.MOVESAVAILABLE];
    _count = 0;
  }

  public List<String> newMoves(int level) {
    List<MoveMap> maps = MoveMap.get(_pokemonNumber, level);
    List<String> names = new ArrayList<String>();

    for (MoveMap map : maps)
      names.add(MoveInfo.get(map.getMove_number()).getName());

    return names;
  }

  public void randomize(int level) {
    ArrayList<Integer> possible = new ArrayList<Integer>();

    for (int currentLevel = 1; currentLevel <= level; currentLevel++)
      for (MoveMap map : MoveMap.get(_pokemonNumber, currentLevel))
        if (!possible.contains(map.getMove_number()))
          possible.add(map.getMove_number());

    if (possible.isEmpty())
      return;

    removeAll();
    while (!possible.isEmpty() && _count < Constants.MOVESAVAILABLE)
      add(possible.remove((int) (Math.random() * possible.size())));
  }

  @Override
  public Iterator<Move> iterator() {
    return new MoveBlockIterator();
  }

  private boolean contains(int number) {
    for (Move m : this)
      if (m.number() == number)
        return true;

    return false;
  }

  private class MoveBlockIterator implements Iterator<Move> {
    public MoveBlockIterator() {
      _index = 0;
    }

    @Override
    public boolean hasNext() {
      return _index < MoveBlock.this._count;
    }

    @Override
    public Move next() {
      return MoveBlock.this.get(_index++);
    }

    @Override
    public void remove() { // Nope
    }

    private int _index;
  }

  private Move[] _data;
  private int _count, _pokemonNumber;
}