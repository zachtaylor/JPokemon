package org.jpokemon.action;

public abstract class SqliteActionBinding {
  public Action getAction() {
    switch (ActionType.valueOf(getType())) {
    case SPEECH:
      return new SpeechAction(getData());
    case EVENT:
      return new EventAction(getData());
    case ITEM:
      return new ItemAction(getData());
    case POKEMON:
      return new PokemonAction();
    case BATTLE:
      return new BattleAction(getData());
    case UPGRADE:
      return new UpgradeAction();
    case STORE:
      return new StoreAction(getData());
    case HEAL:
      return new HealAction();
    }

    return null;
  }

  public abstract String getType();

  public abstract void setType(String t);

  public abstract void commitTypeChange(String t);

  public abstract String getData();

  public abstract void setData(String d);

  public abstract void commitDataChange(String newData);
}