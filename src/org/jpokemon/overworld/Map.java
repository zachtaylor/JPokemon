package org.jpokemon.overworld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jpokemon.pokemon.Pokemon;

public class Map {
  private String name;
  private int width, height, entityZ;
  private List<Entity> entities = new ArrayList<Entity>();
  private List<String> players = new ArrayList<String>();

  public Map(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Entity getEntity(Location target) {
    for (Entity entity : entities) {
      if (entity.getLocation().contains(target)) {
        return entity;
      }
    }

    return null;
  }

  public void addEntity(Entity entity) {
    entities.add(entity);
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getEntityZ() {
    return entityZ;
  }

  public void setEntityZ(int entityZ) {
    this.entityZ = entityZ;
  }

  public void addPlayer(String playerId) {
    players.add(playerId);
  }

  public List<String> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  public void setPlayers(List<String> players) {
    this.players = players;
  }

  public void removePlayer(String playerId) {
    players.remove(playerId);
  }
}