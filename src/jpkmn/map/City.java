package jpkmn.map;

/**
 * Areas that can house buildings, and may have water.
 * 
 * @author Zach
 */
public class City extends Area {
  public City(int cityNumber) {
    if (cityNumber == 1) {
      name("Pallet Town");
      buildings(Building.HOME);
      buildings(Building.EVENTHOUSE); // Map
      rivalBattle(1);

      Water water = new Water();
      water.species(60, 1); // Poliwag
      water.species(72, 1); // Tentacool
      water.species(120, 1); // Staryu
      water(water);
    }
    else if (cityNumber == 2) {
      name("Viridian City");
      buildings(Building.CENTER);
      buildings(Building.MART);
      buildings(Building.GYM); // TODO Gym 8

      Water water = new Water();
      water.species(60, 1); // Poliwag
      water.species(72, 1); // Tentacool
      water(water);
    }
    else if (cityNumber == 3) {
      name("Pewter City");
      buildings(Building.CENTER);
      buildings(Building.MART);
      buildings(Building.GYM); // TODO Gym 1
      buildings(Building.EVENTHOUSE); // Science Museum
    }
    else if (cityNumber == 4) {
      name("Cerulean City");
      buildings(Building.CENTER);
      buildings(Building.MART);
      buildings(Building.GYM); // TODO Gym 2
      buildings(Building.EVENTHOUSE); // Trade Poliwhirl for Jynx
      
      Water water = new Water();
      water.species(54, 1); // Psyduck
      water.species(98, 1); // Krabby
      water.species(118, 2); // Goldeen
      water.species(119, 1); // Seaking
      water(water);
    }
    else if (cityNumber == 5) {
      name("Vermilion City");
      buildings(Building.CENTER);
      buildings(Building.MART);
      buildings(Building.GYM); // TODO Gym 3
      buildings(Building.EVENTHOUSE); // Trade Spearow for Farfetch'd
      buildings(Building.EVENTHOUSE); // Bike Voucher
      buildings(Building.EVENTHOUSE); // Old Rod
      
      Water water = new Water();
      water.species(72, 9); // Tentacool
      water.species(90, 5); // Shelder
      water.species(98, 5); // Krabby
      water.species(116, 2); // Horsea
      water(water);
    }
    else if (cityNumber == 6) {
      name("Lavender Town");
      buildings(Building.CENTER);
      buildings(Building.MART);
      buildings(Building.EVENTHOUSE); // Pokemon Tower
    }
    else if (cityNumber == 7) {
      name("Celadon City");
      buildings(Building.CENTER);
      buildings(Building.MART);
      buildings(Building.GYM); // TODO Gym 4
      buildings(Building.EVENTHOUSE); // Casino
      buildings(Building.EVENTHOUSE); // Department Store
      buildings(Building.EVENTHOUSE); // Mansion to get Eevee
    }
    else if (cityNumber == 7) {
      name("Celadon City");
      buildings(Building.CENTER);
      buildings(Building.MART);
      buildings(Building.GYM); // TODO Gym 5
      buildings(Building.EVENTHOUSE); // Get psychic
      buildings(Building.EVENTHOUSE); // Fighting dojo
      buildings(Building.EVENTHOUSE); // Trade pokedoll for Mimic
    }
    else if (cityNumber == 8) {
      name("Fuchsia City");
      buildings(Building.CENTER);
      buildings(Building.MART);
      buildings(Building.GYM); // TODO Gym 6
      buildings(Building.EVENTHOUSE); // good rod
      buildings(Building.EVENTHOUSE); // Safari Zone - Strength
    }
    else if (cityNumber == 8) {
      name("Cinnabar Island");
      buildings(Building.CENTER);
      buildings(Building.MART);
      buildings(Building.GYM); // TODO Gym 7
      buildings(Building.EVENTHOUSE); // get metronome
      buildings(Building.EVENTHOUSE); // lots of trades
    }
  }
}