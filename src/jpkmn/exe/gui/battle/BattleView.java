package jpkmn.exe.gui.battle;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import jpkmn.exceptions.ServiceException;
import jpkmn.exe.gui.JPokemonView;
import jpkmn.game.service.BattleService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BattleView extends JPokemonView {
  public BattleView(int playerID) {
    _playerID = playerID;
    _user = new JPanel();
    _enemies = new JPanel();
    JPanel userAndButtons = new JPanel();
    JPanel buttonPanel = new JPanel();

    _fightButton = new JButton("FIGHT");
    buttonPanel.add(_fightButton);
    _fightButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fight();
      }
    });

    _itemButton = new JButton("ITEM");
    buttonPanel.add(_itemButton);
    _itemButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        item();
      }
    });

    _swapButton = new JButton("SWAP");
    buttonPanel.add(_swapButton);
    _swapButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        swap();
      }
    });

    _runButton = new JButton("RUN");
    buttonPanel.add(_runButton);
    _runButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        run();
      }
    });

    userAndButtons.add(_user);
    userAndButtons.add(buttonPanel);
    userAndButtons.add(spacer());

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    _user.setLayout(new BoxLayout(_user, BoxLayout.Y_AXIS));
    _enemies.setLayout(new BoxLayout(_enemies, BoxLayout.X_AXIS));
    userAndButtons.setLayout(new BoxLayout(userAndButtons, BoxLayout.Y_AXIS));

    add(userAndButtons);
    add(_enemies);
  }

  public void refresh() {
    try {
      _data = BattleService.info(_playerID);
    } catch (ServiceException e) {
      e.printStackTrace();
      return;
    }

    _user.removeAll();
    _enemies.removeAll();

    try {
      JSONArray allTeams = _data.getJSONArray("teams");
      JSONArray teamData;
      JSONObject trainerData;
      JPanel teamPanel;

      for (int i = 0; i < allTeams.length(); i++) {
        teamPanel = new JPanel();
        teamPanel.setLayout(new BoxLayout(teamPanel, BoxLayout.Y_AXIS));

        teamData = allTeams.getJSONArray(i);

        for (int j = 0; j < teamData.length(); j++) {
          trainerData = teamData.getJSONObject(j);
          if (trainerData.getInt("team") == _data.getInt("user_team"))
            _user.add(new PartyPanel(trainerData, _playerID));
          else
            teamPanel.add(new PartyPanel(trainerData, _playerID));
        }
        _enemies.add(teamPanel);
      }

    } catch (JSONException e) {
      e.printStackTrace();
      return;
    }

    enableButtons(true);
    validate();
  }

  public Dimension dimension() {
    return new Dimension(625, 200);
  }

  private void fight() {
    enableButtons(false);

    int moveIndex = 0;
    // TODO : getMoveIndex

    int enemySlotID = 1;
    // TODO : target choice

    BattleService.attack(_playerID, enemySlotID, moveIndex);
    refresh();
  }

  private void swap() {
    enableButtons(false);

    int slotIndex = 0;
    // TODO : getSwapIndex

    BattleService.swap(_playerID, slotIndex);
    refresh();
  }

  private void item() {
    enableButtons(false);

    int itemID = 0;
    // TODO : getItemChoice

    int targetID = 0;
    // TODO : target choice

    BattleService.item(_playerID, targetID, itemID);
    refresh();
  }

  private void run() {
    enableButtons(false);

    BattleService.run(_playerID);
    refresh();
  }

  private void enableButtons(boolean enable) {
    _fightButton.setEnabled(enable);
    _itemButton.setEnabled(enable);
    _swapButton.setEnabled(enable);
    _runButton.setEnabled(enable);
  }

  private int _playerID;
  private JPanel _enemies, _user;
  private JSONObject _data;
  private JButton _fightButton, _itemButton, _swapButton, _runButton;
  private static final long serialVersionUID = 1L;
}