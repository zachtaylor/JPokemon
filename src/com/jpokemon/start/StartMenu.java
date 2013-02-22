package com.jpokemon.start;

import javax.swing.BoxLayout;

import com.jpokemon.GameWindow;
import com.jpokemon.JPokemonMenu;
import com.jpokemon.JPokemonMenuEntry;

public class StartMenu extends JPokemonMenu {
  public StartMenu(GameWindow g) {
    super(g);

    _select = 0;
    _entries = new StartMenuEntry[StartEntryValue.values().length];

    for (int i = 0; i < StartEntryValue.values().length; ++i)
      _entries[i] = new StartMenuEntry(this, StartEntryValue.valueOf(i));

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    for (StartMenuEntry entry : _entries)
      add(entry);

    select(_select);
  }

  public JPokemonMenuEntry[] entries() {
    return _entries;
  }

  public int width() {
    return 100;
  }

  private int _select;
  private StartMenuEntry[] _entries;
  private static final long serialVersionUID = 1L;
}