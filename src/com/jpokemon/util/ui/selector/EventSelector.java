package com.jpokemon.util.ui.selector;

import org.jpokemon.trainer.Event;

import com.jpokemon.util.ui.JPokemonSelector;

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