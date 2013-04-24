package com.jpokemon;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;

public class JPokemonDialog {
  public JPokemonDialog(GameWindow parent) {
    _parent = parent;
  }

  public void showMessages(JSONArray data) {
    try {
      for (int i = 0; i < data.length(); i++) {
        String message = data.getString(i);
        showAlert(message);
      }
    } catch (JSONException e) {
    }
  }

  public void showAlert(String s) {
    JOptionPane.showConfirmDialog(_parent, s, "Title", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
  }

  private GameWindow _parent;
}