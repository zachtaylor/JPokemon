package com.jpokemon.util.savefilegenerator;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;

import org.jpokemon.trainer.Player;

public class SaveGeneratorWindow extends JFrame {
  public SaveGeneratorWindow() {
    setLayout(new BorderLayout());

    add(playerDetailsPanel, BorderLayout.CENTER);

    setVisible(true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    refresh();
  }

  public Player getPlayer() {
    return player;
  }

  public void toggleEditParty() {
    boolean containsEditorPanel = false;

    remove(playerEditorPanel);
    for (Component c : getContentPane().getComponents()) {
      if (c == partyEditorPanel) {
        containsEditorPanel = true;
        break;
      }
    }

    if (containsEditorPanel) {
      remove(partyEditorPanel);
    }
    else {
      add(partyEditorPanel, BorderLayout.EAST);
    }

    refresh();
  }

  public void toggleEditPlayer() {
    boolean containsEditorPanel = false;

    remove(partyEditorPanel);
    for (Component c : getContentPane().getComponents()) {
      if (c == playerEditorPanel) {
        containsEditorPanel = true;
        break;
      }
    }

    if (containsEditorPanel) {
      remove(playerEditorPanel);
    }
    else {
      add(playerEditorPanel, BorderLayout.EAST);
    }

    refresh();
  }

  public void refresh() {
    playerDetailsPanel.refresh();
    partyEditorPanel.refresh();

    pack();
    repaint();
  }

  private Player player = new Player("undefined");
  private PlayerDetailsPanel playerDetailsPanel = new PlayerDetailsPanel(this);
  private PartyEditorPanel partyEditorPanel = new PartyEditorPanel(this);
  private PlayerEditorPanel playerEditorPanel = new PlayerEditorPanel(this);

  public static final long serialVersionUID = 1L;
}