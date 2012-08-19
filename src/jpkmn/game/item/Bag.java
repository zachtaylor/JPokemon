package jpkmn.game.item;

import java.util.HashMap;
import java.util.Map;

import jpkmn.Constants;
import jpkmn.game.base.ItemInfo;

public class Bag {
  public Bag() {
    _pockets = new HashMap<ItemType, BagPocket>();

    for (ItemType type : ItemType.values()) {
      _pockets.put(type, new BagPocket());
    }

    ItemInfo nfo;
    ItemType type;
    BagPocket pocket;

    for (int itemNum = 1; itemNum <= Constants.ITEMNUMBER; itemNum++) {
      nfo = ItemInfo.getInfo(itemNum);
      type = ItemType.valueOf(nfo.getType());

      pocket = _pockets.get(type);

      if (type == ItemType.BALL)
        pocket.add(new Ball(nfo.getData(), nfo.getName(), nfo.getValue()));
      else if (type == ItemType.POTION)
        pocket.add(new Potion(nfo.getData(), nfo.getName(), nfo.getValue()));
      else if (type == ItemType.XSTAT)
        pocket.add(new XStat(nfo.getData(), nfo.getValue()));
      else if (type == ItemType.STONE)
        pocket.add(new Stone(nfo.getData(), nfo.getName(), nfo.getValue()));
      else if (type == ItemType.MACHINE)
        pocket.add(new Machine(nfo.getData(), nfo.getValue()));
      else if (type == ItemType.KEYITEM)
        pocket.add(new KeyItem(nfo.getData(), nfo.getName(), nfo.getValue()));
    }
  }

  public BagPocket pocket(ItemType type) {
    return _pockets.get(type);
  }

  private Map<ItemType, BagPocket> _pockets;
}