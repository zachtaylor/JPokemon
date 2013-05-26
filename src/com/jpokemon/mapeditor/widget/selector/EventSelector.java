package com.jpokemon.mapeditor.widget.selector;

import org.jpokemon.map.Event;

public class EventSelector extends JPokemonSelector<Event> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    Event event;
    for (int i = 1; (event = Event.get(i)) != null; i++) {
      addElementToModel(event);
    }
  }

  private static final long serialVersionUID = 1L;
}