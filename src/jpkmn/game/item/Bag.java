package jpkmn.game.item;

import java.util.HashMap;
import java.util.Map;

import jpkmn.Constants;
import jpkmn.game.base.ItemInfo;

public class Bag {
  public Bag() {
    _pockets = new HashMap<ItemType, BagPocket>();

    for (ItemType type : ItemType.values()) 
      _pockets.put(type, new BagPocket());

    ItemInfo nfo;
    ItemType type;
    BagPocket pocket;

    for (int id = 1; id <= Constants.ITEMNUMBER; id++) {
      nfo = ItemInfo.getInfo(id);
      type = ItemType.valueOf(nfo.getType());

      pocket = _pockets.get(type);

      if (type == ItemType.BALL)
        pocket.add(new Ball(nfo.getName(), id, nfo.getValue(), nfo.getData()));
      else if (type == ItemType.POTION)
        pocket.add(new Potion(nfo.getName(), id, nfo.getValue(), nfo.getData()));
      else if (type == ItemType.XSTAT)
        pocket.add(new XStat(id, nfo.getValue(), nfo.getData()));
      else if (type == ItemType.STONE)
        pocket.add(new Stone(nfo.getName(), id, nfo.getValue(), nfo.getData()));
      else if (type == ItemType.MACHINE)
        pocket.add(new Machine(id, nfo.getValue(), nfo.getData()));
      else if (type == ItemType.KEYITEM)
        pocket.add(new KeyItem(nfo.getName(), id, nfo.getValue(), nfo.getData()));
    }
  }

  public BagPocket pocket(ItemType type) {
    return _pockets.get(type);
  }

  private Map<ItemType, BagPocket> _pockets;
}