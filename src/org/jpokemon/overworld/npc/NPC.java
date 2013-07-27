package org.jpokemon.overworld.npc;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.action.ActionSet;
import org.jpokemon.server.JPokemonServer;
import org.jpokemon.trainer.Player;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.AutoIncrement;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class NPC {
  public static NPC createNew() {
    NPC npc = new NPC();
    npc.setType(0);
    npc.setName("undefined");

    try {
      SqlStatement.insert(npc).execute();
    } catch (DataConnectionException e) {
      npc = null;
      e.printStackTrace();
    }

    return npc;
  }

  public static NPC get(int number) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    try {
      List<NPC> query = SqlStatement.select(NPC.class).where("number").eq(number).getList();

      if (query.size() > 0) {
        return query.get(0);
      }
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

  public NPCType getNPCType() {
    return npctype;
  }

  public int getType() {
    return type;
  }

  public void setType(int t) {
    type = t;
    npctype = NPCType.get(t);
  }

  public String getNameFormatted() {
    return name.replace("{typename}", getNPCType().getName());
  }

  public String getName() {
    return name;
  }

  public void setName(String n) {
    name = n;
  }

  public String getIcon() {
    return getNPCType().getIcon();
  }

  public void addActionSet(ActionSet actionset) {
    _actions.add(actionset);
  }

  public ActionSet actionset(String option) {
    for (ActionSet as : _actions) {
      if (option.equals(as.getOption())) {
        return as;
      }
    }

    return null;
  }

  public List<String> getOptionsForPlayer(Player player) {
    List<String> options = new ArrayList<String>();

    for (ActionSet actionset : _actions) {
      if (actionset.isOkay(player)) {
        options.add(actionset.getOption());
      }
    }

    return options;
  }

  public String toString() {
    return "NPC#" + number + ": " + getNameFormatted();
  }

  @PrimaryKey
  @AutoIncrement
  private int number;

  private int type;
  private String name;
  private NPCType npctype;
  private List<ActionSet> _actions = new ArrayList<ActionSet>();
}