package jpkmn.game.player;

import jpkmn.game.pokemon.Pokemon;

public class GymLeader extends AbstractPlayer {
  public GymLeader(int badgeNumber) {
    _id = badgeNumber;

    if (_id == 1) {
      name("Brock");
      _prize = 1386;

      Pokemon p1 = new Pokemon(74, 12); // Geodude
      p1.moves.removeAll();
      p1.moves.add(143, 0); // Tackle
      p1.moves.add(27, 1); // Defense Curl
      party.add(p1);

      Pokemon p2 = new Pokemon(95, 14); // Onix
      p2.moves.removeAll();
      p1.moves.add(143, 0); // Tackle
      p1.moves.add(113, 1); // Screech
      p1.moves.add(9, 2); // Bide
      p1.moves.add(10, 3); // Bind
      party.add(p2);
    }
    else if (_id == 2) {
      name("Misty");
      _prize = 2079;

      Pokemon p1 = new Pokemon(120, 18); // Staryu
      p1.moves.removeAll();
      p1.moves.add(143, 0); // Tackle
      p1.moves.add(159, 1); // Water Gun
      party.add(p1);

      Pokemon p2 = new Pokemon(121, 21); // Starmie
      p1.moves.removeAll();
      p1.moves.add(143, 0); // Tackle
      p1.moves.add(159, 1); // Water Gun
      p1.moves.add(57, 2); // Harden
      p1.moves.add(17, 3); // Bubblebeam
      party.add(p2);
    }
    else if (_id == 3) {
      name("Lt. Surge");
      _prize = 2376;

      Pokemon p1 = new Pokemon(100, 21); // Voltorb
      p1.moves.removeAll();
      p1.moves.add(143, 0); // Tackle
      p1.moves.add(113, 1); // Screech
      p1.moves.add(128, 2); // SonicBoom
      party.add(p1);

      Pokemon p2 = new Pokemon(25, 18); // Pikachu
      p2.moves.removeAll();
      p2.moves.add(152, 0); // ThunderShock
      p2.moves.add(149, 1); // Thunder Wave
      p2.moves.add(53, 2); // Growl
      p2.moves.add(100, 3); // Quick Attack
      party.add(p2);

      Pokemon p3 = new Pokemon(26, 24); // Raichu
      p3.moves.removeAll();
      p2.moves.add(150, 0); // Thunderbolt
      p2.moves.add(152, 1); // ThunderShock
      p2.moves.add(149, 2); // Thunder Wave
      p2.moves.add(53, 3); // Growl
      party.add(p3);
    }
    else if (_id == 4) {
      name("Erika");
      _prize = 2871;

      Pokemon p1 = new Pokemon(71, 29); // Victreebel
      p1.moves.removeAll();
      p1.moves.add(164, 0); // Wrap
      p1.moves.add(95, 1); // PoisonPowder
      p1.moves.add(122, 2); // Sleep Powder
      p1.moves.add(102, 3); // Razor Leaf
      party.add(p1);

      Pokemon p2 = new Pokemon(114, 24); // Tangela
      p2.moves.removeAll();
      p2.moves.add(152, 0); // Bind
      p2.moves.add(149, 1); // Constrict
      party.add(p2);

      Pokemon p3 = new Pokemon(45, 29); // Vileplume
      p3.moves.removeAll();
      p3.moves.add(95, 0); // PoisonPowder
      p3.moves.add(80, 1); // Mega Drain
      p3.moves.add(122, 2); // Sleep Powder
      p3.moves.add(91, 3); // Petal Dance
      party.add(p3);
    }
    else if (_id == 5) {

    }
    else if (_id == 6) {

    }
    else if (_id == 7) {

    }
    else if (_id == 8) {

    }
    else
      throw new IllegalArgumentException("Cannot create Gym Leader: " + _id);
  }

  public int prize() {
    return _prize;
  }

  private int _id, _prize;
}