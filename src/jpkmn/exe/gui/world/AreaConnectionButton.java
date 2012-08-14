package jpkmn.exe.gui.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import jpkmn.exe.gui.GameWindow;
import jpkmn.map.Direction;

public class AreaConnectionButton extends JButton implements ActionListener {
  public AreaConnectionButton(WorldView view, Direction d) {
    super(d.toString());

    _direction = d;
    _window = view.window;

    addActionListener(this);
  }

  public void setUp(int areaID) {
    // TODO : setup
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    System.out.println("You clicked " + _direction.toString());
    // TODO : navigate
  }

  private GameWindow _window;
  private Direction _direction;

  private static final long serialVersionUID = 1L;
}