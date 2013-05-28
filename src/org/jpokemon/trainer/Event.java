package org.jpokemon.trainer;

import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.AutoIncrement;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class Event {
  @PrimaryKey
  @AutoIncrement
  private int number;
  private String description;

  public static Event createNew() {
    Event event = new Event();
    event.setDescription("undefined");

    try {
      SqlStatement.insert(event).execute();
    } catch (DataConnectionException e) {
      event = null;
      e.printStackTrace();
    }

    return event;
  }

  public static Event get(int number) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    Event event = null;

    try {
      List<Event> query = SqlStatement.select(Event.class).where("number").eq(number).getList();

      if (!query.isEmpty()) {
        event = query.get(0);
      }
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return event;
  }

  public void commit() {
    try {
      SqlStatement.update(this).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  public String toString() {
    return "Event#" + getNumber() + " " + getDescription();
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public String getDescription() {return description;} public void setDescription(String s) {description = s;}
  //@format
}