package org.jpokemon.action;

public interface ActionSetEntry {
  public int getType();

  public void setType(int t);

  public void commitTypeChange(int t);

  public String getData();

  public void setData(String d);

  public void commitDataChange(String newData);
}