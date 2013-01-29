package jpkmn.exe.gui.pokemonupgrade;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ExitButton extends JButton implements ActionListener {
  public ExitButton(PokemonUpgradeView view) {
    super("Done");
    _view = view;

    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    _view.window.showMain();
  }

  private PokemonUpgradeView _view;
  private static final long serialVersionUID = 1L;

}