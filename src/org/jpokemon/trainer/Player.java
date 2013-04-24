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

import com.zachtaylor.jnodalxml.XMLNode;

public class Player implements PokemonTrainer {
  public static final String XML_NODE_NAME = "player";

  public Player() {
    _id = PLAYER_COUNT++;
    _area = 1;

    _bag = new Bag();
    _pokedex = new Pokedex();
    _progress = new Progress();
    _trainers = new Progress();
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

  @Override
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

  public TrainerType type() {
    return TrainerType.PLAYER;
  }

  public double xpFactor() {
    return 0;
  }

  public PokemonStorageUnit party() {
    return _storage.get(0);
  }

  public boolean add(Pokemon p) {
    _pokedex.own(p.number());

    for (PokemonStorageUnit unit : _storage) {
      if (unit.add(p)) {
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

  public Progress events() {
    return _progress;
  }

  public Progress trainers() {
    return _trainers;
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
    node.setAttribute("cash", _cash + "");
    node.setAttribute("badge", _badge + "");
    node.setAttribute("area", _area + "");

    node.addChild(_bag.toXML());
    node.addChild(_pokedex.toXML());
    node.addChild(_storage.toXML());

    node.addChild(_progress.toXML().setAttribute("group", "events"));
    node.addChild(_trainers.toXML().setAttribute("group", "trainers"));

    return node;
  }

  public void loadXML(XMLNode node) {
    _name = node.getAttribute("name");
    _cash = Integer.parseInt(node.getAttribute("cash"));
    _badge = Integer.parseInt(node.getAttribute("badge"));
    _area = Integer.parseInt(node.getAttribute("area"));

    _bag.loadXML(node.getChildren(Bag.XML_NODE_NAME).get(0));
    _pokedex.loadXML(node.getChildren(Pokedex.XML_NODE_NAME).get(0));
    _storage.loadXML(node.getChildren(PokemonStorageBlock.XML_NODE_NAME).get(0));

    for (XMLNode progressNode : node.getChildren(Progress.XML_NODE_NAME)) {
      if (progressNode.getAttribute("group").equals("events"))
        _progress.loadXML(progressNode);
      else if (progressNode.getAttribute("group").equals("trainers"))
        _trainers.loadXML(progressNode);
    }
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
  private Pokedex _pokedex;
  private TrainerState _state;
  private PokemonStorageBlock _storage;
  private Progress _progress, _trainers;
  private int _id, _area, _badge, _cash;

  private static int PLAYER_COUNT = 1;
}