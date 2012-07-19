package jpkmn.exe.gui.battle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel {
  public ButtonPanel(BattleView view) {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    _run = new RunButton(view);
    _swap = new SwapButton(view);
    _item = new ItemButton(view);
    _fight = new FightButton(view);

    add(_fight);
    add(_item);
    add(_swap);
    add(_run);
  }

  public void enable() {
    _run.setEnabled(true);
    _swap.setEnabled(true);
    _item.setEnabled(true);
    _fight.setEnabled(true);
  }

  public void disable() {
    _run.setEnabled(false);
    _swap.setEnabled(false);
    _item.setEnabled(false);
    _fight.setEnabled(false);
  }

  private RunButton _run;
  private SwapButton _swap;
  private ItemButton _item;
  private FightButton _fight;

  private static final long serialVersionUID = 1L;
}
