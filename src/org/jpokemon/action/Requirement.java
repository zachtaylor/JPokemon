package org.jpokemon.action;

import org.jpokemon.trainer.Player;

public interface Requirement {
  public boolean isOkay(Player player);

  public int getType();

  public void setType(int t);

  public void commitTypeChange(int newType);

  public int getData();

  public void setData(int d);

  public void commitDataChange(int newData);
}