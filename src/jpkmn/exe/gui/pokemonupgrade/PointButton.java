package jpkmn.exe.gui.pokemonupgrade;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class PointButton extends JButton implements ActionListener {
  public PointButton(StatPanel statPanel) {
    super("+");
    _stat = statPanel;
    
    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    _stat.addPoint();
  }

  private StatPanel _stat;
  private static final long serialVersionUID = 1L;
}