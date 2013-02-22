package com.jpokemon.party;

import javax.swing.BoxLayout;

import org.json.JSONArray;
import org.json.JSONException;

import com.jpokemon.GameWindow;
import com.jpokemon.JPokemonMenu;
import com.jpokemon.JPokemonMenuEntry;
import com.jpokemon.pokemon.PokemonView;

public class PartyMenu extends JPokemonMenu {
  public PartyMenu(GameWindow parent, PokemonView view) {
    super(parent);

    _view = view;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
  }

  public void update(JSONArray data) throws JSONException {
    removeAll();

    _entries = new PartyEntry[data.length()];

    for (int i = 0; i < data.length(); i++) {
      _entries[i] = new PartyEntry(this, data.getJSONObject(i));
      add(_entries[i]);
    }

    _entries[0].active(true);
  }

  public void navPokemon() {
    _view.navPokemon(select());
  }

  public JPokemonMenuEntry[] entries() {
    return _entries;
  }

  @Override
  public int width() {
    return 200;
  }

  private PokemonView _view;
  private PartyEntry[] _entries;
}