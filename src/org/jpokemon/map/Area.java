package org.jpokemon.map;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.map.npc.NPC;
import org.jpokemon.pokemon.Pokemon;

public class Area {
  public Area(int id) {
    _id = id;

    _info = AreaInfo.get(id);
    _pokemon = WildPokemon.get(id);
  }

  public String name() {
    return _info.getName();
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

  public List<NPC> npcs() {
    return _npcs;
  }

  public void addNPC(NPC npc) {
    _npcs.add(npc);
  }

  public void removeNPC(NPC npc) {
    _npcs.remove(npc);
  }

  public List<Border> borders() {
    return _borders;
  }

  public void addBorder(Border b) {
    if (b == null || _borders.contains(b))
      return;

    _borders.add(b);
  }

  public void removeBorder(Border b) {
    if (b == null || !_borders.contains(b))
      return;

    _borders.remove(b);
  }

  public boolean equals(Object o) {
    if (o instanceof Area)
      return ((Area) o)._id == _id;

    return false;
  }

  public int hashCode() {
    return _id;
  }

  private int _id;
  private AreaInfo _info;
  private List<NPC> _npcs;
  private List<WildPokemon> _pokemon;
  private List<Border> _borders = new ArrayList<Border>();
}