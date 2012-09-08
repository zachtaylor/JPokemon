package jpkmn.exe.gui.battle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import jpkmn.exceptions.LoadException;
import jpkmn.game.battle.Battle;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.battle.Slot;
import jpkmn.game.player.MockPlayer;
import jpkmn.game.player.Player;
import jpkmn.game.player.PlayerRegistry;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.map.AreaManager;

public class BattleView extends JPanel {
  public static void main(String[] args) {
    try {
      Player zach = PlayerRegistry.fromFile("Zach");

      zach.area(AreaManager.get(10)); // route 1

      Pokemon wild = zach.area().spawn("");
      MockPlayer mock = new MockPlayer();
      mock.party.add(wild);

      BattleRegistry.make(zach, mock);
    } catch (LoadException l) {
      l.printStackTrace();
    }
  }

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
    _enabled = true;
    _slotID = slotID;
    _battleID = battleID;

    Battle b = BattleRegistry.get(battleID);

    _user.setup(b.get(slotID).party(), true);
  }

  public void refresh() {
    if (!_enabled) return;

    Battle b = BattleRegistry.get(_battleID);

    _user.refresh(b.get(_slotID).party());

    _enemies.removeAll();
    for (Slot s : b) {
      if (s.id() != _slotID) {
        _enemies.add(new PartyPanel(s.party(), false));
      }
    }

    enableButtons();
  }

  public void enableButtons() {
    buttons.enable();
  }

  public void disableButtons() {
    buttons.disable();
  }

  public void disable() {
    _enabled = false;
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
    BattleRegistry.get(_battleID).run(_slotID);
  }

  private JPanel _enemies;
  private PartyPanel _user;
  private boolean _enabled;
  private ButtonPanel buttons;
  private int _battleID, _slotID;
  private static final long serialVersionUID = 1L;
}