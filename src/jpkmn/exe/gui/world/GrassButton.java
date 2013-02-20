package jpkmn.exe.gui.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import jpkmn.exceptions.ServiceException;
import jpkmn.exe.gui.GameWindow;
import jpkmn.game.service.BattleService;

public class GrassButton extends JButton implements ActionListener {
  public GrassButton(WorldView view) {
    super("Tall Grass");

    _window = view.parent();

    setFocusable(false);
    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    int playerID = _window.playerID();

    try {
      BattleService.grass(playerID);
    } catch (ServiceException s) {
      _window.inbox().addMessage(s.getMessage());
    }
  }

  private GameWindow _window;
  private static final long serialVersionUID = 1L;
}