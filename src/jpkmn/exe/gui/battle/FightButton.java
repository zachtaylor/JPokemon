package jpkmn.exe.gui.battle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class FightButton extends JButton implements ActionListener {
  public FightButton(BattleView view) {
    super("FIGHT");

    _view = view;

    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    _view.fight();
  }

  private BattleView _view;

  private static final long serialVersionUID = 1L;
}
