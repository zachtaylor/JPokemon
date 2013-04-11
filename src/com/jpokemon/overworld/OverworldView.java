package com.jpokemon.overworld;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.GameWindow;
import com.jpokemon.JPokemonView;

public class OverworldView extends JPokemonView {
  public OverworldView(GameWindow parent) {
    super(parent);

    nameLabel = new JLabel("x");
    infoPanel = new JPanel();
    npcPanel = new JPanel();

    infoPanel.add(nameLabel);
    add(infoPanel);
    add(npcPanel);
  }

  @Override
  public Dimension dimension() {
    return new Dimension(400, 200);
  }

  @Override
  public void update(JSONObject data) {
    try {
      nameLabel.setText(data.getString("name"));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean key(KeyEvent arg0) {
    return false;
  }

  private JLabel nameLabel;
  private JPanel npcPanel, infoPanel;
}