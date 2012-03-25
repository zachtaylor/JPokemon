package map;

import java.util.ArrayList;
import java.util.List;

/**
 * Properly initializes and maintains a list of areas.
 * 
 * @author Zach
 */
public class AreaManager {
  
  public static void init() {
    initAreas();
    initConnections();
  }
  
  private static void initAreas() {
    areas.clear();
    Route route;
    Water water;
    City city;
    
    city = new City("Pallet Town");
    water = new Water();
    city.addBuilding(Building.HOME);
    city.addBuilding(Building.EVENTHOUSE); // Map
    city.addRivalBattle(1);
    water.addSpecies(60, 1); // Poliwag
    water.addSpecies(72, 1); // Tentacool
    water.addSpecies(120, 1); // Staryu
    city.setWater(water);
    
    route = new Route("1");
    route.addSpecies(16, 7); // Pidgey
    route.addSpecies(19, 3); // Rattata
    
    city = new City("Viridian City");
    city.addBuilding(Building.CENTER);
    city.addBuilding(Building.MART);
    city.addBuilding(Building.GYM); // Gym 8
    water.addSpecies(60, 1); // Poliwag
    water.addSpecies(72, 1); // Tentacool
    city.setWater(water);
    
    route = new Route("2");
    route.addSpecies(10, 3); // Caterpie
    route.addSpecies(13, 3); // Weedle
    route.addSpecies(16, 6); // Pidgey
    route.addSpecies(19, 8); // Rattata
    route.addSpecies(29, 3); // Nidorano
    route.addSpecies(32, 3); // Nidorana
    

    route = new Route("22");
    route.addBuilding(Building.ELITE4);
    route.addRivalBattle(2);
    route.addSpecies(19, 1); // Rattata
    route.addSpecies(21, 1); // Spearow
    route.addSpecies(29, 3); // Nidorano
    route.addSpecies(32, 3); // Nidorana
    route.addSpecies(56, 2); // Mankey
    
    route = new Route("Veridian Forest");
    route.addSpecies(10, 10); // Caterpie
    route.addSpecies(11, 7); // Metapod
    route.addSpecies(13, 10); // Weedle
    route.addSpecies(14, 7); // Kakuna
    route.addSpecies(25, 1); // Pikachu
    route.addSpecies(16, 5); // Pidgey
    route.addSpecies(17, 1); // Pidgeotto
    
    city = new City("Pewter City");
    city.addBuilding(Building.CENTER);
    city.addBuilding(Building.MART);
    city.addBuilding(Building.GYM);
    city.addBuilding(Building.EVENTHOUSE); // Science Museum

    route = new Route("3");
    route.addBuilding(Building.CENTER);
    route.addBuilding(Building.EVENTHOUSE); // Buy a magikarp
    route.addSpecies(16, 10); // Pidgey
    route.addSpecies(19, 3); // Rattata
    route.addSpecies(21, 11); // Spearow
    route.addSpecies(27, 3); // Sandshrew
    route.addSpecies(39, 2); // Jigglypuff
    route.addSpecies(56, 3); // Mankey
    
    route = new Route("Mt. Moon 1F");
    route.addBuilding(Building.EVENTHOUSE); // Moon Stone
    route.addSpecies(41, 15); // Zubat
    route.addSpecies(74, 4); // Geodude
    route.addSpecies(46, 1); // Paras
    route.addSpecies(27, 1); // Sandshrew
    
    route = new Route("Mt. Moon B1");
    route.addSpecies(41, 13); // Zubat
    route.addSpecies(74, 5); // Geodude
    route.addSpecies(46, 2); // Paras
    route.addSpecies(35, 1); // Clefairy
    
    route = new Route("Mt. Moon B2");
    route.addBuilding(Building.EVENTHOUSE); // Hidden Moon Stone
    route.addBuilding(Building.EVENTHOUSE); // Fossil choice
    route.addSpecies(41, 12); // Zubat
    route.addSpecies(74, 3); // Geodude
    route.addSpecies(46, 3); // Paras
    route.addSpecies(35, 2); // Clefairy
    
    route = new Route("4");
    route.addSpecies(19, 3); // Rattata
    route.addSpecies(21, 11); // Spearow
    route.addSpecies(27, 3); // Sandshrew
    route.addSpecies(23, 5); // Ekans
    route.addSpecies(15, 3); // Mankey
    
    city = new City("Cerulean City");
    water = new Water();
    city.addBuilding(Building.CENTER);
    city.addBuilding(Building.MART);
    city.addBuilding(Building.GYM);
    city.addBuilding(Building.EVENTHOUSE); // Trade Poliwhirl for Jynx
    water.addSpecies(54, 1); // Psyduck
    water.addSpecies(98, 1); // Krabby
    water.addSpecies(118, 2); // Goldeen
    water.addSpecies(119, 1); // Seaking
    city.setWater(water);
    
    route = new Route("24");
    water = new Water();
    route.addRivalBattle(3);
    route.addSpecies(10, 5); // Caterpie
    route.addSpecies(11, 3); // Metapod
    route.addSpecies(13, 5); // Weedle
    route.addSpecies(14, 3); // Kakuna
    route.addSpecies(16, 5); // Pidgey
    route.addSpecies(17, 1); // Pidgeotto
    route.addSpecies(43, 7); // Oddish
    route.addSpecies(48, 2); // Venonat
    route.addSpecies(63, 3); // Abra
    route.addSpecies(69, 5); // Bellsprout
    water.addSpecies(54, 2); // Psyduck
    water.addSpecies(98, 2); // Krabby
    water.addSpecies(118, 5); // Goldeen
    water.addSpecies(119, 1); // Seaking
    route.setWater(water);
    
    route = new Route("25");
    water = new Water();
    route.addBuilding(Building.EVENTHOUSE); // Activate PC, SS Ticket
    route.addSpecies(10, 5); // Caterpie
    route.addSpecies(11, 3); // Metapod
    route.addSpecies(13, 5); // Weedle
    route.addSpecies(14, 3); // Kakuna
    route.addSpecies(16, 5); // Pidgey
    route.addSpecies(17, 1); // Pidgeotto
    route.addSpecies(43, 7); // Oddish
    route.addSpecies(48, 2); // Venonat
    route.addSpecies(63, 3); // Abra
    route.addSpecies(69, 5); // Bellsprout
    water.addSpecies(54, 2); // Psyduck
    water.addSpecies(98, 4); // Krabby
    water.addSpecies(99, 1); // Kingler
    route.setWater(water);
    
    route = new Route("5");
    route.addBuilding(Building.EVENTHOUSE); // Day Care
    route.addSpecies(16, 9); // Pidgey
    route.addSpecies(17, 1); // Pidgeotto
    route.addSpecies(19, 5); // Rattata
    route.addSpecies(39, 2); // Jigglypuff
    route.addSpecies(43, 8); // Oddish
    route.addSpecies(52, 5); // Meowth
    route.addSpecies(56, 5); // Mankey
    route.addSpecies(63, 3); // Abra
    route.addSpecies(69, 8); // Bellsprout
    
    route = new Route("6");
    water = new Water();
    route.addSpecies(16, 9); // Pidgey
    route.addSpecies(17, 1); // Pidgeotto
    route.addSpecies(19, 5); // Rattata
    route.addSpecies(39, 2); // Jigglypuff
    route.addSpecies(43, 8); // Oddish
    route.addSpecies(52, 5); // Meowth
    route.addSpecies(56, 5); // Mankey
    route.addSpecies(63, 3); // Abra
    route.addSpecies(69, 8); // Bellsprout
    water.addSpecies(90, 1); // Shelder
    water.addSpecies(98, 1); // Krabby
    water.addSpecies(118, 2); // Goldeen
    route.setWater(water);
    
    city = new City("Vermilion City");
    water = new Water();
    city.addBuilding(Building.CENTER);
    city.addBuilding(Building.MART);
    city.addBuilding(Building.GYM);
    city.addBuilding(Building.EVENTHOUSE); // Trade Spearow for Farfetch'd
    city.addBuilding(Building.EVENTHOUSE); // Bike Voucher
    city.addBuilding(Building.EVENTHOUSE); // Old Rod
    water.addSpecies(72, 9); // Tentacool
    water.addSpecies(90, 5); // Shelder
    water.addSpecies(98, 5); // Krabby
    water.addSpecies(116, 2); // Horsea
    city.setWater(water);
    
    route = new Route("11");
    route.addSpecies(16, 8); // Pidgey
    route.addSpecies(17, 2); // Pidgeotto
    route.addSpecies(19, 5); // Rattata
    route.addSpecies(20, 1); // Raticate
    route.addSpecies(21, 6); // Spearow
    route.addSpecies(23, 9); // Ekans
    route.addSpecies(27, 9); // Sandshrew
    route.addSpecies(96, 5); // Drowzee
    route.addSpecies(16, 8); // Pidgey
    
    route = new Route("Diglett's Cave");
    route.addSpecies(50, 19); // Diglett
    route.addSpecies(51, 1); // Dugtrio
    
    route = new Route("9");
    route.addSpecies(19, 3); // Rattata
    route.addSpecies(20, 1); // Raticate
    route.addSpecies(21, 6); // Spearow
    route.addSpecies(22, 1); // Fearow
    route.addSpecies(23, 5); // Ekans
    route.addSpecies(27, 5); // Sandshrew
    route.addSpecies(29, 7); // Nidorano
    route.addSpecies(30, 1); // Nidorino
    route.addSpecies(32, 5); // Nidorana
    route.addSpecies(33, 1); // Nidorina
    
    route = new Route("10");
    water = new Water();
    route.addBuilding(Building.CENTER);
    route.addSpecies(19, 3); // Rattata
    route.addSpecies(20, 1); // Raticate
    route.addSpecies(21, 6); // Spearow
    route.addSpecies(23, 5); // Ekans
    route.addSpecies(27, 5); // Sandshrew
    route.addSpecies(29, 2); // Nidorano
    route.addSpecies(32, 2); // Nidorana
    route.addSpecies(66, 1); // Machop
    route.addSpecies(81, 11); // Magnemite
    route.addSpecies(100, 9); // Voltorb
    water.addSpecies(61, 5); // Poliwhirl
    water.addSpecies(79, 5); // Slowpoke
    water.addSpecies(116, 2); // Horsea
    water.addSpecies(98, 7); // Krabby
    water.addSpecies(99, 1); // Kingler
    route.setWater(water);
    
    
    
  }

  private static void initConnections() {
    
  }
  
  public static void registerArea(Area a) {
    if (areas.contains(a)) return;
    areas.add(a);
  }
  
  public static Area get(String name) {
    for (Area a : areas) {
      if (a.getName().equals(name))
        return a;
    }
    return null;
  }
  
  private static List<Area> areas = new ArrayList<Area>();
}
