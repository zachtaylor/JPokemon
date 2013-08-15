package org.jpokemon.overworld;

public class Location {
  private String area;
  private int[] coordinates;
  private Direction direction;

  public Location() {
    coordinates = new int[4];
  }

  public String getArea() {
    return area;
  }

  public void setArea(String a) {
    area = a;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public void setRegion(int x0, int x1, int y0, int y1) {
    coordinates[0] = x0;
    coordinates[1] = y0;
    coordinates[2] = x1;
    coordinates[3] = y1;
  }

  public boolean contains(Location location) {
    return coordinates[0] <= location.coordinates[0] && coordinates[1] <= location.coordinates[1]
        && coordinates[2] >= location.coordinates[2] && coordinates[3] >= location.coordinates[3];
  }
}