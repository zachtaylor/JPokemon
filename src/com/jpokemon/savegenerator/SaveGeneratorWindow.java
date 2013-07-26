package com.jpokemon.savegenerator;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.PokemonInfo;

public class SaveGeneratorWindow extends JFrame {
  public SaveGeneratorWindow() {
    setLayout(new BorderLayout());

    add(partyEditorPanel, BorderLayout.WEST);

    setVisible(true);
    setSize(500, 400);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public void addPokemon(PokemonInfo pi) {
    Pokemon pokemon = new Pokemon(pi.getNumber());
    partyEditorPanel.addPokemon(pokemon);
    reloadParty();
  }

  public void removePokemon(Pokemon p) {
    partyEditorPanel.removePokemon(p);
    reloadParty();
    remove(pokemonDetailsPanel);
  }

  public void showAddPokemon() {
    remove(pokemonDetailsPanel);
    add(pokemonSelectorPanel, BorderLayout.EAST);

    validate();
    repaint();
  }

  public void showEditPokemon(Pokemon p) {
    remove(pokemonSelectorPanel);
    add(pokemonDetailsPanel, BorderLayout.EAST);
    pokemonDetailsPanel.show(p);

    validate();
    repaint();
  }

  public void reloadParty() {
    partyEditorPanel.reload();

    validate();
    repaint();
  }

  private PartyEditorPanel partyEditorPanel = new PartyEditorPanel(this);
  private PokemonDetailsPanel pokemonDetailsPanel = new PokemonDetailsPanel(this);
  private PokemonSelectorPanel pokemonSelectorPanel = new PokemonSelectorPanel(this);

  public static final long serialVersionUID = 1L;
}