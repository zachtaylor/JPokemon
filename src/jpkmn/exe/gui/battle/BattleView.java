package jpkmn.exe.gui.battle;

import javax.swing.JPanel;

public class BattleView extends JPanel {
  public BattleView(int battleID, int slotID) {
    _battleID = battleID;
    _slotID = slotID;
  }

  private int _battleID, _slotID;
  private static final long serialVersionUID = 1L;
}