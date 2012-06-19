package jpkmn.game.pokemon.storage;

import java.util.Iterator;

import jpkmn.Constants;
import jpkmn.game.pokemon.Pokemon;

public abstract class AbstractParty implements Iterable<Pokemon> {
  public AbstractParty() {
    _amount = 0;
    _data = new Pokemon[Constants.PARTYSIZE];
  }

  public abstract boolean add(Pokemon p);

  public abstract boolean remove(int index);

  public int size() {
    return _amount;
  }

  public int countAwake() {
    int answer = 0;

    for (int i = 0; i < _amount; i++) {
      if (_data[i].condition.getAwake()) answer++;
    }

    return answer;
  }

  public Pokemon getLeader() {
    return _data[0];
  }

  public Pokemon get(int i) {
    if (i < 0 || i > _amount) return null;

    return _data[i];
  }

  public boolean remove(Pokemon p) {
    return remove(indexOf(p));
  }

  public boolean contains(Pokemon p) {
    for (int i = 0; i < _amount; i++) {
      if (_data[i].equals(p)) return true;
    }
    return false;
  }

  public boolean swap(int index1, int index2) {
    if (index1 < 0 || index2 < 0) return false;
    if (index1 >= _amount || index2 >= _amount) return false;
    if (index1 == index2) return false;

    Pokemon swap = _data[index1];
    _data[index1] = _data[index2];
    _data[index2] = swap;

    return true;
  }

  @Override
  public Iterator<Pokemon> iterator() {
    return new AbstractPartyIterator(this);
  }

  private int indexOf(Pokemon p) {
    for (int i = 0; i < _amount; i++)
      if (_data[i].equals(p)) return i;

    return -1;
  }

  private class AbstractPartyIterator implements Iterator<Pokemon> {
    public AbstractPartyIterator(AbstractParty p) {
      _party = p;
    }

    @Override
    public boolean hasNext() {
      return position < _party._amount;
    }

    public Pokemon next() {
      return _party.get(position++);
    }

    @Override
    public void remove() {
      // Not needed
    }

    private int position = 0;
    private AbstractParty _party;
  }

  protected int _amount;
  protected Pokemon[] _data;
}
