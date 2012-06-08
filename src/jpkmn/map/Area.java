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
  public Area(String name) {
    _name = name;
    _neighbors = new ArrayList<AreaConnection>();
    _buildings = new ArrayList<Building>();
  }

  public String getName() {
    return _name;
  }

  public List<Building> getBuildings() {
    return _buildings;
  }

  public void addBuilding(Building b) {
    if (!_buildings.contains(b)) _buildings.add(b);
  }

  public void setWater(Water w) {
    _water = w;
  }

  public Pokemon useWater() {
    return _water == null ? null : _water.getRandomPokemon();
  }

  public void connect(String area) {
    // TODO : I don't know how to do this.
  }
  
  public void addRivalBattle(int num) {
    _rivalBattle = num;
  }
  
  public boolean hasRivalBattle() {
    return _rivalBattle == 0;
  }
  
  public void battleRival() {
    // TODO: Implement rival battles
  }
  
  protected String _name;
  protected List<AreaConnection> _neighbors;
  protected List<Building> _buildings;
  protected Water _water;
  protected int _rivalBattle = 0;
}