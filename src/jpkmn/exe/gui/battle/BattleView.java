package jpkmn.exe.gui.battle;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import jpkmn.exe.gui.JPokemonView;
import jpkmn.game.battle.Battle;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.battle.Slot;

public class BattleView extends JPokemonView {
  public BattleView() {
    _enemies = new JPanel();
    JPanel userPanel = new JPanel();
    _user = new PartyPanel();
    buttons = new ButtonPanel(this);

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

    userPanel.add(_user);
    userPanel.add(buttons);
    userPanel.add(new JPanel());

    add(userPanel);
    add(_enemies);
  }

  public void setup(int battleID, int slotID) {
    _slotID = slotID;
    _battleID = battleID;

    Battle b = BattleRegistry.get(battleID);

    _user.setup(b.get(slotID).party(), true);
  }

  public void refresh() {
    Battle b = BattleRegistry.get(_battleID);

    _user.refresh(b.get(_slotID).party());

    _enemies.removeAll();
    for (Slot s : b) {
      if (s.id() != _slotID) {
        _enemies.add(new PartyPanel(s.party(), false));
      }
    }

    buttons.enable();
  }

  public Dimension dimension() {
    return new Dimension(625, 200);
  }

  public void fight() {
    buttons.disable();
    BattleRegistry.get(_battleID).fight(_slotID);
  }

  public void swap() {
    buttons.disable();
    BattleRegistry.get(_battleID).swap(_slotID);
  }

  public void item() {
    buttons.disable();
    BattleRegistry.get(_battleID).item(_slotID);
  }

  public void run() {
    buttons.disable();
    BattleRegistry.get(_battleID).run(_slotID);
  }

  private JPanel _enemies;
  private PartyPanel _user;
  private ButtonPanel buttons;
  private int _battleID, _slotID;
  private static final long serialVersionUID = 1L;
}