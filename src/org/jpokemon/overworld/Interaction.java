package org.jpokemon.overworld;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class Interaction {
  private String map, name, action, dataref;
  private int actiongroup;

  public static List<Interaction> get(String map, String name) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    try {
      return SqlStatement.select(Interaction.class).where("map").eq(map).and("name").eq(name).getList();

    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<Interaction>();
  }

  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    this.map = map;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getActiongroup() {
    return actiongroup;
  }

  public void setActiongroup(int actiongroup) {
    this.actiongroup = actiongroup;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getDataref() {
    return dataref;
  }

  public void setDataref(String dataref) {
    this.dataref = dataref;
  }
}