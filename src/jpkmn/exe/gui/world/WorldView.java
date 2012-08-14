package jpkmn.exe.gui.world;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jpkmn.exceptions.LoadException;
import jpkmn.exe.gui.GameWindow;
import jpkmn.game.player.Player;
import jpkmn.game.player.PlayerRegistry;
import jpkmn.map.Area;
import jpkmn.map.AreaManager;
import jpkmn.map.Building;
import jpkmn.map.Route;

public class WorldView extends JPanel {
  public static void main(String[] args) {
    try {
      Player dev = PlayerRegistry.fromFile("newfile");

      Area route1 = AreaManager.get(10); // route 1;
      Area viridian = AreaManager.get(1); // viridian city

      dev.area(viridian);

      System.out.println(route1.buildings());

      dev.screen.showWorld();
    } catch (LoadException e) {
      e.printStackTrace();
    }
  }

  public WorldView(GameWindow g) {
    window = g;
    _title = new JLabel();
    _buttons = new JPanel();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    _buttons.setLayout(new BoxLayout(_buttons, BoxLayout.X_AXIS));

    add(_title);
    add(_buttons);
  }

  public void refresh() {
    if (!_enabled) {
      System.out.println("WorldView refresh skipped");
      return;
    }

    Area a = AreaManager.get(_areaID);

    _title.setText(a.name());

    _buttons.removeAll();

    if (a instanceof Route) {
      System.out.println("Tall Grass added");
      _buttons.add(new GrassButton(this, _areaID));
    }
    if (a.water() != null) {
      System.out.println("Fishing added");
      _buttons.add(new FishButton(this, _areaID));
    }
    for (Building b : a.buildings()) {
      System.out.println(b.toString());
      if (b == Building.CENTER)
        _buttons.add(new CenterButton(this, _areaID));
      else if (b == Building.MART) _buttons.add(new MartButton(this, _areaID));
      // TODO : Figure the rest out
    }
  }

  public void disable() {
    _enabled = false;
  }

  public void setup(int areaID) {
    _enabled = true;
    _areaID = areaID;
  }

  GameWindow window;
  private int _areaID;
  private JLabel _title;
  private JPanel _buttons;
  private boolean _enabled;
  private static final long serialVersionUID = 1L;
}