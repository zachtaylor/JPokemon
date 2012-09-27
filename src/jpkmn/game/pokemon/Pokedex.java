package jpkmn.game.pokemon;

import java.util.Scanner;

import jpkmn.Constants;
import jpkmn.exceptions.LoadException;

public class Pokedex {
  public Pokedex() {
    _own = new boolean[Constants.POKEMONNUMBER];
    _saw = new boolean[Constants.POKEMONNUMBER];
  }

  /**
   * Update the Pokedex with having seen a new Pokemon
   * 
   * @param num Pokemon number
   */
  public void saw(int num) {
    _saw[num - 1] = true;
  }

  /**
   * Update the Pokedex with having caught a new Pokemon.
   * 
   * @param num Pokemon number
   */
  public void own(int num) {
    _own[num - 1] = _saw[num - 1] = true;
  }

  /**
   * Gets the status of a Pokemon in the Pokedex
   * 
   * @param num Pokemon number
   * @return 0 if nothing, 1 if seen, 2 if owned.
   */
  public int status(int num) {
    if (_own[num - 1])
      return 2;
    else if (_saw[num - 1])
      return 1;
    else
      return 0;
  }

  /**
   * Gets a string representation of the current data in the Pokedex, based on
   * status. Status 2 indicates seen and owned, and status 1 indicates seen. If
   * a Pokemon has neither been seen or caught, nothing will be present for
   * that number.
   * 
   * @return
   */
  public String save() {
    StringBuilder list = new StringBuilder();
    list.append("DEX: ");

    for (int i = 1; i < +Constants.POKEMONNUMBER; i++) {
      int status = status(i);

      if (status > 0) {
        list.append(i);
        list.append("-");
        list.append(status);
        list.append(" ");
      }
    }

    list.append("\n");

    return list.toString();
  }

  /**
   * Loads this Pokedex with data. Counterpart method to {@link save}.
   * 
   * @param s String representation of the Pokedex data
   * @throws LoadException
   */
  public void load(String s) throws LoadException {
    try {
      Scanner scan = new Scanner(s);

      if (!scan.next().equals("DEX:")) throw new Exception();

      int number, status;
      String[] parts;

      while (scan.hasNext()) {
        parts = scan.next().split("-");
        number = Integer.parseInt(parts[0]);
        status = Integer.parseInt(parts[1]);

        if (status == 2)
          own(number);
        else if (status == 1)
          saw(number);
        else
          throw new Exception();
      }
    } catch (Exception e) {
      throw new LoadException("Pokedex could not load " + s);
    }
  }

  private boolean[] _own, _saw;
}