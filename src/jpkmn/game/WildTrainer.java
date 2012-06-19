package jpkmn.game;

import java.util.List;

public class WildTrainer extends PokemonTrainer {
  public WildTrainer(String name) {
    super(name);
  }

  @Override
  public void notify(List<String> s) {
    return; // Do nothing
  }
}
