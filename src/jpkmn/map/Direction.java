package jpkmn.map;

public enum Direction {
  NORTH, EAST, SOUTH, WEST;

  public static Direction valueOf(int num) {
    return Direction.values()[num];
  }
}