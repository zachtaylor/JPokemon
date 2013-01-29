package org.jpokemon.trainer;

import java.util.Scanner;

import jpkmn.exceptions.LoadException;
import jpkmn.exe.gui.GameWindow;
import jpkmn.game.item.Bag;
import jpkmn.game.item.Item;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.pokedex.Pokedex;
import org.jpokemon.pokemon.storage.PokemonStorageBlock;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class Player implements PokemonTrainer {
  public Player() {
    _id = PLAYER_COUNT++;
    _area = 1;

    _bag = new Bag();
    _pokedex = new Pokedex();
    _progress = new Progress();
    _trainers = new Progress();
    _gameWindow = new GameWindow(_id);
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

    for (PokemonStorageUnit unit : _storage)
      if (unit.add(p)) {
    	p.caughtByAt(name(), area());
    	return true;
      }

    return false;
  }

  public void notify(String... message) {
    _gameWindow.inbox().addMessage(message);
  }

  public void setState(String state) {
    if (state.equalsIgnoreCase("world"))
      _gameWindow.showMain();
    else if (state.equalsIgnoreCase("battle")) _gameWindow.showBattle();
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

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();

    try {
      data.put("id", id());
      data.put("name", name());
      data.put("cash", cash());
      data.put("badges", badge());
      data.put("area", area());
      data.put("bag", _bag.toJSON());
      data.put("pokedex", _pokedex.toJSON());
      data.put("progress", _progress.toJSON());
      data.put("pokemon", _storage.toJSON());

    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  public void load(Scanner scan) throws LoadException {
    try {
      name(scan.nextLine());
      cash(scan.nextInt());
      badge(scan.nextInt());
      area(Integer.parseInt(scan.nextLine().trim()));

      // Load bag
      _bag.load(scan.nextLine());

      // Load pokedex
      _pokedex.load(scan.nextLine());

      // Load progress
      _progress.loadJSON(null); // BROKEN
      scan.nextLine(); // BROKEN

      // load pcstorage
      _storage.load(scan);
    } catch (LoadException le) {
      throw le;
    } catch (Exception e) {
      throw new LoadException("Player could not load ");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Player)) return false;
    return ((Player) o)._id == _id;
  }

  @Override
  public int hashCode() {
    return _id;
  }

  private Bag _bag;
  private String _name;
  private Pokedex _pokedex;
  private GameWindow _gameWindow;
  private PokemonStorageBlock _storage;
  private Progress _progress, _trainers;
  private int _id, _area, _badge, _cash;

  private static int PLAYER_COUNT = 1;
}