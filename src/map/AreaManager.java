package map;

/**
 * Properly initializes and maintains a list of areas.
 * 
 * @author Zach
 */
public class AreaManager {

  public static void init() {
    load("Pallet Town");
  }

  private static void load(String areaName) {
    if (areaName.equals("Pallet Town")) {
      currentArea = new City("Pallet Town");
      Water water = new Water();
      currentArea.addBuilding(Building.HOME);
      currentArea.addBuilding(Building.EVENTHOUSE); // Map
      currentArea.addRivalBattle(1);
      water.addSpecies(60, 1); // Poliwag
      water.addSpecies(72, 1); // Tentacool
      water.addSpecies(120, 1); // Staryu
      currentArea.setWater(water);
    }
    else if (areaName.equals("1")) {
      currentArea = new Route("1");
      ((Route) currentArea).addSpecies(16, 7); // Pidgey
      ((Route) currentArea).addSpecies(19, 3); // Rattata
    }
    else if (areaName.equals("Viridian City")) {
      currentArea = new City("Viridian City");
      Water water = new Water();
      currentArea.addBuilding(Building.CENTER);
      currentArea.addBuilding(Building.MART);
      currentArea.addBuilding(Building.GYM); // Gym 8
      water.addSpecies(60, 1); // Poliwag
      water.addSpecies(72, 1); // Tentacool
      currentArea.setWater(water);
    }
    else if (areaName.equals("2")) {
      currentArea = new Route("2");
      ((Route) currentArea).addSpecies(10, 3); // Caterpie
      ((Route) currentArea).addSpecies(13, 3); // Weedle
      ((Route) currentArea).addSpecies(16, 6); // Pidgey
      ((Route) currentArea).addSpecies(19, 8); // Rattata
      ((Route) currentArea).addSpecies(29, 3); // Nidorano
      ((Route) currentArea).addSpecies(32, 3); // Nidorana
    }
    else if (areaName.equals("22")) {
      currentArea = new Route("22");
      currentArea.addBuilding(Building.ELITE4);
      currentArea.addRivalBattle(2);
      ((Route) currentArea).addSpecies(19, 1); // Rattata
      ((Route) currentArea).addSpecies(21, 1); // Spearow
      ((Route) currentArea).addSpecies(29, 3); // Nidorano
      ((Route) currentArea).addSpecies(32, 3); // Nidorana
      ((Route) currentArea).addSpecies(56, 2); // Mankey
    }
    else if (areaName.equals("Veridian Forest")) {
      currentArea = new Route("Veridian Forest");
      ((Route) currentArea).addSpecies(10, 10); // Caterpie
      ((Route) currentArea).addSpecies(11, 7); // Metapod
      ((Route) currentArea).addSpecies(13, 10); // Weedle
      ((Route) currentArea).addSpecies(14, 7); // Kakuna
      ((Route) currentArea).addSpecies(25, 1); // Pikachu
      ((Route) currentArea).addSpecies(16, 5); // Pidgey
      ((Route) currentArea).addSpecies(17, 1); // Pidgeotto
    }
    else if (areaName.equals("Pewter City")) {
      currentArea = new City("Pewter City");
      currentArea.addBuilding(Building.CENTER);
      currentArea.addBuilding(Building.MART);
      currentArea.addBuilding(Building.GYM);
      currentArea.addBuilding(Building.EVENTHOUSE); // Science Museum
    }
    else if (areaName.equals("3")) {
      currentArea = new Route("3");
      currentArea.addBuilding(Building.CENTER);
      currentArea.addBuilding(Building.EVENTHOUSE); // Buy a magikarp
      ((Route) currentArea).addSpecies(16, 10); // Pidgey
      ((Route) currentArea).addSpecies(19, 3); // Rattata
      ((Route) currentArea).addSpecies(21, 11); // Spearow
      ((Route) currentArea).addSpecies(27, 3); // Sandshrew
      ((Route) currentArea).addSpecies(39, 2); // Jigglypuff
      ((Route) currentArea).addSpecies(56, 3); // Mankey
    }
    else if (areaName.equals("Mt. Moon 1F")) {
      currentArea = new Route("Mt. Moon 1F");
      currentArea.addBuilding(Building.EVENTHOUSE); // Moon Stone
      ((Route) currentArea).addSpecies(41, 15); // Zubat
      ((Route) currentArea).addSpecies(74, 4); // Geodude
      ((Route) currentArea).addSpecies(46, 1); // Paras
      ((Route) currentArea).addSpecies(27, 1); // Sandshrew
    }
    else if (areaName.equals("Mt. Moon B1")) {
      currentArea = new Route("Mt. Moon B1");
      ((Route) currentArea).addSpecies(41, 13); // Zubat
      ((Route) currentArea).addSpecies(74, 5); // Geodude
      ((Route) currentArea).addSpecies(46, 2); // Paras
      ((Route) currentArea).addSpecies(35, 1); // Clefairy
    }
    else if (areaName.equals("Mt. Moon B2")) {
      currentArea = new Route("Mt. Moon B2");
      currentArea.addBuilding(Building.EVENTHOUSE); // Hidden Moon Stone
      currentArea.addBuilding(Building.EVENTHOUSE); // Fossil choice
      ((Route) currentArea).addSpecies(41, 12); // Zubat
      ((Route) currentArea).addSpecies(74, 3); // Geodude
      ((Route) currentArea).addSpecies(46, 3); // Paras
      ((Route) currentArea).addSpecies(35, 2); // Clefairy
    }
    else if (areaName.equals("4")) {
      currentArea = new Route("4");
      ((Route) currentArea).addSpecies(19, 3); // Rattata
      ((Route) currentArea).addSpecies(21, 11); // Spearow
      ((Route) currentArea).addSpecies(27, 3); // Sandshrew
      ((Route) currentArea).addSpecies(23, 5); // Ekans
      ((Route) currentArea).addSpecies(15, 3); // Mankey
    }
    else if (areaName.equals("Cerulean City")) {
      currentArea = new City("Cerulean City");
      Water water = new Water();
      currentArea.addBuilding(Building.CENTER);
      currentArea.addBuilding(Building.MART);
      currentArea.addBuilding(Building.GYM);
      currentArea.addBuilding(Building.EVENTHOUSE); // Trade Poliwhirl for Jynx
      water.addSpecies(54, 1); // Psyduck
      water.addSpecies(98, 1); // Krabby
      water.addSpecies(118, 2); // Goldeen
      water.addSpecies(119, 1); // Seaking
      currentArea.setWater(water);
    }
    else if (areaName.equals("24")) {
      currentArea = new Route("24");
      Water water = new Water();
      currentArea.addRivalBattle(3);
      ((Route) currentArea).addSpecies(10, 5); // Caterpie
      ((Route) currentArea).addSpecies(11, 3); // Metapod
      ((Route) currentArea).addSpecies(13, 5); // Weedle
      ((Route) currentArea).addSpecies(14, 3); // Kakuna
      ((Route) currentArea).addSpecies(16, 5); // Pidgey
      ((Route) currentArea).addSpecies(17, 1); // Pidgeotto
      ((Route) currentArea).addSpecies(43, 7); // Oddish
      ((Route) currentArea).addSpecies(48, 2); // Venonat
      ((Route) currentArea).addSpecies(63, 3); // Abra
      ((Route) currentArea).addSpecies(69, 5); // Bellsprout
      water.addSpecies(54, 2); // Psyduck
      water.addSpecies(98, 2); // Krabby
      water.addSpecies(118, 5); // Goldeen
      water.addSpecies(119, 1); // Seaking
      currentArea.setWater(water);
    }
    else if (areaName.equals("25")) {
      currentArea = new Route("25");
      Water water = new Water();
      currentArea.addBuilding(Building.EVENTHOUSE); // Activate PC, SS Ticket
      ((Route) currentArea).addSpecies(10, 5); // Caterpie
      ((Route) currentArea).addSpecies(11, 3); // Metapod
      ((Route) currentArea).addSpecies(13, 5); // Weedle
      ((Route) currentArea).addSpecies(14, 3); // Kakuna
      ((Route) currentArea).addSpecies(16, 5); // Pidgey
      ((Route) currentArea).addSpecies(17, 1); // Pidgeotto
      ((Route) currentArea).addSpecies(43, 7); // Oddish
      ((Route) currentArea).addSpecies(48, 2); // Venonat
      ((Route) currentArea).addSpecies(63, 3); // Abra
      ((Route) currentArea).addSpecies(69, 5); // Bellsprout
      water.addSpecies(54, 2); // Psyduck
      water.addSpecies(98, 4); // Krabby
      water.addSpecies(99, 1); // Kingler
      currentArea.setWater(water);
    }
    else if (areaName.equals("5")) {
      currentArea = new Route("5");
      currentArea.addBuilding(Building.EVENTHOUSE); // Day Care
      ((Route) currentArea).addSpecies(16, 9); // Pidgey
      ((Route) currentArea).addSpecies(17, 1); // Pidgeotto
      ((Route) currentArea).addSpecies(19, 5); // Rattata
      ((Route) currentArea).addSpecies(39, 2); // Jigglypuff
      ((Route) currentArea).addSpecies(43, 8); // Oddish
      ((Route) currentArea).addSpecies(52, 5); // Meowth
      ((Route) currentArea).addSpecies(56, 5); // Mankey
      ((Route) currentArea).addSpecies(63, 3); // Abra
      ((Route) currentArea).addSpecies(69, 8); // Bellsprout
    }
    else if (areaName.equals("6")) {
      currentArea = new Route("6");
      Water water = new Water();
      ((Route) currentArea).addSpecies(16, 9); // Pidgey
      ((Route) currentArea).addSpecies(17, 1); // Pidgeotto
      ((Route) currentArea).addSpecies(19, 5); // Rattata
      ((Route) currentArea).addSpecies(39, 2); // Jigglypuff
      ((Route) currentArea).addSpecies(43, 8); // Oddish
      ((Route) currentArea).addSpecies(52, 5); // Meowth
      ((Route) currentArea).addSpecies(56, 5); // Mankey
      ((Route) currentArea).addSpecies(63, 3); // Abra
      ((Route) currentArea).addSpecies(69, 8); // Bellsprout
      water.addSpecies(90, 1); // Shelder
      water.addSpecies(98, 1); // Krabby
      water.addSpecies(118, 2); // Goldeen
      currentArea.setWater(water);
    }
    else if (areaName.equals("Vermilion City")) {
      currentArea = new City("Vermilion City");
      Water water = new Water();
      currentArea.addBuilding(Building.CENTER);
      currentArea.addBuilding(Building.MART);
      currentArea.addBuilding(Building.GYM);
      currentArea.addBuilding(Building.EVENTHOUSE); // Trade Spearow for
                                                    // Farfetch'd
      currentArea.addBuilding(Building.EVENTHOUSE); // Bike Voucher
      currentArea.addBuilding(Building.EVENTHOUSE); // Old Rod
      water.addSpecies(72, 9); // Tentacool
      water.addSpecies(90, 5); // Shelder
      water.addSpecies(98, 5); // Krabby
      water.addSpecies(116, 2); // Horsea
      currentArea.setWater(water);
    }
    else if (areaName.equals("11")) {
      currentArea = new Route("11");
      ((Route) currentArea).addSpecies(16, 8); // Pidgey
      ((Route) currentArea).addSpecies(17, 2); // Pidgeotto
      ((Route) currentArea).addSpecies(19, 5); // Rattata
      ((Route) currentArea).addSpecies(20, 1); // Raticate
      ((Route) currentArea).addSpecies(21, 6); // Spearow
      ((Route) currentArea).addSpecies(23, 9); // Ekans
      ((Route) currentArea).addSpecies(27, 9); // Sandshrew
      ((Route) currentArea).addSpecies(96, 5); // Drowzee
      ((Route) currentArea).addSpecies(16, 8); // Pidgey
    }
    else if (areaName.equals("Diglett's Cave")) {
      currentArea = new Route("Diglett's Cave");
      ((Route) currentArea).addSpecies(50, 19); // Diglett
      ((Route) currentArea).addSpecies(51, 1); // Dugtrio
    }
    else if (areaName.equals("9")) {
      currentArea = new Route("9");
      ((Route) currentArea).addSpecies(19, 3); // Rattata
      ((Route) currentArea).addSpecies(20, 1); // Raticate
      ((Route) currentArea).addSpecies(21, 6); // Spearow
      ((Route) currentArea).addSpecies(22, 1); // Fearow
      ((Route) currentArea).addSpecies(23, 5); // Ekans
      ((Route) currentArea).addSpecies(27, 5); // Sandshrew
      ((Route) currentArea).addSpecies(29, 7); // Nidorano
      ((Route) currentArea).addSpecies(30, 1); // Nidorino
      ((Route) currentArea).addSpecies(32, 5); // Nidorana
      ((Route) currentArea).addSpecies(33, 1); // Nidorina
    }
    else if (areaName.equals("10")) {
      currentArea = new Route("10");
      Water water = new Water();
      currentArea.addBuilding(Building.CENTER);
      ((Route) currentArea).addSpecies(19, 3); // Rattata
      ((Route) currentArea).addSpecies(20, 1); // Raticate
      ((Route) currentArea).addSpecies(21, 6); // Spearow
      ((Route) currentArea).addSpecies(23, 5); // Ekans
      ((Route) currentArea).addSpecies(27, 5); // Sandshrew
      ((Route) currentArea).addSpecies(29, 2); // Nidorano
      ((Route) currentArea).addSpecies(32, 2); // Nidorana
      ((Route) currentArea).addSpecies(66, 1); // Machop
      ((Route) currentArea).addSpecies(81, 11); // Magnemite
      ((Route) currentArea).addSpecies(100, 9); // Voltorb
      water.addSpecies(61, 5); // Poliwhirl
      water.addSpecies(79, 5); // Slowpoke
      water.addSpecies(116, 2); // Horsea
      water.addSpecies(98, 7); // Krabby
      water.addSpecies(99, 1); // Kingler
      currentArea.setWater(water);
    }
  }

  public static Area getCurrentArea() {
    return currentArea;
  }

  private static Area currentArea;
}
