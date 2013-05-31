package org.jpokemon.action;

import org.jpokemon.action.performer.ActionPerformer;

public interface Action {
  public ActionPerformer getPerformer();

  public String getType();

  public void setType(String t);

  public void commitTypeChange(String t);

  public String getData();

  public void setData(String d);

  public void commitDataChange(String newData);
}