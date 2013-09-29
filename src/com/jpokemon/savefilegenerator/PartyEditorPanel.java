package com.jpokemon.savefilegenerator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.PokemonInfo;

import com.jpokemon.util.ui.ImageLoader;

public class PartyEditorPanel extends JPanel {
  public static int defaultlevel = 25;

  public PartyEditorPanel(SaveGeneratorWindow sgw) {
    super(new BorderLayout());

    parent = sgw;

    JPanel bodyPanel = new JPanel();
    bodyPanel.setLayout(new BorderLayout());
    bodyPanel.setPreferredSize(new Dimension(250, 300));
    add(bodyPanel, BorderLayout.CENTER);

    bodyPanel.add(new JLabel("Party"), BorderLayout.NORTH);

    partyPanel.setLayout(new BoxLayout(partyPanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(partyPanel);
    bodyPanel.add(scrollPane, BorderLayout.CENTER);

    JPanel southPanel = new JPanel();
    bodyPanel.add(southPanel, BorderLayout.SOUTH);

    showAddPokemon.addMouseListener(new AddPokemonHandler());
    southPanel.add(showAddPokemon);
  }

  public void showEditPokemon(Pokemon p) {
    remove(pokemonSelectorPanel);
    add(pokemonDetailsPanel, BorderLayout.EAST);
    pokemonDetailsPanel.show(p);
    callParentRefresh();
  }

  public void showAddPokemon() {
    remove(pokemonDetailsPanel);
    add(pokemonSelectorPanel, BorderLayout.EAST);
    callParentRefresh();
  }

  public void addPokemon(PokemonInfo pi) {
    Pokemon pokemon = new Pokemon(pi.getNumber(), defaultlevel);
    parent.getPlayer().add(pokemon);
    callParentRefresh();
  }

  public void removePokemon(Pokemon p) {
    parent.getPlayer().party().remove(p);
    callParentRefresh();
  }

  public void callParentRefresh() {
    parent.refresh();
  }

  public void refresh() {
    partyPanel.removeAll();
    for (Pokemon p : parent.getPlayer().party()) {
      partyPanel.add(new OwnedPokemonPanel(this, p));
    }
  }

  private SaveGeneratorWindow parent;
  private JPanel partyPanel = new JPanel();
  private JLabel showAddPokemon = new JLabel(ImageLoader.find("ui/plus"));
  private PokemonDetailsPanel pokemonDetailsPanel = new PokemonDetailsPanel(this);
  private PokemonSearchPanel pokemonSelectorPanel = new PokemonSearchPanel(this);

  public static final long serialVersionUID = 1L;

  private class AddPokemonHandler extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      showAddPokemon();
    }
  }
}