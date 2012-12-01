package jpkmn.exe.gui;

import javax.swing.JFrame;

import jpkmn.exe.gui.battle.BattleView;
import jpkmn.exe.gui.pokemonupgrade.PokemonUpgradeView;
import jpkmn.exe.gui.start.StartView;
import jpkmn.exe.gui.world.WorldView;
import jpkmn.game.service.GraphicsHandler;
import jpkmn.img.ImageFinder;

public class GameWindow extends JFrame {
  public GameWindow(GraphicsHandler graphics, int playerID) {
    _graphics = graphics;
    _playerID = playerID;
    _inbox = new MessageView();
    _battle = new BattleView(playerID);
    _main = new WorldView(this);
    _start = new StartView(this);
    _upgrade = new PokemonUpgradeView(this);

    //setResizable(false);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setIconImage(ImageFinder.find("main-icon").getImage());

    setVisible(true);
  }

  public int playerID() {
    return _playerID;
  }

  public GraphicsHandler graphics() {
    return _graphics;
  }

  public MessageView inbox() {
    return _inbox;
  }

  public void showMain() {
    show(_main);
  }

  public void showStart() {
    show(_start);
  }

  public void showBattle() {
    show(_battle);
  }

  public void showUpgrade(int partyIndex) {
    _upgrade.setup(partyIndex);

    show(_upgrade);
  }

  public void refresh() {
    _active.refresh();
  }

  private void show(JPokemonView view) {
    if (_active != null)
      remove(_active);

    _active = view;
    add(_active);

    setSize(_active.dimension());

    refresh();
  }

  @Override
  public void dispose() {
    super.dispose();
    _inbox.dispose();
  }

  private int _playerID;
  private WorldView _main;
  private StartView _start;
  private BattleView _battle;
  private MessageView _inbox;
  private JPokemonView _active;
  private GraphicsHandler _graphics;
  private PokemonUpgradeView _upgrade;
  private static final long serialVersionUID = 1L;
}