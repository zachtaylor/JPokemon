package jpkmn.exe.gui.battle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class RunButton extends JButton implements ActionListener {
  public RunButton() {
    super("RUN");
    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

  }

  private static final long serialVersionUID = 1L;
}
