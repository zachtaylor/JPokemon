package org.jpokemon.trainer;

import org.jpokemon.item.Bag;
import org.jpokemon.item.Item;
import org.jpokemon.pokedex.Pokedex;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageBlock;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.jpokemon.service.PlayerService;
import org.json.JSONException;
import org.json.JSONObject;
import org.zachtaylor.jnodalxml.XMLNode;

public class Player implements PokemonTrainer {
  public static final String XML_NODE_NAME = "player";

  public Player() {
    _id = PLAYER_COUNT++;
    _area = 1;

    _bag = new Bag();
    _record = new Record();
    _pokedex = new Pokedex();
    _storage = new PokemonStorageBlock();
  }

  public int id() {
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

  public void state(TrainerState state) {
    _state = state;
  }

  public TrainerState state() {
    return _state;
  }

  public Item item(int itemID) {
    return _bag.get(itemID);
  }

  public double xpFactor() {
    return 0;
  }

  public PokemonStorageUnit party() {
    return _storage.get(0);
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

  public void notify(String... messages) {
    for (String message : messages) {
      PlayerService.addToMessageQueue(this, message);
    }
  }

  public Pokedex pokedex() {
    return _pokedex;
  }

  public Record record() {
    return _record;
  }

  public JSONObject toJSON(TrainerState state) {
    JSONObject data = new JSONObject();

    try {
      if (state == null) {
        data.put("id", id());
        data.put("name", name());
      }
      else if (state == TrainerState.BATTLE) {
        data.put("id", id());
        data.put("leader", party().get(0).toJSON(state));
        data.put("party", party().toJSON(state));
        data.put("bag", _bag.toJSON());
      }
      else if (state == TrainerState.UPGRADE) {
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

    node.setAttribute("name", _name);
    node.setAttribute("cash", _cash);
    node.setAttribute("badge", _badge);
    node.setAttribute("area", _area);

    node.addChild(_bag.toXML());
    node.addChild(_record.toXML());
    node.addChild(_pokedex.toXML());
    node.addChild(_storage.toXML());

    return node;
  }

  public void loadXML(XMLNode node) {
    _name = node.getAttribute("name");
    _cash = node.getIntAttribute("cash");
    _badge = node.getIntAttribute("badge");
    _area = node.getIntAttribute("area");

    _bag.loadXML(node.getChildren(Bag.XML_NODE_NAME).get(0));
    _record.loadXML(node.getChildren(Record.XML_NODE_NAME).get(0));
    _pokedex.loadXML(node.getChildren(Pokedex.XML_NODE_NAME).get(0));
    _storage.loadXML(node.getChildren(PokemonStorageBlock.XML_NODE_NAME).get(0));
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Player))
      return false;
    return ((Player) o)._id == _id;
  }

  @Override
  public int hashCode() {
    return _id;
  }

  private Bag _bag;
  private String _name;
  private Record _record;
  private Pokedex _pokedex;
  private TrainerState _state;
  private PokemonStorageBlock _storage;
  private int _id, _area, _badge, _cash;

  private static int PLAYER_COUNT = 1;
}