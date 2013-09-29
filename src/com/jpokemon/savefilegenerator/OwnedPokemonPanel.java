package com.jpokemon.savefilegenerator;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jpokemon.pokemon.Pokemon;

import com.jpokemon.util.ui.ImageLoader;

public class OwnedPokemonPanel extends JPanel {
  public OwnedPokemonPanel(PartyEditorPanel pep, Pokemon p) {
    super(new BorderLayout());

    parent = pep;
    pokemon = p;

    add(new JLabel(ImageLoader.pokemon(p.number() + "")), BorderLayout.WEST);
    add(new JLabel(p.name() + " Lvl. " + p.level()), BorderLayout.CENTER);

    editButton.addMouseListener(new EditHandler());
    add(editButton, BorderLayout.EAST);
  }

  private Pokemon pokemon;
  private PartyEditorPanel parent;
  private JLabel editButton = new JLabel(ImageLoader.find("ui/arrow_right"));

  private static final long serialVersionUID = 1L;

  private class EditHandler extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      parent.showEditPokemon(pokemon);
    }
  }
}