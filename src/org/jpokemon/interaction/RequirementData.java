package org.jpokemon.interaction;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.AutoIncrement;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class RequirementData {
  @PrimaryKey
  @AutoIncrement
  private int id;
  private int actionsetId;
  private String requirement, options;

  public static List<RequirementData> get(int actionsetId) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    try {
      return SqlStatement.select(RequirementData.class).where("actionsetid").eq(actionsetId).getList();
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<RequirementData>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getActionsetId() {
    return actionsetId;
  }

  public void setActionsetId(int actionsetId) {
    this.actionsetId = actionsetId;
  }

  public String getRequirement() {
    return requirement;
  }

  public void setRequirement(String requirement) {
    this.requirement = requirement;
  }

  public String getOptions() {
    return options;
  }

  public void setOptions(String options) {
    this.options = options;
  }
}