package jpkmn.exe.gui.battle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class FightButton extends JButton implements ActionListener {
  public FightButton() {
    super("FIGHT");
    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  private static final long serialVersionUID = 1L;
}
