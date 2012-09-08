package jpkmn.exe.gui.battle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ItemButton extends JButton implements ActionListener {
  public ItemButton(BattleView view) {
    super("ITEM");

    _view = view;

    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    _view.item();
  }

  private BattleView _view;

  private static final long serialVersionUID = 1L;
}