package org.jpokemon.interaction;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.AutoIncrement;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class ActionData {
  @PrimaryKey
  @AutoIncrement
  private int id;
  private int actionsetId;
  private String action, options;

  public static List<ActionData> get(int actionsetId) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    try {
      return SqlStatement.select(ActionData.class).where("actionsetid").eq(actionsetId).getList();
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<ActionData>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getActionsetid() {
    return actionsetId;
  }

  public void setActionsetid(int actionsetid) {
    this.actionsetId = actionsetid;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getOptions() {
    return options;
  }

  public void setOptions(String options) {
    this.options = options;
  }
}