package org.jpokemon.map;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.map.npc.NPC;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Area {
  public int getNumber() {
    return number;
  }

  public void setNumber(int n) {
    number = n;
  }

  public String getName() {
    return name;
  }

  public void setName(String n) {
    name = n;
  }

  public Pokemon pokemon() {
    int totalFlex = 0;

    for (WildPokemon p : _pokemon)
      totalFlex += p.getFlex();

    totalFlex = (int) (totalFlex * Math.random());

    for (WildPokemon p : _pokemon) {
      if (totalFlex < p.getFlex())
        return p.instantiate();
      else
        totalFlex -= p.getFlex();
    }

    return null;
  }

  public void addPokemon(WildPokemon wp) {
    _pokemon.add(wp);
  }

  public List<NPC> npcs() {
    return _npcs;
  }

  public void addNPC(NPC npc) {
    _npcs.add(npc);
  }

  public void removeNPC(int index) {
    _npcs.remove(index);
  }

  public List<Border> borders() {
    return _borders;
  }

  public void addBorder(Border b) {
    if (b == null || _borders.contains(b))
      return;

    _borders.add(b);
  }

  public void removeBorder(int index) {
    _borders.remove(index);
  }

  public JSONObject toJSON(Player p) {
    JSONObject json = new JSONObject();

    try {
      json.put("name", getName());

      JSONArray npcs = new JSONArray();
      for (NPC npc : npcs()) {
        npcs.put(npc.toJSON(p));
      }
      json.put("npcs", npcs);

      JSONArray borders = new JSONArray();
      for (Border b : borders()) {
        borders.put(b.toJSON(p));
      }
      json.put("borders", borders);

    } catch (JSONException e) {
      e.printStackTrace();
    }

    return json;
  }

  public boolean equals(Object o) {
    if (o instanceof Area)
      return ((Area) o).number == number;

    return false;
  }

  public int hashCode() {
    return number;
  }

  private int number;
  private String name;
  private List<NPC> _npcs = new ArrayList<NPC>();
  private List<Border> _borders = new ArrayList<Border>();
  private List<WildPokemon> _pokemon = new ArrayList<WildPokemon>();
}