package jpkmn.exe.gui.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import jpkmn.exe.gui.GameWindow;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;

public class CenterButton extends JButton implements ActionListener {
  public CenterButton(WorldView view) {
    super("Pokemon Center");

    _window = view.window;

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