package com.jpokemon.savefilegenerator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.swing.JFrame;

import org.jpokemon.trainer.Player;
import org.zachtaylor.jnodalxml.XmlParser;

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

  public void loadOrSetName(String name) {
    File file = new File(".", name + ".jpkmn");

    if (file.exists()) {
      player = new Player("undefined");
      try {
        player.loadXML(XmlParser.parse(file).get(0));
      } catch (FileNotFoundException e) {
      }
    }
    else {
      player.setName(name);
    }
  }

  public void savePlayer() {
    File file = new File(".", player.getName() + ".jpkmn");

    try {
      Writer writer = new BufferedWriter(new PrintWriter(file));
      writer.write(player.toXml().printToString(0, "  "));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
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