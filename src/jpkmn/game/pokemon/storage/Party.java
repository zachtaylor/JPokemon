package jpkmn.game.pokemon.storage;

import java.util.Iterator;

import jpkmn.Constants;
import jpkmn.game.player.AbstractPlayer;
import jpkmn.game.pokemon.Pokemon;

public class Party implements Iterable<Pokemon> {
  public Party(AbstractPlayer p) {
    _owner = p;
    _amount = 0;
    _data = new Pokemon[Constants.PARTYSIZE];
  }

  public Pokemon get(int i) {
    if (i < 0 || i > _amount) return null;

    return _data[i];
  }

  public AbstractPlayer owner() {
    return _owner;
  }

  public int size() {
    return _amount;
  }

  public boolean add(Pokemon p) {
    if (p == null || _amount == Constants.PARTYSIZE || contains(p))
      return false;

    _data[_amount++] = p;
    p.owner(_owner);

    return true;
  }

  public boolean remove(Pokemon p) {
    return remove(indexOf(p));
  }

  public boolean remove(int index) {
    if (index < 0) return false;

    for (int i = index; i < _amount - 1; i++)
      _data[i] = _data[i + 1];

    _data[--_amount].owner(null);
    _data[_amount] = null;

    return true;
  }

  public boolean contains(Pokemon p) {
    for (int i = 0; i < _amount; i++) {
      if (_data[i].equals(p)) return true;
    }
    return false;
  }

  public int countAwake() {
    int answer = 0;

    for (int i = 0; i < _amount; i++) {
      if (_data[i].condition.awake()) answer++;
    }

    return answer;
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
    return new PartyIterator(this);
  }

  private int indexOf(Pokemon p) {
    for (int i = 0; i < _amount; i++)
      if (_data[i].equals(p)) return i;

    return -1;
  }

  private class PartyIterator implements Iterator<Pokemon> {
    public PartyIterator(Party p) {
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

    private Party _party;
    private int position = 0;
  }

  private int _amount;
  private Pokemon[] _data;
  private AbstractPlayer _owner;
}
