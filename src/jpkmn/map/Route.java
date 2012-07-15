package jpkmn.map;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.pokemon.Pokemon;

public class Route extends Area implements PokemonGenerator {
  public Route(int routeNumber) {
    super(routeNumber);

    _species = new ArrayList<Integer>();

    if (routeNumber == 10) {
      name("Route-1");
      species(16, 7); // Pidgey
      species(19, 3); // Rattata
    }
    else if (routeNumber == 11) {
      name("Route-2");
      species(10, 3); // Caterpie
      species(13, 3); // Weedle
      species(16, 6); // Pidgey
      species(19, 8); // Rattata
      species(29, 3); // Nidorano
      species(32, 3); // Nidorana
    }
    else if (routeNumber == 12) {
      name("Route-22");
      buildings(Building.ELITE4);
      species(19, 1); // Rattata
      species(21, 1); // Spearow
      species(29, 3); // Nidorano
      species(32, 3); // Nidorana
      species(56, 2); // Mankey

      rivalBattle(2);
    }
    else if (routeNumber == 13) {
      name("Veridian Forest");
      species(10, 10); // Caterpie
      species(11, 7); // Metapod
      species(13, 10); // Weedle
      species(14, 7); // Kakuna
      species(25, 1); // Pikachu
      species(16, 5); // Pidgey
      species(17, 1); // Pidgeotto
    }
    else if (routeNumber == 14) {
      name("Route-3");
      buildings(Building.CENTER);
      buildings(Building.EVENTHOUSE); // Buy a magikarp
      species(16, 10); // Pidgey
      species(19, 3); // Rattata
      species(21, 11); // Spearow
      species(27, 3); // Sandshrew
      species(39, 2); // Jigglypuff
      species(56, 3); // Mankey
    }
    else if (routeNumber == 15) {
      name("Mt. Moon 1F");
      buildings(Building.EVENTHOUSE); // Moon Stone
      species(41, 15); // Zubat
      species(74, 4); // Geodude
      species(46, 1); // Paras
      species(27, 1); // Sandshrew
    }
    else if (routeNumber == 16) {
      name("Mt. Moon B1");
      species(41, 13); // Zubat
      species(74, 5); // Geodude
      species(46, 2); // Paras
      species(35, 1); // Clefairy
    }
    else if (routeNumber == 17) {
      name("Mt. Moon B2");
      buildings(Building.EVENTHOUSE); // Hidden Moon Stone
      buildings(Building.EVENTHOUSE); // Fossil choice
      species(41, 12); // Zubat
      species(74, 3); // Geodude
      species(46, 3); // Paras
      species(35, 2); // Clefairy
    }
    else if (routeNumber == 18) {
      name("Route-4");
      species(19, 3); // Rattata
      species(21, 11); // Spearow
      species(27, 3); // Sandshrew
      species(23, 5); // Ekans
      species(15, 3); // Mankey
    }
    else if (routeNumber == 19) {
      name("Route-24");
      species(10, 5); // Caterpie
      species(11, 3); // Metapod
      species(13, 5); // Weedle
      species(14, 3); // Kakuna
      species(16, 5); // Pidgey
      species(17, 1); // Pidgeotto
      species(43, 7); // Oddish
      species(48, 2); // Venonat
      species(63, 3); // Abra
      species(69, 5); // Bellsprout

      Water water = new Water();
      water.species(54, 2); // Psyduck
      water.species(98, 2); // Krabby
      water.species(118, 5); // Goldeen
      water.species(119, 1); // Seaking
      water(water);

      rivalBattle(3);
    }
    else if (routeNumber == 20) {
      name("Route-25");
      buildings(Building.EVENTHOUSE); // Activate PC, SS Ticket
      species(10, 5); // Caterpie
      species(11, 3); // Metapod
      species(13, 5); // Weedle
      species(14, 3); // Kakuna
      species(16, 5); // Pidgey
      species(17, 1); // Pidgeotto
      species(43, 7); // Oddish
      species(48, 2); // Venonat
      species(63, 3); // Abra
      species(69, 5); // Bellsprout

      Water water = new Water();
      water.species(54, 2); // Psyduck
      water.species(98, 4); // Krabby
      water.species(99, 1); // Kingler
      water(water);
    }
    else if (routeNumber == 21) {
      name("Route-5");
      buildings(Building.EVENTHOUSE); // Day Care
      species(16, 9); // Pidgey
      species(17, 1); // Pidgeotto
      species(19, 5); // Rattata
      species(39, 2); // Jigglypuff
      species(43, 8); // Oddish
      species(52, 5); // Meowth
      species(56, 5); // Mankey
      species(63, 3); // Abra
      species(69, 8); // Bellsprout
    }
    // else if (areaName.equals("6")) {
    // currentArea = new Route("6");
    // Water water = new Water();
    // species(16, 9); // Pidgey
    // species(17, 1); // Pidgeotto
    // species(19, 5); // Rattata
    // species(39, 2); // Jigglypuff
    // species(43, 8); // Oddish
    // species(52, 5); // Meowth
    // species(56, 5); // Mankey
    // species(63, 3); // Abra
    // species(69, 8); // Bellsprout
    // water.addSpecies(90, 1); // Shelder
    // water.addSpecies(98, 1); // Krabby
    // water.addSpecies(118, 2); // Goldeen
    // currentArea.setWater(water);
    // }
    // else if (areaName.equals("11")) {
    // currentArea = new Route("11");
    // species(16, 8); // Pidgey
    // species(17, 2); // Pidgeotto
    // species(19, 5); // Rattata
    // species(20, 1); // Raticate
    // species(21, 6); // Spearow
    // species(23, 9); // Ekans
    // species(27, 9); // Sandshrew
    // species(96, 5); // Drowzee
    // species(16, 8); // Pidgey
    // }
    // else if (areaName.equals("Diglett's Cave")) {
    // currentArea = new Route("Diglett's Cave");
    // species(50, 19); // Diglett
    // species(51, 1); // Dugtrio
    // }
    // else if (areaName.equals("9")) {
    // currentArea = new Route("9");
    // species(19, 3); // Rattata
    // species(20, 1); // Raticate
    // species(21, 6); // Spearow
    // species(22, 1); // Fearow
    // species(23, 5); // Ekans
    // species(27, 5); // Sandshrew
    // species(29, 7); // Nidorano
    // species(30, 1); // Nidorino
    // species(32, 5); // Nidorana
    // species(33, 1); // Nidorina
    // }
    // else if (areaName.equals("10")) {
    // currentArea = new Route("10");
    // Water water = new Water();
    // currentArea.addBuilding(Building.CENTER);
    // species(19, 3); // Rattata
    // species(20, 1); // Raticate
    // species(21, 6); // Spearow
    // species(23, 5); // Ekans
    // species(27, 5); // Sandshrew
    // species(29, 2); // Nidorano
    // species(32, 2); // Nidorana
    // species(66, 1); // Machop
    // species(81, 11); // Magnemite
    // species(100, 9); // Voltorb
    // water.addSpecies(61, 5); // Poliwhirl
    // water.addSpecies(79, 5); // Slowpoke
    // water.addSpecies(116, 2); // Horsea
    // water.addSpecies(98, 7); // Krabby
    // water.addSpecies(99, 1); // Kingler
    // currentArea.setWater(water);
    //
    // }
  }

  @Override
  public void species(int num, int flex) {
    for (int i = 0; i < flex; i++)
      _species.add(num);
  }

  @Override
  public Pokemon species() {
    int number = _species.get((int) (Math.random() * _species.size()));
    int level = _low + (int) (Math.random() * (_high - _low + 1));

    return new Pokemon(number, level);
  }

  @Override
  public void range(int low, int high) {
    _low = low;
    _high = high;
  }

  private int _low, _high;
  private List<Integer> _species;
}