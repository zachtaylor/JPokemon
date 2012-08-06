package jpkmn.exe.gui.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import jpkmn.exe.gui.GameWindow;

public class FishButton extends JButton implements ActionListener {
  public FishButton(WorldView view, int areaID) {
    super("Fish");

    _window = view.window;
    _areaID = areaID;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO : Generate battle
  }

  private int _areaID;
  private GameWindow _window;
  private static final long serialVersionUID = 1L;
}