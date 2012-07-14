package jpkmn.map;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.pokemon.Pokemon;

/**
 * Represents a game area where the player can "be." Areas can contain
 * buildings, and may have water applied to them. Water usage is always
 * optional for the player.
 * 
 * @author Zach
 */
public abstract class Area {
  public Area() {
    _buildings = new ArrayList<Building>();
  }

  public void name(String s) {
    _name = s;
  }

  public String name() {
    return _name;
  }

  public List<Building> buildings() {
    return _buildings;
  }

  public void buildings(Building b) {
    if (!_buildings.contains(b)) _buildings.add(b);
  }

  public void water(Water w) {
    _water = w;
  }

  public Pokemon water() {
    return _water == null ? null : _water.species();
  }

  public void rivalBattle(int num) {
    _rivalBattle = num;
  }

  public int rivalBattle() {
    return _rivalBattle;
  }

  protected String _name;
  protected List<Building> _buildings;
  protected Water _water;
  protected int _rivalBattle = 0;
}