package jpkmn.map;

import jpkmn.game.pokemon.Pokemon;

public class Route extends Area {
  public Route(int routeNumber) {
    super(routeNumber);

    // _spawnermap = new HashMap<String, PokemonSpawner>();

    if (routeNumber == 2) {
      name("Route-1");
    }
    else if (routeNumber == 4) {
      name("Route-2");
    }
    else if (routeNumber == 5) {
      name("Route-22");
      buildings(Building.ELITE4);
      rival(2);
    }
    else if (routeNumber == 6) {
      name("Viridian Forest");
    }
    else if (routeNumber == 8) {
      name("Route-3");
      buildings(Building.CENTER);
      // buildings(Building.EVENTHOUSE); // Buy a magikarp
    }
    else if (routeNumber == 9) {
      name("Mt. Moon 1F");
    }
    else if (routeNumber == 10) {
      name("Mt. Moon B1F");
    }
    else if (routeNumber == 11) {
      name("Mt. Moon B2F");
    }
    else if (routeNumber == 12) {
      name("Route-4");
    }
  }

  private void add(int num, int flex, int low, int high, String s) {
    // _spawnermap.put(s, new PokemonSpawner(num, low, high, flex));
  }

  public Pokemon spawn(String... tags) {
    return null;
  }

  // private Map<String, PokemonSpawner> _spawnermap;
}