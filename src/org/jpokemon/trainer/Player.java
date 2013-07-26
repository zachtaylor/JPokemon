package org.jpokemon.trainer;

import org.jpokemon.item.Bag;
import org.jpokemon.item.Item;
import org.jpokemon.pokedex.Pokedex;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageBlock;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.zachtaylor.jnodalxml.XmlNode;

public class Player implements PokemonTrainer {
  public static final String XML_NODE_NAME = "player";

  public Player(String id) {
    _id = id;
    _area = 1;

    _bag = new Bag();
    _record = new Record(this);
    _pokedex = new Pokedex();
    _storage = new PokemonStorageBlock();
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

  public int area() {
    return _area;
  }

  public void area(int area) {
    _area = area;
  }

  public int badge() {
    return _badge;
  }

  public void badge(int badge) {
    _badge = badge;
  }

  public int cash() {
    return _cash;
  }

  public void cash(int cash) {
    _cash = cash;
  }

  public Bag bag() {
    return _bag;
  }

  public Item item(int itemID) {
    return _bag.get(itemID);
  }

  public PokemonStorageUnit party() {
    return _storage.get(0);
  }

  public PokemonStorageBlock getAllPokemon() {
    return _storage;
  }

  public boolean add(Pokemon p) {
    if (_record.getStarterPokemon() == null) {
      _record.setStarterPokemon(p.name());
    }

    for (PokemonStorageUnit unit : _storage) {
      if (unit.add(p)) {
        _pokedex.own(p.number());
        p.setTrainerName(name());
        return true;
      }
    }

    return false;
  }

  public Pokedex pokedex() {
    return _pokedex;
  }

  public Record record() {
    return _record;
  }

  public XmlNode toXml() {
    XmlNode node = new XmlNode(XML_NODE_NAME);

    node.setAttribute("name", _name);
    node.setAttribute("cash", _cash);
    node.setAttribute("badge", _badge);
    node.setAttribute("area", _area);

    node.addChild(_bag.toXml());
    node.addChild(_record.toXml());
    node.addChild(_pokedex.toXml());
    node.addChild(_storage.toXml());

    return node;
  }

  public void loadXML(XmlNode node) {
    _name = node.getAttribute("name");
    _cash = node.getIntAttribute("cash");
    _badge = node.getIntAttribute("badge");
    _area = node.getIntAttribute("area");

    _bag.loadXml(node.getChildren(Bag.XML_NODE_NAME).get(0));
    _record.loadXml(node.getChildren(Record.XML_NODE_NAME).get(0));
    _pokedex.loadXml(node.getChildren(Pokedex.XML_NODE_NAME).get(0));
    _storage.loadXml(node.getChildren(PokemonStorageBlock.XML_NODE_NAME).get(0));
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Player))
      return false;
    return ((Player) o)._id == _id;
  }

  @Override
  public int hashCode() {
    return _id.hashCode();
  }

  private Bag _bag;
  private Record _record;
  private Pokedex _pokedex;
  private String _name, _id;
  private int _area, _badge, _cash;
  private PokemonStorageBlock _storage;
}