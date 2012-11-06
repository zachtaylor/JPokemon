package jpkmn.game.pokemon.move.effect;

public class MoveEffectInfo {
  private double chance;
  private int move_number, type, target, power;

  //@preformat
  public int getMove_number() {return move_number;} public void setMove_number(int val) {move_number = val;}
  public int getType() {return type;} public void setType(int val) {type = val;}
  public int getTarget() {return target;} public void setTarget(int val) {target = val;}
  public int getPower() {return power;} public void setPower(int val) {power = val;}
  public double getChance() {return chance;} public void setChance(double val) {chance = val;}
  //@format
}