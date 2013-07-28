package org.jpokemon.trainer;

import java.util.Collections;
import java.util.List;

import org.jpokemon.item.Bag;
import org.jpokemon.item.Item;
import org.jpokemon.pokedex.Pokedex;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageBlock;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.zachtaylor.jnodalxml.XmlNode;

public class Player implements PokemonTrainer {
  public static final String XML_NODE_NAME = "player";

  private Bag _bag = new Bag();
  private String _name, _id, _avatar;
  private Pokedex _pokedex = new Pokedex();
  private Record _record = new Record(this);
  private int _area = 1, _badge, _cash, _x, _y, _xp, _level;
  private List<String> _unlockedAvatars, _friends, _blocked;
  private PokemonStorageBlock _storage = new PokemonStorageBlock();

  public Player(String id) {
    _id = id;
  }

  public String id() {
    return _id;
  }

  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
  }

  public int getCash() {
    return _cash;
  }

  public void setCash(int cash) {
    _cash = cash;
  }

  public int getExperience() {
    return _xp;
  }

  public void setExperience(int xp) {
    _xp = xp;
  }

  public String getAvatar() {
    return _avatar;
  }

  public void setAvatar(String name) {
    if (!_unlockedAvatars.contains(name)) {
      return;
    }
    _avatar = name;
  }

  public List<String> getAvatars() {
    return Collections.unmodifiableList(_unlockedAvatars);
  }

  public void addAvatar(String name) {
    _unlockedAvatars.add(name);
  }

  public void removeAvatar(String name) {
    _unlockedAvatars.remove(name);
  }

  public int getLevel() {
    return _level;
  }

  public void setLevel(int level) {
    _level = level;
  }

  public int getArea() {
    return _area;
  }

  public void setArea(int area) {
    _area = area;
  }

  public int getX() {
    return _x;
  }

  public void setX(int x) {
    _x = x;
  }

  public int getY() {
    return _y;
  }

  public void setY(int y) {
    _y = y;
  }

  public List<String> getFriends() {
    return Collections.unmodifiableList(_friends);
  }

  public void addFriend(String name) {
    _friends.add(name);
    removeBlocked(name);
  }

  public void removeFriend(String name) {
    _friends.remove(name);
  }

  public List<String> getBlocked() {
    return Collections.unmodifiableList(_blocked);
  }

  public void addBlocked(String name) {
    _blocked.add(name);
    removeFriend(name);
  }

  public void removeBlocked(String name) {
    _blocked.remove(name);
  }

  public int getBadgeCount() {
    return _badge;
  }

  public void setBadgeCount(int badge) {
    _badge = badge;
  }

  public Bag getBag() {
    return _bag;
  }

  public Item item(int itemID) {
    return _bag.get(itemID);
  }

  public PokemonStorageUnit party() {
    return box(0);
  }

  public PokemonStorageUnit box(int box) {
    return _storage.get(box);
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
        p.setTrainerName(getName());
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

}