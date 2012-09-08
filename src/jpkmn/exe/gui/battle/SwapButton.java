package jpkmn.exe.gui.battle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class SwapButton extends JButton implements ActionListener {
  public SwapButton(BattleView view) {
    super("SWAP");

    _view = view;

    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    _view.swap();
  }

  private BattleView _view;

  private static final long serialVersionUID = 1L;
}