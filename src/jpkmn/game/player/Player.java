package jpkmn.game.player;

import java.util.Scanner;

import jpkmn.exceptions.LoadException;
import jpkmn.game.item.Bag;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.service.GraphicsHandler;

import org.jpokemon.player.Progress;
import org.jpokemon.pokedex.Pokedex;
import org.jpokemon.pokedex.PokedexStatus;
import org.jpokemon.pokemon.storage.PokemonStorageBlock;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class Player implements PokemonTrainer {
  public final Bag bag;

  public Player() {
    super();

    _id = PLAYER_COUNT++;
    _area = 1;

    bag = new Bag();
    _pokedex = new Pokedex();
    _progress = new Progress();
    _graphics = new GraphicsHandler(this);
    _storage = new PokemonStorageBlock(this);
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

  public TrainerType type() {
    return TrainerType.PLAYER;
  }

  public PokemonStorageUnit party() {
    return _storage.get(0);
  }

  public boolean add(Pokemon p) {
    putPokedex(p.number(), PokedexStatus.OWN);

    for (PokemonStorageUnit unit : _storage) {
      if (unit.add(p))
        return true;
    }

    return false;
  }

  public void notify(String... message) {
    _graphics.notify(message);
  }

  public void setState(String state, int... battleInfo) {
    if (state.equalsIgnoreCase("world"))
      _graphics.showWorld();
    else if (state.equalsIgnoreCase("battle"))
      _graphics.showBattle(battleInfo[0], battleInfo[1]);
  }

  public PokedexStatus getPokedex(int id) {
    return _pokedex.status(id);
  }

  public void putPokedex(int id, PokedexStatus status) {
    if (status == PokedexStatus.SAW)
      _pokedex.saw(id);
    else if (status == PokedexStatus.OWN)
      _pokedex.own(id);
  }

  public boolean getEvent(int id) {
    return _progress.get(id);
  }

  public void putEvent(int id) {
    _progress.put(id);
  }

  public JSONObject toJSONObject() {
    JSONObject data = new JSONObject();

    try {
      data.put("name", name());
      data.put("cash", cash());
      data.put("badges", badge());
      data.put("area", area());
      data.put("bag", bag.toJSONArray());
      data.put("pokedex", _pokedex.toJSONObject());
      data.put("progress", _progress.toJSONArray());
      data.put("pokemon", _storage.toJSONObject());

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
      bag.load(scan.nextLine());

      // Load pokedex
      _pokedex.load(scan.nextLine());

      // Load progress
      _progress.load(scan.nextLine());

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
    if (!(o instanceof Player))
      return false;
    return ((Player) o)._id == _id;
  }

  @Override
  public int hashCode() {
    return _id;
  }

  private String _name;
  private Pokedex _pokedex;
  private Progress _progress;
  private GraphicsHandler _graphics;
  private PokemonStorageBlock _storage;
  private int _id, _area, _badge, _cash;

  private static int PLAYER_COUNT = 1;
}