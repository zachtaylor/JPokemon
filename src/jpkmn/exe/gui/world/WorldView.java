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
import jpkmn.map.Direction;
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

    JPanel left = new JPanel();
    JPanel right = new JPanel();
    JPanel center = new JPanel();

    _north = new AreaConnectionButton(this, Direction.NORTH);
    _east = new AreaConnectionButton(this, Direction.EAST);
    _south = new AreaConnectionButton(this, Direction.SOUTH);
    _west = new AreaConnectionButton(this, Direction.WEST);

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
    right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
    center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
    _buttons.setLayout(new BoxLayout(_buttons, BoxLayout.X_AXIS));

    //@preformat
    add(left);
      left.add(_west);
    add(spacer());
    add(center);
      center.add(_north);
      center.add(spacer());
      center.add(_title);
      center.add(_buttons);
      center.add(spacer());
      center.add(_south);
    add(spacer());
    add(right);
      right.add(_east);
    //@format
  }

  public void refresh() {
    if (!_enabled) return;

    Area a = AreaManager.get(_areaID);

    _title.setText(a.name());

    _buttons.removeAll();

    if (a instanceof Route) {
      _buttons.add(new GrassButton(this, _areaID));
    }
    if (a.water() != null) {
      _buttons.add(new FishButton(this, _areaID));
    }
    for (Building b : a.buildings()) {
      if (b == Building.CENTER)
        _buttons.add(new CenterButton(this, _areaID));
      else if (b == Building.MART)
        _buttons.add(new MartButton(this, _areaID));
      // TODO : Figure the rest out
    }

    _north.setUp(a);
    _east.setUp(a);
    _south.setUp(a);
    _west.setUp(a);
  }

  public void disable() {
    _enabled = false;
  }

  public void setup(int areaID) {
    _enabled = true;
    _areaID = areaID;
  }

  private JPanel spacer() {
    return new JPanel();
  }

  GameWindow window;
  private int _areaID;
  private JLabel _title;
  private JPanel _buttons;
  private boolean _enabled;
  private AreaConnectionButton _north, _east, _south, _west;

  private static final long serialVersionUID = 1L;
}