package jpkmn.game.pokemon.move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jpkmn.Constants;
import jpkmn.exceptions.LoadException;

public class MoveBlock implements Iterable<Move> {
  public MoveBlock(int pokemonNumber) throws LoadException {
    _pokemonNumber = pokemonNumber;
    _data = new Move[Constants.MOVESAVAILABLE];

    List<MoveMap> maps = MoveMap.get(_pokemonNumber, 1);

    try {
      for (MoveMap map : maps)
        add(map.getMove_number());
    } catch (IllegalStateException e) {
      throw new LoadException(e.getMessage());
    }
  }

  public Move get(int i) {
    if (i < 0 || i >= _amount)
      throw new IllegalArgumentException("Index out of bounds");

    return _data[i];
  }

  public void add(int number) {
    if (_amount == Constants.MOVESAVAILABLE)
      throw new IllegalStateException("MoveBlock is full");

    add(number, _amount);
  }

  public void add(int number, int position) {
    if (position < 0 || position >= Constants.MOVESAVAILABLE)
      throw new IllegalArgumentException("Position out of bounds: " + position);
    if (number < 1 || number > Constants.MOVENUMBER || contains(number))
      throw new IllegalArgumentException("Illegal move number: " + number);

    _data[position] = new Move(number);
    _amount++;
  }

  public void restoreAll() {
    for (Move move : this)
      move.restore();
  }

  public void removeAll() {
    _data = new Move[Constants.MOVESAVAILABLE];
    _amount = 0;
  }

  public boolean check(int level) {
    List<MoveMap> maps = MoveMap.get(_pokemonNumber, level);

    return !maps.isEmpty();
  }

  public void randomize(int level) {
    removeAll();

    ArrayList<Integer> possible = new ArrayList<Integer>();

    for (int currentLevel = 1; currentLevel <= level; currentLevel++)
      for (MoveMap map : MoveMap.get(_pokemonNumber, currentLevel))
        if (!possible.contains(map.getMove_number()))
          possible.add(map.getMove_number());

    while (!possible.isEmpty() && _amount < Constants.MOVESAVAILABLE)
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
      return _index < MoveBlock.this._amount;
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
  private int _amount, _pokemonNumber;
}