package org.jpokemon.action;

import org.jpokemon.action.performer.ActionPerformer;

public interface Action {
  public ActionPerformer getPerformer();

  public int getType();

  public void setType(int t);

  public void commitTypeChange(int t);

  public String getData();

  public void setData(String d);

  public void commitDataChange(String newData);
}