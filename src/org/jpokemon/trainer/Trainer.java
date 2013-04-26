package org.jpokemon.trainer;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.json.JSONException;
import org.json.JSONObject;

import com.zachtaylor.jnodalxml.XMLNode;

public class Trainer implements PokemonTrainer {
  public static String XML_NODE_NAME;

  public int id() {
    return _id;
  }

  public String name() {
    return _name;
  }

  public void name(String name) {
    _name = name;
  }

  public double xpFactor() {
    // TODO : do something useful here...
    return JPokemonConstants.TRAINER_EXPERIENCE_MODIFIER;
  }

  public PokemonStorageUnit party() {
    return _party;
  }

  public boolean add(Pokemon p) {
    return party().add(p);
  }

  public void notify(String... message) {
  }

  public void state(TrainerState state) { // Do nothing
  }

  public JSONObject toJSON(TrainerState state) {
    JSONObject data = new JSONObject();

    try {
      if (state == TrainerState.BATTLE) {
        data.put("id", id());
        data.put("leader", party().get(0).toJSON(state));
        data.put("party", party().toJSON(state));
      }

    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    node.setAttribute("use_gym_xp_factor", _useGymXPFactor + "");
    node.addChild(_party.toXML());

    return node;
  }

  public boolean equals(Object o) {
    if (!(o instanceof Trainer))
      return false;
    return ((Trainer) o)._id == _id;
  }

  private int _id = -1;
  private String _name = null;
  private boolean _useGymXPFactor = false;
  private PokemonStorageUnit _party = new PokemonStorageUnit(JPokemonConstants.TRAINER_PARTY_SIZE);
}