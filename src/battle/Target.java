package battle;

public enum Target {
  SELF, ENEMY;
  
  public static Target valueOf(int target) {
      return target == 0 ? SELF : ENEMY;
  }
}