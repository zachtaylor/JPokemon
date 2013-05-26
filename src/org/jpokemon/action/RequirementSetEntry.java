package org.jpokemon.action;

public interface RequirementSetEntry {
  public int getType();

  public void setType(int t);

  public void commitTypeChange(int newType);

  public int getData();

  public void setData(int d);

  public void commitDataChange(int newData);
}