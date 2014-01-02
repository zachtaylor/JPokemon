package org.jpokemon.interaction;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.server.JPokemonServer;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;

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

  public static List<ActionSet> get(String name) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    try {
      List<ActionSet> actionSets = SqlStatement.select(ActionSet.class).where("name").eq(name).getList();

      if (actionSets != null && !actionSets.isEmpty()) {
        for (ActionSet actionSet : actionSets) {
          fillActionSet(actionSet);
        }
      }

      return actionSets;
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<ActionSet>();
  }

  public static List<ActionSet> get(String context, String name) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    try {
      List<ActionSet> actionSets = SqlStatement.select(ActionSet.class).where("context").eq(context).and("name").eq(name).getList();

      if (actionSets != null && !actionSets.isEmpty()) {
        for (ActionSet actionSet : actionSets) {
          fillActionSet(actionSet);
        }
      }

      return actionSets;
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<ActionSet>();
  }

  private static void fillActionSet(ActionSet actionSet) {
    for (ActionData actionData : ActionData.get(actionSet.id)) {
      Action action = ActionFactoryRegistry.get(actionData.getAction(), actionData.getOptions());
      actionSet.addAction(action);
    }

    for (RequirementData requirementData : RequirementData.get(actionSet.id)) {
      Requirement requirement = RequirementFactoryRegistry.get(requirementData.getRequirement(), requirementData.getOptions());
      actionSet.addRequirement(requirement);
    }
  }

  public int getId() {
    return id;
  }

  public ActionSet setId(int id) {
    this.id = id;
    return this;
  }

  public void commit() {
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
  }

  public String getContext() {
    return context;
  }

  public ActionSet setContext(String s) {
    context = s;
    return this;
  }

  public String getName() {
    return name;
  }

  public ActionSet setName(String s) {
    name = s;
    return this;
  }

  public String getOption() {
    return option;
  }

  public void setOption(String o) {
    option = o;
  }

  public void execute(Player player) throws ServiceException {
    if (!meetsRequirements(player)) {
      throw new ServiceException("Requirements not satisfied for actionset#" + id + " and " + player.toString());
    }

    for (Action action : _actions) {
      action.execute(player);
    }
  }

  public boolean meetsRequirements(Player player) {
    for (Requirement requirement : _requirements) {
      if (!requirement.isSatisfied(player)) {
        return false;
      }
    }

    return true;
  }

  public void addAction(Action action) {
    _actions.add(action);
  }

  public void addRequirement(Requirement requirement) {
    _requirements.add(requirement);
  }
}