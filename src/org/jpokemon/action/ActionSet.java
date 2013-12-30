package org.jpokemon.action;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.action.requirement.Requirement;
import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.AutoIncrement;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class ActionSet {
  @PrimaryKey
  @AutoIncrement
  private int id = -1;
  private String context, name, option;
  private List<Action> _actions = new ArrayList<Action>();
  private List<Requirement> _requirements = new ArrayList<Requirement>();

  public static List<ActionSet> get(String context, String entity) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    try {
      List<ActionSet> actionSets = SqlStatement.select(ActionSet.class).where("context").eq(context).and("name").eq(entity).getList();

      if (actionSets != null && !actionSets.isEmpty()) {
        // TODO - fill actions and requirements
      }

      return actionSets;
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<ActionSet>();
  }

  public String getContext() {
    return context;
  }

  public void setContext(String s) {
    context = s;
  }

  public String getName() {
    return name;
  }

  public void setName(String s) {
    name = s;
  }

  public String getOption() {
    return option;
  }

  public void setOption(String o) {
    option = o;
  }

  public List<Action> actions() {
    return _actions;
  }

  public List<Requirement> requirements() {
    return _requirements;
  }
}