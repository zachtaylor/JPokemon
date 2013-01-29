package jpkmn.map;

/**
 * Enumeration of the directions one can travel between Areas
 * 
 * @author zach
 */
public enum Direction {
  NORTH, EAST, SOUTH, WEST;

  public static Direction valueOf(int num) {
    return Direction.values()[num];
  }
}