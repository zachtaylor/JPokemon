package jpkmn.exe.gui.battle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class RunButton extends JButton implements ActionListener {
  public RunButton(BattleView view) {
    super("RUN");

    _view = view;

    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    _view.run();
  }

  private BattleView _view;

  private static final long serialVersionUID = 1L;
}