package org.jpokemon.trainer;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.zachtaylor.jnodalxml.XMLNode;

public class Trainer implements PokemonTrainer {
  public static String XML_NODE_NAME = "trainer";

  public Trainer(String id) {
    _id = id;
  }

  public String id() {
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
    p.setTrainerName(name());
    return party().add(p);
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    node.setAttribute("name", _name);
    node.setAttribute("use_gym_xp_factor", _useGymXPFactor);
    node.addChild(_party.toXML());

    return node;
  }

  public void loadXML(XMLNode node) {
    _name = node.getAttribute("name");
    _useGymXPFactor = node.getBoolAttribute("use_gym_xp_factor");

    _party.loadXML(node.getChildren(PokemonStorageUnit.XML_NODE_NAME).get(0));
  }

  public boolean equals(Object o) {
    if (!(o instanceof Trainer))
      return false;
    return ((Trainer) o)._id == _id;
  }

  public int hashCode() {
    return _id.hashCode();
  }

  private String _name = null, _id = null;
  private boolean _useGymXPFactor = false;
  private PokemonStorageUnit _party = new PokemonStorageUnit(JPokemonConstants.TRAINER_PARTY_SIZE);
}