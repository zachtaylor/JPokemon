package com.jpokemon.savegenerator;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jpokemon.pokemon.Pokemon;

import com.jpokemon.ui.ImageLoader;

public class PartyEditorPanel extends JPanel {
  public PartyEditorPanel(SaveGeneratorWindow sgw) {
    parent = sgw;
    setLayout(new BorderLayout());

    partyPanel.setLayout(new BoxLayout(partyPanel, BoxLayout.Y_AXIS));
    add(partyPanel, BorderLayout.CENTER);

    JPanel southPanel = new JPanel();
    add(southPanel, BorderLayout.SOUTH);

    showAddPokemon.addMouseListener(new AddPokemonHandler());
    southPanel.add(showAddPokemon);
  }

  public void addPokemon(Pokemon p) {
    pokemon.add(p);
  }

  public void editPokemon(Pokemon p) {
    parent.showEditPokemon(p);
  }

  public void removePokemon(Pokemon p) {
    pokemon.remove(p);
  }

  public void reload() {
    partyPanel.removeAll();
    for (Pokemon poke : pokemon) {
      partyPanel.add(new OwnedPokemonPanel(this, poke));
    }
  }

  private SaveGeneratorWindow parent;
  private JPanel partyPanel = new JPanel();
  private List<Pokemon> pokemon = new ArrayList<Pokemon>();
  private JLabel showAddPokemon = new JLabel(ImageLoader.find("ui/plus"));

  public static final long serialVersionUID = 1L;

  private class AddPokemonHandler extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      parent.showAddPokemon();
    }
  }
}