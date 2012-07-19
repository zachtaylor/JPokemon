package jpkmn.exe.gui.battle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import jpkmn.game.battle.Battle;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.battle.Slot;

public class BattleView extends JPanel {
  public BattleView(int battleID, int slotID) {
    _battleID = battleID;
    _slotID = slotID;

    Battle b = BattleRegistry.get(battleID);

    enemies = new JPanel();
    JPanel userPanel = new JPanel();
    user = new PartyPanel(b.get(slotID).getParty(), true);
    buttons = new ButtonPanel(this);

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
    userPanel.add(user);
    userPanel.add(buttons);
    add(userPanel);

    add(enemies);

    refresh();
  }

  public void refresh() {
    Battle b = BattleRegistry.get(_battleID);

    user.refresh();

    enemies.removeAll();
    for (Slot s : b) {
      if (s.id() != _slotID) {
        enemies.add(new PartyPanel(s.getParty(), false));
      }
    }
  }

  public void enableButtons() {
    buttons.enable();
  }

  public void disableButtons() {
    buttons.disable();
  }

  public void fight() {
    disableButtons();
    BattleRegistry.get(_battleID).fight(_slotID);
  }

  public void swap() {
    disableButtons();
    BattleRegistry.get(_battleID).swap(_slotID);
  }

  public void item() {
    disableButtons();
    BattleRegistry.get(_battleID).item(_slotID);
  }

  public void run() {
    disableButtons();
    BattleRegistry.get(_battleID).item(_slotID);
  }

  private JPanel enemies;
  private PartyPanel user;
  private ButtonPanel buttons;
  private int _battleID, _slotID;
  private static final long serialVersionUID = 1L;
}