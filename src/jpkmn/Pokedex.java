package jpkmn;

public class Pokedex {
  private boolean[] own;
  private boolean[] seen;

  public Pokedex() {
    own = new boolean[151];
    seen = new boolean[151];
  }

  /**
   * Update the pokedex to reflect having seen something new.
   * 
   * @param num The Pokemon number
   */
  public void saw(int num) {
    seen[num - 1] = true;
  }

  /**
   * Update the Pokedex to reflect having caught something new.
   * 
   * @param num The Pokemon number
   */
  public void caught(int num) {
    own[num - 1] = true;
  }

  /**
   * Gets the status of a Pokemon in the Pokedex
   * 
   * @param num Pokemon number
   * @return 0 if nothing, 1 if seen, 2 if owned.
   */
  public int getStatus(int num) {
    int ans = 0;
    ans += seen[num - 1] ? 1 : 0;
    ans += own[num - 1] ? 1 : 0;
    return ans;
  }
}
