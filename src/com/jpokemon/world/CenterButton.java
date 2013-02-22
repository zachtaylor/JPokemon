package com.jpokemon.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;

import com.jpokemon.GameWindow;

public class CenterButton extends JButton implements ActionListener {
  public CenterButton(WorldView view) {
    super("Pokemon Center");

    _window = view.parent();

    setFocusable(false);
    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    // TODO the right way
    Player player = PlayerFactory.get(_window.playerID());

    for (Pokemon p : player.party()) {
      p.healDamage(p.maxHealth());
    }

    player.notify("Your Pokemon have been fully healed!", "Please come again!");
  }

  private GameWindow _window;
  private static final long serialVersionUID = 1L;
}