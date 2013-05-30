package org.jpokemon.map.gps;

import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.action.Action;
import org.jpokemon.action.ActionSet;
import org.jpokemon.action.Requirement;
import org.jpokemon.manager.LoadException;
import org.jpokemon.trainer.Player;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class Border {
  public int getArea() {
    return area;
  }

  public void setArea(int a) {
    area = a;
  }

  public int getNext() {
    return next;
  }

  public void setNext(int n) {
    next = n;
  }

  public void addAction(Action action) {
    actionSet.addAction(action);
  }

  public void addRequirement(Requirement requirement) {
    actionSet.addRequirement(requirement);
  }

  public boolean performAction(Player player) throws LoadException {
    if (isOkay(player)) {
      actionSet.execute(player);
      return true;
    }

    return false;
  }

  public boolean isOkay(Player player) {
    return actionSet.isOkay(player);
  }

  public static List<Border> get(int area) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(Border.class).where("area").eq(area).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  private int area, next;
  private ActionSet actionSet = new ActionSet();
}