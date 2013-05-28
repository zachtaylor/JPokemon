package org.jpokemon.map.gps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.map.WildPokemon;
import org.jpokemon.map.npc.NPC;
import org.jpokemon.pokemon.Pokemon;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.AutoIncrement;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class Area {
  public static Area createNew() {
    Area area = new Area();
    area.setName("undefined");

    try {
      SqlStatement.insert(area).execute();
    } catch (DataConnectionException e) {
      area = null;
      e.printStackTrace();
    }

    return area;
  }

  public static Area get(int number) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      List<Area> query = SqlStatement.select(Area.class).where("number").eq(number).getList();

      if (query.size() > 0)
        return query.get(0);
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  public void commit() {
    try {
      SqlStatement.update(this).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

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

  public List<WildPokemon> wildPokemon() {
    return Collections.unmodifiableList(_pokemon);
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
    return Collections.unmodifiableList(_npcs);
  }

  public NPC getNpc(int number) {
    for (NPC npc : npcs()) {
      if (npc.getNumber() == number)
        return npc;
    }

    return null;
  }

  public void addNPC(NPC npc) {
    _npcs.add(npc);
  }

  public void removeNPC(int index) {
    _npcs.remove(index);
  }

  public List<Border> borders() {
    return Collections.unmodifiableList(_borders);
  }

  public Border getBorder(String name) {
    for (Border border : _borders) {
      if (name.equals(Map.area(border.getNext()).getName())) {
        return border;
      }
    }

    return null;
  }

  public void addBorder(Border b) {
    if (b == null || _borders.contains(b))
      return;

    _borders.add(b);
  }

  public void removeBorder(int index) {
    _borders.remove(index);
  }

  public String toString() {
    return "Area#" + getNumber() + ": " + getName();
  }

  public boolean equals(Object o) {
    if (o instanceof Area)
      return ((Area) o).number == number;

    return false;
  }

  public int hashCode() {
    return number;
  }

  @PrimaryKey
  @AutoIncrement
  private int number;
  private String name;
  private List<NPC> _npcs = new ArrayList<NPC>();
  private List<Border> _borders = new ArrayList<Border>();
  private List<WildPokemon> _pokemon = new ArrayList<WildPokemon>();
}