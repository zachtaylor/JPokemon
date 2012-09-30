package jpkmn.exe.gui.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import jpkmn.exceptions.ServiceException;
import jpkmn.exe.gui.GameWindow;
import jpkmn.game.service.PlayerService;

public class AreaConnectionButton extends JButton implements ActionListener {
  public AreaConnectionButton(WorldView view, String direction) {
    super(direction);

    _direction = direction;
    _window = view.window;

    setFocusable(false);
    addActionListener(this);
  }

  public void setUp(String area) {
    if (area.equals(""))
      setEnabled(false);
    else {
      setEnabled(true);
      setToolTipText(area);
    }
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    int playerID = _window.playerID();

    try {
      PlayerService.areaChange(playerID, _direction);
    } catch (ServiceException s) {
      _window.inbox().addMessage(s.getMessage());
    }

    _window.refresh();
  }

  private String _direction;
  private GameWindow _window;

  private static final long serialVersionUID = 1L;
}