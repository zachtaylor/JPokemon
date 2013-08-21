package org.jpokemon.overworld.npc;

import java.util.List;

import org.jpokemon.overworld.Entity;
import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class Npc extends Entity {
  private String id, type;

  public Npc() {
    setSolid(true);
  }

  public String getId() {
    return id;
  }

  public void setId(String i) {
    id = i;
  }

  public String getType() {
    return type;
  }

  public void setType(String t) {
    type = t;
  }

  public static Npc createNew() {
    Npc npc = new Npc();
    npc.setName("undefined");
    npc.setType("undefined");

    try {
      SqlStatement.insert(npc).execute();
    }
    catch (DataConnectionException e) {
      npc = null;
      e.printStackTrace();
    }

    return npc;
  }

  public static Npc get(String id) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    try {
      List<Npc> results = SqlStatement.select(Npc.class).where("id").eq(id).getList();
      if (results.size() > 0) { return results.get(0); }
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  public void commit() {
    try {
      SqlStatement.update(this).execute();
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  public String toString() {
    return "Npc:" + name;
  }
}