package jpkmn.game;

public class WildTrainer extends PokemonTrainer {
  public WildTrainer(String name) {
    super(name);
  }

  @Override
  public void notify(String... s) {
    return; // Do nothing
  }
}
