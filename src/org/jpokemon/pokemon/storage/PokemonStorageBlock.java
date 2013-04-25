package org.jpokemon.pokemon.storage;

import java.util.Iterator;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.trainer.TrainerState;
import org.json.JSONException;
import org.json.JSONObject;

import com.zachtaylor.jnodalxml.XMLException;
import com.zachtaylor.jnodalxml.XMLNode;

/**
 * A representation of all the PokemonStorageUnits allocated to a Player. <br>
 * <br>
 * PokemonStorageBlock supports 1 unit of unique size, to be used for the party.
 * Other units have common size.
 */
public class PokemonStorageBlock implements Iterable<PokemonStorageUnit> {
  public static final String XML_NODE_NAME = "pokemonstorage";

  public PokemonStorageBlock() {
    _data = new PokemonStorageUnit[JPokemonConstants.PLAYER_STORAGE_UNIT_COUNT + 1];

    _data[0] = new PokemonStorageUnit(JPokemonConstants.TRAINER_PARTY_SIZE);
    for (int i = 1; i <= JPokemonConstants.PLAYER_STORAGE_UNIT_COUNT; i++)
      _data[i] = new PokemonStorageUnit(JPokemonConstants.PLAYER_STORAGE_UNIT_SIZE);
  }

  public PokemonStorageUnit get(int box) {
    if (box < 0 || box > JPokemonConstants.PLAYER_STORAGE_UNIT_COUNT)
      throw new IllegalArgumentException("Invalid box number: " + box);

    return _data[box];
  }

  public JSONObject toJSON(TrainerState state) {
    JSONObject data = new JSONObject();

    try {
      data.put("party", _data[0].toJSON(state));

      for (int i = 1; i <= JPokemonConstants.PLAYER_STORAGE_UNIT_COUNT; i++)
        data.put("box" + i, _data[i].toJSON(state));

    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    for (PokemonStorageUnit psu : _data) {
      node.addChild(psu.toXML());
    }

    return node;
  }

  public void loadXML(XMLNode node) {
    if (!XML_NODE_NAME.equals(node.getName()))
      throw new XMLException("Cannot read node");

    int i = 0;
    for (XMLNode child : node.getChildren(PokemonStorageUnit.XML_NODE_NAME)) {
      get(i++).loadXML(child);
    }
  }

  @Override
  public Iterator<PokemonStorageUnit> iterator() {
    return new PokemonStorageBlockIterator();
  }

  private class PokemonStorageBlockIterator implements Iterator<PokemonStorageUnit> {
    @Override
    public boolean hasNext() {
      return index <= JPokemonConstants.PLAYER_STORAGE_UNIT_COUNT;
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