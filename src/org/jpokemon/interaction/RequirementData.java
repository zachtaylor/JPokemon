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
  private int id = -1;
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

  public RequirementData commit() {
    try {
      if (id < 0) {
        SqlStatement.insert(this).execute();
      }
      else {
        SqlStatement.update(this).where("id").eq(id).execute();
      }
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return this;
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

  public RequirementData setActionsetId(int actionsetId) {
    this.actionsetId = actionsetId;
    return this;
  }

  public String getRequirement() {
    return requirement;
  }

  public RequirementData setRequirement(String requirement) {
    this.requirement = requirement;
    return this;
  }

  public String getOptions() {
    return options;
  }

  public void setOptions(String options) {
    this.options = options;
  }
}