package jpkmn.map;

public enum RequirementType {
  MOVE, BADGE, POKEDEX, ITEM;

  public static RequirementType valueOf(int r) {
    return RequirementType.values()[r];
  }
}