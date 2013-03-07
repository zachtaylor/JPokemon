package org.jpokemon.map;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.map.npc.NPC;
import org.jpokemon.pokemon.Pokemon;

public class Area {
  public int getArea() {
    return area;
  }

  public void setArea(int a) {
    area = a;
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

  public boolean equals(Object o) {
    if (o instanceof Area)
      return ((Area) o).area == area;

    return false;
  }

  public int hashCode() {
    return area;
  }

  private int area;
  private String name;
  private List<NPC> _npcs = new ArrayList<NPC>();
  private List<Border> _borders = new ArrayList<Border>();
  private List<WildPokemon> _pokemon = new ArrayList<WildPokemon>();
}