package jpkmn.map;

public enum Direction {
  NORTH, EAST, SOUTH, WEST;

  public Direction valueOf(int num) {
    return Direction.values()[num];
  }
}