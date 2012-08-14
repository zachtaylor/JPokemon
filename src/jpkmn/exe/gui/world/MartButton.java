package jpkmn.exe.gui.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import jpkmn.exe.gui.GameWindow;

public class MartButton extends JButton implements ActionListener {
  public MartButton(WorldView view, int areaID) {
    super("Pokemon Mart");

    _window = view.window;
    _areaID = areaID;

    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    System.out.println("You clicked Mart");
    // TODO : Generate battle
  }

  private int _areaID;
  private GameWindow _window;
  private static final long serialVersionUID = 1L;
}
