package org.jpokemon.overworld;

public class Location {
  private Map map;
  private int[] coordinates = new int[4];

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  public int getLeft() {
    return coordinates[0];
  }

  public int getTop() {
    return coordinates[1];
  }

  public int getRight() {
    return coordinates[2];
  }

  public int getBottom() {
    return coordinates[3];
  }

  public int getWidth() {
    return coordinates[2] - coordinates[0] + 1;
  }

  public int getHeight() {
    return coordinates[3] - coordinates[1] + 1;
  }

  public void setBounds(int left, int right, int top, int bottom) {
    coordinates[0] = left;
    coordinates[1] = top;
    coordinates[2] = right;
    coordinates[3] = bottom;
  }

  public boolean contains(Location location) {
    return coordinates[0] <= location.coordinates[0] && coordinates[1] <= location.coordinates[1]
        && coordinates[2] >= location.coordinates[2] && coordinates[3] >= location.coordinates[3];
  }
}