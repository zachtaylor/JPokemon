package org.jpokemon.action.performer;

public abstract class AbstractActionPerformer implements ActionPerformer {
  public AbstractActionPerformer(String s) {
    data = s;
  }

  protected String getData() {
    return data;
  }

  private String data;
}