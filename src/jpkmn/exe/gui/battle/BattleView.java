package jpkmn.exe.gui.battle;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import jpkmn.exe.gui.JPokemonView;
import jpkmn.game.battle.Battle;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.battle.Slot;
import jpkmn.game.service.BattleService;

public class BattleView extends JPokemonView {
  public BattleView() {
    _enemies = new JPanel();
    JPanel userPanel = new JPanel();
    _user = new PartyPanel();
    JPanel buttonPanel = new JPanel();

    _fightButton = new JButton("FIGHT");
    _fightButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fight();
      }
    });

    _itemButton = new JButton("ITEM");
    _itemButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        item();
      }
    });

    _swapButton = new JButton("SWAP");
    _swapButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        swap();
      }
    });

    _runButton = new JButton("RUN");
    _runButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        run();
      }
    });

    buttonPanel.add(_fightButton);
    buttonPanel.add(_itemButton);
    buttonPanel.add(_swapButton);
    buttonPanel.add(_runButton);

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
    _enemies.setLayout(new BoxLayout(_enemies, BoxLayout.Y_AXIS));

    userPanel.add(_user);
    userPanel.add(buttonPanel);
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

    if (b == null)
      return;

    _enemies.removeAll();
    for (Slot s : b) {
      if (s.id() != _slotID) {
        _enemies.add(new PartyPanel(s.party(), false));
      }
    }

    _user.refresh(b.get(_slotID).party());

    enableButtons(true);
    setVisible(false);
    setVisible(true);
  }

  public Dimension dimension() {
    return new Dimension(625, 200);
  }

  public void fight() {
    enableButtons(false);

    int moveIndex = 0;
    // TODO : getMoveIndex

    int enemySlotID = (_slotID + 1) % 2;
    // TODO : target choice

    BattleService.attack(_battleID, _slotID, enemySlotID, moveIndex);
    refresh();
  }

  public void swap() {
    enableButtons(false);

    int slotIndex = 0;
    // TODO : getSwapIndex

    BattleService.swap(_battleID, _slotID, slotIndex);
    refresh();
  }

  public void item() {
    enableButtons(false);

    int itemID = 0;
    // TODO : getItemChoice

    int targetID = 0;
    // TODO : target choice

    BattleService.item(_battleID, _slotID, targetID, itemID);
    refresh();
  }

  public void run() {
    enableButtons(false);

    BattleService.run(_battleID, _slotID);
    refresh();
  }

  private void enableButtons(boolean enable) {
    _fightButton.setEnabled(enable);
    _itemButton.setEnabled(enable);
    _swapButton.setEnabled(enable);
    _runButton.setEnabled(enable);
  }

  private JPanel _enemies;
  private PartyPanel _user;
  private int _battleID, _slotID;
  private JButton _fightButton, _itemButton, _swapButton, _runButton;
  private static final long serialVersionUID = 1L;
}