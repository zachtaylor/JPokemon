package org.jpokemon.trainer;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.zachtaylor.jnodalxml.XmlNode;

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

  public boolean isGym() {
    return _isGym;
  }

  public void setGym(boolean isGym) {
    _isGym = isGym;
  }

  public PokemonStorageUnit party() {
    return _party;
  }

  public boolean add(Pokemon p) {
    p.setTrainerName(name());
    return party().add(p);
  }

  public XmlNode toXml() {
    XmlNode node = new XmlNode(XML_NODE_NAME);

    node.setAttribute("name", _name);
    node.setAttribute("gym", _isGym);
    node.addChild(_party.toXml());

    return node;
  }

  public void loadXml(XmlNode node) {
    _name = node.getAttribute("name");
    _isGym = node.getBoolAttribute("gym");

    _party.loadXml(node.getChildren(PokemonStorageUnit.XML_NODE_NAME).get(0));
  }

  public boolean equals(Object o) {
    if (!(o instanceof Trainer))
      return false;
    return ((Trainer) o)._id == _id;
  }

  public int hashCode() {
    return _id.hashCode();
  }

  private boolean _isGym = false;
  private String _name = null, _id = null;
  private PokemonStorageUnit _party = new PokemonStorageUnit(JPokemonConstants.TRAINER_PARTY_SIZE);
}