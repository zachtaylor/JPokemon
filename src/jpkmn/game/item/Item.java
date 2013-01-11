package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.pokemon.Type;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.stat.StatType;
import org.json.JSONException;
import org.json.JSONObject;

public class Item {
  public Item(int itemID) {
    _info = ItemInfo.getInfo(itemID);
    _type = ItemType.valueOf(_info.getType());
  }

  public String name() {
    return _info.getName();
  }

  public ItemType type() {
    return _type;
  }

  public int value() {
    return _info.getValue();
  }

  public Target target() {
    return _type.target();
  }

  public int amount() {
    return _quantity;
  }

  public void amount(int quantity) {
    _quantity = quantity;
  }

  public void add(int quantity) {
    amount(quantity + amount());
  }

  public boolean effect(Pokemon p) {
    if (_quantity < 1)
      return false;

    boolean result = false;
    if (result = doEffect(p))
      --_quantity;

    return result;
  }

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();

    try {
      data.put("id", _info.getNumber());
      data.put("name", name());
      data.put("value", value());
      data.put("amount", amount());
      data.put("type", _type.name());

    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  public String toString() {
    return _info.getNumber() + "-" + _quantity;
  }

  private boolean doEffect(Pokemon p) {
    switch (type()) {
    case BALL:
      int HPmax = p.maxHealth(),
      HPcur = p.health(),
      BALL = _info.getData(),
      STAT = p.catchBonus(),
      q = BALL * 4 * STAT / HPmax * ((3 * HPmax) - (2 * HPcur));

      if (q >= 255)
        return true;
      else {
        double r = Math.sqrt(Math.sqrt(((double) q) / (255.0)));
        for (int i = 0; i < 4; i++)
          if (r < Math.random())
            return false;

        return true;
      }
    case POTION:
      p.healDamage(_info.getData());
      return true;
    case XSTAT:
      p.getStat(StatType.valueOf(_info.getData())).effect(1);
      return true;
    case STONE:
      int n = p.number();

      // Eevee (#133) evolutions are not linear
      switch (Type.valueOf(_info.getData())) {
      case FIRE:
        if (n == 37 || n == 58)
          p.evolve();
        else if (n == 133)
          p.evolve(136);
      break;
      case WATER:
        if (n == 60 || n == 90 || n == 120)
          p.evolve();
        else if (n == 133)
          p.evolve(134);
      break;
      case ELECTRIC:
        if (p.number() == 25)
          p.evolve();
        else if (n == 133)
          p.evolve(135);
      break;
      case GRASS:
        if (n == 44 || n == 70 || n == 102)
          p.evolve();
      break;
      case NORMAL:
        if (n == 30 || n == 33 || n == 35 || n == 39)
          p.evolve();
      break;
      default:
      break;
      }
      return p.number() != n;
    case MACHINE:
      if (new Move(_info.getData()).STAB(p) == 1)
        return false;

      try {
        p.addMove(_info.getData());
      } catch (IllegalStateException e) {
        ; // TODO : calculate position and ask
      }
      return false;
    case KEYITEM:
      // TODO : useful stuff
      return false;
    default:
      return false;
    }
  }

  private int _quantity;
  private ItemInfo _info;
  private ItemType _type;
}